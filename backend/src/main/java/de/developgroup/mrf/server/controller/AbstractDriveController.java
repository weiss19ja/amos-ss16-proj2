/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import java.io.IOException;

/**
 * Provides common methods for interface DriveController to all subclasses.
 */
public abstract class AbstractDriveController implements DriveController {

    /**
     * Motor turn rate value. Needed to map speed values to turn rate.
     */
    private int maxTurnRate = 300;

    public void setContinuousDriving(int angle, int speed) {
        // TODO
    }

    public void setAndApply(int speed, int turnrate) throws IOException {
        setDesiredSpeed(speed);
        setDesiredTurnrate(turnrate);
        updateMotors();
    }
}
