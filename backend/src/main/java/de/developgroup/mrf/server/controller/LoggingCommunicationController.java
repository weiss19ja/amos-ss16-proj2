package de.developgroup.mrf.server.controller;

import java.io.IOException;
import java.util.List;

public interface LoggingCommunicationController {

	/**
	 * Reads the log file and sends all log entries which are newer than the lastEntry parameter to the client.
	 * @param clientId The clientId from the frontend request.
	 * @param lastEntry Last log entry in the client.
	 * @throws IOException
	 */
	void getLoggingEntries(int clientId, String lastEntry) throws IOException;

	/**
	 * Send the requested log entries back to the client.
	 * @param clientId The clientId from the frontend request.
	 * @param logEntries The log entries to send to the client.
	 * @throws IOException
	 */
	void sendLogEntriesToClient(int clientId, List<String> logEntries) throws IOException;

	/**
	 * This method is called if there are no log entries or no newer ones.
	 * @param clientId The clientId from the frontend request.
	 * @param lastEntry Last log entry in the client.
	 * @throws IOException
	 */
	void handleNoEntryFound(int clientId, String lastEntry) throws IOException;

}
