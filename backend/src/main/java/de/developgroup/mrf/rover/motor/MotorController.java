/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.motor;

import java.io.IOException;

public interface MotorController {

	int SPEED_STOP = 0;
	int SPEED_MAX_FORWARD = 1000;
	int SPEED_MAX_BACKWARD = -1 * SPEED_MAX_FORWARD;

	/**
	 * Set the motor speed and direction.
	 * 
	 * @param speed
	 *            A value between {@link #SPEED_MAX_FORWARD} and
	 *            {@link #SPEED_MAX_BACKWARD}. {@link #SPEED_STOP} stops the
	 *            motor.
	 * @throws IOException
	 */
	void setSpeed(int speed) throws IOException;

	/**
	 * Set the motor speed as percentage of the maximum speed allowed by the motors.
	 *
	 * @param speed the speed to set in the interval [-1, 1].
	 * @throws IOException if the PWM could not be accessed
	 */
	void setSpeedPercentage(double speed) throws IOException;

	void close() throws IOException;

}
