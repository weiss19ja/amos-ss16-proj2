package de.developgroup.mrf.server;


import de.developgroup.mrf.server.controller.DummyRoverController;
import de.developgroup.mrf.server.controller.RoverController;

public class DummyServer extends AbstractServer {

	public static void main(String[] args) {
		new DummyServer().run();
	}

	@Override
	public RoverController getController() {
		return DummyRoverController.INSTANCE;
	}
}