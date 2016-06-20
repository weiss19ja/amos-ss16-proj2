package de.developgroup.mrf.rover.collision;

import java.util.Observable;

import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * this implementation uses the observer pattern to notify observers about
 * sensor value changes
 * 
 */
@Singleton
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
		/*
		initSenderGPIOs();
		initReceiverGPIOs();

		irReceiverFrontRight.addListener(new SensorListener());
		irReceiverFrontLeft.addListener(new SensorListener());
		irReceiverBackLeft.addListener(new SensorListener());
		irReceiverBackRight.addListener(new SensorListener());
		*/

	}

	private void initSenderGPIOs() {
		irSenderFrontRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,
				SENDER_FRONT_RIGHT, PinState.HIGH);
		irSenderFrontRight.setShutdownOptions(true, PinState.LOW);
		irSenderFrontLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05,
				SENDER_FRONT_LEFT, PinState.HIGH);
		irSenderFrontLeft.setShutdownOptions(true, PinState.LOW);
		irSenderBackLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24,
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
		irReceiverBackLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23,
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
