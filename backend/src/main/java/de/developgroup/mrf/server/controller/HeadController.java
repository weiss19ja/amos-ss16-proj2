package de.developgroup.mrf.server.controller;

import org.cfg4j.provider.ConfigurationProvider;

import java.io.IOException;

public interface HeadController {

    void initialize(ConfigurationProvider configurationProvider) throws IOException;

    void turnHeadUp(int angle) throws IOException;

    void turnHeadDown(int angle) throws IOException;

    void turnHeadLeft(int angle) throws IOException;

    void turnHeadRight(int angle) throws IOException;

    void stopHead();
}