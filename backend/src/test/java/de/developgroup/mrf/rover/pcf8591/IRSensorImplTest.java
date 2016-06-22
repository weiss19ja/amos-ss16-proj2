/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.pcf8591;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IRSensorImplTest {

    PCF8591ADConverter adConverter;

    GpioPinDigitalOutput gpioPin;

    IRSensor sensor;

    @Before
    public void setUp() {
        adConverter = Mockito.mock(PCF8591ADConverter.class);
        gpioPin = Mockito.mock(GpioPinDigitalOutput.class);
        sensor = new IRSensorImpl(adConverter, PCF8591ADConverter.InputChannel.ONE, gpioPin);
    }

    @Test
    public void testSwitchIrOn() {
        sensor.switchIrOn();

        verify(gpioPin).setState(PinState.HIGH);
    }

    @Test
    public void testSwitchIrOff() {
        sensor.switchIrOff();

        verify(gpioPin).setState(PinState.LOW);
    }

    @Test
    public void testGetRawReadingValueOk() throws IOException {
        when(adConverter.getChannelValue(PCF8591ADConverter.InputChannel.ONE)).thenReturn(42);

        Assert.assertEquals(42, sensor.getRawReading());
    }

    @Test
    public void testGetCompensatedPercentage() throws IOException {
        when(adConverter.getChannelValue(PCF8591ADConverter.InputChannel.ONE)).thenReturn(20, 80);

        Assert.assertEquals(0.2543, sensor.getCompensatedPercentage(), 0.001);
    }

    @Test
    public void testIsEnvironmentTooBright() throws IOException {
        when(adConverter.getChannelValue(PCF8591ADConverter.InputChannel.ONE)).thenReturn(40);

        Assert.assertEquals(false, sensor.isEnvironmentTooBright());

        when(adConverter.getChannelValue(PCF8591ADConverter.InputChannel.ONE)).thenReturn(240);

        Assert.assertEquals(true, sensor.isEnvironmentTooBright());
    }
}
