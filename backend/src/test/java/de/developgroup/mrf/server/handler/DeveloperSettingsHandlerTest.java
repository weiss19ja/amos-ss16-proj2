package de.developgroup.mrf.server.handler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import de.developgroup.mrf.server.rpc.msgdata.RoverStatusVO;

public class DeveloperSettingsHandlerTest {

	DeveloperSettingsHandler handler;

	@Before
	public void setUp() {
		handler = new DeveloperSettingsHandler(mock(ClientManager.class),
				mock(RoverHandler.class));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testSetKillswitch() throws IOException {
		handler.setKillswitchEnabled(true, "message");
		assertTrue("killswitchEnabled should be true after setting it to true",
				handler.killswitchEnabled);

		handler.setKillswitchEnabled(false, "message");
		assertFalse(
				"killswitchEnabled should be false after setting it to true",
				handler.killswitchEnabled);
	}

	@Test
	public void testCheckKillswitchEnabled() throws IOException {
		handler.setKillswitchEnabled(true, "message");
		assertEquals(
				"checkKillswitchEnabled and isKillswitchEnabled should always give the same result",
				handler.checkKillswitchEnabled(), handler.isKillswitchEnabled());

		handler.setKillswitchEnabled(false, "message");
		assertEquals(
				"checkKillswitchEnabled and isKillswitchEnabled should always give the same result",
				handler.checkKillswitchEnabled(), handler.isKillswitchEnabled());
	}

	@Test
	public void testNotifyClientsAboutButtonState() throws IOException {
		JsonRpc2Request testNotification;
		RoverStatusVO roverState = new RoverStatusVO();

		roverState.isKillswitchEnabled = true;
		testNotification = new JsonRpc2Request("updateRoverState", roverState);
		handler.setKillswitchEnabled(true, "testMessage");
		verify(handler.clientManager).notifyAllClients(testNotification);

		roverState.isKillswitchEnabled = false;
		testNotification = new JsonRpc2Request("updateRoverState", roverState);
		handler.setKillswitchEnabled(false, "testMessage");
		verify(handler.clientManager).notifyAllClients(testNotification);

	}
}
