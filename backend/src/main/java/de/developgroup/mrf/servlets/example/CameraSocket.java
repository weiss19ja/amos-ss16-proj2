package de.developgroup.mrf.servlets.example;

import com.google.inject.Inject;
import de.developgroup.mrf.controllers.CameraController;
import de.developgroup.mrf.server.rpc.JsonRpcSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraSocket extends JsonRpcSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleSocket.class);

    @Inject
    public static CameraController controller;

    public CameraSocket() {
    }

    public String ping(Number sqn) {
        LOGGER.trace("ping({})", sqn);
        return controller.handlePing(sqn.intValue());
    }
}
