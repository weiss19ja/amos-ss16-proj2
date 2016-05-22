package de.developgroup.mrf.server.handler;

import java.io.IOException;
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
    void stop() throws IOException;

	/**
	 * Set up the connections and initialize the driver controller.
	 * @param roverProperties
	 * @throws IOException
	 */
	void initRover(ConfigurationProvider roverProperties) throws IOException;

	void turnHeadUp(int angle) throws IOException;
	void turnHeadDown(int angle)  throws IOException;
	void turnHeadLeft(int angle) throws IOException;
	void turnHeadRight(int angle) throws IOException;
	void stopHead();

	/**
	 * Send the shutdown command to the GPIO controller.
	 */
	void shutdownRover();
}