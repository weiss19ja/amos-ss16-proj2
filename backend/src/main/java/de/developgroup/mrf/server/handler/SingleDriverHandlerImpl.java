/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import java.io.IOException;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.ClientManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import de.developgroup.mrf.server.rpc.msgdata.RoverStatusVO;

@Singleton
public class SingleDriverHandlerImpl implements SingleDriverHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SingleDriverHandlerImpl.class);

	private ClientManager clientManager;
	private RoverHandler roverHandler;
	private int currentDriverId = -1;

	@Inject
	public SingleDriverHandlerImpl(ClientManager clientManager,
			RoverHandler roverHandler) {
		this.clientManager = clientManager;
		this.roverHandler = roverHandler;
	}

	@Override
	public synchronized void acquireDriver(int clientId) {
		LOGGER.info("user " + clientId + " try to acqurie driver mode");
		if (currentDriverId == -1) {
			this.currentDriverId = clientId;
		} else if (currentDriverId != clientId) {
			LOGGER.info("driver mode already taken by user " + currentDriverId);
		}
		LOGGER.info("current driver is "+ this.currentDriverId);
		sendClientNotification();
	}

	@Override
	public void releaseDriver(int clientId) {

		if (clientId == currentDriverId) {
			doReleaseDriver();
			LOGGER.info("Driver with clientId "+clientId+" got released");
		} else {
			LOGGER.info("release driver mode denied for user " + clientId);
		}

	}

	@Override
	public void verifyDriverAvailability() {
		if (!clientManager.isClientConnected(currentDriverId)) {
			doReleaseDriver();
		}
	}

	@Override
	public int getCurrentDriverId() {
		return currentDriverId;
	}

	/**
	 * Helper Method releases Driver and sends notification about driver mode
	 * availability to all clients. currentDriverId = -1 signals that no client
	 * is current driver.
	 * 
	 * @return void
	 */
	private void doReleaseDriver() {
		this.currentDriverId = -1;
		try {
			roverHandler.stop();
		} catch (IOException e) {
			LOGGER.info("stop movement error detected ", e);
		}
		sendClientNotification();
	}

	@Override
	public void sendClientNotification() {
		RoverStatusVO roverState = new RoverStatusVO();
		roverState.currentDriverId = currentDriverId;
		JsonRpc2Request notification = new JsonRpc2Request("updateRoverState",
				roverState);
		clientManager.notifyAllClients(notification);
	}

}
