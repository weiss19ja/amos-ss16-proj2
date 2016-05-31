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

    private final ClientManager clientManager;

    @Inject
    public CameraSnapshotControllerImpl(ClientManager clientManager) throws IOException {
        this.clientManager = clientManager;
    };

    @Override
    public void getCameraSnapshot(int clientId) throws IOException {
        synchronized (this) {
            URL url = new URL("http", "localhost", 9000, "/stream/snapshot.jpeg");
            LOGGER.debug("URL:" + url.toString());
            BufferedImage bufferedImage = ImageIO.read(url);
            String base64ImgString = getBase64EncodedStringFromImage(bufferedImage);
            sendImageResponseToClient(clientId, base64ImgString);
        }
    }

    @Override
    public void sendImageResponseToClient(int clientId, String response) throws IOException {
        LOGGER.info("Sending image response to client {}", clientId);
        clientManager.sendSnapshotResponseToClient(clientId, response);
    }

}
