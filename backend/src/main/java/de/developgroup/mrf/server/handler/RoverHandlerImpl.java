package de.developgroup.mrf.server.handler;

import java.util.Observable;

import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;

import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerImpl;

@Singleton
public class RoverHandlerImpl implements RoverHandler {

	final CollisionController collisionController = new CollisionControllerImpl();
	final GpioController gpio = GpioFactory.getInstance();

	public RoverHandlerImpl() {

	}

	public String handlePing(int sqn) {
		return "pong " + (sqn + 1);
	}

	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent) arg;
		System.out.println("GPIO PIN STATE CHANGE: " + event.getPin()
				+ " with Name " + event.getPin().getName() + " = "
				+ event.getState());
	}

	@Override
	public void initRover() {
		((Observable) collisionController).addObserver(this);
	}

	@Override
	public void shutdownRover() {
		gpio.shutdown();
	}
}
