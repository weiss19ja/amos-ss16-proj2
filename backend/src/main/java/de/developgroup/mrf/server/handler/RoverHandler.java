package de.developgroup.mrf.server.handler;

import java.io.IOException;
import java.util.Observer;

import org.cfg4j.provider.ConfigurationProvider;

public interface RoverHandler extends Observer {

	String handlePing(int sqn);

	void driveForward(int desiredSpeed) throws IOException;

	void driveBackward(int desiredSpeed) throws IOException;

	void stop() throws IOException;

	void turnLeft(int turnRate) throws IOException;

	void turnRight(int turnRate) throws IOException;

	void initRover(ConfigurationProvider roverProperties) throws IOException;

	void turnHeadUp();
	void turnHeadDown();
	void turnHeadLeft();
	void turnHeadRight();
	void stopHead();

	void shutdownRover();
}