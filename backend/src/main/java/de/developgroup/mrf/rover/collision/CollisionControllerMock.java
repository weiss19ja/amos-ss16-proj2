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
