package de.developgroup.mrf.rover.collision;

import java.util.Observable;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/*
 * 
 +-----+-----+---------+------+---+---Pi 2---+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 |     |     |    3.3v |      |   |  1 || 2  |   |      | 5v      |     |     |
 |   2 |   8 |   SDA.1 | ALT0 | 1 |  3 || 4  |   |      | 5V      |     |     |
 |   3 |   9 |   SCL.1 | ALT0 | 1 |  5 || 6  |   |      | 0v      |     |     |
 |   4 |   7 | GPIO. 7 |   IN | 0 |  7 || 8  | 1 | ALT0 | TxD     | 15  | 14  |
 |     |     |      0v |      |   |  9 || 10 | 1 | ALT0 | RxD     | 16  | 15  |
 |  17 |   0 | GPIO. 0 |   IN | 0 | 11 || 12 | 0 | IN   | GPIO. 1 | 1   | 18  |
 |  27 |   2 | GPIO. 2 |   IN | 0 | 13 || 14 |   |      | 0v      |     |     |
 |  22 |   3 | GPIO. 3 |   IN | 0 | 15 || 16 | 0 | IN   | GPIO. 4 | 4   | 23  |
 |     |     |    3.3v |      |   | 17 || 18 | 0 | IN   | GPIO. 5 | 5   | 24  |
 |  10 |  12 |    MOSI | ALT0 | 0 | 19 || 20 |   |      | 0v      |     |     |
 |   9 |  13 |    MISO | ALT0 | 0 | 21 || 22 | 0 | IN   | GPIO. 6 | 6   | 25  |
 |  11 |  14 |    SCLK | ALT0 | 0 | 23 || 24 | 1 | OUT  | CE0     | 10  | 8   |
 |     |     |      0v |      |   | 25 || 26 | 1 | OUT  | CE1     | 11  | 7   |
 |   0 |  30 |   SDA.0 |   IN | 1 | 27 || 28 | 1 | IN   | SCL.0   | 31  | 1   |
 |   5 |  21 | GPIO.21 |   IN | 1 | 29 || 30 |   |      | 0v      |     |     |
 |   6 |  22 | GPIO.22 |   IN | 1 | 31 || 32 | 0 | IN   | GPIO.26 | 26  | 12  |
 |  13 |  23 | GPIO.23 |   IN | 0 | 33 || 34 |   |      | 0v      |     |     |
 |  19 |  24 | GPIO.24 |   IN | 0 | 35 || 36 | 0 | IN   | GPIO.27 | 27  | 16  |
 |  26 |  25 | GPIO.25 |   IN | 0 | 37 || 38 | 0 | IN   | GPIO.28 | 28  | 20  |
 |     |     |      0v |      |   | 39 || 40 | 0 | IN   | GPIO.29 | 29  | 21  |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+---Pi 2---+---+------+---------+-----+-----+

 +-----+-----+--------------------------+------------------------
 | BCM | wPi |   Name  					|	Signal
 +-----+-----+--------------------------+------------------------
 |  27 |   2 | IR  Receiver Front Right | 	Signal Out 1
 |  22 |   3 | IR  Sender Front Right  	|	Signal In 1
 +-----+-----+--------------------------+------------------------
 |  23 |   4 | IR  Receiver Front Left 	|	Signal Out 2
 |  24 |   5 | IR  Sender Front Left 	|	Signal In 2
 +-----+-----+--------------------------+------------------------
 |  05 |  21 | IR  Receiver Back Left 	|	Signal Out 3
 |  06 |  22 | IR  Sender Back Left 	|	Signal In 3
 +-----+-----+--------------------------+------------------------
 |  20 |  28 | IR  Receiver Back Right 	|	Signal Out 4
 |  21 |  29 | IR  Sender Back Right 	|	Signal In 4
 +-----+-----+--------------------------+------------------------
 */

public class CollisionControllerImpl extends Observable implements
		CollisionController {

	final GpioController gpio = GpioFactory.getInstance();
	private GpioPinDigitalOutput irSenderFrontRight;
	private GpioPinDigitalOutput irSenderFrontLeft;
	private GpioPinDigitalOutput irSenderBackLeft;
	private GpioPinDigitalOutput irSenderBackRight;

	private GpioPinDigitalInput irReceiverFrontRight;
	private GpioPinDigitalInput irReceiverFrontLeft;
	private GpioPinDigitalInput irReceiverBackLeft;
	private GpioPinDigitalInput irReceiverBackRight;

	public CollisionControllerImpl() {
		initSenderGPIOs();
		initReceiverGPIOs();

		irReceiverFrontRight.addListener(new SensorListener());
		irReceiverFrontLeft.addListener(new SensorListener());
		irReceiverBackLeft.addListener(new SensorListener());
		irReceiverBackRight.addListener(new SensorListener());

	}

	private void initSenderGPIOs() {
		irSenderFrontRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,
				SENDER_FRONT_RIGHT, PinState.HIGH);
		irSenderFrontRight.setShutdownOptions(true, PinState.LOW);
		irSenderFrontLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05,
				SENDER_FRONT_LEFT, PinState.HIGH);
		irSenderFrontLeft.setShutdownOptions(true, PinState.LOW);
		irSenderBackLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22,
				SENDER_BACK_LEFT, PinState.HIGH);
		irSenderBackLeft.setShutdownOptions(true, PinState.LOW);
		irSenderBackRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29,
				SENDER_BACK_RIGHT, PinState.HIGH);
		irSenderBackRight.setShutdownOptions(true, PinState.LOW);
	}

	private void initReceiverGPIOs() {
		irReceiverFrontRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				RECEIVER_FRONT_RIGHT, PinPullResistance.PULL_DOWN);
		irReceiverFrontLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,
				RECEIVER_FRONT_LEFT, PinPullResistance.PULL_DOWN);
		irReceiverBackLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21,
				RECEIVER_BACK_LEFT, PinPullResistance.PULL_DOWN);
		irReceiverBackRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28,
				RECEIVER_BACK_RIGHT, PinPullResistance.PULL_DOWN);
	}

	private class SensorListener implements GpioPinListenerDigital {
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {
			setChanged();
			notifyObservers(event);
		}
	}

	@Override
	public boolean hasCollisionFrontRight() {
		return irReceiverFrontRight.getState().isHigh();
	}

	@Override
	public boolean hasCollisionFrontLeft() {
		return irReceiverFrontLeft.getState().isHigh();
	}

	@Override
	public boolean hasCollisionBackLeft() {
		return irReceiverBackLeft.getState().isHigh();
	}

	@Override
	public boolean hasCollisionBackRight() {
		return irReceiverBackRight.getState().isHigh();
	}
}
