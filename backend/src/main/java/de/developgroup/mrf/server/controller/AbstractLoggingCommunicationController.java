package de.developgroup.mrf.server.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class AbstractLoggingCommunicationController {

	private final String LOGFILENAME = "all.log";

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
		}
		fileInputStream.close();
		return resultLogEntries.size() > 0 ? resultLogEntries : null;
	}

}
