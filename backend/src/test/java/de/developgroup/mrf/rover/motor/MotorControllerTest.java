/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.motor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.PinState;

import de.developgroup.mrf.rover.pwmgenerator.PCA9685Mock;
import de.developgroup.mrf.rover.pwmgenerator.PCA9685PWMGenerator;

public class MotorControllerTest {

	private static final int CHANNEL = 0;
	PCA9685Mock pwmGenDeviceMock;
	GpioPinDigitalOutputMock directionPinMock;
	PCA9685PWMGenerator pwmGen;
	MotorController motorController;
	MotorConfigurationMock configuration;

	@Before
	public void setUp() throws Exception {
		pwmGenDeviceMock = new PCA9685Mock();
		pwmGen = new PCA9685PWMGenerator(pwmGenDeviceMock);

		pwmGen.open();
		pwmGen.setFrequency(50);

		directionPinMock = new GpioPinDigitalOutputMock();
		configuration = new MotorConfigurationMock();
		motorController = new MotorControllerImpl(pwmGen.getOutput(CHANNEL),
				directionPinMock, configuration);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetSpeed() throws IOException {
		motorController.setSpeed(MotorController.SPEED_STOP);

		assertThat(pwmGenDeviceMock.getOnTimerValue(CHANNEL), is(0));
		assertThat(pwmGenDeviceMock.getOffTimerValue(CHANNEL), is(0));
		assertThat(directionPinMock.getState(), is(PinState.HIGH));

		motorController.setSpeed(MotorController.SPEED_MAX_FORWARD);

		assertThat(pwmGenDeviceMock.getOnTimerValue(CHANNEL), is(0));
		assertThat(pwmGenDeviceMock.getOffTimerValue(CHANNEL), is(4096));
		assertThat(directionPinMock.getState(), is(PinState.HIGH));

		motorController.setSpeed(MotorController.SPEED_MAX_BACKWARD);

		assertThat(pwmGenDeviceMock.getOnTimerValue(CHANNEL), is(0));
		assertThat(pwmGenDeviceMock.getOffTimerValue(CHANNEL), is(4096));
		assertThat(directionPinMock.getState(), is(PinState.LOW));

	}

	@Test
	public void testSetSpeedReversed() throws IOException {
		configuration.reversed = true;

		motorController.setSpeed(MotorController.SPEED_STOP);

		assertThat(pwmGenDeviceMock.getOnTimerValue(CHANNEL), is(0));
		assertThat(pwmGenDeviceMock.getOffTimerValue(CHANNEL), is(0));
		assertThat(directionPinMock.getState(), is(PinState.LOW));

		motorController.setSpeed(MotorController.SPEED_MAX_FORWARD);

		assertThat(pwmGenDeviceMock.getOnTimerValue(CHANNEL), is(0));
		assertThat(pwmGenDeviceMock.getOffTimerValue(CHANNEL), is(4096));
		assertThat(directionPinMock.getState(), is(PinState.LOW));

		motorController.setSpeed(MotorController.SPEED_MAX_BACKWARD);

		assertThat(pwmGenDeviceMock.getOnTimerValue(CHANNEL), is(0));
		assertThat(pwmGenDeviceMock.getOffTimerValue(CHANNEL), is(4096));
		assertThat(directionPinMock.getState(), is(PinState.HIGH));

	}

}
