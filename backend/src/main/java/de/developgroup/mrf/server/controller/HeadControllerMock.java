package de.developgroup.mrf.server.controller;

import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeadControllerMock implements HeadController{

    private static Logger LOGGER = LoggerFactory.getLogger(HeadControllerMock.class);

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        LOGGER.info("initializing HeadController");
    }

    @Override
    public void turnHeadUp(int angle) {
        LOGGER.debug("Turning head up");
    }

    @Override
    public void turnHeadDown(int angle) {
        LOGGER.debug("Turning head down");
    }

    @Override
    public void turnHeadLeft(int angle) {
        LOGGER.debug("Turning head left");
    }

    @Override
    public void turnHeadRight(int angle) {
        LOGGER.debug("Turning head right");
    }
}
