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
import org.eclipse.jetty.util.IO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class DriveControllerTest {

    DriveControllerImpl driveController;

    ContinuousDrivingAlgorithm drivingAlgorithm;

    CollisionRunnable collisionRunnable;

    @Before
    public void setUp() throws IOException {
        drivingAlgorithm = Mockito.mock(ContinuousDrivingAlgorithm.class);
        collisionRunnable = Mockito.mock(CollisionRunnable.class);
        Mockito.when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(new RoverCollisionInformation());

        driveController = Mockito.spy(new DriveControllerImpl(drivingAlgorithm, collisionRunnable));
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
    public void testDriveForwards() throws IOException {
        ArgumentCaptor<MotorSettings> motorSettingsCaptor = ArgumentCaptor.forClass(MotorSettings.class);

        driveController.driveForwards();

        verify(driveController).applyMotorSettings(motorSettingsCaptor.capture());
        Assert.assertEquals(1d, motorSettingsCaptor.getValue().leftMotorPercentage);
        Assert.assertEquals(1d, motorSettingsCaptor.getValue().rightMotorPercentage);
    }

    @Test
    public void testDriveBackwards() throws IOException {
        ArgumentCaptor<MotorSettings> motorSettingsCaptor = ArgumentCaptor.forClass(MotorSettings.class);

        driveController.driveBackwards();

        verify(driveController).applyMotorSettings(motorSettingsCaptor.capture());
        Assert.assertEquals(-1d, motorSettingsCaptor.getValue().leftMotorPercentage);
        Assert.assertEquals(-1d, motorSettingsCaptor.getValue().rightMotorPercentage);
    }

    @Test
    public void testTurnLeft() throws IOException {
        ArgumentCaptor<MotorSettings> motorSettingsCaptor = ArgumentCaptor.forClass(MotorSettings.class);

        driveController.turnLeft();

        verify(driveController).applyMotorSettings(motorSettingsCaptor.capture());
        Assert.assertEquals(-1d, motorSettingsCaptor.getValue().leftMotorPercentage);
        Assert.assertEquals(1d, motorSettingsCaptor.getValue().rightMotorPercentage);
    }

    @Test
    public void testTurnRight() throws IOException {
        ArgumentCaptor<MotorSettings> motorSettingsCaptor = ArgumentCaptor.forClass(MotorSettings.class);

        driveController.turnRight();

        verify(driveController).applyMotorSettings(motorSettingsCaptor.capture());
        Assert.assertEquals(1d, motorSettingsCaptor.getValue().leftMotorPercentage);
        Assert.assertEquals(-1d, motorSettingsCaptor.getValue().rightMotorPercentage);
    }

    @Test
    public void testStop() throws IOException {
        ArgumentCaptor<MotorSettings> motorSettingsCaptor = ArgumentCaptor.forClass(MotorSettings.class);

        driveController.stop();

        verify(driveController).applyMotorSettings(motorSettingsCaptor.capture());
        Assert.assertEquals(0d, motorSettingsCaptor.getValue().leftMotorPercentage);
        Assert.assertEquals(0d, motorSettingsCaptor.getValue().rightMotorPercentage);
    }

    @Test
    public void testDriveForwardsBlocked() throws IOException {
        RoverCollisionInformation info = new RoverCollisionInformation();
        info.collisionFrontLeft = CollisionState.Close;
        when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(info);

        driveController.driveForwards();

        verify(driveController, never()).applyMotorSettings(any());
    }

    @Test
    public void testDriveBackwardsBlocked() throws IOException {
        RoverCollisionInformation info = new RoverCollisionInformation();
        info.collisionBackRight = CollisionState.Close;
        when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(info);

        driveController.driveBackwards();

        verify(driveController, never()).applyMotorSettings(any());
    }

    @Test
    public void testJoystickNoDrivingForwardsIfCollision() throws IOException {
        // assure a collision front is encountered
        RoverCollisionInformation collisionFront = new RoverCollisionInformation();
        collisionFront.collisionFrontLeft = CollisionState.Close;
        Mockito.when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(collisionFront);
        Mockito.when(drivingAlgorithm.calculateMotorSetting(anyInt(), anyInt())).thenReturn(new MotorSettings(1, 1));

        driveController.leftMotor = Mockito.mock(MotorController.class);
        driveController.rightMotor = Mockito.mock(MotorController.class);

        driveController.setContinuousDriving(90, 100);

        verify(driveController).stop();
    }

    @Test
    public void testJoystickNoDrivingBackwardsIfCollision() throws IOException {
        // make collision back happen
        RoverCollisionInformation collisionBack = new RoverCollisionInformation();
        collisionBack.collisionBackRight = CollisionState.Close;
        Mockito.when(collisionRunnable.getCurrentCollisionInformation()).thenReturn(collisionBack);
        Mockito.when(drivingAlgorithm.calculateMotorSetting(anyInt(), anyInt())).thenReturn(new MotorSettings(-1, -1));

        driveController.leftMotor = Mockito.mock(MotorController.class);
        driveController.rightMotor = Mockito.mock(MotorController.class);

        driveController.setContinuousDriving(270, 100);

        verify(driveController).stop();
    }

    @Test
    public void testUpdateWithCollidingCollisionInformationStopsForwards() throws IOException {
        RoverCollisionInformation info = Mockito.mock(RoverCollisionInformation.class);
        Mockito.when(info.hasCollisionFront()).thenReturn(true);

        driveController.driveForwards();
        driveController.update(collisionRunnable, info);

        verify(driveController).stop();
    }

    @Test
    public void testUpdateWithNonCollidingCollisionInformationDoesNotStopForwards() throws IOException {
        RoverCollisionInformation info = Mockito.mock(RoverCollisionInformation.class);
        Mockito.when(info.hasCollisionFront()).thenReturn(false);

        driveController.driveForwards();
        driveController.update(collisionRunnable, info);

        verify(driveController, never()).stop();
    }

    @Test
    public void testUpdateWithCollidingCollisionInformationStopsBackwards() throws IOException {
        RoverCollisionInformation info = Mockito.mock(RoverCollisionInformation.class);
        Mockito.when(info.hasCollisionBack()).thenReturn(true);

        driveController.driveBackwards();
        driveController.update(collisionRunnable, info);

        verify(driveController).stop();
    }

    @Test
    public void testUpdateWithNonCollidingCollisionInformationDoesNotStopBackwards() throws IOException {
        RoverCollisionInformation info = Mockito.mock(RoverCollisionInformation.class);
        Mockito.when(info.hasCollisionBack()).thenReturn(false);

        driveController.driveBackwards();
        driveController.update(collisionRunnable, info);

        verify(driveController, never()).stop();
    }

    @Test
    public void testSpeedMultiplierGetSet() throws IOException {
        double value = 0.42;

        driveController.setSpeedMultiplier(value);
        Assert.assertEquals(value, driveController.getSpeedMultiplier());
    }

    @Test
    public void testSpeedMultiplierIsBeingApplied() throws IOException {
        driveController.setSpeedMultiplier(0.42);
        driveController.driveForwards();

        verify(driveController.leftMotor).setSpeedPercentage(0.42);
        verify(driveController.rightMotor).setSpeedPercentage(0.42);
    }

    @Test
    public void testNewSpeedMultiplierIsAppliedInstantly() throws IOException {
        driveController.setSpeedMultiplier(0.42);

        verify(driveController).applyMotorSettings(any());
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
