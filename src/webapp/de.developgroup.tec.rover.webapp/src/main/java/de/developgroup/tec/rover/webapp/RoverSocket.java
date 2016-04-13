package de.developgroup.tec.rover.webapp;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.developgroup.tec.rover.webapp.rpc.JsonRpcSocket;
import de.developgroup.tec.rover.webapp.rpc.Watchdog;

public class RoverSocket extends JsonRpcSocket {

	private static final long WATCHDOG_TIMEOUT_MS = 1000L;
	private static final Logger LOG = LoggerFactory.getLogger(RoverSocket.class);

	private final Watchdog watchdog;

	public RoverSocket() {
		watchdog = new Watchdog(WATCHDOG_TIMEOUT_MS);

		watchdog.addHandler(() -> {
			try {
				stop();
			} catch (IOException e) {
				LOG.error("Could not stop rover", e);
			}
		});
	}

	/**
	 * Immediately stop the rover.
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException {
		LOG.trace("Stopping rover");
		AbstractServer.INSTANCE.getController().stop();
	}

	/**
	 * Resets the communication watchdog. This method must be called regularly, at least every
	 * {@value #WATCHDOG_TIMEOUT_MS} ms. Otherwise, the rover is stopped.
	 */
	public void resetWatchdog() {
		LOG.trace("Reset watchdog");
		watchdog.reset();
	}

	/**
	 * Take a camera snapshot and return the file name of the image.
	 * 
	 * @param cameraID
	 *            The ID of the camera.
	 * 
	 * @return The image file name.
	 */
	public String takeSnapshot(Number cameraID) throws IOException {
		final int cameraIDValue = cameraID.intValue();

		LOG.trace("Take snapshot for camera {}", cameraIDValue);

		checkCameraID(cameraIDValue);

		final String path = AbstractServer.INSTANCE.getController().takeSnapshot();
		LOG.trace("Snapshot has been taken: {}", path);
		return path;
	}

	/**
	 * Open a video stream from the camera.
	 * 
	 * @param cameraID
	 *            The ID of the camera.
	 * 
	 * @return The TCP/IP port.
	 */
	public int openVideoStream(Number cameraID) throws IOException {
		final int cameraIDValue = cameraID.intValue();

		LOG.trace("Open video stream for camera {}", cameraIDValue);

		checkCameraID(cameraIDValue);

		final int port = AbstractServer.INSTANCE.getController().openVideoStream();
		LOG.trace("Video stream opened on port: {}", port);
		return port;
	}

	private void checkCameraID(int cameraID) {
		if (cameraID != 0) {
			throw new IllegalArgumentException("Camera " + cameraID + " is not supported");
		}
	}

	/**
	 * Set the speed value.
	 * 
	 * @param speed
	 *            The speed value. Allowed rage is -1000 to 1000. A value of 1000 means maximum speed forwards. A value
	 *            of -1000 means maximum speed backwards. A value of 0 means no movement.
	 * 
	 * @throws IOException
	 */
	public void setSpeed(Number speed) throws IOException {
		final int speedValue = speed.intValue();
		LOG.trace("Set speed to {} %", speedValue / 10);
		AbstractServer.INSTANCE.getController().setSpeed(speedValue);
	}

	/**
	 * Set the turn rate. The turn rate may be independent of the actual speed {@link #setSpeed(Number)} of the rover,
	 * i.e., the rover may be able to turn in place. However, the turn rate may influence the maximum achievable speed,
	 * e.g., a tracked vehicle is not able to achieve its maximum speed if the turn rate is non-zero.
	 *
	 * @param turnRate
	 *            The turn rate. Allowed rage is -1000 to 1000. A value of 1000 means maximum turn rate clockwise. A
	 *            value of -1000 means maximum turn rate counter-clockwise. A value of 0 means no turning.
	 * @throws IOException
	 */
	public void setTurnRate(Number turnRate) throws IOException {
		final int turnRateValue = turnRate.intValue();
		LOG.trace("Set turn rate to {} %", turnRateValue / 10);
		AbstractServer.INSTANCE.getController().setTurnRate(turnRateValue);
	}

	/**
	 * Set the pan angle of a camera.
	 * 
	 * @param cameraID
	 *            The ID of the camera.
	 * @param panAngle
	 *            The desired angle measured in arc seconds. A positive value means an angle in clockwise direction.
	 * @throws IOException
	 */
	public void setPan(Number cameraID, Number panAngle) throws IOException {
		final int cameraIDValue = cameraID.intValue();
		final int panAngleValue = panAngle.intValue();

		LOG.trace("Set pan to {} '' for camera {}", panAngleValue, cameraIDValue);

		checkCameraID(cameraIDValue);

		AbstractServer.INSTANCE.getController().setPan(panAngleValue);
	}

	/**
	 * Set the tilt angle of a camera.
	 * 
	 * @param cameraID
	 *            The ID of the camera.
	 * @param tiltAngle
	 *            The desired angle measured in arc seconds. A positive value means an angle above neutral position.
	 * @throws IOException
	 */
	public void setTilt(Number cameraID, Number tiltAngle) throws IOException {
		final int cameraIDValue = cameraID.intValue();
		final int tiltAngleValue = tiltAngle.intValue();

		LOG.trace("Set tilt to {} '' for camera {}", tiltAngleValue, cameraIDValue);

		checkCameraID(cameraIDValue);

		AbstractServer.INSTANCE.getController().setTilt(tiltAngleValue);
	}

	@Override
	public void onWebSocketConnect(Session sess) {

		watchdog.start();

		try {
			AbstractServer.INSTANCE.getController().init();
		} catch (IOException e) {
			LOG.error("Could not init rover", e);
		}

		super.onWebSocketConnect(sess);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);

		watchdog.shutdown();

		try {
			AbstractServer.INSTANCE.getController().close();
		} catch (IOException e) {
			LOG.error("Could not close rover", e);
		}
	}

}