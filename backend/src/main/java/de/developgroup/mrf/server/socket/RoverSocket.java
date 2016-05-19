package de.developgroup.mrf.server.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.rpc.JsonRpcSocket;

public class RoverSocket extends JsonRpcSocket {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoverSocket.class);

	@Inject
	public static RoverHandler roverHandler;

	public RoverSocket() {
	}

	public String ping(Number sqn) {
		LOGGER.trace("ping({})", sqn);
		return roverHandler.handlePing(sqn.intValue());
	}

	public void driveForward(Number desiredSpeed) {
		LOGGER.trace("driveForeward({})", desiredSpeed);
		LOGGER.info("driveForeward called");
	}

	public void driveBackward(Number desiredSpeed) {
		LOGGER.trace("driveBackward({})", desiredSpeed);
	}
}
