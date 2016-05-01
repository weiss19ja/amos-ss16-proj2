package de.developgroup.tec.rover.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.reload.strategy.PeriodicalReloadStrategy;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import de.developgroup.tec.pi.driver.motor.MotorController;
import de.developgroup.tec.pi.driver.motor.MotorControllerConfiguration;
import de.developgroup.tec.pi.driver.motor.internal.MotorControllerImpl;
import de.developgroup.tec.pi.driver.pwmgen.pca9685.PCA9685PWMGenerator;
import de.developgroup.tec.pi.driver.servo.ServoConfiguration;
import de.developgroup.tec.pi.driver.servo.ServoController;
import de.developgroup.tec.pi.driver.servo.internal.ServoControllerImpl;
import de.developgroup.tec.rover.RoverController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RoverControllerImpl implements RoverController {

	public static final RoverController INSTANCE = new RoverControllerImpl();

	private static final int DEFAULT_VIDEO_PORT = 8001;

	private I2CBus bus;
	private GpioController gpio;
	MotorController leftMotor;
	MotorController rightMotor;
	private ServoController panServo;
	private ServoController tiltServo;

	private GpioPinDigitalOutput directionPinLeft;

	private GpioPinDigitalOutput directionPinRight;

	private Process videoStream;

	private int desiredSpeed;
	private int desiredTurnRate;

	RoverControllerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.developgroup.tec.rover.IRoverController#setSpeed(int)
	 */
	@Override
	public void setSpeed(int speed) throws IOException {
		desiredSpeed = speed;
		updateMotors();
	}

	private void updateMotors() throws IOException {
		final int leftSpeed = limit(desiredSpeed - desiredTurnRate);
		final int rightSpeed = limit(desiredSpeed + desiredTurnRate);

		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
	}

	private int limit(int value) {
		if (value > MotorController.SPEED_MAX_FORWARD) {
			return MotorController.SPEED_MAX_FORWARD;
		} else if (value < MotorController.SPEED_MAX_BACKWARD) {
			return MotorController.SPEED_MAX_BACKWARD;
		}

		return value;
	}

	/**
	 * Convert an angle given in arcsec to a servo controller value.
	 * 
	 * @param arcsec
	 * @return
	 */
	private int calculateServoPosition(int arcsec) {
		// TODO: Find better place for conversion?
		int result = (arcsec * ServoController.POS_MAX) / (60 * 3600);
		return result;
	}

	@Override
	public void setTurnRate(int rate) throws IOException {
		desiredTurnRate = rate;
		updateMotors();
	}

	@Override
	public void setPan(int pan) throws IOException {
		panServo.setPosition(calculateServoPosition(pan));
	}

	@Override
	public void setTilt(int tilt) throws IOException {
		tiltServo.setPosition(calculateServoPosition(tilt));
	}

	@Override
	public void stop() throws IOException {
		desiredSpeed = 0;
		desiredTurnRate = 0;
		updateMotors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.developgroup.tec.rover.IRoverController#init()
	 */
	@Override
	public void init() throws IOException {
		if (bus == null) {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
			gpio = GpioFactory.getInstance();

			I2CDevice device = bus.getDevice(0x40);
			PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);

			driver.open();
			driver.setFrequency(50);

			directionPinLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Direction Left", PinState.LOW);
			directionPinLeft.setShutdownOptions(true, PinState.LOW);

			directionPinRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Direction Right", PinState.LOW);
			directionPinRight.setShutdownOptions(true, PinState.LOW);

			ConfigurationSource source = new FilesConfigurationSource(
					() -> Collections.singletonList(Paths.get("rover.properties")));

			ConfigurationProvider provider = new ConfigurationProviderBuilder().withConfigurationSource(source)
					.withReloadStrategy(new PeriodicalReloadStrategy(5, TimeUnit.SECONDS)).build();

			leftMotor = new MotorControllerImpl(driver.getOutput(14), directionPinLeft,
					provider.bind("motorLeft", MotorControllerConfiguration.class));
			rightMotor = new MotorControllerImpl(driver.getOutput(15), directionPinRight,
					provider.bind("motorRight", MotorControllerConfiguration.class));
			panServo = new ServoControllerImpl(driver.getOutput(1), provider.bind("servo1", ServoConfiguration.class));
			tiltServo = new ServoControllerImpl(driver.getOutput(0), provider.bind("servo0", ServoConfiguration.class));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.developgroup.tec.rover.IRoverController#close()
	 */
	@Override
	public void close() throws IOException {
		if (bus != null) {
			panServo.close();
			tiltServo.close();

			leftMotor.close();
			rightMotor.close();

			gpio.unprovisionPin(directionPinLeft);
			gpio.unprovisionPin(directionPinRight);

			gpio.shutdown();
			gpio = null;

			bus.close();
			bus = null;

			videoStream.destroy();
			videoStream = null;
		}
	}

	@Override
	public int openVideoStream() throws IOException {

//		if (videoStream == null) {
//			ProcessBuilder builder = new ProcessBuilder("sudo", "-u", "pi", "/home/pi/camera/opencamera.sh");
//			videoStream = builder.start();
//		}
		int WIDTH = 640;
		int HEIGHT = 480;
		int FRAMERATE = 24;
		int WS_PORT = 8084;
		String COLOR = "u'#444'";
		String BGCOLOR = "u'#333'";
		String JSMPEG_MAGIC = "b'jsmp'";
		String JSMPEG_HEADER = "Struct('>4sHH')";

		// Initialize Websocket on port 8084
		// TODO: Watchdog?
		// TODO: new Thread?
		try (
				ServerSocket serverSocket =
						new ServerSocket(WS_PORT);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out =
						new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));

		) {
			// Open Stream on Raspberry and convert it to another format
			// because Streaming H264 Raw is way too slow
			if (videoStream == null) {
				videoStream = Runtime.getRuntime().exec("raspivid -w 640 -h 480 -n -t 5000 -b 3000000 -o - | avconv -r 24 -i rawvideo.h264 -vcodec copy -");
			}
			BufferedInputStream bis = new BufferedInputStream(videoStream.getInputStream());
			//Direct methode p.getInputStream().read() also possible, but BufferedInputStream gives 0,5-1s better performance
			int read = bis.read();
			out.write(read);

			// Send video to client
			System.out.println("start writing");
			while (read != -1) {
				read = bis.read();
				out.write(read);
			}
			System.out.println("end writing");
			bis.close();
			in.close();
			out.close();
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ WS_PORT + " or listening for a connection");
			System.out.println(e.getMessage());
		}
		System.out.println("Server thinks he worked enough");

		// TODO: Shutdown everything


		/*System.out.println("START PROGRAM");
		long start = System.currentTimeMillis();
		try
		{

			Process p = Runtime.getRuntime().exec("raspivid -w 640 -h 360 -n -t 10000 -b 3000000 -o -");
            Process p = Runtime.getRuntime().exec("raspivid -w 640 -h 360 -fps 30 -t 10000 -b 3000000 -o - | nc  10.0.0.81 5001");
			BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
			//Direct methode p.getInputStream().read() also possible, but BufferedInputStream gives 0,5-1s better performance
			FileOutputStream fos = new FileOutputStream("testvid.h264");

			System.out.println("start writing");
			int read = bis.read();
			fos.write(read);

			while (read != -1)
			{
				read = bis.read();
				fos.write(read);
			}
			System.out.println("end writing");
			bis.close();
			fos.close();

		}
		catch (IOException ieo)
		{
			ieo.printStackTrace();
		}
		System.out.println("END PROGRAM");
		System.out.println("Duration in ms: " + (System.currentTimeMillis() - start));*/
//		return DEFAULT_VIDEO_PORT;
		return WS_PORT;
	}

	@Override
	public String takeSnapshot() throws IOException {
		File imageFile = File.createTempFile("Snapshot", ".jpg");

		ProcessBuilder builder = new ProcessBuilder("raspistill", "--output", imageFile.getAbsolutePath());
		Process snapshot = builder.start();

		try {
			if (!snapshot.waitFor(5, TimeUnit.SECONDS)) {
				throw new IOException("Timeout while waiting for camera snapshot to be taken");
			}
		} catch (InterruptedException e) {
			// Ignore
		}

		return imageFile.getName();
	}

}
