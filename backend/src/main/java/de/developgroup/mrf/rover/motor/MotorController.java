/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.motor;

import java.io.IOException;

public interface MotorController {

	public static int SPEED_STOP = 0;
	public static int SPEED_MAX_FORWARD = 1000;
	public static int SPEED_MAX_BACKWARD = -1 * SPEED_MAX_FORWARD;

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

	void close() throws IOException;

}
