package de.developgroup.mrf.server.controller;

import org.cfg4j.provider.ConfigurationProvider;

import java.io.IOException;

public interface DriveController {

    void initialize(ConfigurationProvider configurationProvider) throws IOException;

    /**
     * Convenience method that allows setting speed and turnrate and switches the motors.
     * @param speed value for the speed (negative and positive)
     * @param turnrate set turnrate
     */
    void setAndApply(int speed, int turnrate) throws IOException;

    void setDesiredSpeed(int speed);

    void setDesiredTurnrate(int turnrate);

    void updateMotors() throws IOException;
}
