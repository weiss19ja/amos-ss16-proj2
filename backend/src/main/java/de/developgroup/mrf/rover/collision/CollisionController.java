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

	public static final String RECEIVER_FRONT_RIGHT = "IRReceiverFrontRight";
	public static final String RECEIVER_FRONT_LEFT = "IRReceiverFrontLeft";
	public static final String RECEIVER_BACK_LEFT = "IRReceiverBackLeft";
	public static final String RECEIVER_BACK_RIGHT = "IRReceiverBackRight";
	public static final String SENDER_FRONT_RIGHT = "IRSenderFrontRight";
	public static final String SENDER_FRONT_LEFT = "IRSenderFrontLeft";
	public static final String SENDER_BACK_LEFT = "IRSenderBackLeft";
	public static final String SENDER_BACK_RIGHT = "IRSenderBackRight";

	/**
	 * Methods returns sensor value of the front right corner.
	 * 
	 * @return true if collision detected
	 */
	public boolean hasCollisionFrontRight();

	/**
	 * Methods returns sensor value of the front left corner.
	 * 
	 * @return true if collision detected
	 */
	public boolean hasCollisionFrontLeft();

	/**
	 * Methods returns sensor value of the back left corner.
	 * 
	 * @return true if collision detected
	 */
	public boolean hasCollisionBackLeft();

	/**
	 * Methods returns sensor value of the back right corner.
	 * 
	 * @return true if collision detected
	 */
	public boolean hasCollisionBackRight();

}
