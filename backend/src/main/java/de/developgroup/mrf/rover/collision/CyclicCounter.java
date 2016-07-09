/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.rover.collision;

/**
 * Class to implement a cyclic counter that returns true every n cycles.
 */
public class CyclicCounter {

    /**
     * The cycle number after which the counter should be true.
     */
    private final int CycleThreshold;

    /**
     * Current counter value.
     */
    private int counterValue;

    /**
     * Create a new cyclic counter.
     * @param cycleThreshold the number of iterations after which the counter should be true.
     */
    public CyclicCounter(int cycleThreshold) {
        this.CycleThreshold = cycleThreshold;
        this.counterValue = 0;
    }

    /**
     * Increment the counter value.
     */
    public void increment() {
        counterValue = (counterValue + 1) % CycleThreshold;
    }

    /**
     * Check if this counter has reached an n-th iteration.
     * @return true if this is an n-th iteration.
     */
    public boolean triggered() {
        return counterValue % CycleThreshold == 0;
    }

    /**
     * Convenience method to perform increment and check value in one step.
     * @return true if the counter was in an n-th iteration after incrementing.
     */
    public boolean incrementAndCheck() {
        increment();
        return triggered();
    }
}
