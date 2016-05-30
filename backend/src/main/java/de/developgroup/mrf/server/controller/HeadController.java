package de.developgroup.mrf.server.controller;

import org.cfg4j.provider.ConfigurationProvider;

import java.io.IOException;

public interface HeadController {

    /**
     * Set up head-controlling hardware with values from configuration
     * @param configurationProvider configuration which includes the values
     * @throws IOException
     */
    void initialize(ConfigurationProvider configurationProvider) throws IOException;

    /**
     * Turn head upwards by given angle, will always turn upwards, regardless of angle's sign
     * Will stop when moving limit is reached
     *
     * @param angle
     * @throws IOException
     */
    void turnHeadUp(int angle) throws IOException;

    /**
     * Turn head downward by given angle, will always turn upwards, regardless of angle's sign
     * Will stop when moving limit is reached
     *
     * @param angle
     * @throws IOException
     */
    void turnHeadDown(int angle) throws IOException;

    /**
     * Turn head left by given angle, will always turn upwards, regardless of angle's sign
     * Will stop when moving limit is reached
     *
     * @param angle
     * @throws IOException
     */
    void turnHeadLeft(int angle) throws IOException;

    /**
     * Turn head right by given angle, will always turn upwards, regardless of angle's sign
     * Will stop when moving limit is reached
     *
     * @param angle
     * @throws IOException
     */
    void turnHeadRight(int angle) throws IOException;

    /**
     * Reset head to neutral position (angles 0, 0).
     * @throws IOException
     */
    void resetHeadPosition() throws IOException;
}