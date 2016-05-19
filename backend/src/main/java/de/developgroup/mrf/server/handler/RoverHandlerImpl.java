package de.developgroup.mrf.server.handler;

import java.util.Observable;

import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;

import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerImpl;

@Singleton
public class RoverHandlerImpl implements RoverHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoverHandlerImpl.class);

	final CollisionController collisionController = new CollisionControllerImpl();
	final GpioController gpio = GpioFactory.getInstance();

	public RoverHandlerImpl() {
		LOGGER.info("RoverHandlerImpl startup");
	}

	public String handlePing(int sqn) {
		LOGGER.debug("handling ping for sqn " + sqn);
		return "pong " + (sqn + 1);
	}

	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent) arg;
		if (event.getState().isHigh()) {
			LOGGER.info("Sensor " + event.getPin().getName()
					+ " Collision detected");
		} else {
			LOGGER.info("Sensor " + event.getPin().getName()
					+ " Collision voided");
		}
	}

	@Override
	public void driveForward(int desiredSpeed) {
		LOGGER.debug("driveForeward() with speed " + desiredSpeed);
	}

	@Override
	public void driveBackward(int desiredSpeed) {
		LOGGER.debug("driveBackward() with speed " + desiredSpeed);

	}

	@Override
	public void initRover(ConfigurationProvider roverProperties) {
		((Observable) collisionController).addObserver(this);
		LOGGER.info("Rover initialized for collistion detection");

	}

	@Override
	public void shutdownRover() {
		gpio.shutdown();
	}
}
