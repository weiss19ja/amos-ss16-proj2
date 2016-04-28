package de.developgroup.mrf.rover.pwmgenerator;

import java.io.IOException;

public interface PWMGenerator {

	/**
	 * Set the PWM frequency. This frequency applies to all outputs.
	 * 
	 * @param frequency in Hz.
	 */
	public void setFrequency(int frequency) throws IOException;

	/**
	 * Get the output of the given channel.
	 * 
	 * @param channel
	 *            The number of the channel.
	 * @return
	 */
	public PWMOutput getOutput(int channel);

}
