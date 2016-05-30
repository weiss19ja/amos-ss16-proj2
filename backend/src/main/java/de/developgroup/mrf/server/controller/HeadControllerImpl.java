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
import org.eclipse.jetty.util.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class to handle sending the head turning commands to the actual hardware.
 * Not testable - hardware dependant!
 */
@Singleton
public class HeadControllerImpl extends AbstractHeadController implements HeadController{

    private static Logger LOGGER = LoggerFactory.getLogger(HeadControllerImpl.class);

    private ServoController verticalHeadMotor;
    private ServoController horizontalHeadMotor;


    @Inject
    public HeadControllerImpl() throws IOException {
    }

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        super.initialize(configurationProvider);

        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice device = bus.getDevice(0x40);
        PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);
        driver.open();
        driver.setFrequency(50);

        horizontalHeadMotor = new ServoControllerImpl(driver.getOutput(1),
                configurationProvider.bind("servo1", ServoConfiguration.class));
        verticalHeadMotor = new ServoControllerImpl(driver.getOutput(0),
                configurationProvider.bind("servo0", ServoConfiguration.class));

        horizontalHeadMotor.setPosition(headPositionHorizontal);
        verticalHeadMotor.setPosition(headPositionVertical);
        LOGGER.debug("Completed setting up HeadController");
    }

    @Override
    public void turnHeadVertically(int angle) throws IOException {
        super.turnHeadVertically(angle);
        verticalHeadMotor.setPosition(headPositionVertical);
        LOGGER.debug("Set Position Vertical to: "+ headPositionVertical);
    }

    @Override
    public void turnHeadHorizontally(int angle) throws IOException {
        super.turnHeadHorizontally(angle);
        horizontalHeadMotor.setPosition(headPositionHorizontal);
        LOGGER.debug("Set Position Horizontal to: "+ headPositionHorizontal);
    }

    @Override
    public void resetHeadPosition() throws IOException {
        super.resetHeadPosition();
        horizontalHeadMotor.setPosition(headPositionHorizontal);
        horizontalHeadMotor.setPosition(headPositionVertical);
    }
}
