/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

import java.util.Observable;

public class CollisionControllerMock extends Observable implements CollisionController {
    @Override
    public boolean hasCollisionFrontRight() {
        return false;
    }

    @Override
    public boolean hasCollisionFrontLeft() {
        return false;
    }

    @Override
    public boolean hasCollisionBackLeft() {
        return false;
    }

    @Override
    public boolean hasCollisionBackRight() {
        return false;
    }
}
