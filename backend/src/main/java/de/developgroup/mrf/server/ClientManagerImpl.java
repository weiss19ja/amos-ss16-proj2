/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.inject.Inject;
import de.developgroup.mrf.server.handler.ClientInformation;
import de.developgroup.mrf.server.handler.ClientInformationHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.developgroup.mrf.server.rpc.JsonRpc2Request;

/**
 * Client Manager handles sessions for connected clients. If a clients connects
 * via websocket he will be listed here and gets an ID.
 */
@Singleton
public class ClientManagerImpl extends Observable implements ClientManager {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ClientManagerImpl.class);
	private static final String TEXT_NOTIFICATION_METHOD = "incomingNotification";

	private static final Map<Integer, Session> sessions = Collections
			.synchronizedMap(new HashMap<Integer, Session>());

	@Inject
	static ClientInformationHandler clientInformationHandler;

	private AtomicInteger lastClientId = new AtomicInteger(5000);


	@Inject
	public ClientManagerImpl(ClientInformationHandler clientInformationHandler){
		this.clientInformationHandler = clientInformationHandler;
	}

	/**
	 * Add a client to the list of connected clients. Each client gets an ID,
	 * which the client manger communicate to the client via JSON-RPC
	 * notification. This ID can be used for further communication if necessary.
	 *
	 * @param session
	 *            Session from Jetty.
	 * @return the newly created client's id
	 */
	@Override
	public int addClient(final Session session) {
		int clientId = generateClientId();
		sessions.put(clientId, session);
		sendClientId(session, clientId);
		String msg = "new client has connected to server, id: " + clientId;
		notifyAllClients(msg);

		String ipAddress = session.getRemoteAddress().getHostString();
		clientInformationHandler.addConnection(ipAddress,clientId);
		// Notify Observers, e.g. Developer Settings Handler so that the connected users list can be updated
		setChanged();
		notifyObservers();
		return clientId;
	}

	@Override
	public Map<Integer, Session> getSessions() {
		return sessions;
	}

	/**
	 * Remove closed sessions from listed sessions. If any client disconnects
	 * from the websocket he will be removed from the list of connected clients.
	 */
	@Override
	public void removeClosedSessions() {
		// New set because otherwise data wouldn't be copied
		Set<Integer> clientIdsBefore = new HashSet<Integer>();
		clientIdsBefore.addAll(sessions.keySet());

		boolean removedSomething = false;
		Iterator<Session> iter = sessions.values().iterator();
		while (iter.hasNext()) {
			Session session = iter.next();
			if (!session.isOpen()) {
				String ipAddress = session.getRemoteAddress().toString();
				LOGGER.info("Remove session: "
						+ session.getRemoteAddress().toString());
				iter.remove();
				removedSomething = true;
			}
		}
		if(removedSomething){
			// Remove unused clientIds from ClientInformation
			Set<Integer> clientIdsAfterwards = sessions.keySet();
			for(int clientId : clientIdsBefore){
				if(! clientIdsAfterwards.contains(clientId)){
					clientInformationHandler.removeConnection(clientId);
				}
			}
			// Notify Observers, e.g. Developer Settings Handler so that the connected users list can be updated
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Get the amount of connected clients.
	 *
	 * @return number of connected clients.
	 */
	@Override
	public int getConnectedClientsCount() {
		return sessions.size();
	}

	/**
	 * Check if any client is connected to the server.
	 *
	 * @return true if no client is connected.
	 */
	@Override
	public boolean isNoClientConnected() {
		return sessions.isEmpty();
	}

	/**
	 * Check if a specific client is connected to the server.
	 *
	 * @param clientId
	 *            ID of the client given by the client manager.
	 */
	@Override
	public boolean isClientConnected(int clientId) {
		Session session = sessions.get(clientId);
		boolean isClientConnected = session != null;
		return isClientConnected;
	}

	/**
	 * Notify all connected clients with a custom notification.
	 *
	 * @param notification
	 *            JSON-RPC 2.0 Notification object.
	 */
	@Override
	public void notifyAllClients(JsonRpc2Request notification) {
		for (Map.Entry<Integer, Session> entry : sessions.entrySet()) {
			int clientId = entry.getKey();
			doSendNotificationToClient(clientId, notification);
		}
	}

	/**
	 * Notify all connected clients with a general text notification. A general
	 * text notification is a JSON-RPC 2.0 Notification where the destination
	 * method is {@value #TEXT_NOTIFICATION_METHOD} and the message string is
	 * the parameter.
	 *
	 * @param message
	 *            message text.
	 */
	@Override
	public void notifyAllClients(String message) {
		JsonRpc2Request notification = generateNotificationFromText(message);

		notifyAllClients(notification);
	}

	/**
	 * Notify a specific connected client with a custom notification.
	 *
	 * @param clientId
	 *            ID of the client given by the client manager.
	 * @param notification
	 *            JSON-RPC 2.0 Notification object.
	 */
	@Override
	public void notifyClientById(int clientId, JsonRpc2Request notification) {
		doSendNotificationToClient(clientId, notification);
	}

	/**
	 * Notify a specific clients with a general text notification. A general
	 * text notification is a JSON-RPC 2.0 Notification where the destination
	 * method is {@value #TEXT_NOTIFICATION_METHOD} and the message string is
	 * the parameter.
	 *
	 * @param clientId
	 *            ID of the client given by the client manager.
	 * @param message
	 *            message text.
	 */
	@Override
	public void notifyClientById(int clientId, String message) {
		JsonRpc2Request notification = generateNotificationFromText(message);
		doSendNotificationToClient(clientId, notification);
	}

	private int generateClientId() {
		return lastClientId.getAndIncrement();
	}

	private void sendClientId(final Session session, final int clientId) {
		List<Object> params = new ArrayList<>();
		params.add(clientId);
		JsonRpc2Request notification = new JsonRpc2Request("setClientId",
				params);
		try {
			session.getRemote().sendString(notification.toString());
		} catch (IOException e) {
			LOGGER.error("An error has occurred by sending id to client");
		}
	}

	private synchronized void doSendNotificationToClient(int clientId,
			JsonRpc2Request notification) {
		Session session = sessions.get(clientId);
		try {
			session.getRemote().sendString(notification.toString());
		} catch (IOException e) {
			LOGGER.error("An error has occurred by sending notification: "
					+ notification.toString() + " to client with id "
					+ clientId);
		}
	}

	private JsonRpc2Request generateNotificationFromText(String message) {
		List<Object> params = new ArrayList<>();
		params.add(message);
		JsonRpc2Request notification = new JsonRpc2Request(
				TEXT_NOTIFICATION_METHOD, params);

		return notification;
	}

	/**
	 * This function stores information about the clients so they can lateron be used to provide the
	 * developer with further information.
	 * @param clientId 	The client's id
	 * @param browser	String containing user's browser
	 * @param operatingSystem	String containiing user's operatingSystem
     */
	@Override
	public void setClientInformation(int clientId, String browser, String operatingSystem) {
		clientInformationHandler.addClientInformation(clientId, browser, operatingSystem);
		setChanged();
		notifyObservers();
	}

	/**
	 * This method returns a list of all blocked clients, grouped by ipAddress
	 * @return
     */
	@Override
	public List<ClientInformation> getBlockedConnections(){
		return clientInformationHandler.getBlockedConnections();
	}

	/**
	 * This method returns a list of all unblocked clients, grouped by ipAddress
	 * @return
	 */
	@Override
	public List<ClientInformation> getUnblockedConnections(){
		return clientInformationHandler.getUnblockedConnections();
	}

	/**
	 * adds the ipAddress to blockedIps list
	 * and informs developerSettingsHandler about change
	 * @param ipAddress
     */
	@Override
	public void blockIp(String ipAddress) {
		clientInformationHandler.blockIp(ipAddress);
		setChanged();
		notifyObservers();
	}

	/**
	 * removes the ipAddress from blockedIps list
	 * and informs developerSettingsHandler about change
	 * @param ipAddress
	 */
	@Override
	public void unblockIp(String ipAddress) {
		clientInformationHandler.unblockIp(ipAddress);
		setChanged();
		notifyObservers();
	}

	/**
	 * looks up whether the specified ipAddress is on the blockedIps list
	 * @param ipAddress
	 * @return
     */
	@Override
	public boolean clientIsBlocked(String ipAddress){
		return clientInformationHandler.isBlocked(ipAddress);
	}

	@Override
	public boolean clientIdIsBlocked(int clientId){
		return clientInformationHandler.isBlocked(clientId);
	}

	@Override
	public void releaseDriverIfBlocked(){
		clientInformationHandler.releaseDriverIfBlocked();
	}
}
