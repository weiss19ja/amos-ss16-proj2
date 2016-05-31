package de.developgroup.mrf.server.controller;

import java.io.IOException;

public interface CameraSnapshotController {

    /**
     * Request camera snapshot from the uv4l_raspicam server
     * @param clientId The clientId from the frontend request
     * @throws IOException
     */
    void getCameraSnapshot(int clientId) throws IOException;

    /**
     * Sends the image Base64 encoded to the frontend
     * @param clientId The clientId which should receive the image
     * @throws IOException
     */
    void sendImageResponseToClient(int clientId, String response) throws IOException;

}
