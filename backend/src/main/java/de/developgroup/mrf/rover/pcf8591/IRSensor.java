package de.developgroup.mrf.rover.pcf8591;

import java.io.IOException;

/**
 * An IR sensor consists of an IR LED and an IR receiver (via a PCF8591).
 */
public interface IRSensor {

    /**
     * Switch the IR LED of this sensor on.
     */
    void switchIrOn();

    /**
     * Switch the IR LED of this sensor off.
     */
    void switchIrOff();


    /**
     * Get the raw reading of the sensor without smoothing out the environmental lighting.
     * @return a value between 0 and 255.
     * @throws IOException if the IR sensor canot be read correctly.
     */
    int getRawReading() throws IOException;

    /**
     * Get a percentage of the reading, compensated by the environmental lighting.
     * @return percentage value between 0d and 1d.
     * @throws IOException if the IR sensor cannot be read correctly.
     */
    double getCompensatedPercentage() throws IOException;

    /**
     * Determine whether the environment is too bright to use the IR sensor.
     * @return true if the environment is too bright.
     * @throws IOException if the IR sensor cannot be read correctly.
     */
    boolean isEnvironmentTooBright() throws IOException;
}
