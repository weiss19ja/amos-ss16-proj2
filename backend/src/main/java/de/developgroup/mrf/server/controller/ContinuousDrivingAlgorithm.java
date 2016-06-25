/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.server.controller;

/**
 * Interface for an algorithm that converts joystick driving information to motor commands.
 */
public interface ContinuousDrivingAlgorithm {
    /**
     * For a given angle and settings of the joystick, calculate a motor settings object.
     * @param angle the angle of the joystick
     * @param speed the deviation of the joystick, e.g. its speed
     * @return a motor settings object that describes the movement of the robot for this input
     */
    MotorSetting calculateMotorSetting(int angle, int speed);
}
