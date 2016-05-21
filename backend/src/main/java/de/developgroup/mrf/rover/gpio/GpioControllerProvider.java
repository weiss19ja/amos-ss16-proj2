package de.developgroup.mrf.rover.gpio;

import com.google.inject.Provider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class GpioControllerProvider implements Provider<GpioController> {
    public GpioController get() {
        return GpioFactory.getInstance();
    }
}
