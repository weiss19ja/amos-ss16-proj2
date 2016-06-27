/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

public interface SingleDriverHandler {

	/**
	 * Acquires exclusive driver mode. There can only be one driver at a time so
	 * this method must check for existing drivers and notify all clients about
	 * the new (or old) driver.
	 * 
	 * @param clientId
	 *            that acquires the driver mode
	 * @return void
	 */
	public void acquireDriver(int clientId);

	/**
	 * release exclusive driver mode. All clients get notified about released
	 * driver mode. Another client might now control the rover (must call
	 * aquireDriver()).
	 * 
	 * @param clientId
	 *            that releases the driver mode
	 * @return void
	 */
	public void releaseDriver(int clientId);

	/**
	 * Gets called everytime a connection closes and verifies that driver is
	 * still available in websocket connection pool. When driver closes
	 * connection (i.e. close browser) the driver must be released.
	 * 
	 * @return void
	 */
	public void verifyDriverAvailability();

	/**
	 * Get client id of the current driver
	 * 
	 * @return current driver clientId
	 */
	public int getCurrentDriverId();

	void sendClientNotification();
}
