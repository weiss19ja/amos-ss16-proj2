/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import de.developgroup.mrf.server.ClientManager;
import junit.runner.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ClientInformationHandlerTest {
    ClientInformationHandler handler;

    @Before
    public void setUp() {
        handler = new ClientInformationHandler();
        System.out.println("Set up handler");
//                mock(ClientManager.class)

    }

    @After
    public void tearDown() {
    }


    @Test
    public void testIsBlockedUnknownIp() throws IOException {
        boolean isBlocked = handler.isBlocked("ipAddressThatIsNotContainedInList");
        assertFalse(isBlocked);
    }

    @Test
    public void testClientInformationListEmpty() throws IOException {
        List<ClientInformation> clientInformationList = handler.getClientInformationList();
        assertEquals("clientInformationList should initially be empty", 0, clientInformationList.size());
    }

    @Test
    public void testGetClientInformationAddUserWithUnregisteredIp() throws IOException {
        handler.addClientInformation(1337, "Firefox", "Windows");

        List<ClientInformation> clientInformationList = handler.getClientInformationList();

        assertEquals("clientInformationList should contain no intems after inserting information " +
                "with unregistered ip, but contains " + clientInformationList.size(), 0, clientInformationList.size());
    }

    @Test
    public void testGetClientInformationAddUserWithKnownIp() throws IOException {
        handler.addConnection("192.168.0.42", 1337);
        handler.addClientInformation(1337, "Firefox", "Windows");

        List<ClientInformation> clientInformationList = handler.getClientInformationList();

        assertEquals("clientInformationList should contain 1 intem after inserting it, but contains " +
                clientInformationList.size(), 1, clientInformationList.size());
        ClientInformation clientInformation = clientInformationList.get(0);
        assertEquals("IpAddres should be the one inserted before", "192.168.0.42", clientInformation.getIpAddress());
        assertEquals("OperatingSystem should be the one inserted before", "Windows", clientInformation.getOperatingSystem());
        List<String> browsers = clientInformation.getBrowsers();
        assertEquals("Browser should contain exactly one element", 1, browsers.size());
        assertEquals("Browser should be the one inserted before", "Firefox", (String) browsers.get(0));
        List<Integer> clientIds = clientInformation.getClientIds();
        assertTrue("ClientId should contain exactly one element", clientIds.size() == 1);
        assertEquals("ClientId should be the one inserted before", 1337, (int) clientIds.get(0));
    }

    @Test
    public void testNoConnectedClientsOnStartup() throws IOException{
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should contain no items on startup, " +
                "but contains " + blockedConnections.size(), 0, blockedConnections.size());

        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("unblockedConnections should contain no items on startup, " +
                "but contains " + unblockedConnections.size(), 0, unblockedConnections.size());

    }

    @Test
    public void testAddConnection() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after adding a connection, the unblocked connections list should contain one item",
                unblockedConnections.size(), 1);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should not contain any items after adding a new connections, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 0);
    }

    @Test
    public void testBlockIp() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        handler.blockIp("192.168.0.42");

        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after blocking the connection, the unblocked connections list should be empty, " +
                "but contains " + unblockedConnections.size(), unblockedConnections.size(), 0);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should contain 1 item after blocking an existing connection, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 1);
    }

    @Test
    public void testUnblockIp() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        handler.blockIp("192.168.0.42");
        handler.unblockIp("192.168.0.42");

        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after unblocking the connection, the unblocked connections list should contain one item",
                unblockedConnections.size(), 1);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should not contain any items after unblocking the connection, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 0);
    }

    @Test
    public void testBlockUnconnectedIp() throws IOException{
        handler.blockIp("192.168.0.42");
        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after blocking the connection, the unblocked connections list should be empty, " +
                "but contains " + unblockedConnections.size(), unblockedConnections.size(), 0);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should contain 1 item after blocking an existing connection, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 1);
    }

    @Test
    public void testAddMultipleClientIds() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        handler.addConnection("192.168.0.42", 1338);
        handler.addConnection("192.168.0.42", 1339);

        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after adding connections with the same ip, the unblocked connections list should contain one entry, " +
                "but contains " + unblockedConnections.size(), unblockedConnections.size(), 1);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should contain no items, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 0);


        ClientInformation clientInformation = unblockedConnections.get(0);
        assertEquals("After adding three clientIds, the clientIdList should" +
                " contain three entries", clientInformation.getClientIds().size(), 3);
    }

    @Test
    public void testRemoveUnblockedConnection() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        handler.addConnection("192.168.0.42", 1338);
        handler.addConnection("192.168.0.42", 1339);
        handler.removeConnection(1337);


        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after removing the connection, the unblocked connections list should contain one entry, " +
                "but contains " + unblockedConnections.size(), unblockedConnections.size(), 1);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should contain no items, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 0);

        ClientInformation clientInformation = unblockedConnections.get(0);
        assertEquals("After removing one of three clientIds, the clientIdList should" +
                " contain two entries", clientInformation.getClientIds().size(), 2);
    }

    @Test
    public void testRemoveUnblockedConnectionLastConnection() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        handler.removeConnection(1337);

        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("after removing the connection, the unblocked connections list should contain one entry, " +
                "but contains " + unblockedConnections.size(), unblockedConnections.size(), 0);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should contain no items, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 0);
    }

    @Test
    public void testRemoveBlockedConnectionLastConnection() throws IOException{
        handler.addConnection("192.168.0.42", 1337);
        handler.blockIp("192.168.0.42");
        handler.removeConnection(1337);

        List<ClientInformation> unblockedConnections = handler.getUnblockedConnections();
        assertEquals("the unblocked connections list should be empty, " +
                "but contains " + unblockedConnections.size(), unblockedConnections.size(), 0);
        List<ClientInformation> blockedConnections = handler.getBlockedConnections();
        assertEquals("blockedConnections should still contain one items, " +
                "but contains " + blockedConnections.size(), blockedConnections.size(), 1);

    }

    //addClientInformation

    // add something to clientInfoList

}
