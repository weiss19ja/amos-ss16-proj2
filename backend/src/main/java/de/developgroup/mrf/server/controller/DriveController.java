/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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
     * Set continous driving.
     * @param angle Angle in degrees. 0° is right, 90° is forward.
     * @param speed value between 0 (stop) and 100 (full speed).
     */
    void setContinuousDriving(int angle, int speed);

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

    /**
     * Apply raw input to motor values.
     * @param settings the settings object to apply to the motors.
     */
    void applyMotorSettings(MotorSetting settings) throws IOException;
}
