package de.developgroup.mrf.rover.collision;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class CollisionControllerImpl implements CollisionController {

	final GpioController gpio = GpioFactory.getInstance();
	private final GpioPinDigitalOutput irSenderFrontRight;
	private final GpioPinDigitalInput irReceiverFrontRight;

	public CollisionControllerImpl() {
		irSenderFrontRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,
				"IRSenderFrontRight", PinState.HIGH);
		irSenderFrontRight.setShutdownOptions(true, PinState.LOW);

		irReceiverFrontRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		irReceiverFrontRight.addListener(new GpioPinListenerDigital() {
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				System.out.println(" --> GPIO PIN STATE CHANGE: "
						+ event.getPin() + " = " + event.getState());
			}
		});
	}

	// benachrichtige RoverController über state change!
}
