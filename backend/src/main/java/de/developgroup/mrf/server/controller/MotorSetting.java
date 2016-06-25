/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.server.controller;

public class MotorSetting {

    public double leftMotorPercentage;

    public double rightMotorPercentage;
    
    public MotorSetting(double leftMotorPercentage, double rightMotorPercentage) {
        this.leftMotorPercentage = leftMotorPercentage;
        this.rightMotorPercentage = rightMotorPercentage;
    }
}
