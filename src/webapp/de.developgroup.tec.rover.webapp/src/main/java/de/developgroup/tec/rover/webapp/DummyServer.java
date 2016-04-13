package de.developgroup.tec.rover.webapp;

import de.developgroup.tec.rover.RoverController;
import de.developgroup.tec.rover.impl.DummyRoverController;

public class DummyServer extends AbstractServer {

	public static void main(String[] args) {
		new DummyServer().run();
	}

	@Override
	protected RoverController getController() {
		return DummyRoverController.INSTANCE;
	}
}