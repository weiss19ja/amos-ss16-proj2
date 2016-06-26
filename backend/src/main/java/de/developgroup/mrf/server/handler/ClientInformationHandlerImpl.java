/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientInformationHandlerImpl implements ClientInformationHandler {
    // Contains Client's IP and additional information
    private final List<ClientInformation> clientInformationList = Collections
            .synchronizedList(new ArrayList<>());

    private final List<String> blockedIpsList = Collections
            .synchronizedList(new ArrayList<>());

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ClientInformationHandlerImpl.class);

    @Inject
    static SingleDriverHandler singleDriverHandler;




    @Inject
    public ClientInformationHandlerImpl(SingleDriverHandler singleDriverHandler) {
        LOGGER.debug("Creating new instance of ClientInformationHandlerImpl");
        this.singleDriverHandler = singleDriverHandler;
    }

    @Override
    public List<ClientInformation> getClientInformationList() {
        return clientInformationList;
    }

    @Override
    public void blockIp(String ipAddress) {
        blockedIpsList.add(ipAddress);
    }

    @Override
    public void unblockIp(String ipAddress) {
        int indexToRemove = blockedIpsList.indexOf(ipAddress);
        blockedIpsList.remove(indexToRemove);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void addClientInformation(int clientId, String browser, String operatingSystem) {
        for (ClientInformation item : clientInformationList) {
            if (item.containsClientId(clientId)) {
                item.addBrowser(browser);
                item.setOperatingSystem(operatingSystem);
            }
        }
    }

    @Override
    public boolean isBlocked(String ipAddress) {
        return blockedIpsList.contains(ipAddress);
    }
}
