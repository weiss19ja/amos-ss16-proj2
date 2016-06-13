package de.developgroup.mrf.server;


import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ClientManagerTest {

    private Injector injector;
    private ClientManager clientManager;

    private static Session session;
    private static RemoteEndpoint remoteEndpoint;

    private String sendFirstClientMsg = "{\"method\":\"setClientId\",\"params\":[5000],\"jsonrpc\":\"2.0\"}";
    private String sendSecondClientMsg = "{\"method\":\"setClientId\",\"params\":[5000],\"jsonrpc\":\"2.0\"}";

    @BeforeClass
    public static void setUpBeforeAll() throws IOException {

    }

    @Before
    public void setUp() throws Exception {
        //injector = Guice.createInjector();
        //clientManager = injector.getInstance(ClientManager.class);
        clientManager = new ClientManager();

        // mocking session
        session= mock(Session.class);
        remoteEndpoint = mock(RemoteEndpoint.class);
        when(session.getRemote()).thenReturn(remoteEndpoint);
        when(session.getRemoteAddress()).thenReturn(new InetSocketAddress(0));
        when(session.isOpen()).thenReturn(false);
    }

    @After
    public void tearDown() throws Exception {
        clientManager.removeClosedSessions();
        injector = null;
    }

    @Test
    public void testConnectedCllientCountIsEmpty(){
        int size = clientManager.getConnectedClientsCount();
        assertEquals(0, size);
    }

    @Test
    public void testNoClientConnectedAtBegin(){
        assertTrue(clientManager.isNoClientConnected());
    }

    @Test
    public void testAddAndRemoveClients() throws IOException {
        clientManager.addClient(session);
        verify(remoteEndpoint).sendString(sendFirstClientMsg);
        assertEquals(1,clientManager.getConnectedClientsCount());

        clientManager.addClient(session);
        verify(remoteEndpoint).sendString(sendSecondClientMsg);
        assertEquals(2,clientManager.getConnectedClientsCount());

        // remove all session
        verify(remoteEndpoint,atLeastOnce()).sendString(anyString());
        clientManager.removeClosedSessions();
        assertEquals(0,clientManager.getConnectedClientsCount());
    }

    @Test
    public void testNotifyClientByIdText() throws IOException {
        clientManager.addClient(session);
        // send id to client
        verify(remoteEndpoint,atLeastOnce()).sendString(sendFirstClientMsg);
        // notification that a new client has connected to the server
        verify(remoteEndpoint,atLeastOnce()).sendString(anyString());

        clientManager.notifyClientById(5000,"Test Another Notification");
        String notificationMsg = "{\"method\":\"incomingNotification\",\"params\":[\"Test Another Notification\"],\"jsonrpc\":\"2.0\"}";
        verify(remoteEndpoint).sendString(notificationMsg);

    }

    @Test
    public void testNotifyClientById() throws IOException {
        clientManager.addClient(session);
        // send id to client
        verify(remoteEndpoint,atLeastOnce()).sendString(sendFirstClientMsg);
        // notification that a new client has connected to the server
        verify(remoteEndpoint,atLeastOnce()).sendString(anyString());

        List<Object> params = new ArrayList<>();
        params.add("testParam");
        JsonRpc2Request notification = new JsonRpc2Request("Notification 123",params);
        clientManager.notifyClientById(5000,notification);

        String notificationMsg = "{\"method\":\"Notification 123\",\"params\":[\"testParam\"],\"jsonrpc\":\"2.0\"}";
        verify(remoteEndpoint,atLeastOnce()).sendString(notificationMsg);

    }

    @Test
    public void testNotifyAllClientsText() throws IOException {
        clientManager.addClient(session);
        clientManager.addClient(session);

        clientManager.notifyAllClients("Test Notification");
        String notificationMsg = "{\"method\":\"incomingNotification\",\"params\":[\"Test Notification\"],\"jsonrpc\":\"2.0\"}";
        verify(remoteEndpoint,times(2)).sendString(notificationMsg);
    }

    @Test
    public void testNotifyAllClients() throws IOException {
        clientManager.addClient(session);
        clientManager.addClient(session);

        List<Object> params = new ArrayList<>();
        params.add("testParam");
        JsonRpc2Request notification = new JsonRpc2Request("Notification 456",params);

        clientManager.notifyAllClients(notification);
        String notificationMsg = "{\"method\":\"Notification 456\",\"params\":[\"testParam\"],\"jsonrpc\":\"2.0\"}";
        verify(remoteEndpoint,times(2)).sendString(notificationMsg);
    }

    @Test
    public void testIsClientConnected(){
        clientManager.addClient(session);
        assertTrue(clientManager.isClientConnected(5000));
    }

    @Test
    public void testSetClientInformation() {
        clientManager.addClient(session);

        clientManager.setClientInformation(5000, "1234", "Firefox", "Windows");

        Map<Integer, String> clientInfo = clientManager.getClientInformation();
        assertTrue("clientInfo should cointain exactly one element after inserting one", clientInfo.size() == 1);

        String additionalInformation = clientInfo.get(5000);
        assertNotNull("clientInfo should contain addedClient ",additionalInformation);
    }

    @Test
    public void testSetClientInformationContent() {
        clientManager.addClient(session);

        clientManager.setClientInformation(5000, "1234", "Firefox", "Windows");

        Map<Integer, String> clientInfo = clientManager.getClientInformation();

        String additionalInformation = clientInfo.get(5000);

        assertTrue("additional information should contain browser",additionalInformation.contains("Firefox"));
        assertTrue("additional information should contain operating system",additionalInformation.contains("Windows"));
    }

}
