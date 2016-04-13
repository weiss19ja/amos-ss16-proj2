package de.developgroup.tec.pi.driver.motor.internal;

import de.developgroup.tec.pi.driver.motor.MotorControllerConfiguration;

public class MotorConfigurationMock implements MotorControllerConfiguration {

	boolean reversed = false;
	
	@Override
	public boolean reversed() {
		return reversed;
	}

	
	
}
