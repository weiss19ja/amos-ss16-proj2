package de.developgroup.mrf.rover.servo;

public interface ServoConfiguration {

	boolean reversed();
	
	int offset();
	
	int positiveRate();
	
	int negativeRate();
	
}
