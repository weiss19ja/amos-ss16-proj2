/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

import com.pi4j.io.gpio.GpioController;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.ClientManagerImpl;
import de.developgroup.mrf.server.handler.RoverHandler;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CollisionRunnableTest {

    CollisionRunnable runnable;
    IRSensorFactory irSensorFactory;
    GpioController gpio;
    ClientManager clientManager;
    RoverHandler roverHandler;

    @Before
    public void setUp() {
        irSensorFactory = Mockito.mock(IRSensorFactory.class);
        gpio = Mockito.mock(GpioController.class);
        clientManager = Mockito.mock(ClientManagerImpl.class);
        roverHandler = Mockito.mock(RoverHandler.class);

        runnable = new CollisionRunnable(irSensorFactory, gpio, clientManager, roverHandler);
    }

    /* TODO fix
    @Test
    public void testMaybeEmergencyStop() throws IOException {
        RoverCollisionInformation info = new RoverCollisionInformation();
        info.collisionBackRight = CollisionState.Close;
        info.taintedReadings = false;

        runnable.maybeEmergencyStop(info);

        verify(roverHandler).stop();
    }
    */

    @Test
    public void testMaybeEmergencyStopDoesNotStopInSunlight() throws IOException {
        RoverCollisionInformation info = new RoverCollisionInformation();
        info.collisionBackRight = CollisionState.Close;
        info.taintedReadings = true;

        runnable.maybeEmergencyStop(info);

        verify(roverHandler, never()).stop();
    }

    @Test
    public void testSendToClients() {
        ArgumentCaptor<JsonRpc2Request> requestCaptor = ArgumentCaptor.forClass(JsonRpc2Request.class);
        RoverCollisionInformation info = new RoverCollisionInformation();

        runnable.sendToClients(info);

        verify(clientManager).notifyAllClients(requestCaptor.capture());
        JsonRpc2Request actualRequest = requestCaptor.getValue();
        Assert.assertEquals(info, actualRequest.getParams().get(0));
        Assert.assertEquals("updateCollisionInformation", actualRequest.getMethod());
    }

    @Test
    public void testConvertSensorReadingToCollisionState() {
        Assert.assertEquals(CollisionState.None, runnable.convertSensorReadingToCollisionState(0.2));
        Assert.assertEquals(CollisionState.None, runnable.convertSensorReadingToCollisionState(0.34));
        Assert.assertEquals(CollisionState.Far, runnable.convertSensorReadingToCollisionState(0.35));
        Assert.assertEquals(CollisionState.Far, runnable.convertSensorReadingToCollisionState(0.39));
        Assert.assertEquals(CollisionState.Medium, runnable.convertSensorReadingToCollisionState(0.4));
        Assert.assertEquals(CollisionState.Medium, runnable.convertSensorReadingToCollisionState(0.49));
        Assert.assertEquals(CollisionState.Close, runnable.convertSensorReadingToCollisionState(0.5));
    }
}
