package de.developgroup.mrf;

import com.google.inject.AbstractModule;

import com.pi4j.io.gpio.GpioController;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerImpl;
import de.developgroup.mrf.rover.gpio.GpioControllerProvider;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.handler.RoverHandlerImpl;
import de.developgroup.mrf.server.handler.RoverHandlerMock;
import de.developgroup.mrf.server.socket.RoverSocket;

public class NonServletModule extends AbstractModule {

	private boolean useMocks = false;

	public NonServletModule(){

	}

	public NonServletModule(boolean useMocks){
		this.useMocks=useMocks;
	}



	@Override
	protected void configure() {
		// here you can list all bindings of non-servlet classes needed in the
		// backend

		if(useMocks){
			bind(RoverHandler.class).to(RoverHandlerMock.class);
		} else {
			bind(RoverHandler.class).to(RoverHandlerImpl.class);
		}

		bind(CollisionController.class).to(CollisionControllerImpl.class);
		bind(GpioController.class).toProvider(GpioControllerProvider.class);

		requestStaticInjection(RoverSocket.class);
		requestStaticInjection(Main.class);
	}
}
