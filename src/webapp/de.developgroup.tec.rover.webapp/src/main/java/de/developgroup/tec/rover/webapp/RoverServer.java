package de.developgroup.tec.rover.webapp;

import de.developgroup.tec.rover.RoverController;
import de.developgroup.tec.rover.impl.RoverControllerImpl;

public class RoverServer extends AbstractServer {

	public static void main(String[] args) {
		new RoverServer().run();
	}

	@Override
	protected RoverController getController() {
		return RoverControllerImpl.INSTANCE;
	}
}