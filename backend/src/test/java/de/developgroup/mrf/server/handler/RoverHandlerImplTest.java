package de.developgroup.mrf.server.handler;

import com.pi4j.io.gpio.GpioController;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerMock;
import de.developgroup.mrf.rover.gpio.GpioControllerMock;
import de.developgroup.mrf.server.controller.DriveController;
import de.developgroup.mrf.server.controller.DriveControllerMock;
import de.developgroup.mrf.server.controller.HeadController;
import org.cfg4j.provider.ConfigurationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class RoverHandlerImplTest {

    RoverHandlerImpl handler;

    @Before
    public void setUp() {
        handler = new RoverHandlerImpl(
                mock(CollisionController.class),
                mock(GpioController.class),
                mock(DriveController.class),
                mock(HeadController.class)
        );
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDriveForward() throws IOException {
        handler.driveForward(100);

        verify(handler.driveController).setAndApply(100, 0);
    }

    @Test
    public void testDriveBackward() throws IOException {
        handler.driveBackward(100);

        verify(handler.driveController).setAndApply(-100, 0);
    }

    @Test
    public void testStop() throws IOException {
        handler.stop();

        verify(handler.driveController).setAndApply(0, 0);
    }

    @Test
    public void testTurnLeft() throws IOException {
        handler.turnLeft(20);

        verify(handler.driveController).setAndApply(0, 20);
    }

    @Test
    public void testTurnRight() throws IOException {
        handler.turnRight(20);

        verify(handler.driveController).setAndApply(0, -20);
    }

    @Test
    public void testHandlePing() {
        assertEquals(handler.handlePing(1), "pong 2");
    }

    @Test
    public void testShutdownRover() {
        handler.shutdownRover();

        verify(handler.gpio).shutdown();
    }
}
