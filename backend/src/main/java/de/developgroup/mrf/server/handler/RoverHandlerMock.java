package de.developgroup.mrf.server.handler;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

@Singleton
public class RoverHandlerMock implements RoverHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoverHandlerMock.class);


	public RoverHandlerMock() {

	}

	public String handlePing(int sqn) {
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
