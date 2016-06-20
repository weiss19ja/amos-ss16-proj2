package de.developgroup.mrf.rover.collision;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.developgroup.mrf.rover.pcf8591.IRSensor;
import de.developgroup.mrf.rover.pcf8591.PCF8591ADConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.lang.Thread.sleep;



/**
 * Representation of a thread that continously polls the IR sensors (in a defined interval) and sends the information
 * to the frontend.
 */
@Singleton
public class CollisionRunnable implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(CollisionRunnable.class);

    /**
     * collision percentage >= 0.3 -> far collision
     */
    private final double THR_COLLISION_FAR = 0.3;

    /**
     * collision percentage >= 0.4 -> medium collision
     */
    private final double THR_COLLISION_MED = 0.4;

    /**
     * collision percenteage >= 0.5 -> close collision
     */
    private final double THR_COLLISION_CLOSE = 0.5;

    /**
     * Time between two successive sensor queries.
     */
    private final int POLL_INTERVAL_MS = 1000;

    private IRSensor sensorFrontLeft;

    private IRSensor sensorFrontRight;

    private IRSensor sensorBackRight;

    private IRSensor sensorBackLeft;



    @Inject
    public CollisionRunnable(IRSensorFactory sensorFactory, GpioController gpio) {
        LOGGER.info("creating new CollisionRunnable via injected constructor");
        this.sensorFrontLeft = sensorFactory.create(PCF8591ADConverter.InputChannel.ZERO,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW));
        this.sensorFrontRight = sensorFactory.create(PCF8591ADConverter.InputChannel.ONE,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW));
        this.sensorBackRight = sensorFactory.create(PCF8591ADConverter.InputChannel.TWO,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, PinState.LOW));
        this.sensorBackLeft = sensorFactory.create(PCF8591ADConverter.InputChannel.THREE,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW));
    }

    /**
     * Contains the event loop of the collision thread that repeatedly checks for collisions.
     */
    public void run() {
        while(true) {
            try {
                LOGGER.info("Executing IR sensor poll event loop");
                RoverCollisionInformation info = readAllSensors();
                sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                LOGGER.error("An IO exception occured while reading the sensors: " + e);
            }
        }
    }

    /**
     * Gathers information from all sensors and creates a matching RoverCollisionInformation object.
     * @return a RoverCollisionInformation object with information from all sensors.
     * @throws IOException if the sensor fails to read a value.
     */
    private RoverCollisionInformation readAllSensors() throws IOException {
        RoverCollisionInformation info = new RoverCollisionInformation();

        info.taintedReadings = sensorFrontLeft.isEnvironmentTooBright()
                | sensorFrontRight.isEnvironmentTooBright()
                | sensorBackRight.isEnvironmentTooBright()
                | sensorBackLeft.isEnvironmentTooBright();

        info.collisionFrontLeft = convertSensorReadingToCollisionState(sensorFrontLeft.getCompensatedPercentage());
        info.collisionFrontRight = convertSensorReadingToCollisionState(sensorFrontRight.getCompensatedPercentage());
        info.collisionBackRight = convertSensorReadingToCollisionState(sensorBackRight.getCompensatedPercentage());
        info.collisionBackLeft = convertSensorReadingToCollisionState(sensorBackLeft.getCompensatedPercentage());

        return info;
    }

    /**
     * Convert a numerical sensor to a discrete estimation of collision danger.
     * @param sensorReading percentage of collision likeliness.
     * @return a discrete CollisionState information
     */
    private CollisionState convertSensorReadingToCollisionState(double sensorReading) {
        CollisionState retVal = CollisionState.None;
        if (sensorReading >= THR_COLLISION_FAR) {
            retVal = CollisionState.Far;
        } else if (sensorReading >= THR_COLLISION_MED) {
            retVal = CollisionState.Medium;
        } else if (sensorReading >= THR_COLLISION_CLOSE) {
            retVal = CollisionState.Close;
        }
        return retVal;
    }
}
