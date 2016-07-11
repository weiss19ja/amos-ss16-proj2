/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

public interface NotificationHandler {

    /**
     * Send a normal notification to all connected clients.
     *
     * @param message message text of the notification
     */
    void distributeNotification(String message);

    /**
     * Send a alert notification to all connected clients.
     *
     * @param message message text of the notification
     */
    void distributeAlertNotification(String message);

    /**
     * Send a error notification to all connected clients.
     *
     * @param message message text of the notification
     */
    void distributeErrorNotification(String message);
}
