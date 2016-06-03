package de.developgroup.mrf.server;

import com.google.inject.Singleton;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client Manager handles sessions for connected clients.
 * If a clients connects via websocket he will be listed
 * here and gets an ID.
 */
@Singleton
public class ClientManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);
    private static final String TEXT_NOTIFICATION_METHOD = "incomingNotification";

    private static final Map<Integer, Session > sessions = Collections.synchronizedMap( new HashMap<Integer, Session>() );
    private AtomicInteger lastClientId = new AtomicInteger(5000);

    /**
     * Add a client to the list of connected clients.
     * Each client gets an ID, which the client manger
     * communicate to the client via JSON-RPC notification.
     * This ID can be used for further communication if necessary.
     *
     * @param  session Session from Jetty.
     */
    public void addClient(final Session session){
        int clientId = generateClientId();
        sessions.put(clientId, session );
        sendClientId(session, clientId);
        String msg = "new client has connected to server, id: "+clientId;
        notifyAllClients(msg);
    }

    /**
     * Remove closed sessions from listed sessions.
     * If any client disconnects from the websocket
     * he will be removed from the list of connected clients.
     */
    public void removeClosedSessions(){
        Iterator<Session> iter = sessions.values().iterator();
        while (iter.hasNext()) {
            Session session = iter.next();
            if(!session.isOpen()){
                LOGGER.info("Remove session: "+session.getRemoteAddress().toString());
                iter.remove();
            }
        }
    }

    /**
     * Get the amount of connected clients.
     *
     * @return number of connected clients.
     */
    public int getConnectedClientsCount(){
        return sessions.size();
    }

    /**
     * Check if any client is connected to the server.
     *
     * @return true if no client is connected.
     */
    public boolean isNoClientConnected(){
        return sessions.isEmpty();
    }

    /**
     * Check if a specific client is connected to the server.
     *
     * @param  clientId ID of the client given by the client manager.
     */
    public boolean isClientConnected(int clientId){
        Session session = sessions.get(clientId);
        boolean isClientConnected = session != null;
        return isClientConnected;
    }

    /**
     * Notify all connected clients with a custom notification.
     *
     * @param  notification JSON-RPC 2.0 Notification object.
     */
    public void notifyAllClients(JsonRpc2Request notification){
        for( Map.Entry<Integer,Session> entry : sessions.entrySet()){
            int clientId = entry.getKey();
            doSendNotificationToClient(clientId,notification);
        }
    }

    /**
     * Notify all connected clients with a general text notification.
     * A general text notification is a JSON-RPC 2.0 Notification where
     * the destination method is {@value #TEXT_NOTIFICATION_METHOD}
     * and the message string is the parameter.
     *
     * @param  message message text.
     */
    public void notifyAllClients(String message){
        JsonRpc2Request notification = generateNotificationFromText(message);

        notifyAllClients(notification);
    }

    /**
     * Notify a specific connected client with a custom notification.
     *
     * @param  clientId ID of the client given by the client manager.
     * @param  notification JSON-RPC 2.0 Notification object.
     */
    public void notifyClientById(int clientId, JsonRpc2Request notification){
        doSendNotificationToClient(clientId,notification);
    }

    /**
     * Notify a specific clients with a general text notification.
     * A general text notification is a JSON-RPC 2.0 Notification where
     * the destination method is {@value #TEXT_NOTIFICATION_METHOD}
     * and the message string is the parameter.
     *
     * @param  clientId ID of the client given by the client manager.
     * @param  message message text.
     */
    public void notifyClientById(int clientId, String message){
        JsonRpc2Request notification = generateNotificationFromText(message);
        doSendNotificationToClient(clientId,notification);
    }

    private int generateClientId(){
        return lastClientId.getAndIncrement();
    }

    private void sendClientId(final Session session, final int clientId){
        List<Object> params = new ArrayList<>();
        params.add(clientId);
        JsonRpc2Request notification = new JsonRpc2Request("setClientId",params);
        try {
            session.getRemote().sendString(notification.toString());
        } catch (IOException e) {
            LOGGER.error("An error has occurred by sending id to client");
        }
    }

    private void doSendNotificationToClient(int clientId, JsonRpc2Request notification){
        Session session = sessions.get(clientId);
        try {
            session.getRemote().sendString(notification.toString());
        } catch (IOException e) {
            LOGGER.error("An error has occurred by sending notification: "+notification.toString()+" to client with id "+clientId);
        }
    }

    private JsonRpc2Request generateNotificationFromText(String message){
        List<Object> params = new ArrayList<>();
        params.add(message);
        JsonRpc2Request notification = new JsonRpc2Request(TEXT_NOTIFICATION_METHOD,params);

        return notification;
    }

    public static List<Object> paramAsPramList(Object param){
        List<Object> params = new ArrayList<>();
        params.add(param);
        return params;
    }

}
