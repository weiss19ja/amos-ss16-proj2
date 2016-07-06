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
     * Drive the rover forwards.
     * Drives only if no collision front was encountered.
     * @throws IOException
     */
    void driveForwards() throws IOException;

    /**
     * Drive the rover backwards.
     * Drives only if no collision back was encountered.
     * @throws IOException
     */
    void driveBackwards() throws IOException;

    /**
     * Start left turn (the rover will continue to turn until another command is sent).
     * @throws IOException
     */
    void turnLeft() throws IOException;

    /**
     * Start right turn (the rover will continue to turn until another command is sent).
     * @throws IOException
     */
    void turnRight() throws IOException;

    /**
     * Stop the rover.
     */
    void stop() throws IOException;

    /**
     * Apply raw input to motor values.
     * @param settings the settings object to apply to the motors.
     */
    void applyMotorSettings(MotorSettings settings) throws IOException;

    /**
     * Retrieve the currently used motor settings
     * @return a MotorSettings object
     */
    MotorSettings getCurrentMotorSettings();

    /**
     * Set the motor settings to use and store
     * @param newSettings the new motor settings for this instance of DriveController
     */
    void setCurrentMotorSettings(MotorSettings newSettings);

    /**
     * Set a multiplier to cap the speed. All speed values to be set will be multiplied by this value.
     *
     * If a value outside the range is passed, it will be silently ignored to not cause unwanted exceptions in the
     * driving stack.
     * @param value percentage between 0.0 and 1.0
     */
    void setSpeedMultiplier(double value) throws IOException;

    /**
     * Get the speed multiplier.
     * @return a speed muliplier value in [0.0; 1.0]
     */
    double getSpeedMultiplier();
}
