/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.motor;

import java.io.IOException;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import de.developgroup.mrf.rover.pwmgenerator.PWMOutput;

public class MotorControllerImpl implements MotorController {

	private final PWMOutput output;
	private final MotorControllerConfiguration configuration;
	private GpioPinDigitalOutput motorPin;
	private GpioController gpio;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MotorControllerImpl.class);

	public MotorControllerImpl(PWMOutput output,
			MotorControllerConfiguration configuration) {

		this.output = output;
		this.configuration = configuration;
		this.gpio = GpioFactory.getInstance();

		try {
			Pin myGPIOMotorPin = (Pin) FieldUtils.readDeclaredStaticField(
					RaspiPin.class, configuration.gpioPin());

			motorPin = gpio.provisionDigitalOutputPin(myGPIOMotorPin,
					configuration.name(), PinState.LOW);
			motorPin.setShutdownOptions(true, PinState.LOW);

		} catch (IllegalAccessException e) {
			LOGGER.error("Error on construct MotorControllerImpl", e);
		}

	}

	public MotorControllerImpl(PWMOutput output,
			GpioPinDigitalOutput directionPin,
			MotorControllerConfiguration configuration) {
		this.output = output;
		this.motorPin = directionPin;
		this.configuration = configuration;
	}

	@Override
	public void setSpeed(int speed) throws IOException {
		motorPin.setState(speed < 0 ^ configuration.reversed() ? PinState.LOW
				: PinState.HIGH);
		int pwm = (output.getCycleCount() * Math.abs(speed))
				/ SPEED_MAX_FORWARD;
		output.setPWM(pwm);
	}

	@Override
	public void setSpeedPercentage(double percentage) throws IOException {
		setSpeed((int)(percentage * SPEED_MAX_FORWARD));
	}

	@Override
	public void close() throws IOException {
		output.setPWM(0);
	}

}
