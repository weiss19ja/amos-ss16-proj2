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
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class to handle sending drive commands to the actual hardware.
 * Not testable - hardware dependant!
 */
@Singleton
public class DriveControllerImpl implements DriveController {

    private static Logger LOGGER = LoggerFactory.getLogger(DriveControllerImpl.class);

    private MotorController leftMotor;
    private MotorController rightMotor;

    private int desiredSpeed = 0;
    private int desiredTurnrate = 0;

    @Inject
    public DriveControllerImpl() throws IOException {
    }

    @Override
    public void initialize(ConfigurationProvider configurationProvider) throws IOException {
        // this is a "robot leg problem"
        // see https://github.com/google/guice/wiki/FrequentlyAskedQuestions#How_do_I_build_two_similar_but_slightly_different_trees_of_objec
        // we have a leftMotor and a rightMotor - same class, different configuration
        // I did not use the private module solution as it is a bit scary to look at. But maybe:
        // TODO: clean this mess up - MotorControllers should be injected
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice device = bus.getDevice(0x40);
        PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);
        driver.open();
        driver.setFrequency(50);

        leftMotor = new MotorControllerImpl(driver.getOutput(14),
                configurationProvider.bind("motorLeft", MotorControllerConfiguration.class));
        rightMotor = new MotorControllerImpl(driver.getOutput(15),
                configurationProvider.bind("motorRight", MotorControllerConfiguration.class));

        LOGGER.debug("Completed setting up DriveController");
    }

    @Override
    public void setAndApply(int speed, int turnrate) throws IOException {
        setDesiredSpeed(speed);
        setDesiredTurnrate(turnrate);
        updateMotors();
    }

    @Override
    public void setDesiredSpeed(int speed) {
        desiredSpeed = speed;
    }

    @Override
    public void setDesiredTurnrate(int turnrate) {
        desiredTurnrate = turnrate;
    }

    @Override
    public void updateMotors() throws IOException {
        final int leftSpeed = clamp(desiredSpeed - desiredTurnrate, leftMotor.SPEED_MAX_BACKWARD, leftMotor.SPEED_MAX_FORWARD);
        final int rightSpeed = clamp(desiredSpeed + desiredTurnrate, leftMotor.SPEED_MAX_BACKWARD, leftMotor.SPEED_MAX_FORWARD);

        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
