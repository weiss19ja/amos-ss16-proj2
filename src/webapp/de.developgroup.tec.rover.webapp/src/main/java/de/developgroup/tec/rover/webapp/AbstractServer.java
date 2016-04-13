package de.developgroup.tec.rover.webapp;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import de.developgroup.tec.rover.RoverController;

//FIXME: This is a kludge. Use proper DI with Guice etc.
public abstract class AbstractServer {

	public static AbstractServer INSTANCE;

	protected abstract RoverController getController();

	public void run() {
		INSTANCE = this;
		
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8000);
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
