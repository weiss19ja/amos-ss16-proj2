/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.pwmgenerator;

import java.io.IOException;

/**
 * An interface for a PWM output.
 * 
 * @author Ralf Ellner
 *
 */
public interface PWMOutput {

	/**
	 * Get the number of timer cycles that the output supports.
	 * 
	 * @return
	 */
	int getCycleCount();

	/**
	 * Get the PWM frequency.
	 * 
	 * @return
	 */
	int getFrequency();

	/**
	 * Set the PWM. The output will be set to high after highAfter timer cycles. It will be set to low after lowAfter
	 * timer cycles.
	 * 
	 * @param highAfter
	 *            The number of timer cycles in a PWM cycle after which the output shall be set to high. The value must
	 *            be between 0 and {@link #getCycleCount()} - 1.
	 * @param lowAfter
	 *            The number of timer cycles in a PWM cycle after which the output shall be set to high. The value must
	 *            be between 0 and {@link #getCycleCount()} - 1
	 */
	void setPWM(int highAfter, int lowAfter) throws IOException;

	/**
	 * Set the PWM. The rising edge of the PWM signal is aligned with the PWM cycle, i.e. it starts at timer cycle 0.
	 * 
	 * @param highCycles
	 *            The number of timer cycles for which the output shall be high. The value must be between 0 and
	 *            {@link #getCycleCount()} - 1.
	 */
	void setPWM(int highCycles) throws IOException;

}
