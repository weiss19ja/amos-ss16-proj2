/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.rover.collision;

/**
 * The collision controller is responsible for the collision detection of the IR
 * sensors of the rover. The rover has 4 ir sensors, one for each corner,
 * labeled front right, front left, back right and back left.
 * 
 * See concrete wiring of GPIO pins in github wiki:
 * https://github.com/weiss19ja/amos-ss16-proj2/wiki/Hardware-GPIO-Mapping
 */
public interface CollisionController {

	String RECEIVER_FRONT_RIGHT = "IRReceiverFrontRight";
	String RECEIVER_FRONT_LEFT = "IRReceiverFrontLeft";
	String RECEIVER_BACK_LEFT = "IRReceiverBackLeft";
	String RECEIVER_BACK_RIGHT = "IRReceiverBackRight";
	String SENDER_FRONT_RIGHT = "IRSenderFrontRight";
	String SENDER_FRONT_LEFT = "IRSenderFrontLeft";
	String SENDER_BACK_LEFT = "IRSenderBackLeft";
	String SENDER_BACK_RIGHT = "IRSenderBackRight";

	/**
	 * Methods returns sensor value of the front right corner.
	 * 
	 * @return true if collision detected
	 */
	boolean hasCollisionFrontRight();

	/**
	 * Methods returns sensor value of the front left corner.
	 * 
	 * @return true if collision detected
	 */
	boolean hasCollisionFrontLeft();

	/**
	 * Methods returns sensor value of the back left corner.
	 * 
	 * @return true if collision detected
	 */
	boolean hasCollisionBackLeft();

	/**
	 * Methods returns sensor value of the back right corner.
	 * 
	 * @return true if collision detected
	 */
	boolean hasCollisionBackRight();

}
