/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.servo;

public interface ServoConfiguration {

	boolean reversed();
	
	int offset();
	
	int positiveRate();
	
	int negativeRate();
	
}
