package de.developgroup.mrf.server.controller;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractCameraSnapshotController {

    /**
     * Encode a BufferedImage in a Base64 string
     * @param img Image to encode
     * @return Encoded Base64 image string
     */
    protected String getBase64EncodedStringFromImage(BufferedImage img) throws IOException {
        byte[] imgData = getByteArrayFromImage(img);
        String base64bytes = Base64.encode(imgData);
        return "data:image/png;base64," + base64bytes;
    }

    /**
     * Returns the byte array of a BufferedImage
     * @param img
     * @return byte array of the image
     * @throws IOException
     */
    protected byte[] getByteArrayFromImage(BufferedImage img) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", out);
        return out.toByteArray();
    }
}
