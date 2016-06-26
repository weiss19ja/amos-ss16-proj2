/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import java.util.List;

public interface ClientInformationHandler {
    List<ClientInformation> getClientInformationList();

    /**
     * Adds the specified ipAddress to the blockedIpsList
     * @param ipAddress
     */
    void blockIp(String ipAddress);

    /**
     * Unblocks specified ipAddress
     * @param ipAddress
     */
    void unblockIp(String ipAddress);

    /**
     * This method returns a list of all blocked ips with additional client information.
     * If there are no current connections from the blocked ip, only the ip is shown.
     * If the blocked user is connected, all available information is presented
     *
     * @return List containing information about all blocked users
     */
    List<ClientInformation> getBlockedConnections();

    /**
     * This method returns all connected clients that are not blocked.
     * In contrast to getBlockedConnections, only connected Ips get displayed
     *
     * @return
     */
    List<ClientInformation> getUnblockedConnections();

    /**
     * Add a connection specified by ipAddress and clientId.
     * If a ClientInformation object with the corresponding ipAddress
     * already exists, the clientId will be added to this object
     * @param ipAddress
     * @param clientId
     */
    void addConnection(String ipAddress, int clientId);

    /**
     * Remove a certain clientId from the connections.
     * @param clientId
     */
    void removeConnection(int clientId);

    /**
     * Adds additional information to the ClientInformation object that contains
     * the corresponding clientId
     * One client might have different browsers, so the new browser is added to the list
     * @param clientId
     * @param browser Additional information about the browser
     * @param operatingSystem Additional information about the operating system
     */
    void addClientInformation(int clientId, String browser, String operatingSystem);

    /**
     * Check whether the specified ipAddress is blocked or not
     * @param ipAddress
     * @return
     */
    boolean isBlocked(String ipAddress);
}
