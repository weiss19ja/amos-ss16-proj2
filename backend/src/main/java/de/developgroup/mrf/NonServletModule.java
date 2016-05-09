package de.developgroup.mrf;

import com.google.inject.AbstractModule;

import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.handler.RoverHandlerImpl;
import de.developgroup.mrf.server.socket.RoverSocket;

public class NonServletModule extends AbstractModule {
	@Override
	protected void configure() {
		// here you can list all bindings of non-servlet classes needed in the
		// backend
		// e.g. bind(IMotorController.class).to(SimpleMotorController.class);

		bind(RoverHandler.class).to(RoverHandlerImpl.class);
		requestStaticInjection(RoverSocket.class);
	}
}
