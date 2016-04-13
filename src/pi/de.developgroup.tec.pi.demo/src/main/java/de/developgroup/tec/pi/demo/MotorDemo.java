package de.developgroup.tec.pi.demo;

import java.io.IOException;

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

public class MotorDemo {

	public static void main(String[] args) throws IOException, InterruptedException {

		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		final GpioController gpio = GpioFactory.getInstance();

		try {
			I2CDevice device = bus.getDevice(0x40);
			PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);

			driver.open();
			driver.setFrequency(50);

			final GpioPinDigitalOutput directionPinLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07,
					"Direction Left", PinState.LOW);
			directionPinLeft.setShutdownOptions(true, PinState.LOW);

			final GpioPinDigitalOutput directionPinRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
					"Direction Right", PinState.LOW);
			directionPinRight.setShutdownOptions(true, PinState.LOW);

			MotorController leftMotor = new MotorControllerImpl(driver.getOutput(14), directionPinLeft,
					new MotorControllerConfiguration() {
						@Override
						public boolean reversed() {
							return false;
						}
					});
			MotorController rightMotor = new MotorControllerImpl(driver.getOutput(15), directionPinRight,
					new MotorControllerConfiguration() {
						@Override
						public boolean reversed() {
							return false;
						}
					});

			for (int r = 0; r < 100; ++r) {

				leftMotor.setSpeed(500);
				rightMotor.setSpeed(500);

				Thread.sleep(2000L);

				leftMotor.setSpeed(200);
				rightMotor.setSpeed(-200);

				Thread.sleep(2000L);

				leftMotor.setSpeed(500);
				rightMotor.setSpeed(500);

				Thread.sleep(2000L);

				leftMotor.setSpeed(-200);
				rightMotor.setSpeed(200);

				Thread.sleep(2000L);
			}

		} finally {
			gpio.shutdown();
			bus.close();
		}

	}

}
