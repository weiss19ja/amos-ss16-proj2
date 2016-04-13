package de.developgroup.tec.pi.demo;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import de.developgroup.tec.pi.driver.pwmgen.PWMOutput;
import de.developgroup.tec.pi.driver.pwmgen.pca9685.PCA9685PWMGenerator;

public class PWMDemo {

	public static void main(String[] args) throws IOException, InterruptedException {

		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

		try {
			I2CDevice device = bus.getDevice(0x40);
			PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);

			driver.open();
			driver.setFrequency(1000);

			for (int channel = 0; channel < 16; ++channel) {

				System.out.println("Using channel " + channel);

				PWMOutput output = driver.getOutput(channel);

				for (int ticks = 0; ticks < output.getCycleCount(); ++ticks) {
					output.setPWM(ticks);

					System.out.println("PWM is " + ticks + "/" + output.getCycleCount());
					Thread.sleep(5L);
				}
			}

		} finally {
			bus.close();
		}

	}

}
