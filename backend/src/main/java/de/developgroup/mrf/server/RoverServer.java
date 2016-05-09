package de.developgroup.mrf.server;

import de.developgroup.mrf.server.controller.RoverController;
import de.developgroup.mrf.server.controller.RoverControllerImpl;

public class RoverServer extends AbstractServer {

	/*
	public static void main(String[] args) {
		new RoverServer().run();
	}
	*/

	@Override
	public RoverController getController() {
		return RoverControllerImpl.INSTANCE;
	}
}