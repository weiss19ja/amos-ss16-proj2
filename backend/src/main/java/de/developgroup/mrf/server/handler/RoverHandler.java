package de.developgroup.mrf.server.handler;

import java.util.Observer;

import org.cfg4j.provider.ConfigurationProvider;

public interface RoverHandler extends Observer {

	String handlePing(int sqn);

	void driveForward(int desiredSpeed);

	void driveBackward(int desiredSpeed);

	void initRover(ConfigurationProvider roverProperties);

	void shutdownRover();
}