package de.developgroup.mrf.server;

import com.google.inject.Singleton;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ClientManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);
    private static final String TEXT_NOTIFICATION_METHOD = "incomingNotification";

    private static final Map<Integer, Session > sessions = Collections.synchronizedMap( new HashMap<Integer, Session>() );
    private AtomicInteger lastClientId = new AtomicInteger(5000);

    public void addClient(final Session session){
        int clientId = generateClientId();
        sessions.put(clientId, session );
        sendClientId(session, clientId);
        String msg = "new client has connected to server, id: "+clientId;
        notifyAllClients(msg);
    }

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

    public int getConnectedClientsCount(){
        return sessions.size();
    }

    public boolean isNoClientConnected(){
        return sessions.isEmpty();
    }

    public boolean isClientConnected(int clientId){
        Session session = sessions.get(clientId);
        boolean isClientConnected = session != null;
        return isClientConnected;
    }

    public void notifyAllClients(JsonRpc2Request notification){
        for( Map.Entry<Integer,Session> entry : sessions.entrySet()){
            int clientId = entry.getKey();
            doSendNotificationToClient(clientId,notification);
        }
    }

    public void notifyAllClients(String message){
        JsonRpc2Request notification = generateNotificationFromText(message);

        notifyAllClients(notification);
    }

    public void notifyClientById(int clientId, JsonRpc2Request notification){
        doSendNotificationToClient(clientId,notification);
    }

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

}
