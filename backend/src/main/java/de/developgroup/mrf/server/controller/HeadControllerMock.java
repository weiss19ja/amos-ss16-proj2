package de.developgroup.mrf.server.controller;

import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeadControllerMock extends AbstractHeadController implements HeadController {

    private static Logger LOGGER = LoggerFactory.getLogger(HeadControllerMock.class);

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        super.initialize(configurationProvider);
        LOGGER.debug("Completed setting up HeadController");
    }

    @Override
    public void turnHeadVertically(int angle) throws IOException {
        super.turnHeadVertically(angle);
        LOGGER.debug("Set Position Vertical to: " + headPositionVertical);
    }

    @Override
    public void turnHeadHorizontally(int angle) throws IOException {
        super.turnHeadHorizontally(angle);
        LOGGER.debug("Set Position Horizontal to: " + headPositionHorizontal);
    }

}
