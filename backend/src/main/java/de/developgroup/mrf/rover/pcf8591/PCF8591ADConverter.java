/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.pcf8591;

import java.io.IOException;

/**
 * Interface representing a PCF8591 A/D converter.
 * Only its read-only A/D capabilites are exposed here as we do not need the
 * one D/A channel.
 */
public interface PCF8591ADConverter {

    /**
     * Specifies which of the 4 input channels should be used.
     */
    enum InputChannel {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3);

        private final int value;

        InputChannel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Read a value from one channel of the A/D converter.
     * @param channel the channel to read.
     * @return a value between 0 and 255 representing the reading.
     * @throws IOException if the device could not be spoken to
     */
    public int getChannelValue(InputChannel channel) throws IOException;
}
