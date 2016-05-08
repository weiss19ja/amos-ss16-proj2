package de.developgroup.mrf.servlets.example;

import com.google.inject.Singleton;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@Singleton
public class CameraServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(CameraSocket.class);
    }
}
