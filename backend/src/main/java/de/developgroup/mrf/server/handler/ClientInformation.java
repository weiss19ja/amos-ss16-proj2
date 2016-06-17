package de.developgroup.mrf.server.handler;

import java.util.LinkedList;

public class ClientInformation {
    private String ipAddress = "";
    private int numberOfConnections = 0;
    private LinkedList<Integer> clientIds = new LinkedList<>();
    private String operatingSystem = "";
    private String[] browsers = new String[0];

    public ClientInformation() {
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

    public String[] getBrowsers() {
        return browsers;
    }

    public void setBrowsers(String[] browsers) {
        this.browsers = browsers;
    }

    public void addClientId(int clientId) {
        clientIds.add(clientId);
        numberOfConnections = clientIds.size();
    }

    public void removeClientId(int clientId) {
        clientIds.remove(clientId);
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
