package de.developgroup.mrf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.developgroup.mrf.server.RoverServer;

public class Main {
    public static int main(String[] args) {
        NonServletModule nonServletModule = new NonServletModule();
        RoverServletsModule roverServletsModule = new RoverServletsModule();
        Injector injector = Guice.createInjector(nonServletModule, roverServletsModule);

        RoverServer server = new RoverServer();
        server.run();

        return 0;
    }
}
