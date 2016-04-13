package de.developgroup.tec.pi.driver.servo.internal;

import java.io.IOException;

import de.developgroup.tec.pi.driver.pwmgen.PWMOutput;
import de.developgroup.tec.pi.driver.servo.ServoConfiguration;
import de.developgroup.tec.pi.driver.servo.ServoController;

public class ServoControllerImpl implements ServoController {

	/**
	 * The PWM pulse duration in micro seconds for minimum standard servo throw.
	 */
	private static final int POS_MIN_US = 1000;
	
	/**
	 * The lower PWM pulse duration limit in micro seconds.
	 */
	private static final int PULSE_LOWER_LIMIT_US = 800;
	
	/**
	 * The PWM pulse duration in micro seconds for maximum standard servo throw.
	 */
	private static final int POS_MAX_US = 2000;
	
	/**
	 * The upper PWM pulse duration limit in micro seconds.
	 */
	private static final int PULSE_UPPER_LIMIT_US = 2200;

	final PWMOutput output;
	final ServoConfiguration configuration;

	final int posNeutralTimerTicks;
	final int tickScaleFactor;
	final int lowerLimitTimerTicks;
	final int upperLimitTimerTicks;

	public ServoControllerImpl(PWMOutput output, ServoConfiguration configuration) {
		this.output = output;
		this.configuration = configuration;

		final int pwmFreq = output.getFrequency();
		final int cycleCount = output.getCycleCount();

		final int tickDurationUs = (1000 * 1000) / (cycleCount * pwmFreq);

		final int posMinTimerTicks = POS_MIN_US / tickDurationUs;
		final int posMaxTimerTicks = POS_MAX_US / tickDurationUs;
		
		posNeutralTimerTicks = (posMinTimerTicks + posMaxTimerTicks) / 2;
		tickScaleFactor = (posMaxTimerTicks - posMinTimerTicks) / 2;
		lowerLimitTimerTicks = PULSE_LOWER_LIMIT_US / tickDurationUs;
		upperLimitTimerTicks = PULSE_UPPER_LIMIT_US / tickDurationUs;
	}

	@Override
	public void setPosition(final int position) throws IOException {

		if (position < POS_MIN || position > POS_MAX) {
			throw new IllegalArgumentException("Invalid position " + position);
		}

		int rate = position < 0 ? configuration.negativeRate() : configuration.positiveRate();
		int adaptedPosition = (position * rate) / 100;

		adaptedPosition += configuration.offset();

		if (configuration.reversed()) {
			adaptedPosition *= -1;
		}

		int timerTicks = posNeutralTimerTicks + (adaptedPosition * tickScaleFactor) / 1000;

		
		if (timerTicks < lowerLimitTimerTicks) {
			timerTicks = lowerLimitTimerTicks;
		} else if (timerTicks > upperLimitTimerTicks) {
			timerTicks = upperLimitTimerTicks;
		}

		output.setPWM(timerTicks);
	}

	@Override
	public void close() throws IOException {
		// Set the signal to low. This disables most servos.
		output.setPWM(0);
	}

}
