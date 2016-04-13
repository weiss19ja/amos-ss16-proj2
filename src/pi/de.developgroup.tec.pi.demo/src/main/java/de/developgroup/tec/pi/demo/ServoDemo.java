package de.developgroup.tec.pi.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import de.developgroup.tec.pi.driver.pwmgen.PWMOutput;
import de.developgroup.tec.pi.driver.pwmgen.pca9685.PCA9685PWMGenerator;
import de.developgroup.tec.pi.driver.servo.ServoConfiguration;
import de.developgroup.tec.pi.driver.servo.ServoController;
import de.developgroup.tec.pi.driver.servo.internal.ServoControllerImpl;

public class ServoDemo {

	public static void main(String[] args) throws IOException, InterruptedException {

		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

		try {
			I2CDevice device = bus.getDevice(0x40);
			PCA9685PWMGenerator driver = new PCA9685PWMGenerator(device);

			driver.open();
			driver.setFrequency(50);

			List<ServoController> controllers = new ArrayList<ServoController>();

			for (int channel = 0; channel < 16; ++channel) {

				PWMOutput output = driver.getOutput(channel);
				ServoController controller = new ServoControllerImpl(output, new ServoConfiguration() {
					
					@Override
					public boolean reversed() {
						return false;
					}
					
					@Override
					public int positiveRate() {
						return 100;
					}
					
					@Override
					public int offset() {
						return 0;
					}
					
					@Override
					public int negativeRate() {
						return 100;
					}
				});
				controllers.add(controller);
			}

			for (int r = 0; r < 100; ++r) {

				for (int i = 0; i < controllers.size(); ++i) {

					int position = (int) (Math.random() * 1000.0 - 1.0);
					controllers.get(i).setPosition(position);
					System.out.println("Servo " + i + " -> " + position);
					Thread.sleep(500L);
				}
			}

		} finally {
			bus.close();
		}

	}

}
