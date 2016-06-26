package de.developgroup.mrf.server;

import de.developgroup.mrf.server.handler.ClientInformation;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import javafx.beans.Observable;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;
import java.util.Map;
import java.util.Observer;

public interface ClientManager{
	/**
	 * Getter for sessions
	 *
	 * @return Map that contains all active sessions and ids
	 */
	Map<Integer, Session> getSessions();

	int addClient(Session session);

	void removeClosedSessions();

	int getConnectedClientsCount();

	boolean isNoClientConnected();

	boolean isClientConnected(int clientId);

	void notifyAllClients(JsonRpc2Request notification);

	void notifyAllClients(String message);

	void notifyClientById(int clientId, JsonRpc2Request notification);

	void notifyClientById(int clientId, String message);

	void setClientInformation(int clientId, String browser, String operatingSystem);

	List<ClientInformation> getBlockedConnections();

	List<ClientInformation> getUnblockedConnections();

	void blockIp(String ipAddress);

	void unblockIp(String ipAddress);

	boolean clientIsBlocked(String ipAddress);

	void addObserver(Observer o);

	boolean clientIdIsBlocked(int clientId);

	void releaseDriverIfBlocked();
}
