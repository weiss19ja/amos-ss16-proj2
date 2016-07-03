/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.developgroup.mrf.rover.pcf8591.IRSensor;
import de.developgroup.mrf.rover.pcf8591.PCF8591ADConverter;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.sleep;



/**
 * Representation of a thread that continuously polls the IR sensors (in a defined interval) and sends the information
 * to the frontend.
 */
@Singleton
public class CollisionRunnable extends Observable implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(CollisionRunnable.class);

    /**
     * collision percentage >= 0.3 -> far collision
     */
    private final double THR_COLLISION_FAR = 0.35;

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
     * Should make for approx. 10 checks/second. This is not a real-time system anyways.
     */
    private final int POLL_INTERVAL_MS = 90;

    private IRSensor sensorFrontLeft;

    private IRSensor sensorFrontRight;

    private IRSensor sensorBackRight;

    private IRSensor sensorBackLeft;

    private ClientManager clientManager;

    private RoverHandler roverHandler;

    /**
     * Contains recently gathered collision information.
     * Also used to prevent spamming the clients: only new infos are sent to the client.
     *
     * Synchronize access - this information may be accessed by multiple threads.
     */
    private RoverCollisionInformation currentCollisionInformation;

    /**
     * Lock object for currentCollisionInformation
     */
    private final Object collisionInformationLock = new Object();

    @Inject
    public CollisionRunnable(IRSensorFactory sensorFactory,
                             GpioController gpio,
                             ClientManager clientManager,
                             RoverHandler roverHandler) {
        LOGGER.info("creating new CollisionRunnable via injected constructor");
        this.sensorFrontRight = sensorFactory.create(PCF8591ADConverter.InputChannel.ZERO,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW));
        this.sensorFrontLeft = sensorFactory.create(PCF8591ADConverter.InputChannel.ONE,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW));
        this.sensorBackRight = sensorFactory.create(PCF8591ADConverter.InputChannel.THREE,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, PinState.LOW));
        this.sensorBackLeft = sensorFactory.create(PCF8591ADConverter.InputChannel.TWO,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW));

        this.clientManager = clientManager;
        this.roverHandler = roverHandler;
    }

    /**
     * Contains the event loop of the collision thread that repeatedly checks for collisions.
     */
    public void run() {
        while(true) {
            try {
                RoverCollisionInformation info = readAllSensors();
                if (!info.equals(getCurrentCollisionInformation())) {
                    setChanged();
                    notifyObservers(info);
                    // only send to client if anything new occurred
                    sendToClients(info);
                    setCurrentCollisionInformation(info);
                }
                sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                LOGGER.error("An IO exception occured while reading the sensors: " + e);
            }
        }
    }
    
    public RoverCollisionInformation getCurrentCollisionInformation() {
        synchronized (collisionInformationLock) {
            return currentCollisionInformation;
        }
    }

    public void setCurrentCollisionInformation(RoverCollisionInformation newCollisionInfo) {
        synchronized (collisionInformationLock) {
            currentCollisionInformation = newCollisionInfo;
        }
    }

    /**
     * Send collision information to all connected clients.
     * @param info the information to send to the clients.
     */
    public void sendToClients(RoverCollisionInformation info) {
        // pack a collision information object
        ArrayList<Object> params = new ArrayList<>();
        params.add(info);

        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request("updateCollisionInformation", params);

        clientManager.notifyAllClients(jsonRpc2Request);
    }

    /**
     * Gathers information from all sensors and creates a matching RoverCollisionInformation object.
     * @return a RoverCollisionInformation object with information from all sensors.
     * @throws IOException if the sensor fails to read a value.
     */
    public RoverCollisionInformation readAllSensors() throws IOException {
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
    public CollisionState convertSensorReadingToCollisionState(double sensorReading) {
        CollisionState retVal = CollisionState.None;
        if (sensorReading >= THR_COLLISION_FAR) {
            retVal = CollisionState.Far;
        }
        if (sensorReading >= THR_COLLISION_MED) {
            retVal = CollisionState.Medium;
        }
        if (sensorReading >= THR_COLLISION_CLOSE) {
            retVal = CollisionState.Close;
        }
        return retVal;
    }
}
