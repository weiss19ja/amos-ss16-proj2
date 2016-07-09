/*
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

package de.developgroup.mrf.rover.collision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CyclicCounterTest {

    CyclicCounter counter;

    int counterThreshold = 6;

    @Before
    public void setUp() {
        counter = new CyclicCounter(counterThreshold);
    }

    @Test
    public void testTriggeredFalseWorks() {
        Assert.assertTrue(counter.triggered());

        counter.increment();

        Assert.assertFalse(counter.triggered());

        counter.increment();

        Assert.assertFalse(counter.triggered());
    }

    @Test
    public void testTriggeredTrueWorks() {
        Assert.assertTrue(counter.triggered());

        for (int i = 0; i < counterThreshold - 1; i++) {
            counter.increment();
            Assert.assertFalse(counter.triggered());
        }

        counter.increment();

        Assert.assertTrue(counter.triggered());
    }

    @Test
    public void testIncrementAndCheck() {
        Assert.assertTrue(counter.triggered());

        for (int i = 0; i < counterThreshold - 1; i++) {
            counter.increment();
            Assert.assertFalse(counter.triggered());
        }

        Assert.assertTrue(counter.incrementAndCheck());
    }
}
