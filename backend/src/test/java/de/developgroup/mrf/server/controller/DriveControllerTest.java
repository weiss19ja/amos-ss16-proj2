/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.server.controller;

import de.developgroup.mrf.rover.motor.MotorController;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.verify;

public class DriveControllerTest {

    DriveControllerImpl driveController;

    @Before
    public void setUp() throws IOException {
        ContinuousDrivingAlgorithm drivingAlgorithm = Mockito.mock(ContinuousDrivingAlgorithm.class);
        driveController = new DriveControllerImpl(drivingAlgorithm);
        driveController.leftMotor = Mockito.mock(MotorController.class);
        driveController.rightMotor = Mockito.mock(MotorController.class);
    }

    @Test
    public void testApplyMotorSettings() throws IOException {
        MotorSetting newSetting = new MotorSetting(0.6, -0.8);

        driveController.applyMotorSettings(newSetting);

        verify(driveController.leftMotor).setSpeedPercentage(0.6);
        verify(driveController.rightMotor).setSpeedPercentage(-0.8);
    }

    @Test
    public void testUpdateMotors() throws IOException {
        driveController.setDesiredSpeed(42);
        driveController.setDesiredTurnrate(23);

        driveController.updateMotors();

        verify(driveController.leftMotor).setSpeed(42 - 23);
        verify(driveController.rightMotor).setSpeed(42 + 23);
    }

    @Test
    public void testClamp() {
        int min = -23;
        int max = 451;

        Assert.assertEquals(42, driveController.clamp(42, min, max));
        Assert.assertEquals(min, driveController.clamp(-1337, min, max));
        Assert.assertEquals(max, driveController.clamp(666, min, max));
    }

}
