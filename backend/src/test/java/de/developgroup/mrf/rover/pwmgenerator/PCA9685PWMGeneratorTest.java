package de.developgroup.mrf.rover.pwmgenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import static de.developgroup.mrf.rover.pwmgenerator.PCA9685PWMGenerator.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PCA9685PWMGeneratorTest {

	PCA9685Mock device;
	PCA9685PWMGenerator pwmGenerator;

	@Before
	public void setUp() throws Exception {
		device = new PCA9685Mock();
		pwmGenerator = new PCA9685PWMGenerator(device);

		pwmGenerator.open();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPrescale() throws IOException {
		assertThat(device.memory[REG_PRESCALE], is((byte) 0x1E));

		pwmGenerator.setFrequency(50);
		assertThat(device.memory[REG_PRESCALE], is((byte) 121));
		assertThat(pwmGenerator.getFrequency(), is(50));

		pwmGenerator.setFrequency(200);
		assertThat(device.memory[REG_PRESCALE], is((byte) 0x1E));
		assertThat(pwmGenerator.getFrequency(), is(197));

		pwmGenerator.setFrequency(433);
		assertThat(device.memory[REG_PRESCALE], is((byte) 0x0D));
		assertThat(pwmGenerator.getFrequency(), is(436));

		try {
			pwmGenerator.setFrequency(20000);
			fail("No exception");
		} catch (IllegalArgumentException e) {
		}

		try {
			pwmGenerator.setFrequency(0);
			fail("No exception");
		} catch (IllegalArgumentException e) {
		}

		try {
			pwmGenerator.setFrequency(-1);
			fail("No exception");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPWM() throws IOException {
		for (int chan = 0; chan < 16; ++chan) {
			testSetPWM(chan);
		}
	}

	private void testSetPWM(int chan) throws IOException {

		int offset = chan * 4;

		assertThat(device.memory[REG_LED0_ON_L + offset], is((byte) 0x00));
		assertThat(device.memory[REG_LED0_ON_H + offset], is((byte) 0x00));
		assertThat(device.getOnTimerValue(chan), is(0));

		assertThat(device.memory[REG_LED0_OFF_L + offset], is((byte) 0x00));
		assertThat(device.memory[REG_LED0_OFF_H + offset], is((byte) 0x10));
		assertThat(device.getOffTimerValue(chan), is(4096));

		PWMOutput out = pwmGenerator.getOutput(chan);

		out.setPWM(200);

		assertThat(device.memory[REG_LED0_ON_L + offset], is((byte) 0x00));
		assertThat(device.memory[REG_LED0_ON_H + offset], is((byte) 0x00));
		assertThat(device.getOnTimerValue(chan), is(0));

		assertThat(device.memory[REG_LED0_OFF_L + offset], is((byte) 200));
		assertThat(device.memory[REG_LED0_OFF_H + offset], is((byte) 0x00));
		assertThat(device.getOffTimerValue(chan), is(200));

		out.setPWM(4000);

		assertThat(device.memory[REG_LED0_ON_L + offset], is((byte) 0x00));
		assertThat(device.memory[REG_LED0_ON_H + offset], is((byte) 0x00));
		assertThat(device.getOnTimerValue(chan), is(0));

		assertThat(device.memory[REG_LED0_OFF_L + offset], is((byte) 0xA0));
		assertThat(device.memory[REG_LED0_OFF_H + offset], is((byte) 0x0F));
		assertThat(device.getOffTimerValue(chan), is(4000));
	}

}
