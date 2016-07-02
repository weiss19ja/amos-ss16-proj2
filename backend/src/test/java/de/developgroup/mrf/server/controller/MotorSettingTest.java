/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.server.controller;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class MotorSettingTest {

    MotorSetting setting;

    @Before
    public void setUp() {
        setting = new MotorSetting(0d, 0d);
    }

    @Test
    public void testNeitherForwardsNorBackwardsInNeutral() {
        Assert.assertFalse(setting.drivesForwards());
        Assert.assertFalse(setting.drivesBackwards());
    }

    @Test
    public void testDrivesForwardsIfLeft() {
        setting.leftMotorPercentage = 0.5;

        Assert.assertTrue(setting.drivesForwards());
    }

    @Test
    public void testDrivesForwardsIfRight() {
        setting.rightMotorPercentage = 0.5;

        Assert.assertTrue(setting.drivesForwards());
    }

    @Test
    public void testDrivesBackwardsIfLeft() {
        setting.leftMotorPercentage = -0.5;

        Assert.assertTrue(setting.drivesBackwards());
    }

    @Test
    public void testDrivesBackwardsIfRight() {
        setting.rightMotorPercentage = -0.5;

        Assert.assertTrue(setting.drivesBackwards());
    }
}

