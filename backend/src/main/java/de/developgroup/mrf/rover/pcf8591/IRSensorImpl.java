/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.pcf8591;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.io.IOException;

public class IRSensorImpl implements IRSensor {

    /**
     * Value between 0 and 255 that signifies when the environment (= IR LED off) is too bright.
     */
    public static int ENV_TOO_BRIGHT_THRESHOLD = 220;

    private PCF8591ADConverter converter;

    private PCF8591ADConverter.InputChannel channel;

    private GpioPinDigitalOutput irLed;

    @Inject
    public IRSensorImpl(PCF8591ADConverter converter,
                        @Assisted PCF8591ADConverter.InputChannel channel,
                        @Assisted GpioPinDigitalOutput irLed) {
        this.converter = converter;
        this.channel = channel;
        this.irLed = irLed;
    }

    @Override
    public void switchIrOn() {
        irLed.setState(PinState.HIGH);
    }

    @Override
    public void switchIrOff() {
        irLed.setState(PinState.LOW);
    }

    @Override
    public int getRawReading() throws IOException {
        return converter.getChannelValue(channel);
    }

    @Override
    public double getCompensatedPercentage() throws IOException {
        switchIrOff();
        int offValue = getRawReading();
        switchIrOn();
        int onValue = getRawReading();
        switchIrOff();

        int overBaseline = onValue - offValue;
        return ((double)overBaseline)/((double)255 - offValue + 1);
    }

    @Override
    public boolean isEnvironmentTooBright() throws IOException {
        PinState prevState = irLed.getState();

        switchIrOff();
        int value = getRawReading();
        irLed.setState(prevState);

        return value > ENV_TOO_BRIGHT_THRESHOLD;
    }
}
