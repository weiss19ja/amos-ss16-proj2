/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import java.io.File;
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

import de.developgroup.mrf.rover.motor.MotorController;
import de.developgroup.mrf.rover.motor.MotorControllerConfiguration;
import de.developgroup.mrf.rover.motor.MotorControllerImpl;
import de.developgroup.mrf.rover.pwmgenerator.PCA9685PWMGenerator;
import de.developgroup.mrf.rover.servo.ServoConfiguration;
import de.developgroup.mrf.rover.servo.ServoController;
import de.developgroup.mrf.rover.servo.ServoControllerImpl;

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

			directionPinLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07,
					"Direction Left", PinState.LOW);
			directionPinLeft.setShutdownOptions(true, PinState.LOW);

			directionPinRight = gpio.provisionDigitalOutputPin(
					RaspiPin.GPIO_00, "Direction Right", PinState.LOW);
			directionPinRight.setShutdownOptions(true, PinState.LOW);

			ConfigurationSource source = new FilesConfigurationSource(
					() -> Collections.singletonList(Paths
							.get("rover.properties")));

			ConfigurationProvider provider = new ConfigurationProviderBuilder()
					.withConfigurationSource(source)
					.withReloadStrategy(
							new PeriodicalReloadStrategy(5, TimeUnit.SECONDS))
					.build();

			leftMotor = new MotorControllerImpl(driver.getOutput(14),
					provider.bind("motorLeft",
							MotorControllerConfiguration.class));
			rightMotor = new MotorControllerImpl(driver.getOutput(15),
					provider.bind("motorRight",
							MotorControllerConfiguration.class));
			panServo = new ServoControllerImpl(driver.getOutput(1),
					provider.bind("servo1", ServoConfiguration.class));
			tiltServo = new ServoControllerImpl(driver.getOutput(0),
					provider.bind("servo0", ServoConfiguration.class));
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

		if (videoStream == null) {
			ProcessBuilder builder = new ProcessBuilder("sudo", "-u", "pi",
					"/home/pi/camera/opencamera.sh");
			videoStream = builder.start();
		}
		return DEFAULT_VIDEO_PORT;
	}

	@Override
	public String takeSnapshot() throws IOException {
		File imageFile = File.createTempFile("Snapshot", ".jpg");

		ProcessBuilder builder = new ProcessBuilder("raspistill", "--output",
				imageFile.getAbsolutePath());
		Process snapshot = builder.start();

		try {
			if (!snapshot.waitFor(5, TimeUnit.SECONDS)) {
				throw new IOException(
						"Timeout while waiting for camera snapshot to be taken");
			}
		} catch (InterruptedException e) {
			// Ignore
		}

		return imageFile.getName();
	}

}
