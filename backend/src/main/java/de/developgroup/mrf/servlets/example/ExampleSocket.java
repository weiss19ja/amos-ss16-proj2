package de.developgroup.mrf.servlets.example;

import com.google.inject.Inject;
import de.developgroup.mrf.controllers.ExampleController;
import de.developgroup.mrf.server.rpc.JsonRpcSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleSocket extends JsonRpcSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleSocket.class);

    @Inject
    public ExampleController controller;

    public ExampleSocket() {
    }

    public String ping(int sqn) {
        LOGGER.trace("ping({})", sqn);
        return controller.handlePing(sqn);
    }
}
