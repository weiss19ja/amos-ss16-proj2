package de.developgroup.mrf.rover.servo;

import java.io.IOException;

public interface ServoController {

	public static int POS_NEUTRAL = 0;
	public static int POS_MAX = 1000;
	public static int POS_MIN = -1000;

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
