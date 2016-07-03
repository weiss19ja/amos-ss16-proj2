/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.server.controller;

import de.developgroup.mrf.rover.collision.CollisionRunnable;
import de.developgroup.mrf.rover.collision.CollisionState;
import de.developgroup.mrf.rover.collision.RoverCollisionInformation;
import de.developgroup.mrf.rover.motor.MotorController;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class DriveControllerTest {

    DriveControllerImpl driveController;

    ContinuousDrivingAlgorithm drivingAlgorithm;

    CollisionRunnable collisionRunnable;

    @Before
    public void setUp() throws IOException {
        drivingAlgorithm = Mockito.mock(ContinuousDrivingAlgorithm.class);
        collisionRunnable = Mockito.mock(CollisionRunnable.class);
        driveController = new DriveControllerImpl(drivingAlgorithm, collisionRunnable);
        driveController.leftMotor = Mockito.mock(MotorController.class);
        driveController.rightMotor = Mockito.mock(MotorController.class);
    }

    @Test
    public void testApplyMotorSettings() throws IOException {
        MotorSettings newSetting = new MotorSettings(0.6, -0.8);

        driveController.applyMotorSettings(newSetting);

        verify(driveController.leftMotor).setSpeedPercentage(0.6);
        verify(driveController.rightMotor).setSpeedPercentage(-0.8);
    }

    @Test
    public void testNoDrivingForwardsIfCollision() throws IOException {
        driveController = Mockito.spy(new DriveControllerImpl(new ContinuousDrivingAlgorithmImpl(), collisionRunnable));
        // assure a collision front is encountered
        RoverCollisionInformation collisionFront = new RoverCollisionInformation();
        collisionFront.collisionFrontLeft = CollisionState.Close;
        Mockito.when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(collisionFront);

        driveController.leftMotor = Mockito.mock(MotorController.class);
        driveController.rightMotor = Mockito.mock(MotorController.class);

        driveController.setContinuousDriving(90, 100);

        verify(driveController).stop();
    }

    @Test
    public void testNoDrivingBackwardsIfCollision() throws IOException {
        driveController = Mockito.spy(new DriveControllerImpl(new ContinuousDrivingAlgorithmImpl(), collisionRunnable));
        // make collision back happen
        RoverCollisionInformation collisionBack = new RoverCollisionInformation();
        collisionBack.collisionBackRight = CollisionState.Close;
        Mockito.when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(collisionBack);

        driveController.leftMotor = Mockito.mock(MotorController.class);
        driveController.rightMotor = Mockito.mock(MotorController.class);

        driveController.setContinuousDriving(270, 100);

        verify(driveController).stop();
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
