package de.developgroup.mrf.server.servlet;

import de.developgroup.mrf.server.socket.RoverSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class RoverServlet extends WebSocketServlet {
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(RoverSocket.class);
	}
}