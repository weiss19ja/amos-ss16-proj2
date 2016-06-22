/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.pcf8591;

import com.pi4j.io.i2c.I2CDevice;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PCF8591ADConverterImplTest {

    I2CDevice fakeDevice;
    PCF8591ADConverter converter;

    @Before
    public void setUp() {
        fakeDevice = Mockito.mock(I2CDevice.class);
        converter = new PCF8591ADConverterImpl(fakeDevice);
    }

    @Test
    public void testCorrectCommandValues() throws IOException {
        // channel zero
        converter.getChannelValue(PCF8591ADConverter.InputChannel.ZERO);
        verify(fakeDevice).write((byte)0x40);

        // channel one
        converter.getChannelValue(PCF8591ADConverter.InputChannel.ONE);
        verify(fakeDevice).write((byte)0x41);

        // channel two
        converter.getChannelValue(PCF8591ADConverter.InputChannel.TWO);
        verify(fakeDevice).write((byte)0x42);

        // channel three
        converter.getChannelValue(PCF8591ADConverter.InputChannel.THREE);
        verify(fakeDevice).write((byte)0x43);
    }

    @Test
    public void testFakeReadWasIssued() throws IOException {
        converter.getChannelValue(PCF8591ADConverter.InputChannel.ZERO);
        verify(fakeDevice, times(2)).read();
    }

    @Test
    public void testCorrectValueReturned() throws IOException {
        when(fakeDevice.read()).thenReturn(42);

        Assert.assertEquals(42, converter.getChannelValue(PCF8591ADConverter.InputChannel.ZERO));
    }
}
