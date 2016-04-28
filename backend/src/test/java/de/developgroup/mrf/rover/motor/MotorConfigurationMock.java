package de.developgroup.mrf.rover.motor;

public class MotorConfigurationMock implements MotorControllerConfiguration {

	boolean reversed = false;
	
	@Override
	public boolean reversed() {
		return reversed;
	}

	
	
}
