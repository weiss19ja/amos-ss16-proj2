/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import de.developgroup.mrf.server.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;

@Singleton
public class CameraSnapshotControllerMock extends AbstractCameraSnapshotController implements CameraSnapshotController{

    private static Logger LOGGER = LoggerFactory.getLogger(CameraSnapshotControllerMock.class);

    private final ClientManager clientManager;
    private String urlString;

    @Inject
    public CameraSnapshotControllerMock(ClientManager clientManager) throws IOException {
        this.clientManager = clientManager;
    }

    @Override
    public void getCameraSnapshot(int clientId) throws IOException {
	//TODO: CameraSnapshotControllerMock getCameraSnapshot implementation
    }

    @Override
    public void sendImageResponseToClient(int clientId, String response) throws IOException {
	//TODO: CameraSnapshotControllerMock sendImageResponseToClient implementation
    }

}
