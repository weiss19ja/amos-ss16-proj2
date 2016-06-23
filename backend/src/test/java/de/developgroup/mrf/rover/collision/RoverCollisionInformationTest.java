/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RoverCollisionInformationTest {

    RoverCollisionInformation info;

    @Before
    public void setUp() {
        info = new RoverCollisionInformation();
    }

    @Test
    public void testIsNotDangerousCollision() {
        info.collisionBackRight
                = info.collisionFrontRight
                = CollisionState.None;
        info.collisionBackLeft = CollisionState.Far;
        info.collisionFrontLeft = CollisionState.Medium;

        Assert.assertFalse(info.hasDangerousCollision());
    }

    @Test
    public void testisDangerousCollision() {
        info.collisionBackRight = CollisionState.Close;
        info.collisionFrontRight
                = info.collisionFrontLeft
                = info.collisionFrontRight
                = CollisionState.None;

        Assert.assertTrue(info.hasDangerousCollision());
    }
}
