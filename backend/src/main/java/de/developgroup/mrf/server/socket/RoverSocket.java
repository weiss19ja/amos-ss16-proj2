package de.developgroup.mrf.server.socket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.rpc.JsonRpcSocket;

public class RoverSocket extends JsonRpcSocket {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoverSocket.class);

	@Inject
	public static RoverHandler roverHandler;

	@Inject
	private static ClientManager clientManager;

	protected boolean isBlocked = false;

	public RoverSocket() {
	}

	@Override
	public void onWebSocketConnect(final Session sess) {
		super.onWebSocketConnect(sess);
		clientManager.addClient(sess);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		clientManager.removeClosedSessions();
		if (clientManager.isNoClientConnected()) {
			try {
				roverHandler.stop();
			} catch (IOException e) {
				LOGGER.info("stop movement error detected ", e);
			}
		}
	}

	public String ping(Number sqn) {
		LOGGER.trace("ping({})", sqn);
		return roverHandler.handlePing(sqn.intValue());
	}

	public void driveForward(Number desiredSpeed) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("driveForeward({})", desiredSpeed);
		roverHandler.driveForward(desiredSpeed.intValue());
	}

	public void driveBackward(Number desiredSpeed) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("driveBackward({})", desiredSpeed);
		roverHandler.driveBackward(desiredSpeed.intValue());
	}

	public void stop() throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("stop()");
		roverHandler.stop();
	}

	public void turnLeft(Number turnRate) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("turnLeft({})", turnRate);
		roverHandler.turnLeft(turnRate.intValue());
	}

	public void turnRight(Number turnRate) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("turnRight({})", turnRate);
		roverHandler.turnRight(turnRate.intValue());
	}

	public void turnHeadUp(Number angle) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("turnHeadUp({})", angle);
		roverHandler.turnHeadUp(angle.intValue());
	}

	public void turnHeadDown(Number angle) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("turnHeadDown({})", angle);
		roverHandler.turnHeadDown(angle.intValue());
	}

	public void turnHeadLeft(Number angle) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("turnHeadLeft({})", angle);
		roverHandler.turnHeadLeft(angle.intValue());
	}

	public void turnHeadRight(Number angle) throws IOException {
		if(isBlocked()){
			LOGGER.trace("Developer blocked this action");
			return;
		}
		LOGGER.trace("turnHeadRight({})", angle);
		roverHandler.turnHeadRight(angle.intValue());
	}

	/**
	 * Checks whether rover-actions are being blocked by a developer via the killswitch
	 * @return returns true if actions are blocked, false otherwise
     */
	protected boolean isBlocked(){
		return isBlocked();
	}

	/**
	 * Blocks or unblocks rover-actions
	 * @param isBlocked if true, actions get blocked, if false, clients can steer the rover
     */
	protected void setBlocked(boolean isBlocked){
		this.isBlocked = isBlocked;
	}
}
