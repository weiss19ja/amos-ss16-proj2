package de.developgroup.mrf.server.handler;

public interface NotificationHandler {

    public void distributeNotification(String message);

    public void distributeAlertNotification(String message);

    public void distributeErrorNotification(String message);
}
