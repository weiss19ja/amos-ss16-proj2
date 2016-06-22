/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import com.google.gson.Gson;
import com.google.inject.Inject;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

        // store collision state
        CollisionEvent collisionState = new CollisionEvent();

        collisionState.frontLeft = collisionController.hasCollisionFrontLeft();
        collisionState.frontRight = collisionController.hasCollisionFrontRight();
        collisionState.backLeft = collisionController.hasCollisionBackLeft();
        collisionState.backRight = collisionController.hasCollisionBackRight();

        // create JSON RPC object
        ArrayList<Object> params = new ArrayList<>();
        params.add(collisionState);

        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request("updateCollisionInformation", params);

        LOGGER.debug("dispatching collision to clients");
        clientManager.notifyAllClients(jsonRpc2Request);
    }

    /**
     * Container for the robot's collision state.
     * Serializable to JSON.
     *
     * TODO: refactor once the notification bus is available
     */
    private class CollisionEvent {
        
        public String eventName = "collisionEvent";

        public boolean frontLeft;

        public boolean frontRight;

        public boolean backLeft;

        public boolean backRight;

    }
}
