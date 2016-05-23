package de.developgroup.mrf.server.controller;

import de.developgroup.mrf.rover.servo.ServoController;
import org.cfg4j.provider.ConfigurationProvider;

import java.io.IOException;

/**
 * This class provides all methods that can be used by both by the mock and the actual implementation
 */
public abstract class AbstractHeadController implements HeadController {

    protected int headPositionHorizontal = ServoController.POS_NEUTRAL;
    protected int headPositionVertical = ServoController.POS_NEUTRAL;


    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        // Set head to look straight foreward as initial positon
        headPositionHorizontal = ServoController.POS_NEUTRAL;
        headPositionVertical = ServoController.POS_NEUTRAL;
    }

    @Override
    public void turnHeadUp(int angle) throws IOException {
        turnHeadVertically(Math.abs(angle));
    }


    @Override
    public void turnHeadDown(int angle) throws IOException {
        turnHeadVertically(-Math.abs(angle));
    }


    @Override
    public void turnHeadLeft(int angle) throws IOException {
        turnHeadHorizontally(-Math.abs(angle));
    }


    @Override
    public void turnHeadRight(int angle) throws IOException {
        turnHeadHorizontally(Math.abs(angle));
    }


    /**
     * Convert an angle given in degree to a servo controller position.
     *
     * @param angle
     * @return ServoControllerPosition
     */
    protected int calculateHeadTurnRate(int angle) {
        int arcsec = angle * 3600;
        int result = (arcsec * ServoController.POS_MAX) / (60 * 3600);
        return result;
    }

    /**
     * Clamp val to [min, max]
     *
     * @param val
     * @param min
     * @param max
     * @return Clamped value
     */
    protected int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Turn head by defined angle, negative angle turns down, positive angle up.
     * Only turns until max/min angle is reached
     *
     * @param angle Angle in degree
     * @throws IOException
     */
    public void turnHeadVertically(int angle) throws IOException {
        headPositionVertical = headPositionVertical + calculateHeadTurnRate(angle);
        // limit head turning
        headPositionVertical = clamp(headPositionVertical, ServoController.POS_MIN, ServoController.POS_MAX);
    }

    /**
     * Turn head by defined angle, negative angle turns left, positive angle right
     *
     * @param angle Angle in degree
     * @throws IOException
     */
    public void turnHeadHorizontally(int angle) throws IOException {
        headPositionHorizontal = headPositionHorizontal + calculateHeadTurnRate(angle);
        // limit head turning
        headPositionHorizontal = clamp(headPositionHorizontal, ServoController.POS_MIN, ServoController.POS_MAX);
    }

    public int getHeadPositionVertical() {
        return headPositionVertical;
    }

    public int getHeadPositionHorizontal() {
        return headPositionHorizontal;
    }
}
