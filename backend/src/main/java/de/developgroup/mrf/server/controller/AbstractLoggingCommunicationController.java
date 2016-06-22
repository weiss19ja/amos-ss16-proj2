/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import java.io.*;
import java.util.ArrayList;

public abstract class AbstractLoggingCommunicationController {

	private final String LOGFILENAME = "all.log";
	private final int MAXLOGENTRIES = 50;

	/**
	 * Reads maximal 50 log entries and returns them in an ArrayList
	 * @param lastLogEntry Last log entry which was already fetched. If the parameter is null it will return the first 50 entries.
	 * @return A list of max 50 log entries which are newer than the lastLogEntry
	 * @throws IOException
	 */
	protected ArrayList<String> getNewLogEntries(String lastLogEntry) throws IOException {
		boolean isLastLogEntry = lastLogEntry.isEmpty() || lastLogEntry == null;
		ArrayList<String> resultLogEntries = new ArrayList<>();
		FileInputStream fileInputStream = new FileInputStream(LOGFILENAME);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
		String logEntry;
		while ((logEntry = bufferedReader.readLine()) != null) {
			if(!isLastLogEntry) {
				if(logEntry.equals(lastLogEntry)) {
					isLastLogEntry = true;
				}
				continue;
			}
			resultLogEntries.add(logEntry);
			if(resultLogEntries.size() >= MAXLOGENTRIES) {
				break;
			}
		}
		fileInputStream.close();
		return resultLogEntries.size() > 0 ? resultLogEntries : null;
	}

	protected  String getSystemUpTimeString() throws InterruptedException, IOException {
		String osName = System.getProperty("os.name");
		if(osName.contains("Windows")) {
			return "The uptime command is only available on unix distributions!";
		}
		Process upTimeProcess = Runtime.getRuntime().exec("uptime");
		BufferedReader reader = new BufferedReader(new InputStreamReader(upTimeProcess.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
			builder.append(line);
		}
		upTimeProcess.waitFor();
        return builder.toString();
	}

}
