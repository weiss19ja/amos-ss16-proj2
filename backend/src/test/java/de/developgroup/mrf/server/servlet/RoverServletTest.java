package de.developgroup.mrf.server.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.mrf.server.socket.RoverSocket;

public class RoverServletTest {

	RoverServlet roverServlet;
	WebSocketServletFactory webSocketServletFactory;

	@Before
	public void setUp() {
		roverServlet = new RoverServlet();
		webSocketServletFactory = mock(WebSocketServletFactory.class);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testConfigure() {
		roverServlet.configure(webSocketServletFactory);
		verify(webSocketServletFactory).register(RoverSocket.class);
	}

}
