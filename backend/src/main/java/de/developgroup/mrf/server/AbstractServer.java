package de.developgroup.mrf.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.developgroup.mrf.NonServletModule;
import de.developgroup.mrf.RoverServletsModule;
import de.developgroup.mrf.server.controller.RoverController;
import de.developgroup.mrf.server.servlet.RoverServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


//FIXME: This is a kludge. Use proper DI with Guice etc.
public abstract class AbstractServer {

	public static AbstractServer INSTANCE;

	public abstract RoverController getController();

	public void run() {
		INSTANCE = this;

		NonServletModule nonServletModule = new NonServletModule();
        RoverServletsModule roverServletsModule = new RoverServletsModule();
        Injector injector = Guice.createInjector(nonServletModule, roverServletsModule);


        Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.addConnector(connector);

		// Setup the basic application "context" for this application at "/"
		// This is also known as the handler tree (in jetty speak)
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		server.setHandler(servletContextHandler);

		// Add a websocket to a specific path spec
		ServletHolder holder = new ServletHolder("ws-events", RoverServlet.class);
		servletContextHandler.addServlet(holder, "/events/*");

		// client folder on classpath
		String clientDir = RoverServer.class.getClassLoader().getResource("client").toExternalForm();

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });

		resourceHandler.setResourceBase(clientDir);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, servletContextHandler, new DefaultHandler() });
		server.setHandler(handlers);

		try {
			server.start();
			server.dump(System.err);
			server.join();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

}
