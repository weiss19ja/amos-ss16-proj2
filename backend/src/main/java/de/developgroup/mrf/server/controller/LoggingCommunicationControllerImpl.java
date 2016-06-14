package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoggingCommunicationControllerImpl extends AbstractLoggingCommunicationController implements LoggingCommunicationController {

	private static Logger LOGGER = LoggerFactory.getLogger(LoggingCommunicationControllerImpl.class);

	private final ClientManager clientManager;

	@Inject
	public LoggingCommunicationControllerImpl(ClientManager clientManager) throws IOException {
		this.clientManager = clientManager;
	}

	@Override
	public void getLoggingEntries(int clientId, String lastEntry) throws IOException {
		LOGGER.info("Get log entries from client with id: {} and last entry: {}", clientId, lastEntry);
		ArrayList<String> result = getNewLogEntries(lastEntry);
		if(result != null) {
			sendLogEntriesToClient(clientId, result);
		} else {
			handleNoEntryFound(clientId, lastEntry);
		}
	}

	@Override
	public void sendLogEntriesToClient(int clientId, List<String> logEntries) throws IOException {
		LOGGER.info("Sending log entries response to client {}", clientId);
		Boolean hasNewEntries = false;
		if(logEntries != null) {
			hasNewEntries = true;
		}
		List<Object> params = new ArrayList<>();
		params.add(hasNewEntries);
		if(hasNewEntries) {
			for (String entry : logEntries) {
				params.add(entry);
			}
		}
		JsonRpc2Request notification = new JsonRpc2Request("incomingLogEntries",params);
		clientManager.notifyClientById(clientId, notification);
	}

	@Override
	public void handleNoEntryFound(int clientId, String lastEntry) throws IOException {
		LOGGER.info("There is no log entry for the client: {} with the last entry: {}.", clientId, lastEntry);
		sendLogEntriesToClient(clientId, null);
	}
}
