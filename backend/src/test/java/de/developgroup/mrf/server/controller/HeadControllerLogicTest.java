/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import de.developgroup.mrf.Main;
import de.developgroup.mrf.rover.servo.ServoController;
import org.cfg4j.provider.ConfigurationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This class tests the logic of the HeadController, that means everything that doesn't involve GPIO-Pins
 */
public class HeadControllerLogicTest {

    HeadControllerMock controller;
    ConfigurationProvider roverProperties;

    @Before
    public void setUp() throws Exception {
        controller = new HeadControllerMock();
        roverProperties = Main.getPropertiesProvider();
        controller.initialize(roverProperties);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInit() throws IOException {
        controller.initialize(roverProperties);
        int horizontal = controller.getHeadPositionHorizontal();
        int vertical = controller.getHeadPositionVertical();
        assertTrue("Horizontal position should be 0", horizontal == 0);
        assertTrue("Vertical position should be 0", vertical == 0);
    }

    @Test
    public void testMovementUpwards() throws IOException {
        controller.initialize(roverProperties);
        int initialPos = controller.getHeadPositionVertical();
        controller.turnHeadUp(10);
        int newPos = controller.getHeadPositionVertical();
        assertTrue("Before turning, the head position was " + initialPos +
                " after turning upwards, the head position is " + newPos +
                ". The new position should be greater than the initial one", initialPos < newPos);
    }

    @Test
    public void testMovementDownwards() throws IOException {
        controller.initialize(roverProperties);
        int initialPos = controller.getHeadPositionVertical();
        controller.turnHeadDown(10);
        int newPos = controller.getHeadPositionVertical();
        assertTrue("Before turning, the head position was " + initialPos +
                " after turning downwards, the head position is " + newPos +
                ". The new position should be smaller than the initial one", initialPos > newPos);
    }

    @Test
    public void testMovementLeft() throws IOException {
        controller.initialize(roverProperties);
        int initialPos = controller.getHeadPositionHorizontal();
        controller.turnHeadLeft(10);
        int newPos = controller.getHeadPositionHorizontal();
        assertTrue("Before turning, the head position was " + initialPos +
                " after turning left, the head position is " + newPos +
                ". The new position should be smaller than the initial one", initialPos > newPos);
    }

    @Test
    public void testMovementRight() throws IOException {
        controller.initialize(roverProperties);
        int initialPos = controller.getHeadPositionHorizontal();
        controller.turnHeadRight(10);
        int newPos = controller.getHeadPositionHorizontal();
        assertTrue("Before turning, the head position was " + initialPos +
                " after turning right, the head position is " + newPos +
                ". The new position should be greater than the initial one", initialPos < newPos);
    }

    @Test
    public void testHeadReset() throws IOException {
        controller.turnHeadRight(10);
        controller.turnHeadDown(30);

        controller.resetHeadPosition();

        assertEquals(ServoController.POS_NEUTRAL, controller.getHeadPositionHorizontal());
        assertEquals(ServoController.POS_NEUTRAL, controller.getHeadPositionVertical());
    }

    @Test
    public void testLimitUpwards() throws IOException {
        // Number that is way to high so limit is reached
        controller.turnHeadUp(1000);
        int pos = controller.getHeadPositionVertical();
        int posMax = ServoController.POS_MIN;
        assertTrue("Head position is " + pos + " but should not exceed limit of " + posMax, pos == posMax);
    }

    @Test
    public void testLimitDownwards() throws IOException {
        // Number that is way to high so limit is reached
        controller.turnHeadDown(1000);
        int pos = controller.getHeadPositionVertical();
        int posMax = ServoController.POS_MAX;
        assertTrue("Head position is " + pos + " but should not exceed limit of " + posMax, pos == posMax);
    }

    @Test
    public void testLimitLeft() throws IOException {
        controller.initialize(roverProperties);
        // Number that is way to high so limit is reached
        controller.turnHeadLeft(1000);
        int pos = controller.getHeadPositionHorizontal();
        int posMax = ServoController.POS_MAX;
        assertTrue("Head position is " + pos + " but should not exceed limit of " + posMax, pos == posMax);
    }

    @Test
    public void testLimitRight() throws IOException {
        controller.initialize(roverProperties);
        // Number that is way to high so limit is reached
        controller.turnHeadRight(1000);
        int pos = controller.getHeadPositionHorizontal();
        int posMax = ServoController.POS_MIN;
        assertTrue("Head position is " + pos + " but should not exceed limit of " + posMax, pos == posMax);
    }

    @Test
    public void testSignConversionUpwards() throws IOException {
        controller.initialize(roverProperties);
        controller.turnHeadUp(-10);
        int pos1 = controller.getHeadPositionVertical();

        controller.initialize(roverProperties);
        controller.turnHeadUp(10);
        int pos2 = controller.getHeadPositionVertical();
        assertTrue("Turning head upwards by an negative angle should have the same result as a positive one. " +
                "But here turning negative results in " + pos1 + " and turning positive results in " + pos2, pos1 == pos2);
    }

    @Test
    public void testSignConversionDownwards() throws IOException {
        controller.initialize(roverProperties);
        controller.turnHeadDown(-10);
        int pos1 = controller.getHeadPositionVertical();

        controller.initialize(roverProperties);
        controller.turnHeadDown(10);
        int pos2 = controller.getHeadPositionVertical();
        assertTrue("Turning head upwards by an negative angle should have the same result as a positive one. " +
                "But here turning negative results in " + pos1 + " and turning positive results in " + pos2, pos1 == pos2);
    }

    @Test
    public void testSignConversionLeft() throws IOException {
        controller.initialize(roverProperties);
        controller.turnHeadLeft(-10);
        int pos1 = controller.getHeadPositionHorizontal();

        controller.initialize(roverProperties);
        controller.turnHeadLeft(10);
        int pos2 = controller.getHeadPositionHorizontal();
        assertTrue("Turning head upwards by an negative angle should have the same result as a positive one. " +
                "But here turning negative results in " + pos1 + " and turning positive results in " + pos2, pos1 == pos2);
    }

    @Test
    public void testSignConversionRight() throws IOException {
        controller.initialize(roverProperties);
        controller.turnHeadRight(-10);
        int pos1 = controller.getHeadPositionVertical();

        controller.initialize(roverProperties);
        controller.turnHeadRight(10);
        int pos2 = controller.getHeadPositionVertical();
        assertTrue("Turning head upwards by an negative angle should have the same result as a positive one. " +
                "But here turning negative results in " + pos1 + " and turning positive results in " + pos2, pos1 == pos2);
    }
}
