package de.developgroup.mrf.server.handler;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClientInformation {
    private String ipAddress = "";
    private int numberOfConnections = 0;
    private LinkedList<Integer> clientIds = new LinkedList<>();
    private String operatingSystem = "";
    private LinkedList<String> browsers = new LinkedList<>();

    public ClientInformation() {
    }

    public LinkedList<Integer> getClientIds() {
        return clientIds;
    }

    public boolean containsClientId(int clientId) {
        return clientIds.contains(clientId);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public LinkedList<String> getBrowsers() {
        return browsers;
    }


    public void addBrowser(String browser) {
        if(!browsers.contains(browser)) {
            browsers.add(browser);
        }
    }

    public void addClientId(int clientId) {
        clientIds.add(clientId);
        numberOfConnections = clientIds.size();
    }

    public void removeClientId(int clientId) {
        int indexToRemove = clientIds.indexOf(clientId);
        clientIds.remove(indexToRemove);
        numberOfConnections = clientIds.size();
    }

    /**
     * Method to check whether client still maintains a connection
     *
     * @return Boolean, true if no connection belongs to this ip,
     * false if there are still some connections
     */
    public boolean hasNoClientId() {
        if (clientIds.size() > 0) {
            return false;
        }
        return true;
    }
}
