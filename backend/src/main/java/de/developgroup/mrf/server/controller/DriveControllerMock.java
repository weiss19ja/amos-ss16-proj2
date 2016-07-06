/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import de.developgroup.mrf.rover.collision.CollisionRunnable;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DriveControllerMock extends AbstractDriveController {

    private static Logger LOGGER = LoggerFactory.getLogger(DriveControllerMock.class);

    @Inject
    public DriveControllerMock(ContinuousDrivingAlgorithm drivingAlgorithm, CollisionRunnable collisionRunnable)  {
        super(drivingAlgorithm, collisionRunnable);
    }

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        LOGGER.info("initializing DriveController");
    }

    @Override
    public void applyMotorSettings(MotorSettings settings) throws IOException {
        LOGGER.info("applying motor settings object " + settings);
    }

    @Override
    public void setSpeedMultiplier(double value) {
        LOGGER.info("setting speed multiplier to " + value);
    }

    @Override
    public double getSpeedMultiplier() {
        return 0;
    }
}
