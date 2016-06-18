package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoggingCommunicationControllerImpl extends AbstractLoggingCommunicationController implements LoggingCommunicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCommunicationControllerImpl.class);

    private final ClientManager clientManager;

    @Inject
    public LoggingCommunicationControllerImpl(ClientManager clientManager) throws IOException {
        this.clientManager = clientManager;
    }

    @Override
    public void getLoggingEntries(int clientId, String lastEntry) {
        LOGGER.info("Get log entries from client with id: {} and last entry: {}", clientId, lastEntry);
        new Thread(() -> {
            try {
                boolean isFirstIteration = true;
                ArrayList<String> result;
                String newLastEntry = lastEntry;
                do {
                    result = getNewLogEntries(newLastEntry);
                    if (result != null) {
                        newLastEntry = result.get(result.size() - 1);
                        sendLogEntriesToClient(clientId, result);
                        isFirstIteration = false;
                    } else {
                        if (isFirstIteration) {
                            handleNoEntryFound(clientId, lastEntry);
                        }
                    }

                } while (result != null);
            } catch (IOException ioExc) {
                LOGGER.error("IOException while getting and sending log entries async:\n{}", ioExc.toString());
                ioExc.printStackTrace();
            }
        }).start();
    }

    @Override
    public void sendLogEntriesToClient(int clientId, List<String> logEntries) {
        Boolean hasNewEntries = false;
        if (logEntries != null) {
            hasNewEntries = true;
        }
        List<Object> params = new ArrayList<>();
        params.add(hasNewEntries);
        if (hasNewEntries) {
            for (String entry : logEntries) {
                params.add(entry);
            }
        }
        sendResponseToClient(clientId, "incomingLogEntries", params);
    }

    @Override
    public void handleNoEntryFound(int clientId, String lastEntry) {
        LOGGER.info("There is no log entry for the client: {} with the last entry: {}.", clientId, lastEntry);
        sendLogEntriesToClient(clientId, null);
    }

    @Override
    public void getSystemUpTime(int clientId) {
        LOGGER.info("Get system up time and send it back to client: {}", clientId);
        new Thread(() -> {
            try {
                sendUpTimeToClient(clientId, getSystemUpTimeString());
            } catch (IOException ioExc) {
                LOGGER.error("IOException while executing uptime terminal command async:\n{}", ioExc.toString());
                ioExc.printStackTrace();
            } catch (InterruptedException inExc) {
                LOGGER.error("InterruptedException while waiting for uptime command execution async:\n{}", inExc.toString());
                inExc.printStackTrace();
            }
        }).start();
    }

    @Override
    public void handleSystemUpTimeException(int clientId, Throwable throwable) {
        if(throwable instanceof IOException) {
            LOGGER.error("IOException while executing uptime terminal command async:\n{}", throwable.toString());
        } else if (throwable instanceof InterruptedException) {
            LOGGER.error("InterruptedException while waiting for uptime command execution async:\n{}", throwable.toString());
        } else {
            LOGGER.error("An exception occurred while trying to get the system uptime.");
        }
        throwable.printStackTrace();
        sendUpTimeToClient(clientId, "ERROR");
    }

    @Override
    public void sendUpTimeToClient(int clientId, String response) {
        List<Object> params = new ArrayList<>();
        params.add(response);
        sendResponseToClient(clientId, "incomingUpTime", params);
    }

    private void sendResponseToClient(int clientId, String method, List<Object> params) {
        JsonRpc2Request notification = new JsonRpc2Request(method, params);
        clientManager.notifyClientById(clientId, notification);
    }
}
