package de.developgroup.mrf.rover.pcf8591;

import junit.framework.Assert;
import org.junit.Test;

public class InputChannelTest {
    @Test
    public void testCorrectValuesReturned() {
        Assert.assertEquals(0, PCF8591ADConverter.InputChannel.ZERO.getValue());
        Assert.assertEquals(1, PCF8591ADConverter.InputChannel.ONE.getValue());
        Assert.assertEquals(2, PCF8591ADConverter.InputChannel.TWO.getValue());
        Assert.assertEquals(3, PCF8591ADConverter.InputChannel.THREE.getValue());
    }
}
