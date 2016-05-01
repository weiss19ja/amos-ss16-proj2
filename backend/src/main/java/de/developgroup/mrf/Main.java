package de.developgroup.mrf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import de.developgroup.mrf.server.RoverServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Main {
    public static void main(String[] args) {
        NonServletModule nonServletModule = new NonServletModule();
        RoverServletsModule roverServletsModule = new RoverServletsModule();
        Injector injector = Guice.createInjector(nonServletModule, roverServletsModule);

        int port = 8080;
        Server server = new Server(port);
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        servletContextHandler.addServlet(DefaultServlet.class, "/");

        try {
            server.start();
            server.join();
        } catch (Exception e) {

        }
    }
}
