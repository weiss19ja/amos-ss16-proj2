package de.developgroup.mrf.server.controller;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import de.developgroup.mrf.rover.motor.MotorController;
import de.developgroup.mrf.rover.motor.MotorControllerConfiguration;
import de.developgroup.mrf.rover.motor.MotorControllerImpl;
import de.developgroup.mrf.rover.pwmgenerator.PCA9685PWMGenerator;
import de.developgroup.mrf.rover.servo.ServoConfiguration;
import de.developgroup.mrf.rover.servo.ServoController;
import de.developgroup.mrf.rover.servo.ServoControllerImpl;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class to handle sending the head turning commands to the actual hardware.
 * Not testable - hardware dependant!
 */
@Singleton
public class HeadControllerImpl implements HeadController{

    private static Logger LOGGER = LoggerFactory.getLogger(HeadControllerImpl.class);

    private ServoController verticalHeadMotor;
    private ServoController horizontalHeadMotor;

    private int headPositionVertical = 0;
    private int headPositionHorizontal = 0;

    @Inject
    public HeadControllerImpl() throws IOException {
    }

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice device = bus.getDevice(0x40);
        PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);
        driver.open();
        driver.setFrequency(50);

        horizontalHeadMotor = new ServoControllerImpl(driver.getOutput(1),
                configurationProvider.bind("servo1", ServoConfiguration.class));
        verticalHeadMotor = new ServoControllerImpl(driver.getOutput(0),
                configurationProvider.bind("servo0", ServoConfiguration.class));

        LOGGER.debug("Completed setting up HeadController");
    }

    @Override
    public void turnHeadUp(int angle) throws IOException{
        turnHeadVertically(angle);
    }

    @Override
    public void turnHeadDown(int angle)throws IOException {
        turnHeadVertically(-angle);
    }

    @Override
    public void turnHeadLeft(int angle) throws IOException{
        turnHeadHorizontally(-angle);
    }

    @Override
    public void turnHeadRight(int angle) throws IOException{
        turnHeadHorizontally(angle);
    }


    /**
     * Convert an angle given in degree to a servo controller position.
     *
     * @param angle
     * @return ServoControllerPosition
     */
    private int calculateHeadTurnRate(int angle) {
        int arcsec = angle * 3600;
        int result = (arcsec * ServoController.POS_MAX) / (60 * 3600);
        return result;
    }

    /**
     * Clamp val to [min, max]
     * @param val
     * @param min
     * @param max
     * @return Clamped value
     */
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Turn head by defined angle, negative angle turns down, positive angle up.
     * Only turns until max/min angle is reached
     * @param angle Angle in degree
     * @throws IOException
     */
    public void turnHeadVertically(int angle) throws IOException {
        headPositionVertical = headPositionVertical + calculateHeadTurnRate(angle);
        // limit head turning
        headPositionVertical = clamp(headPositionVertical, ServoController.POS_MIN, ServoController.POS_MAX);
        verticalHeadMotor.setPosition(headPositionVertical);
        LOGGER.debug("Set Position Vertical to: "+ headPositionVertical);
    }

    /**
     * Turn head by defined angle, negative angle turns left, positive angle right
     * @param angle Angle in degree
     * @throws IOException
     */
    public void turnHeadHorizontally(int angle) throws IOException {
        headPositionHorizontal = headPositionHorizontal + calculateHeadTurnRate(angle);
        // limit head turning
        headPositionHorizontal = clamp(headPositionHorizontal, ServoController.POS_MIN, ServoController.POS_MAX);
        horizontalHeadMotor.setPosition(headPositionHorizontal);
        LOGGER.debug("Set Position Horizontal to: "+ headPositionHorizontal);
    }
}
