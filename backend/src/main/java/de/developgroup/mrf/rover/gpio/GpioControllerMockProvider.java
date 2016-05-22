package de.developgroup.mrf.rover.gpio;

import com.google.inject.Provider;
import com.pi4j.io.gpio.GpioController;

public class GpioControllerMockProvider implements Provider<GpioController> {
    @Override
    public GpioController get() {
        return new GpioControllerMock();
    }
}
