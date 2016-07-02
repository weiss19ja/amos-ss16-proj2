/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

/**
 * Container for collision states for all four edges of the rover.
 */
public class RoverCollisionInformation {
    /**
     * Flag that is set if the environment is too bright and readings may be tainted.
     *
     * This tainted readings you've given
     * I gave you all a rover could give you
     *
     * TAINTED READINGS OOOOOOOH
     */
    public boolean taintedReadings;

    /**
     * Collision states for all sensors.
     */
    public CollisionState collisionFrontLeft;
    public CollisionState collisionFrontRight;
    public CollisionState collisionBackRight;
    public CollisionState collisionBackLeft;

    public RoverCollisionInformation() {
        taintedReadings = false;
        collisionFrontLeft = CollisionState.None;
        collisionFrontRight = CollisionState.None;
        collisionBackLeft = CollisionState.None;
        collisionBackRight = CollisionState.None;
    }

    /**
     * Determine whether this instance of collision information is very close, so that e.g. driving should not be
     * possible right now.
     * @return true if the rover is too close to an obstacle; else false
     */
    public boolean hasDangerousCollision() {
        return (collisionFrontLeft == CollisionState.Close)
                || (collisionFrontRight == CollisionState.Close)
                || (collisionBackLeft == CollisionState.Close)
                || (collisionBackRight == CollisionState.Close);
    }

    /**
     * Determine whether this instance of collision information has a collision on the front of the rover
     * @return true if the rover's front is too close to an obstacle; else false
     */
    public boolean hasCollisionFront() {
        return (collisionFrontLeft == CollisionState.Close)
                || (collisionFrontRight == CollisionState.Close);
    }

    /**
     * Determin whether this instance of collision information has a collision on the bakc of the rover
     * @return true if the rover's back is too close to an obstacle; else false
     */
    public boolean hasCollisionBack() {
        return (collisionBackLeft == CollisionState.Close)
                || (collisionBackRight == CollisionState.Close);
    }

    @Override
    public String toString() {
        return "RoverCollisionInformation{" +
                "taintedReadings=" + taintedReadings +
                ", collisionFrontLeft=" + collisionFrontLeft +
                ", collisionFrontRight=" + collisionFrontRight +
                ", collisionBackRight=" + collisionBackRight +
                ", collisionBackLeft=" + collisionBackLeft +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoverCollisionInformation that = (RoverCollisionInformation) o;

        if (taintedReadings != that.taintedReadings) return false;
        if (collisionFrontLeft != that.collisionFrontLeft) return false;
        if (collisionFrontRight != that.collisionFrontRight) return false;
        if (collisionBackRight != that.collisionBackRight) return false;
        return collisionBackLeft == that.collisionBackLeft;
    }

    @Override
    public int hashCode() {
        int result = (taintedReadings ? 1 : 0);
        result = 31 * result + collisionFrontLeft.hashCode();
        result = 31 * result + collisionFrontRight.hashCode();
        result = 31 * result + collisionBackRight.hashCode();
        result = 31 * result + collisionBackLeft.hashCode();
        return result;
    }
}
