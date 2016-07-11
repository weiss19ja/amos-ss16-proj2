/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import com.google.inject.Inject;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;

public class NotificationHandlerImpl implements NotificationHandler {

    private ClientManager clientManager;

    @Inject
    public NotificationHandlerImpl(ClientManager clientManager){
        this.clientManager = clientManager;
    }

    @Override
    public void distributeNotification(String message) {
        JsonRpc2Request notification = new JsonRpc2Request(
                "incomingNotification", message);

        clientManager.notifyAllClients(notification);
    }

    @Override
    public void distributeAlertNotification(String message) {
        JsonRpc2Request notification = new JsonRpc2Request(
                "showAlertNotification", message);

        clientManager.notifyAllClients(notification);
    }

    @Override
    public void distributeErrorNotification(String message) {
        JsonRpc2Request notification = new JsonRpc2Request(
                "showErrorNotification", message);

        clientManager.notifyAllClients(notification);
    }
}
