package de.developgroup.tec.rover.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.tec.pi.driver.motor.MotorController;

public class RoverControllerImplTest {

	RoverControllerImpl fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new RoverControllerImpl();
		fixture.leftMotor = mock(MotorController.class);
		fixture.rightMotor = mock(MotorController.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testForward() throws IOException {
		fixture.setSpeed(500);

		verify(fixture.leftMotor).setSpeed(500);
		verify(fixture.rightMotor).setSpeed(500);
	}

	@Test
	public void testBackward() throws IOException {
		fixture.setSpeed(-500);

		verify(fixture.leftMotor).setSpeed(-500);
		verify(fixture.rightMotor).setSpeed(-500);
	}

	@Test
	public void testTurnCW() throws IOException {
		fixture.setTurnRate(500);

		verify(fixture.leftMotor).setSpeed(-500);
		verify(fixture.rightMotor).setSpeed(500);
	}

	@Test
	public void testTurnCCW() throws IOException {
		fixture.setTurnRate(-500);

		verify(fixture.leftMotor).setSpeed(500);
		verify(fixture.rightMotor).setSpeed(-500);
	}

	@Test
	public void testLimitUpper() throws IOException {
		fixture.setSpeed(800);
		reset(fixture.leftMotor);
		reset(fixture.rightMotor);

		fixture.setTurnRate(500);
		verify(fixture.leftMotor).setSpeed(300);
		verify(fixture.rightMotor).setSpeed(1000);
	}

	@Test
	public void testLimitLower() throws IOException {
		fixture.setSpeed(-800);
		reset(fixture.leftMotor);
		reset(fixture.rightMotor);

		fixture.setTurnRate(500);
		verify(fixture.leftMotor).setSpeed(-1000);
		verify(fixture.rightMotor).setSpeed(-300);
	}
}
