/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
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
     * Motor turn rate value. Needed to map speed values to turn rate.
     */
    private int maxTurnRate = 300;

    @Inject
    public AbstractDriveController(ContinuousDrivingAlgorithm drivingAlgorithm) {
        this.drivingAlgorithm = drivingAlgorithm;
    }

    public void setContinuousDriving(int angle, int speed) {
        MotorSetting setting = drivingAlgorithm.calculateMotorSetting(angle, speed);
        try {
            applyMotorSettings(setting);
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
