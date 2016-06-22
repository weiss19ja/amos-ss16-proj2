/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

/**
 * Represents the collision information for one single edge of the rover.
 */
public enum CollisionState {
    None(0),
    Far(1),
    Medium(2),
    Close(3);

    private int value;

    CollisionState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
