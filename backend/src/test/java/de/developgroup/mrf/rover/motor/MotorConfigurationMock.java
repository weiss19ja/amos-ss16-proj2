package de.developgroup.mrf.rover.motor;

public class MotorConfigurationMock implements MotorControllerConfiguration {

	boolean reversed = false;
	String gpioPin = "";
	String name = "";

	@Override
	public boolean reversed() {
		return reversed;
	}

	@Override
	public String gpioPin() {
		return gpioPin;
	}

	@Override
	public String name() {
		return name;
	}

}
