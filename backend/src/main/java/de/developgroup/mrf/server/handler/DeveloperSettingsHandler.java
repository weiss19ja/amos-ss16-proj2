package de.developgroup.mrf.server.handler;

import com.google.inject.Inject;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class DeveloperSettingsHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DeveloperSettingsHandler.class);
    protected boolean killswitchEnabled = false;

    private ClientManager clientManager;

    private RoverHandler roverHandler;


    @Inject
    public DeveloperSettingsHandler(
                               ClientManager clientManager, RoverHandler roverHandler) {
        LOGGER.debug("Creating new instance of DeveloperSettingsHandler");
        this.clientManager = clientManager;
        this.roverHandler = roverHandler;
    }

    /**
     * Checks whether rover-actions are being blocked by a developer via the killswitch
     * If so, logs this to console
     *
     * @return returns true if actions are blocked, false otherwise
     */
    public boolean checkKillswitchEnabled() {
        if(killswitchEnabled) {
            LOGGER.trace("Developer blocked this action");
        }
        return killswitchEnabled;
    }

    /**
     * Simple Getter for killswitchState
     * @return killswitchState
     */
    public boolean isKillswitchEnabled() {
        return killswitchEnabled;
    }

    /**
     * Blocks or unblocks rover-actions
     *
     * @param newState if true, actions get blocked, if false, clients can steer the rover
     */
    public void setKillswitchEnabled(Boolean newState) throws IOException {
        LOGGER.debug("Killswitch state is: " + newState);

        // determine whether client needs to get message
        // should always be the case but just to be safe
        boolean notifyClients = false;
        if(killswitchEnabled != newState.booleanValue()){
            notifyClients = true;
        }
        if (newState) {
            roverHandler.stop();
            killswitchEnabled = true;
        }
        else{
            killswitchEnabled = false;
        }
        notifyClientsAboutButtonState();
        if(notifyClients){
            notifyClientsAboutBlockingState();
        }
    }

    public void notifyClientsAboutButtonState() {

        // create JSON RPC object
        ArrayList<Object> params = new ArrayList<>();
        params.add(killswitchEnabled);

        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request("updateKillswitchEnabled", params);

        LOGGER.debug("informing clients about Killswitch-State: "+ killswitchEnabled);
        clientManager.notifyAllClients(jsonRpc2Request);
    }

    /**
     * Sends a message to all clients so they know the developer just
     * changed the killswitch state
     */
    private void notifyClientsAboutBlockingState(){
        String message;
        if (killswitchEnabled){
            message = "All interactions with the rover are blocked";
        }
        else{
            message = "Interactions with the rover are enabled now";
        }
        clientManager.notifyAllClients(message);
    }

}
