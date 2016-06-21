package de.developgroup.mrf.server.handler;


import com.google.inject.Inject;
import de.developgroup.mrf.server.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientInformationHandler {

    // Contains Client's IP and additional information
    private static final List<ClientInformation> clientInformationList = Collections
            .synchronizedList(new ArrayList<ClientInformation>());

    private static final List<String> blockedIpsList = Collections
            .synchronizedList(new ArrayList<String>());

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ClientInformationHandler.class);


    @Inject
    public ClientInformationHandler() {
        LOGGER.debug("Creating new instance of ClientInformationHandler");
    }

    public List<ClientInformation> getClientInformationList() {
        return clientInformationList;
    }

    public void blockIp(String ipAddress) {
        blockedIpsList.add(ipAddress);
    }

    public void unblockIp(String ipAddress) {
        int indexToRemove = blockedIpsList.indexOf(ipAddress);
        blockedIpsList.remove(indexToRemove);
    }

    /**
     * This method returns a list of all blocked ips with additional client information.
     * If there are no current connections from the blocked ip, only the ip is shown.
     * If the blocked user is connected, all available information is presented
     *
     * @return List containing information about all blocked users
     */
    public List<ClientInformation> getBlockedConnections() {
        List<ClientInformation> blockedConnections = new ArrayList<>();

        // add all blocked users that are connected
        for (ClientInformation clientInfo : clientInformationList) {
            if (blockedIpsList.contains(clientInfo.getIpAddress())) {
                blockedConnections.add(clientInfo);
            }
        }
        // check if there is a blocked ip that isn't connected
        for (String ipAddress : blockedIpsList) {
            boolean contained = false;
            for (ClientInformation blockedClientInfo : blockedConnections) {
                if (blockedClientInfo.getIpAddress().equals(ipAddress)) {
                    contained = true;
                    break;
                }
            }
            // If the client isn't connected, add his ip without additional information
            if (!contained) {
                ClientInformation unconnectedIp = new ClientInformation();
                unconnectedIp.setIpAddress(ipAddress);
                blockedConnections.add(unconnectedIp);
            }
        }

        return blockedConnections;
    }

    /**
     * This method returns all connected clients that are not blocked.
     * In contrast to getBlockedConnections, only connected Ips get displayed
     *
     * @return
     */
    public List<ClientInformation> getUnblockedConnections() {

        List<ClientInformation> unblockedConnections = new ArrayList<>();
        // add all blocked users that are connected
        for (ClientInformation clientInfo : clientInformationList) {
            if (!blockedIpsList.contains(clientInfo.getIpAddress())) {
                unblockedConnections.add(clientInfo);
            }
        }
        return unblockedConnections;
    }

    public void addConnection(String ipAddress, int clientId) {
        boolean foundExistingObject = false;
        for (ClientInformation item : clientInformationList) {
            if (item.getIpAddress().equals(ipAddress)) {
                // Found the right client object
                foundExistingObject = true;
                item.addClientId(clientId);
                break;
            }
        }
        if (!foundExistingObject) {
            // add a new one
            ClientInformation item = new ClientInformation();
            item.setIpAddress(ipAddress);
            item.addClientId(clientId);
            clientInformationList.add(item);
        }
    }

    public void removeConnection(int clientId) {
        for (ClientInformation item : clientInformationList) {
            if (item.containsClientId(clientId)) {
                // Found the right client object
                item.removeClientId(clientId);
                // Remove if no more connections to this ip exist
                if (item.hasNoClientId()) {
                    clientInformationList.remove(item);
                }
                break;
            }
        }
    }

    public void addClientInformation(int clientId, String browser, String operatingSystem) {
        for (ClientInformation item : clientInformationList) {
            if (item.containsClientId(clientId)) {
                item.addBrowser(browser);
                item.setOperatingSystem(operatingSystem);
            }
        }
    }

    public boolean isBlocked(String ipAddress) {
        return blockedIpsList.contains(ipAddress);
    }
}
