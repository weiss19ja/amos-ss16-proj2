package de.developgroup.mrf.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import de.developgroup.mrf.server.rpc.msgdata.RoverStatusVO;

public class SingleDriverHandlerImpl implements SingleDriverHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SingleDriverHandlerImpl.class);

	@Inject
	private static ClientManager clientManager;
	private int currentDriverId = 0;

	public SingleDriverHandlerImpl() {

	}

	@Override
	public void acquireDriver(int clientId) {
		// TODO Auto-generated method stub
		this.currentDriverId = clientId;

		sendClientNotification();

	}

	@Override
	public void releaseDriver(int clientId) {
		// TODO Auto-generated method stub
		this.currentDriverId = 0;
		// Stop?
		sendClientNotification();

	}

	@Override
	public void verifyDriverAvailability() {
		// TODO Auto-generated method stub

		// falls nicht --> stop

	}

	private void sendClientNotification() {
		RoverStatusVO roverState = new RoverStatusVO();
		roverState.currentDriverId = currentDriverId;
		JsonRpc2Request notification = new JsonRpc2Request("updateRoverState",
				roverState);
		clientManager.notifyAllClients(notification);
	}

}
