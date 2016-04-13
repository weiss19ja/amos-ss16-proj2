package de.developgroup.tec.pi.driver.servo;

public interface ServoConfiguration {

	boolean reversed();
	
	int offset();
	
	int positiveRate();
	
	int negativeRate();
	
}
