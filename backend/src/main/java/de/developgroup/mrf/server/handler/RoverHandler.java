/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import java.io.IOException;
import java.util.Date;
import java.util.Observer;

import org.cfg4j.provider.ConfigurationProvider;

public interface RoverHandler extends Observer {

	/**
	 * Demonstration method that increments the squence number and responds with a string
	 * @param sqn sequence number that will be incremented
	 * @return the string "pong " + (sequence number + 1)
	 */
	String handlePing(int sqn);

	/**
	 * Heartbeat from clients. If client doesn't send a heartbeat rpc the websocket disconnects on timeout.
	 * @param clientId
     */
	void heartbeat(int clientId);

	/**
	 * Drive forward with the desired speed. Driving forward with negative speeds means driving backwards.
	 * @param desiredSpeed speed (positive for forward driving)
	 * @throws IOException
	 */
	void driveForward(int desiredSpeed) throws IOException;

	/**
	 * Drive backward with the desired speed. Driving backward with a negative speed means driving forward.
	 * @param desiredSpeed speed (positive for backward driving)
	 * @throws IOException
	 */
	void driveBackward(int desiredSpeed) throws IOException;

	/**
	 * Stop all movement.
	 * @throws IOException
	 */
	void stop() throws IOException;

	/**
	 * Turn left at a given speed. Turning left with a negative turnRate means turning right.
	 * @param turnRate the speed at which the rover should turn.
	 * @throws IOException
	 */
	void turnLeft(int turnRate) throws IOException;

	/**
	 * Turn right at a given speed. Turning right with a negative turnRate means turning right.
	 * @param turnRate
	 * @throws IOException
	 */
	void turnRight(int turnRate) throws IOException;

	/**
	 * Set driving direction on speed not in a discrete (forward. left. right. back) but in a continuous mode based
	 * on turning angle and speed.
	 *
	 * Stopping can be achieved via setting the speed to 0, regardless of turning angle.
	 * @param angle Angle in degrees. 0° means "right", 90° means "forward"
	 * @param speed Speed between 0 (stop) and 100 (full power)
	 */
	void driveContinuously(int angle, int speed);

	/**
	 * Set up the connections and initialize the driver controller.
	 * @param roverProperties
	 * @throws IOException
	 */
	void initRover(ConfigurationProvider roverProperties) throws IOException;

	/**
	 * Turn the head upwards by a given angle. Angle should be positive
	 * @param angle
	 * @throws IOException
     */
	void turnHeadUp(int angle) throws IOException;

	/**
	 * Turn the head downwards by a given angle. Angle should be positive
	 * @param angle
	 * @throws IOException
	 */
	void turnHeadDown(int angle)  throws IOException;

	/**
	 * Turn the head left by a given angle. Angle should be positive
	 * @param angle
	 * @throws IOException
	 */
	void turnHeadLeft(int angle) throws IOException;

	/**
	 * Turn the head right by a given angle. Angle should be positive
	 * @param angle
	 * @throws IOException
	 */
	void turnHeadRight(int angle) throws IOException;

	/**
	 * Request a snapshot from the camera
	 * @param clientId
	 * @throws IOException
     */
	void getCameraSnapshot(int clientId) throws IOException;

	/**
	 * Request for the log file entries which are newer than the lastLogEntry value. If the lastLogEntry equals null or is empty it will send all log file entries to the client.
	 * @param clientId
	 * @param lastLogEntry
	 */
	void getLoggingEntries(int clientId, String lastLogEntry);

    /**
     * Request for the systems uptime and send the response to the client with the clientId.
     * @param clientId
     */
	void getSystemUpTime(int clientId);

	/**
	 * Reset the head to neutral position.
	 * @throws IOException
	 */
	void resetHeadPosition() throws IOException;


	/**
	 * Send the shutdown command to the GPIO controller.
	 */
	void shutdownRover();
}