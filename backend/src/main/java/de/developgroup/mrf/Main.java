package de.developgroup.mrf;

import java.net.URL;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerImpl;

public class Main {

	public static void main(String[] args) {
		NonServletModule nonServletModule = new NonServletModule();
		RoverServletsModule roverServletsModule = new RoverServletsModule();
		Injector injector = Guice.createInjector(nonServletModule,
				roverServletsModule);

		CollisionController collisionController = new CollisionControllerImpl();

		// set up jetty default server
		int port = 80;
		Server server = new Server(port);
		ServletContextHandler servletContextHandler = new ServletContextHandler(
				server, "/", ServletContextHandler.SESSIONS);
		servletContextHandler.addFilter(GuiceFilter.class, "/*",
				EnumSet.allOf(DispatcherType.class));

		servletContextHandler.addServlet(DefaultServlet.class, "/");

		// serve client files
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		URL clientDir = Main.class.getClassLoader().getResource("client");
		if (clientDir != null) {
			resourceHandler.setResourceBase(clientDir.toExternalForm());
		} else {

		}
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler,
				servletContextHandler, new DefaultHandler() });
		server.setHandler(handlers);

		try {
			server.start();
			server.join();
		} catch (Exception e) {

		}
	}
}
