package de.developgroup.mrf.server.handler;

public interface NotificationHandler {

    /**
     * Send a normal notification to all connected clients.
     *
     * @param message message text of the notification
     */
    public void distributeNotification(String message);

    /**
     * Send a alert notification to all connected clients.
     *
     * @param message message text of the notification
     */
    public void distributeAlertNotification(String message);

    /**
     * Send a error notification to all connected clients.
     *
     * @param message message text of the notification
     */
    public void distributeErrorNotification(String message);
}
