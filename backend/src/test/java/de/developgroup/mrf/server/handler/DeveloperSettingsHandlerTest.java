package de.developgroup.mrf.server.handler;

import com.pi4j.io.gpio.GpioController;
import de.developgroup.mrf.rover.collision.CollisionController;
import de.developgroup.mrf.rover.collision.CollisionControllerMock;
import de.developgroup.mrf.rover.gpio.GpioControllerMock;
import de.developgroup.mrf.server.ClientManager;
import de.developgroup.mrf.server.controller.CameraSnapshotController;
import de.developgroup.mrf.server.controller.DriveController;
import de.developgroup.mrf.server.controller.DriveControllerMock;
import de.developgroup.mrf.server.controller.HeadController;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.cfg4j.provider.ConfigurationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class DeveloperSettingsHandlerTest {

    DeveloperSettingsHandler handler;

    @Before
    public void setUp() {
        handler = new DeveloperSettingsHandler(
                mock(ClientManager.class),
                mock(RoverHandler.class)
        );
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetKillswitch() throws IOException {
        handler.setKillswitchEnabled(true,"message");
        assertTrue("killswitchEnabled should be true after setting it to true",handler.killswitchEnabled);

        handler.setKillswitchEnabled(false, "message");
        assertFalse("killswitchEnabled should be false after setting it to true",handler.killswitchEnabled);
    }

    @Test
    public void testCheckKillswitchEnabled() throws IOException {
        handler.setKillswitchEnabled(true,"message");
        assertEquals("checkKillswitchEnabled and isKillswitchEnabled should always give the same result",handler.checkKillswitchEnabled(), handler.isKillswitchEnabled());

        handler.setKillswitchEnabled(false,"message");
        assertEquals("checkKillswitchEnabled and isKillswitchEnabled should always give the same result",handler.checkKillswitchEnabled(), handler.isKillswitchEnabled());
    }

    // if KillswitchState is false, the client shouldn't get a message
    @Test
    public void testNotifyIfBlockedNoInteraction() throws IOException {
        handler.killswitchEnabled = false;
        int clientId = 1337;
        String message = "message";
        handler.notifyIfBlocked(clientId, message);
        verifyZeroInteractions(handler.clientManager);
    }
}
