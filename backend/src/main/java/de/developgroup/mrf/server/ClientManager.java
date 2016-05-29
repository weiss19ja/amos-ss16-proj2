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

    private static final Map<Integer, Session > sessions = Collections.synchronizedMap( new HashMap<Integer, Session>() );
    private AtomicInteger lastClientId = new AtomicInteger(5000);

    public void addClient(final Session session){
        int clientId = generateClientId();
        sessions.put(clientId, session );
        try {
            sendClientId(session, clientId);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return false;
    }

    public void notifyAllClients(String msg){

    }

    public void notifyClientById(int clientId, String msg){

    }

    private int generateClientId(){
        return lastClientId.getAndIncrement();
    }

    private void sendClientId(final Session session, final int clientId) throws IOException{
        List<Object> params = new ArrayList<>();
        params.add(clientId);
        JsonRpc2Request notification = new JsonRpc2Request("setClientId",params);
        session.getRemote().sendString(notification.toString());
    }

}
