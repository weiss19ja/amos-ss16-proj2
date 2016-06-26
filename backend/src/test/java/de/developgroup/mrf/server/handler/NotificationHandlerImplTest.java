/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificationHandlerImplTest {

    private ClientManager clientManager;
    private NotificationHandler notificationHandler;

    @Before
    public void setUp() {
        clientManager = mock(ClientManager.class);
        notificationHandler = new NotificationHandlerImpl(clientManager);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDistributeNotification(){
        String message = "Normal Notification";
        notificationHandler.distributeNotification(message);
        JsonRpc2Request notification = new JsonRpc2Request(
                "incomingNotification", message);

        verify(clientManager).notifyAllClients(notification);
    }

    @Test
    public void testDistributeAlertNotification(){
        String message = "Alert Notification";
        notificationHandler.distributeAlertNotification(message);
        JsonRpc2Request notification = new JsonRpc2Request(
                "showAlertNotification", message);

        verify(clientManager).notifyAllClients(notification);
    }

    @Test
    public void testDistributeErrorNotification(){
        String message = "Error Notification";
        JsonRpc2Request notification = new JsonRpc2Request(
                "showErrorNotification", message);
        notificationHandler.distributeErrorNotification("Error Notification");

        verify(clientManager).notifyAllClients(notification);
    }
}
