package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.developgroup.mrf.server.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@Singleton
public class CameraSnapshotControllerImpl extends AbstractCameraSnapshotController implements CameraSnapshotController {

    private static Logger LOGGER = LoggerFactory.getLogger(CameraSnapshotControllerImpl.class);

    private ClientManager clientManager;
    private String cameraHost;
    private int cameraPort;

    @Inject
    public CameraSnapshotControllerImpl() throws IOException {};

    @Override
    public void initialize(ClientManager clientManager) throws IOException {
        this.clientManager = clientManager;
        this.cameraHost = "localhost";
        this.cameraPort = 9000;
        LOGGER.debug("Completed setting up CameraSnapshotController");
    }

    @Override
    public void getCameraSnapshot(int clientId) throws IOException {
        URL url = new URL("http", cameraHost, cameraPort, "/stream/snapshot.jpeg");
        BufferedImage bufferedImage = ImageIO.read(url);
        String base64ImgString = getBase64EncodedStringFromImage(bufferedImage);
        sendImageResponseToClient(clientId, base64ImgString);
    }

    @Override
    public void sendImageResponseToClient(int clientId, String response) throws IOException {
        clientManager.sendSnapshotResponseToClient(clientId, response);
    }

}
