package de.developgroup.mrf.servlets.example;

import de.developgroup.mrf.controllers.ExampleController;
import de.developgroup.mrf.server.rpc.JsonRpcSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleSocket extends JsonRpcSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleSocket.class);

    private final ExampleController controller;

    public ExampleSocket(ExampleController controller) {
        this.controller = controller;
    }

    public String ping(int sqn) {
        LOGGER.trace("ping({})", sqn);
        return controller.handlePing(sqn);
    }
}
