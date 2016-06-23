/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.pcf8591;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IRSensorMock implements IRSensor {

    private static Logger LOGGER = LoggerFactory.getLogger(IRSensorMock.class);;

    @Override
    public void switchIrOn() {
        LOGGER.trace("switching sensor led on");
    }

    @Override
    public void switchIrOff() {
        LOGGER.trace("switching sensor led off");
    }

    @Override
    public int getRawReading() throws IOException {
        LOGGER.trace("obtaining raw reading");
        return 0;
    }

    @Override
    public double getCompensatedPercentage() throws IOException {
        LOGGER.trace("obtaining compensated percentage reading");
        return 0;
    }

    @Override
    public boolean isEnvironmentTooBright() throws IOException {
        LOGGER.trace("testing if environment is too bright");
        return false;
    }
}
