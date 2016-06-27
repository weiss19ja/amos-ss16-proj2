/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.mrf.server.ClientManagerImpl;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import de.developgroup.mrf.server.rpc.msgdata.RoverStatusVO;

public class DeveloperSettingsHandlerTest {

    DeveloperSettingsHandler handler;

    @Before
    public void setUp() {
        handler = new DeveloperSettingsHandler(mock(ClientManagerImpl.class),
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
    public void testNotifyIfBlocked() throws IOException {
        handler.setKillswitchEnabled(true, "killswitch");
        handler.notifyIfBlocked(1337, "message");
        JsonRpc2Request notification = new JsonRpc2Request(
                "showAlertNotification", "message");
        verify(handler.clientManager).notifyClientById(1337, notification);
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

    @Test
    public void testNotifyClientsAboutConnectedUsers() throws IOException {
        ClientInformation[] unblockedUsers = new ClientInformation[1];
        unblockedUsers[0] = new ClientInformation();
        unblockedUsers[0].setIpAddress("123.456.789");
        unblockedUsers[0].setOperatingSystem("Windows");

        ClientInformation[] blockedUsers = new ClientInformation[1];
        blockedUsers[0] = new ClientInformation();
        blockedUsers[0].setIpAddress("987.654.321");
        blockedUsers[0].setOperatingSystem("MacOS");

        ArrayList<Object> params = new ArrayList<>();
        params.add(unblockedUsers);
        params.add(blockedUsers);

        JsonRpc2Request testNotification;
        testNotification = new JsonRpc2Request("updateConnectedUsers", params);


        handler.notifyClientsAboutConnectedUsers(unblockedUsers, blockedUsers);
        verify(handler.clientManager).notifyAllClients(testNotification);
    }

    // Test whether clients get informed about their own blocking state
    @Test
    public void testUpdateSendsOwnClientState() throws IOException {

        // Setup information
        List<ClientInformation> unblockedUsers = new ArrayList<>();
        ClientInformation unblockedUser = new ClientInformation();
        unblockedUser.setIpAddress("123.456.789");
        unblockedUser.setOperatingSystem("Windows");
        unblockedUser.addBrowser("Firefox");
        unblockedUser.addClientId(1337);
        unblockedUsers.add(unblockedUser);

        List<ClientInformation> blockedUsers = new ArrayList<>();
        ClientInformation blockedUser = new ClientInformation();
        blockedUser.setIpAddress("987.654.321");
        blockedUser.setOperatingSystem("MacOS");
        blockedUser.addBrowser("Opera");
        blockedUser.addClientId(7777);
        blockedUsers.add(blockedUser);

        ArrayList<Object> params = new ArrayList<>();
        params.add("123.456.789");
        params.add(false);
        JsonRpc2Request jsonRpc2RequestUnblocked = new JsonRpc2Request("setMyBlockingState", params);

        ArrayList<Object> paramsBlocked = new ArrayList<>();
        paramsBlocked.add("987.654.321");
        paramsBlocked.add(true);
        JsonRpc2Request jsonRpc2RequestBlocked = new JsonRpc2Request("setMyBlockingState", paramsBlocked);

        // mocking client manager methods
        when(handler.clientManager.getBlockedConnections()).thenReturn(blockedUsers);
        when(handler.clientManager.getUnblockedConnections()).thenReturn(unblockedUsers);
        when(handler.clientManager.clientIsBlocked("123.456.789")).thenReturn(false);
        when(handler.clientManager.clientIsBlocked("987.654.321")).thenReturn(true);

        // test
        handler.update(null, null);

        verify(handler.clientManager).notifyClientById(1337, jsonRpc2RequestUnblocked);
        verify(handler.clientManager).notifyClientById(7777, jsonRpc2RequestBlocked);
    }

    // Test whether clients get list of blocked and unblocked users
    @Test
    public void testUpdateSendsConnectedAndBlockedClientList() throws IOException {

        // Setup information
        List<ClientInformation> unblockedUsers = new ArrayList<>();
        ClientInformation unblockedUser = new ClientInformation();
        unblockedUser.setIpAddress("123.456.789");
        unblockedUser.setOperatingSystem("Windows");
        unblockedUser.addBrowser("Firefox");
        unblockedUser.addClientId(1337);
        unblockedUsers.add(unblockedUser);

        List<ClientInformation> blockedUsers = new ArrayList<>();
        ClientInformation blockedUser = new ClientInformation();
        blockedUser.setIpAddress("987.654.321");
        blockedUser.setOperatingSystem("MacOS");
        blockedUser.addBrowser("Opera");
        blockedUser.addClientId(7777);
        blockedUsers.add(blockedUser);


        // mocking client manager methods
        when(handler.clientManager.getBlockedConnections()).thenReturn(blockedUsers);
        when(handler.clientManager.getUnblockedConnections()).thenReturn(unblockedUsers);

        // test
        handler.update(null, null);
        verify(handler.clientManager).notifyAllClients((JsonRpc2Request)anyObject());
    }
}
