package de.developgroup.mrf.server.controller;

import org.cfg4j.provider.ConfigurationProvider;

import java.io.IOException;

public interface DriveController {

    /**
     * Set up hardware-dependant motor controllers with values from configuration.
     * @param configurationProvider configuration to extract values from
     * @throws IOException
     */
    void initialize(ConfigurationProvider configurationProvider) throws IOException;

    /**
     * Convenience method that allows setting speed and turnrate and switches the motors.
     * @param speed value for the speed (negative and positive)
     * @param turnrate set turnrate
     */
    void setAndApply(int speed, int turnrate) throws IOException;

    /**
     * Set motor speed: Positive for forward driving, negative for backward driving.
     * @param speed
     */
    void setDesiredSpeed(int speed);

    /**
     * Set turnrate.
     * @param turnrate
     */
    void setDesiredTurnrate(int turnrate);

    /**
     * Apply values set with setDesiredSpeed() and setDesiredTurnrate() to the motors.
     * @throws IOException
     */
    void updateMotors() throws IOException;
}
