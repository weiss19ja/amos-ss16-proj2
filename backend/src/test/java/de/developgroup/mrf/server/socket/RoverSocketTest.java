package de.developgroup.mrf.server.socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.handler.DeveloperSettingsHandler;
import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.handler.SingleDriverHandler;

public class RoverSocketTest {

	RoverSocket roverSocket = new RoverSocket();
	ClientManager clientManager = mock(ClientManager.class);
	DeveloperSettingsHandler developerSettingsHandler = mock(DeveloperSettingsHandler.class);
	SingleDriverHandler singleDriverHandler = mock(SingleDriverHandler.class);
	RoverHandler roverHandler = mock(RoverHandler.class);

	@Before
	public void setUp() {
		RoverSocket.clientManager = clientManager;
		RoverSocket.developerSettingsHandler = developerSettingsHandler;
		RoverSocket.singleDriverHandler = singleDriverHandler;
		RoverSocket.roverHandler = roverHandler;
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testOnWebSocketConnect() {
		Session sess = mock(Session.class);
		when(clientManager.addClient(sess)).thenReturn(5001);
		roverSocket.onWebSocketConnect(sess);
		verify(RoverSocket.clientManager).addClient(sess);
		verify(RoverSocket.developerSettingsHandler).notifyIfBlocked(5001,
				"Interactions with the rover are blocked at the moment");
	}

	@Test
	public void testOnWebSocketClose() {
		roverSocket.onWebSocketClose(0, "for testing");
		verify(RoverSocket.clientManager).removeClosedSessions();
		verify(RoverSocket.singleDriverHandler).verifyDriverAvailability();
	}

	@Test
	public void testDriveForeward() throws IOException {
		roverSocket.driveForward(350);
		verify(RoverSocket.roverHandler).driveForward(350);

	}

	@Test
	public void testDriveForewardWithKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.driveForward(500);
		verify(RoverSocket.roverHandler, never()).driveForward(500);
	}

	@Test
	public void testDriveBackward() throws IOException {
		roverSocket.driveBackward(350);
		verify(RoverSocket.roverHandler).driveBackward(350);

	}

	@Test
	public void testDriveBackwardWithKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.driveBackward(500);
		verify(RoverSocket.roverHandler, never()).driveBackward(500);
	}

	@Test
	public void testStop() throws IOException {
		roverSocket.stop();
		verify(RoverSocket.roverHandler).stop();

	}

	@Test
	public void testStopWithKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.stop();
		verify(RoverSocket.roverHandler, never()).stop();
	}

	@Test
	public void testTurnLeft() throws IOException {
		roverSocket.turnLeft(100);
		verify(RoverSocket.roverHandler).turnLeft(100);

	}

	@Test
	public void testTurnLeftKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.turnLeft(100);
		verify(RoverSocket.roverHandler, never()).turnLeft(100);
	}

	@Test
	public void testTurnRight() throws IOException {
		roverSocket.turnRight(100);
		verify(RoverSocket.roverHandler).turnRight(100);

	}

	@Test
	public void testTurnRightKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.turnRight(100);
		verify(RoverSocket.roverHandler, never()).turnRight(100);
	}

	// turnHeadUp
	// turnHeadDown
	// turnHeadLeft
	// turnHeadRight
	// resetHeadPosition
}
