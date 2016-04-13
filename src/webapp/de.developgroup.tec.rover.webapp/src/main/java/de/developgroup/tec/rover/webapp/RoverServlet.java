package de.developgroup.tec.rover.webapp;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class RoverServlet extends WebSocketServlet {
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(RoverSocket.class);
	}
}