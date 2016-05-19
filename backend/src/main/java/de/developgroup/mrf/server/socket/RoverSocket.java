package de.developgroup.mrf.server.socket;

import java.io.IOException;

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

	public void driveForward(Number desiredSpeed) throws IOException {
		LOGGER.trace("driveForeward({})", desiredSpeed);
		roverHandler.driveForward(desiredSpeed.intValue());
	}

	public void driveBackward(Number desiredSpeed) throws IOException {
		LOGGER.trace("driveBackward({})", desiredSpeed);
		roverHandler.driveBackward(desiredSpeed.intValue());
	}

	public void stop() throws IOException {
		LOGGER.trace("stop()");
		roverHandler.stop();
	}

	public void turnLeft(Number turnRate) throws IOException {
		LOGGER.trace("turnLeft({})", turnRate);
		roverHandler.turnLeft(turnRate.intValue());
	}

	public void turnRight(Number turnRate) throws IOException {
		LOGGER.trace("turnRight({})", turnRate);
		roverHandler.turnRight(turnRate.intValue());
	}
}
