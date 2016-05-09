package de.developgroup.mrf.server.handler;

import java.util.Observer;

public interface RoverHandler extends Observer {

	String handlePing(int sqn);

	void initRover();

	void shutdownRover();
}