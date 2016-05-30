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

    @Inject
    public static RoverHandler roverHandler;


    @Inject
    public DeveloperSettingsHandler(
                               ClientManager clientManager) {
        LOGGER.debug("Creating new instance of DeveloperSettingsHandler");
        this.clientManager = clientManager;
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
        LOGGER.debug("Got to DeveloperSettings getKillswitchState");
        return killswitchEnabled;
    }

    /**
     * Blocks or unblocks rover-actions
     *
     * @param newState if true, actions get blocked, if false, clients can steer the rover
     */
    public void setKillswitchEnabled(Boolean newState) throws IOException {
        LOGGER.debug("Blocked state is: " + newState);
        if (newState) {
            roverHandler.stop();
            killswitchEnabled = true;
        }
        else{
            killswitchEnabled = false;
        }
        informClients();
    }

    public void informClients() {

        // create JSON RPC object
        ArrayList<Object> params = new ArrayList<>();
        params.add(killswitchEnabled);

        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request("updateKillswitchEnabled", params);

        LOGGER.debug("informing clients about Killswitch-State: "+ killswitchEnabled);
        clientManager.notifyAllClients(jsonRpc2Request);
    }

}
