/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.servo;

import java.io.IOException;

public interface ServoController {

	int POS_NEUTRAL = 0;
	int POS_MAX = 1000;
	int POS_MIN = -1000;

	/**
	 * Set the servo position.
	 * 
	 * @param position
	 *            A value between {@link #POS_MAX} and {@link #POS_MIN}. {@link #POS_NEUTRAL} is the neutral position of
	 *            the servo.
	 * @throws IOException
	 */
	void setPosition(int position) throws IOException;

	void close() throws IOException;

}
