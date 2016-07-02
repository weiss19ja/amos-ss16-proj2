/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import de.developgroup.mrf.rover.collision.CollisionRunnable;
import de.developgroup.mrf.rover.collision.RoverCollisionInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Provides common methods for interface DriveController to all subclasses.
 */
public abstract class AbstractDriveController implements DriveController, Observer {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractDriveController.class);

    /**
     * The algorithm used to convert joystick input to motor commands.
     */
    protected ContinuousDrivingAlgorithm drivingAlgorithm;

    /**
     * The runnable that holds and generates collision information for the rover.
     */
    protected CollisionRunnable collisionRunnable;
    protected MotorSettings currentMotorSettings;
    private Object motorSettingsLock = new Object();

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
                setDesiredSpeed(0);
                updateMotors();
                LOGGER.info("Do not drive - collision in the direction of joystick driving detected.");
            }
        } catch (IOException e)  {
            LOGGER.error("Failed to set motor settings: " + e);
        }
    }

    public void driveForwards() throws IOException {
        MotorSettings settings = new MotorSettings(1d, 1d);
        applyMotorSettings(settings);
    }

    public void driveBackwards() throws IOException {
        MotorSettings settings = new MotorSettings(-1d, -1d);
        applyMotorSettings(settings);
    }

    public void turnLeft() throws IOException {
        MotorSettings settings = new MotorSettings(-1d, 1d);
        applyMotorSettings(settings);
    }

    public void turnRight() throws IOException {
        MotorSettings settings = new MotorSettings(1d, -1d);
        applyMotorSettings(settings);
    }

    public void stop() throws IOException {
        setDesiredSpeed(0);
        updateMotors();
    }

    public void setAndApply(int speed, int turnrate) throws IOException {
        setDesiredSpeed(speed);
        setDesiredTurnrate(turnrate);
        updateMotors();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof RoverCollisionInformation)) {
            return;
        }
        RoverCollisionInformation collisionInformation = (RoverCollisionInformation)arg;
        if (collisionInformation.taintedReadings) {
            // do not brake if rover is exposed to sunlight and sensors do not work
            return;
        }
        if ((getCurrentMotorSettings().drivesForwards() && collisionInformation.hasCollisionFront())
                || (getCurrentMotorSettings().drivesBackwards() && collisionInformation.hasCollisionBack())) {
            try {
                stop();
            } catch (IOException e) {
                LOGGER.error("Cannot stop rover although collision was detected.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public MotorSettings getCurrentMotorSettings() {
        return currentMotorSettings;
    }

    @Override
    public void setCurrentMotorSettings(MotorSettings newSettings) {
        synchronized (motorSettingsLock) {
            currentMotorSettings = newSettings;
        }
    }
}
