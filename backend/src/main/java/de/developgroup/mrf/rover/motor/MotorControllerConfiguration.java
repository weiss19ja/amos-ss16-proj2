package de.developgroup.mrf.rover.motor;

public interface MotorControllerConfiguration {

	boolean reversed();

	String gpioPin();

	String name();

}
