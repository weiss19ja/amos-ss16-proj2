/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DriveControllerMock extends AbstractDriveController {

    private static Logger LOGGER = LoggerFactory.getLogger(DriveControllerMock.class);

    @Inject
    public DriveControllerMock(ContinuousDrivingAlgorithm drivingAlgorithm)  {
        super(drivingAlgorithm);
    }

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        LOGGER.info("initializing DriveController");
    }

    @Override
    public void setAndApply(int speed, int turnrate) throws IOException {
        LOGGER.info("setting and applying speed {} and turnrate {}", speed, turnrate);
        super.setAndApply(speed, turnrate);
    }

    @Override
    public void setDesiredSpeed(int speed) {
        LOGGER.info("setting speed {}", speed);
    }

    @Override
    public void setDesiredTurnrate(int turnrate) {
        LOGGER.info("setting turnate {}", turnrate);
    }

    @Override
    public void updateMotors() throws IOException {
        LOGGER.info("updating motor settings");
    }

    @Override
    public void applyMotorSettings(MotorSettings settings) throws IOException {
        LOGGER.info("applying motor settings object " + settings);
    }
}
