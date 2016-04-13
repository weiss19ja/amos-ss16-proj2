package de.developgroup.tec.pi.driver.motor.internal;

import java.io.IOException;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import de.developgroup.tec.pi.driver.motor.MotorController;
import de.developgroup.tec.pi.driver.motor.MotorControllerConfiguration;
import de.developgroup.tec.pi.driver.pwmgen.PWMOutput;

public class MotorControllerImpl implements MotorController {

	private final PWMOutput output;
	private final GpioPinDigitalOutput directionPin;
	private final MotorControllerConfiguration configuration;

	public MotorControllerImpl(PWMOutput output, GpioPinDigitalOutput directionPin, MotorControllerConfiguration configuration) {
		this.output = output;
		this.directionPin = directionPin;
		this.configuration = configuration;
	}

	@Override
	public void setSpeed(int speed) throws IOException {
		directionPin.setState(speed < 0 ^ configuration.reversed() ? PinState.LOW : PinState.HIGH);
		int pwm = (output.getCycleCount() * Math.abs(speed)) / SPEED_MAX_FORWARD;
		output.setPWM(pwm);
	}

	@Override
	public void close() throws IOException {
		output.setPWM(0);
	}

}
