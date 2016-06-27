/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import java.io.IOException;
import java.util.Observable;

import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.controller.LoggingCommunicationController;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;

import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.server.ClientManagerImpl;
import de.developgroup.mrf.server.controller.CameraSnapshotController;
import de.developgroup.mrf.server.controller.DriveController;
import de.developgroup.mrf.server.controller.HeadController;

@Singleton
public class RoverHandlerImpl implements RoverHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoverHandlerImpl.class);

	@Inject
	private static ClientManager clientManager;

	final CollisionController collisionController;

	final GpioController gpio;

	final DriveController driveController;

	final HeadController headController;

	final CameraSnapshotController cameraSnapshotController;

	final LoggingCommunicationController loggingCommunicationController;

	@Inject
	public RoverHandlerImpl(CollisionController collisionController,
			GpioController gpio, DriveController driveController,
			HeadController headController,
			CameraSnapshotController cameraSnapshotController,
			LoggingCommunicationController loggingCommunicationController) {
		LOGGER.info("RoverHandlerImpl startup");
		this.collisionController = collisionController;
		this.gpio = gpio;
		this.driveController = driveController;
		this.headController = headController;
		this.cameraSnapshotController = cameraSnapshotController;
		this.loggingCommunicationController = loggingCommunicationController;
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
			try {
				stop();
			} catch (IOException io) {
				LOGGER.error("Cannot stop rover on collision event. Goodbye.");
			}
		} else {
			LOGGER.info("Sensor " + event.getPin().getName()
					+ " Collision voided");
		}
	}

	@Override
	public void driveForward(int desiredSpeed) throws IOException {
		LOGGER.debug("driveForward() with speed " + desiredSpeed);
		driveController.setAndApply(desiredSpeed, 0);
	}

	@Override
	public void driveBackward(int desiredSpeed) throws IOException {
		LOGGER.debug("driveBackward() with speed " + desiredSpeed);
		driveController.setAndApply(-desiredSpeed, 0);
	}

	/**
	 * @param roverProperties
	 * @throws IOException
	 */
	@Override
	public void initRover(ConfigurationProvider roverProperties)
			throws IOException {

		driveController.initialize(roverProperties);
		LOGGER.info("Rover initialized for driving");

		headController.initialize(roverProperties);
		LOGGER.info("Rover initialized for head movement");

		// listen for collisions
		((Observable) collisionController).addObserver(this);

		LOGGER.info("Rover initialized for collistion detection");
	}

	@Override
	public void shutdownRover() {
		gpio.shutdown();
	}

	@Override
	public void stop() throws IOException {
		driveController.setAndApply(0, 0);
	}

	@Override
	public void turnLeft(int turnRate) throws IOException {
		// links negativ recht pos
		driveController.setAndApply(0, turnRate);
	}

	@Override
	public void turnRight(int turnRate) throws IOException {
		driveController.setAndApply(0, -turnRate);
	}

	@Override
	public void turnHeadUp(int angle) throws IOException {
		LOGGER.debug("Turn head up by " + angle + " degree");
		headController.turnHeadUp(angle);
	}

	@Override
	public void turnHeadDown(int angle) throws IOException {
		LOGGER.debug("Turn head down by " + angle + " degree");
		headController.turnHeadDown(angle);
	}

	@Override
	public void turnHeadLeft(int angle) throws IOException {
		LOGGER.debug("Turn head left by " + angle + " degree");
		headController.turnHeadLeft(angle);
	}

	@Override
	public void turnHeadRight(int angle) throws IOException {
		LOGGER.debug("Turn head right by " + angle + " degree");
		headController.turnHeadRight(angle);
	}

	@Override
	public void resetHeadPosition() throws IOException {
		LOGGER.debug("Reset head position");
		headController.resetHeadPosition();
	}

	@Override
	public void getCameraSnapshot(int clientId) throws IOException {
		LOGGER.debug("Get snapshot from camera");
		cameraSnapshotController.getCameraSnapshot(clientId);
	}

	@Override
	public void getLoggingEntries(int clientId, String lastLogEntry) {
		LOGGER.debug("Get logging entries");
		loggingCommunicationController.getLoggingEntries(clientId, lastLogEntry);
	}

	@Override
	public void getSystemUpTime(int clientId) {
		LOGGER.debug("Get the systems uptime");
		loggingCommunicationController.getSystemUpTime(clientId);
	}
}
