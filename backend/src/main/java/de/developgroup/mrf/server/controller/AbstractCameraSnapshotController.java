package de.developgroup.mrf.server.controller;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public abstract class AbstractCameraSnapshotController {

    /**
     * Encode a BufferedImage in a Base64 string
     * @param img Image to encode
     * @return Encoded Base64 image string
     */
    protected String getBase64EncodedStringFromImage(BufferedImage img) {
        byte[] imgData = getByteArrayFromImage(img);
        return Base64.encode(imgData);
    }

    /**
     * Returns the byte array of a BufferedImage
     * @param img
     * @return byte array of the image
     */
    protected byte[] getByteArrayFromImage(BufferedImage img) {
        WritableRaster raster = img.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte)raster.getDataBuffer();
        return dataBuffer.getData();
    }
}
