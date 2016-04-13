package de.developgroup.tec.pi.driver.pwmgen.pca9685;

import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

import de.developgroup.tec.pi.driver.pwmgen.PWMGenerator;
import de.developgroup.tec.pi.driver.pwmgen.PWMOutput;

public class PCA9685PWMGenerator implements PWMGenerator {

	/*
	 * Register addresses
	 */
	static final int REG_MODE1 = 0x00;
	static final int REG_MODE2 = 0x01;
	static final int REG_SUBADR1 = 0x02;
	static final int REG_SUBADR2 = 0x03;
	static final int REG_SUBADR3 = 0x04;

	static final int REG_LED0_ON_L = 0x06;
	static final int REG_LED0_ON_H = 0x07;
	static final int REG_LED0_OFF_L = 0x08;
	static final int REG_LED0_OFF_H = 0x09;

	static final int REG_ALL_LED_ON_L = 0xFA;
	static final int REG_ALL_LED_ON_H = 0xFB;
	static final int REG_ALL_LED_OFF_L = 0xFC;
	static final int REG_ALL_LED_OFF_H = 0xFD;

	static final int REG_PRESCALE = 0xFE;

	/*
	 * Bitmasks
	 */
	static final byte RESTART = (byte) 0x80;
	static final byte SLEEP = 0x10;
	static final byte ALLCALL = 0x01;
	static final byte INVRT = 0x10;
	static final byte OUTDRV = 0x04;

	/**
	 * 12 bit timer.
	 */
	static final int TIMER_CYCLE_COUNT = 4096;

	/**
	 * The base frequency of the timer - 25 MHz.
	 */
	static final int TIMER_BASE_FREQUENCY = 25000000;

	final I2CDevice device;
	private int pwmFrequency;

	public PCA9685PWMGenerator(I2CDevice device) {
		this.device = device;
	}

	public void open() throws IOException {
		setAllPWM(0, 0);
		device.write(REG_MODE2, OUTDRV);
		device.write(REG_MODE1, ALLCALL);

		waitForOscillator();

		byte mode1 = (byte) device.read(REG_MODE1);
		mode1 = (byte) (mode1 & ~SLEEP);
		device.write(REG_MODE1, mode1);

		waitForOscillator();

	}

	private void waitForOscillator() {
		try {
			// The oscillator has a max. startup time of 500 us (see datasheet).
			Thread.sleep(0L, 500 * 1000);
		} catch (InterruptedException e) {
			// Ignore
		}
	}

	/**
	 * 
	 * @param on
	 *            Ticks after which the signal shall go high (0 .. 4095)
	 * @param off
	 *            Ticks after which the signal shall go low (0 .. 4095)
	 * @throws IOException
	 */
	void setAllPWM(int on, int off) throws IOException {
		device.write(REG_ALL_LED_ON_L, (byte) on);
		device.write(REG_ALL_LED_ON_H, (byte) (on >> 8));

		device.write(REG_ALL_LED_OFF_L, (byte) off);
		device.write(REG_ALL_LED_OFF_H, (byte) (off >> 8));
	}

	@Override
	public void setFrequency(int frequency) throws IOException {
		if (frequency <= 0) {
			throw new IllegalArgumentException("Frequency must be greater than zero");
		}

		float prescaleval = TIMER_BASE_FREQUENCY;
		prescaleval /= TIMER_CYCLE_COUNT;
		prescaleval /= frequency;

		int prescale = (Math.round(prescaleval)) - 1;
		byte prescaleReg;

		if (prescale > 0xff || prescale < 0) {
			throw new IllegalArgumentException(
					"This device is unable to provide the desired frequency of " + frequency + " Hz");
		}

		prescaleReg = (byte) prescale;

		// Go to sleep mode
		byte oldMode1 = (byte) device.read(REG_MODE1);
		byte newMode1 = (byte) (oldMode1 | SLEEP);
		device.write(REG_MODE1, newMode1);

		device.write(REG_PRESCALE, prescaleReg);
		device.write(REG_MODE1, oldMode1);
		waitForOscillator();
		device.write(REG_MODE1, (byte) (oldMode1 | RESTART));

		// Calculate actual frequency
		pwmFrequency = Math.round(TIMER_BASE_FREQUENCY / (float) (TIMER_CYCLE_COUNT * (prescaleReg + 1)));
	}

	public int getFrequency() {
		return pwmFrequency;
	}

	@Override
	public PWMOutput getOutput(int channel) {
		return new PCA9685PWMOutput(channel);
	}

	private class PCA9685PWMOutput implements PWMOutput {

		private final int channel;

		PCA9685PWMOutput(int channel) {
			this.channel = channel;
		}

		@Override
		public int getCycleCount() {
			return TIMER_CYCLE_COUNT;
		}

		@Override
		public void setPWM(int highAfter, int lowAfter) throws IOException {
			int offset = 4 * channel;

			device.write(REG_LED0_ON_L + offset, (byte) highAfter);
			device.write(REG_LED0_ON_H + offset, (byte) (highAfter >> 8));

			device.write(REG_LED0_OFF_L + offset, (byte) lowAfter);
			device.write(REG_LED0_OFF_H + offset, (byte) (lowAfter >> 8));
		}

		@Override
		public void setPWM(int onCycles) throws IOException {
			setPWM(0, onCycles);
		}

		@Override
		public int getFrequency() {
			return pwmFrequency;
		}

	}

}
