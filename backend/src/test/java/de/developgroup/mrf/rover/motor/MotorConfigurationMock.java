/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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
