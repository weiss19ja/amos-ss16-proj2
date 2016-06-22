/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import java.io.IOException;
import java.util.List;

public interface LoggingCommunicationController {

	/**
	 * Reads the log file and sends all log entries which are newer than the lastEntry parameter to the client.
	 * @param clientId The clientId from the frontend request.
	 * @param lastEntry Last log entry in the client.
	 */
	void getLoggingEntries(int clientId, String lastEntry);

	/**
	 * Send the requested log entries back to the client.
	 * @param clientId The clientId from the frontend request.
	 * @param logEntries The log entries to send to the client.
	 * @throws IOException
	 */
	void sendLogEntriesToClient(int clientId, List<String> logEntries);

	/**
	 * This method is called if there are no log entries or no newer ones.
	 * @param clientId The clientId from the frontend request.
	 * @param lastEntry Last log entry in the client.
	 * @throws IOException
	 */
	void handleNoEntryFound(int clientId, String lastEntry);

    /**
     * Gets the uptime of the system and sends it to the client.
     * @param clientId The clientId from the frontend request.
     */
	void getSystemUpTime(int clientId);

    /**
     * This method is called if an error occurs while getting and sending the system uptime.
     * @param clientId The clientId from the frontend request.
     */
    void handleSystemUpTimeException(int clientId, Throwable throwable);

    /**
     * Sends the response from the uptime command back to the client.
     * @param clientId The clientId from the frontend request.
     * @param response The response for the client.
     */
    void sendUpTimeToClient(int clientId, String response);

}
