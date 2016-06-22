package de.developgroup.mrf.server.socket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.handler.DeveloperSettingsHandler;
import de.developgroup.mrf.server.handler.NotificationHandler;
import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.handler.SingleDriverHandler;

import static org.mockito.Mockito.*;

public class RoverSocketTest {

	// using Mock with call real methods in order to be able to mock the remoteIpIsBlocked method
	RoverSocket roverSocket = mock(RoverSocket.class, CALLS_REAL_METHODS);
	ClientManager clientManager = mock(ClientManager.class);
	DeveloperSettingsHandler developerSettingsHandler = mock(DeveloperSettingsHandler.class);
	SingleDriverHandler singleDriverHandler = mock(SingleDriverHandler.class);
	RoverHandler roverHandler = mock(RoverHandler.class);
	NotificationHandler notificationHandler = mock(NotificationHandler.class);


	@Before
	public void setUp() {
		RoverSocket.clientManager = clientManager;
		RoverSocket.developerSettingsHandler = developerSettingsHandler;
		RoverSocket.singleDriverHandler = singleDriverHandler;
		RoverSocket.roverHandler = roverHandler;
		RoverSocket.notificationHandler = notificationHandler;

		// mock method
		doReturn(false).when(roverSocket).remoteIpIsBlocked();
	}

	@After
	public void tearDown() {
	}

	/**
	 * This method simulates having a client-ip that is not blocked by a developer
	 */
	private void setClientAsUnblocked(){
		when(roverSocket.getSession().getRemoteAddress().getHostString()).thenReturn("123.456.789");
		when(clientManager.clientIsBlocked("123.456.789")).thenReturn(false);
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

	@Test
	public void testTurnHeadUp() throws IOException {
		roverSocket.turnHeadUp(30);
		verify(RoverSocket.roverHandler).turnHeadUp(30);

	}

	@Test
	public void testTurnHeadUpKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.turnHeadUp(30);
		verify(RoverSocket.roverHandler, never()).turnHeadUp(30);
	}

	@Test
	public void testTurnHeadDown() throws IOException {
		roverSocket.turnHeadDown(30);
		verify(RoverSocket.roverHandler).turnHeadDown(30);

	}

	@Test
	public void testTurnHeadDownKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.turnHeadDown(30);
		verify(RoverSocket.roverHandler, never()).turnHeadDown(30);
	}

	@Test
	public void testTurnHeadLeft() throws IOException {
		roverSocket.turnHeadLeft(30);
		verify(RoverSocket.roverHandler).turnHeadLeft(30);

	}

	@Test
	public void testTurnHeadLeftKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.turnHeadLeft(30);
		verify(RoverSocket.roverHandler, never()).turnHeadLeft(30);
	}

	@Test
	public void testTurnHeadRight() throws IOException {
		roverSocket.turnHeadRight(30);
		verify(RoverSocket.roverHandler).turnHeadRight(30);

	}

	@Test
	public void testTurnHeadRightKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.turnHeadRight(30);
		verify(RoverSocket.roverHandler, never()).turnHeadRight(30);
	}

	@Test
	public void testResetHeadPosition() throws IOException {
		roverSocket.resetHeadPosition();
		verify(RoverSocket.roverHandler).resetHeadPosition();
	}

	@Test
	public void testResetHeadPositionKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.resetHeadPosition();
		verify(RoverSocket.roverHandler, never()).resetHeadPosition();
	}

	@Test
	public void testSetKillswitch() throws IOException {
		roverSocket.setKillswitch(true, "enabled killswitch for test");
		verify(RoverSocket.developerSettingsHandler).setKillswitchEnabled(true,
				"enabled killswitch for test");
	}

	@Test
	public void testGetCameraSnapshot() throws IOException {
		roverSocket.getCameraSnapshot(5002);
		verify(RoverSocket.roverHandler).getCameraSnapshot(5002);
	}

	@Test
	public void testGetCameraSnapshotKillswitchEnabled() throws IOException {
		when(developerSettingsHandler.checkKillswitchEnabled())
				.thenReturn(true);
		roverSocket.getCameraSnapshot(5003);
		verify(RoverSocket.roverHandler, never()).getCameraSnapshot(5003);
	}

	@Test
	public void testDistributeAlertNotification() {
		roverSocket.distributeAlertNotification("Test alert message");
		verify(RoverSocket.notificationHandler).distributeAlertNotification(
				"Test alert message");
	}

	@Test
	public void testEnterDriverMode() {
		roverSocket.enterDriverMode(5001);
		verify(RoverSocket.singleDriverHandler).acquireDriver(5001);
	}

	@Test
	public void testExitDriverMode() {
		roverSocket.exitDriverMode(5002);
		verify(RoverSocket.singleDriverHandler).releaseDriver(5002);
	}

	@Test
	public void testGetAllLogEntries() throws IOException {
		roverSocket.getLoggingEntries(5002, "");
		verify(RoverSocket.roverHandler).getLoggingEntries(5002, "");
	}

	@Test
	public void testGetSpecificLogEntries() throws IOException {
		roverSocket.getLoggingEntries(5002, "Test");
		verify(RoverSocket.roverHandler).getLoggingEntries(5002, "Test");
	}

	@Test
	public void testGetSystemUpTime() {
		roverSocket.getSystemUpTime(5002);
		verify(RoverSocket.roverHandler).getSystemUpTime(5002);
	}
}
