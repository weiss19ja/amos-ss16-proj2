/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import de.developgroup.mrf.server.controller.LoggingCommunicationController;
import org.cfg4j.provider.ConfigurationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.GpioController;

import de.developgroup.mrf.rover.collision.CollisionControllerMock;
import de.developgroup.mrf.server.controller.CameraSnapshotController;
import de.developgroup.mrf.server.controller.DriveController;
import de.developgroup.mrf.server.controller.HeadController;

public class RoverHandlerImplTest {

	RoverHandlerImpl handler;

	@Before
	public void setUp() {

		handler = new RoverHandlerImpl(new CollisionControllerMock(),
				mock(GpioController.class), mock(DriveController.class),
				mock(HeadController.class),
				mock(CameraSnapshotController.class),
				mock(LoggingCommunicationController.class));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDriveForward() throws IOException {
		handler.driveForward(100);

		verify(handler.driveController).driveForwards();
	}

	@Test
	public void testDriveBackward() throws IOException {
		handler.driveBackward(100);

		verify(handler.driveController).driveBackwards();
	}

	@Test
	public void testStop() throws IOException {
		handler.stop();

		verify(handler.driveController).stop();
	}

	@Test
	public void testTurnLeft() throws IOException {
		handler.turnLeft(20);

		verify(handler.driveController).turnLeft();
	}

	@Test
	public void testTurnRight() throws IOException {
		handler.turnRight(20);

		verify(handler.driveController).turnRight();
	}

	@Test
	public void testHandlePing() {
		assertEquals(handler.handlePing(1), "pong 2");
	}

	@Test
	public void testShutdownRover() {
		handler.shutdownRover();

		verify(handler.gpio).shutdown();
	}

	@Test
	public void testTurnHeadUp() throws IOException {
		handler.turnHeadUp(20);

		verify(handler.headController).turnHeadUp(20);

	}

	@Test
	public void testTurnHeadDown() throws IOException {
		handler.turnHeadDown(20);

		verify(handler.headController).turnHeadDown(20);

	}

	@Test
	public void testTurnHeadLeft() throws IOException {
		handler.turnHeadLeft(20);

		verify(handler.headController).turnHeadLeft(20);
	}

	@Test
	public void testTurnHeadRight() throws IOException {
		handler.turnHeadRight(20);

		verify(handler.headController).turnHeadRight(20);
	}

	@Test
	public void testResetHeadPosition() throws IOException {
		handler.resetHeadPosition();

		verify(handler.headController).resetHeadPosition();
	}

	@Test
	public void testGetCameraSnapshot() throws IOException {
		handler.getCameraSnapshot(5001);
		verify(handler.cameraSnapshotController).getCameraSnapshot(5001);
	}

	@Test
	public void testGetAllLogEntries() throws IOException {
		handler.getLoggingEntries(5001, "");
		verify(handler.loggingCommunicationController).getLoggingEntries(5001, "");
	}

	@Test
	public void testGetSpecificLogEntries() throws IOException {
		handler.getLoggingEntries(5001, "Test");
		verify(handler.loggingCommunicationController).getLoggingEntries(5001, "Test");
	}

	@Test
	public void testInitRover() throws IOException {
		ConfigurationProvider roverProperties = mock(ConfigurationProvider.class);
		handler.initRover(roverProperties);

		verify(handler.driveController).initialize(roverProperties);
	}

}
