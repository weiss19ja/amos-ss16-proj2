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

	void turnHeadUp(int angle);
	void turnHeadDown(int angle);
	void turnHeadLeft(int angle);
	void turnHeadRight(int angle);
	void stopHead();

	void shutdownRover();
}