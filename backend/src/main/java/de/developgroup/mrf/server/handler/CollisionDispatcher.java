package de.developgroup.mrf.server.handler;

import com.google.inject.Inject;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.server.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

public class CollisionDispatcher implements Observer {

    private static Logger LOGGER = LoggerFactory.getLogger(CollisionDispatcher.class);

    private CollisionController collisionController;

    private ClientManager clientManager;

    @Inject
    public CollisionDispatcher(CollisionController collisionController,
                               ClientManager clientManager) {
        LOGGER.debug("Creating new instance of CollisionDispatcher");
        ((Observable)collisionController).addObserver(this);
        this.collisionController = collisionController;
        this.clientManager = clientManager;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o != collisionController) {
            LOGGER.error("update() called by unknown Observable - ignoring call");
            return;
        }

        LOGGER.debug("dispatching collision to clients");
        clientManager.notifyAllClients("collision occurred");
    }
}
