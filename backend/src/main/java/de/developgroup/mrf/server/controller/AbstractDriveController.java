/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import de.developgroup.mrf.rover.collision.CollisionRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Provides common methods for interface DriveController to all subclasses.
 */
public abstract class AbstractDriveController implements DriveController {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractDriveController.class);

    /**
     * The algorithm used to convert joystick input to motor commands.
     */
    protected ContinuousDrivingAlgorithm drivingAlgorithm;

    /**
     * The runnable that holds and generates collision information for the rover.
     */
    protected CollisionRunnable collisionRunnable;

    @Inject
    public AbstractDriveController(ContinuousDrivingAlgorithm drivingAlgorithm, CollisionRunnable collisionRunnable) {
        this.drivingAlgorithm = drivingAlgorithm;
        this.collisionRunnable = collisionRunnable;
    }

    public void setContinuousDriving(int angle, int speed) {
        MotorSettings setting = drivingAlgorithm.calculateMotorSetting(angle, speed);
        try {
            if (setting.drivesForwards()
                    && !collisionRunnable.getCurrentCollisionInformation().hasCollisionFront()) {
                applyMotorSettings(setting);
            } else if (setting.drivesBackwards()
                    && !collisionRunnable.getCurrentCollisionInformation().hasCollisionBack()) {
                applyMotorSettings(setting);
            } else {
                LOGGER.info("Do not drive - collision in the direction of joystick driving detected.");
            }
        } catch (IOException e)  {
            LOGGER.error("Failed to set motor settings: " + e);
        }
    }

    public void setAndApply(int speed, int turnrate) throws IOException {
        setDesiredSpeed(speed);
        setDesiredTurnrate(turnrate);
        updateMotors();
    }
}
