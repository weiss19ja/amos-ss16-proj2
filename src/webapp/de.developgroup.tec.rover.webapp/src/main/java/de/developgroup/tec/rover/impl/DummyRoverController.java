package de.developgroup.tec.rover.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.developgroup.tec.rover.RoverController;
import de.developgroup.tec.rover.webapp.RoverSocket;

public class DummyRoverController implements RoverController {

	public static final DummyRoverController INSTANCE = new DummyRoverController();
	private static final Logger LOG = LoggerFactory.getLogger(RoverSocket.class);

	private DummyRoverController() {
	}

	@Override
	public void init() throws IOException {
		LOG.info("RoverController#init()");
	}

	@Override
	public void close() throws IOException {
		LOG.info("RoverController#close()");
	}

	@Override
	public String takeSnapshot() throws IOException {
		LOG.info("RoverController#takeSnapshot()");
		return "no.jpg";
	}

	@Override
	public int openVideoStream() throws IOException {
		LOG.info("RoverController#openVideoStream()");
		return 3000;
	}

	@Override
	public void setSpeed(int speed) throws IOException {
		LOG.info("RoverController#setSpeed({})", speed);
	}

	@Override
	public void setTurnRate(int rate) throws IOException {
		LOG.info("RoverController#setTurnRate({})", rate);
	}

	@Override
	public void setPan(int pan) throws IOException {
		LOG.info("RoverController#setPan({})", pan);
	}

	@Override
	public void setTilt(int tilt) throws IOException {
		LOG.info("RoverController#setTilt({})", tilt);
	}

	@Override
	public void stop() throws IOException {
		LOG.info("RoverController#stop()");
	}

}
