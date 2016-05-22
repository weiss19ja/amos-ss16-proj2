package de.developgroup.mrf.server.controller;

import java.io.IOException;

public abstract class AbstractDriveController implements DriveController {
    public void setAndApply(int speed, int turnrate) throws IOException {
        setDesiredSpeed(speed);
        setDesiredTurnrate(turnrate);
        updateMotors();
    }
}
