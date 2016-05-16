package de.developgroup.mrf.server.handler;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public class RoverHandlerMock implements RoverHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoverHandlerMock.class);

	public RoverHandlerMock() {
		LOGGER.info("Setting up RoverHandlerMock");
	}

	public String handlePing(int sqn) {
		LOGGER.debug("handling ping for sqn " + sqn);
		return "pong " + (sqn + 1);
	}

	public void update(Observable o, Object arg) {
	}

	@Override
	public void initRover() {
	}

	@Override
	public void shutdownRover() {

	}
}
