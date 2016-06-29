/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.server.controller;

import java.lang.Math;

public class ContinuousDrivingAlgorithmImpl implements ContinuousDrivingAlgorithm {

    /**
     * n degrees up/down the x-axis of the joystick there is a zone that is meant for rotating the robot in one place.
     */
    private static final int ROTATE_ZONE_DEGREES = 20;


    /**
     * Maximum speed (positive). Will be inverted for driving backward.
     */
    private static double V_MAX = 1d;

    /**
     * Perform the calculation of a simple differential driving algorithm for joysticks with added zone for
     * pivoting the robot...
     * @param angle the angle of the joystick
     * @param speed the deviation of the joystick, e.g. its speed
     * @return new motor settings
     */
    @Override
    public MotorSetting calculateMotorSetting(int angle, int speed) {
        double leftMotorPercentage = 0d;
        double rightMotorPercentage = 0d;

        double angleRadians = (double)angle / 360d * 2 * Math.PI;

        if (angle <= 90 || angle > 270) {
            // right side of coordinate system
            if (angle <= ROTATE_ZONE_DEGREES || angle >= 360 - ROTATE_ZONE_DEGREES) {
                // right robot rotate zone
                leftMotorPercentage = V_MAX;
                rightMotorPercentage = -V_MAX;
            } else {
                if (angle <= 90) {
                    // forward right
                    leftMotorPercentage = V_MAX;
                    rightMotorPercentage = Math.sin(angleRadians) * V_MAX;
                } else {
                    leftMotorPercentage = -V_MAX;
                    rightMotorPercentage = Math.sin(angleRadians) * V_MAX;
                }
            }
        } else {
            // left side of coordinate system
            if (angle >= 160 && angle <= 200) {
                leftMotorPercentage = -V_MAX;
                rightMotorPercentage = V_MAX;
            } else {
                if (angle <= 180) {
                    leftMotorPercentage = Math.sin(angleRadians) * V_MAX;
                    rightMotorPercentage = V_MAX;
                } else {
                    leftMotorPercentage = Math.sin(angleRadians) * V_MAX;
                    rightMotorPercentage = -V_MAX;
                }
            }
        }

        leftMotorPercentage *= (double)speed/100d;
        rightMotorPercentage *= (double)speed/100d;

        return new MotorSetting(leftMotorPercentage, rightMotorPercentage);
    }
}
