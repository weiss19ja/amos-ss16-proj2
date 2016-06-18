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
        collisionFrontLeft = CollisionState.None;
        collisionFrontRight = CollisionState.None;
        collisionBackLeft = CollisionState.None;
        collisionBackRight = CollisionState.None;
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
