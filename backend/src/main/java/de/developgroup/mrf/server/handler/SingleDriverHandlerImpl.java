package de.developgroup.mrf.server.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import de.developgroup.mrf.server.rpc.msgdata.RoverStatusVO;

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
		if (currentDriverId == -1) {
			this.currentDriverId = clientId;
			LOGGER.info("user " + clientId + " try to acqurie driver mode");
		} else {
			LOGGER.info("driver mode already taken by user " + currentDriverId);
		}
		sendClientNotification();
	}

	@Override
	public void releaseDriver(int clientId) {

		if (clientId == currentDriverId) {
			doReleaseDriver();
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

	private void sendClientNotification() {
		RoverStatusVO roverState = new RoverStatusVO();
		roverState.currentDriverId = currentDriverId;
		JsonRpc2Request notification = new JsonRpc2Request("updateRoverState",
				roverState);
		clientManager.notifyAllClients(notification);
	}

}
