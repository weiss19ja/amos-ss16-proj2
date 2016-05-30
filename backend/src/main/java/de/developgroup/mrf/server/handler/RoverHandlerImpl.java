package de.developgroup.mrf.server.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.i2c.I2CBus;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.motor.MotorController;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.controller.DriveController;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.developgroup.mrf.server.controller.HeadController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

@Singleton
public class RoverHandlerImpl implements RoverHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoverHandlerImpl.class);

	@Inject
	private static ClientManager clientManager;

	final CollisionController collisionController;

	final GpioController gpio;

	final DriveController driveController;

	final HeadController headController;

	@Inject
	public RoverHandlerImpl(CollisionController collisionController,
							GpioController gpio,
							DriveController driveController, HeadController headController) {
		LOGGER.info("RoverHandlerImpl startup");
		this.collisionController = collisionController;
		this.gpio = gpio;
		this.driveController = driveController;
		this.headController = headController;
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
			} catch(IOException io) {
				LOGGER.error("Cannot stop rover on collision event. Goodbye.");
			}
		} else {
			LOGGER.info("Sensor " + event.getPin().getName()
					+ " Collision voided");
		}
		sendCollisionDetectionInformationToAllClients();
	}

	private void sendCollisionDetectionInformationToAllClients(){
		List<Object> params = new ArrayList<>();
		params.add(collisionController.hasCollisionFrontRight());
		params.add(collisionController.hasCollisionFrontLeft());
		params.add(collisionController.hasCollisionBackRight());
		params.add(collisionController.hasCollisionBackLeft());

		JsonRpc2Request notification = new JsonRpc2Request("updateCollisionInformation",params);
		clientManager.notifyAllClients(notification);
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
	public void turnHeadUp(int angle) throws IOException{
		LOGGER.debug("Turn head up by " + angle + " degree");
		headController.turnHeadUp(angle);
	}

	@Override
	public void turnHeadDown(int angle)throws IOException {
		LOGGER.debug("Turn head down by " + angle + " degree");
		headController.turnHeadDown(angle);
	}

	@Override
	public void turnHeadLeft(int angle) throws IOException{
		LOGGER.debug("Turn head left by " + angle + " degree");
		headController.turnHeadLeft(angle);
	}

	@Override
	public void turnHeadRight(int angle) throws IOException{
		LOGGER.debug("Turn head right by " + angle + " degree");
		headController.turnHeadRight(angle);
	}
}
