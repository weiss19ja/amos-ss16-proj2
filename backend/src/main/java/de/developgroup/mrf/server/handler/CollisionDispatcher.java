package de.developgroup.mrf.server.handler;

import com.google.gson.Gson;
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

        CollisionEvent collisionState = new CollisionEvent();

        collisionState.frontLeft = collisionController.hasCollisionFrontLeft();
        collisionState.frontRight = collisionController.hasCollisionFrontRight();
        collisionState.backLeft = collisionController.hasCollisionBackLeft();
        collisionState.backRight = collisionController.hasCollisionBackRight();

        Gson gson = new Gson();

        LOGGER.debug("dispatching collision to clients");
        clientManager.notifyAllClients(gson.toJson(collisionState));
    }

    /**
     * Container for the robot's collision state.
     * Serializable to JSON.
     *
     * TODO: refactor once the notification bus is available
     */
    private class CollisionEvent {
        
        public String eventName = "collision-event";

        public boolean frontLeft;

        public boolean frontRight;

        public boolean backLeft;

        public boolean backRight;

    }
}
