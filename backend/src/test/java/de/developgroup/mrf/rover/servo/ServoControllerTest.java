/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.servo;


import de.developgroup.mrf.rover.pwmgenerator.PCA9685Mock;
import de.developgroup.mrf.rover.pwmgenerator.PCA9685PWMGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServoControllerTest {

	private static final int CHANNEL = 0;
	PCA9685Mock device;
	PCA9685PWMGenerator pwmGen;
	ServoControllerImpl servoController;
	ServoConfigurationMock configuration;

	@Before
	public void setUp() throws Exception {
		device = new PCA9685Mock();
		pwmGen = new PCA9685PWMGenerator(device);

		pwmGen.open();
		pwmGen.setFrequency(50);

		configuration = new ServoConfigurationMock();
		
		servoController = new ServoControllerImpl(pwmGen.getOutput(CHANNEL), configuration);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetPosition() throws IOException {
		servoController.setPosition(ServoController.POS_NEUTRAL);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(375));

		servoController.setPosition(ServoController.POS_MIN);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(250));

		servoController.setPosition(ServoController.POS_MAX);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(500));

	}

	@Test
	public void testSetReversed() throws IOException {		
		
		configuration.reversed = true;
		
		servoController.setPosition(ServoController.POS_NEUTRAL);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(375));

		servoController.setPosition(ServoController.POS_MIN);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(500));

		servoController.setPosition(ServoController.POS_MAX);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(250));

	}
	
	
	@Test
	public void testOffset() throws IOException {		
		
		configuration.offset = 500;
		
		servoController.setPosition(ServoController.POS_NEUTRAL);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(437));

		servoController.setPosition(ServoController.POS_MIN);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(313));

		servoController.setPosition(ServoController.POS_MAX);

		assertThat(device.getOnTimerValue(CHANNEL), is(0));
		assertThat(device.getOffTimerValue(CHANNEL), is(550));	// Limited

	}
}
