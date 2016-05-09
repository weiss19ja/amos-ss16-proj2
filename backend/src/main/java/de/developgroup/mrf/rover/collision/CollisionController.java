package de.developgroup.mrf.rover.collision;

public interface CollisionController {

	public static final String RECEIVER_FRONT_RIGHT = "IRReceiverFrontRight";
	public static final String RECEIVER_FRONT_LEFT = "IRReceiverFrontLeft";
	public static final String RECEIVER_BACK_LEFT = "IRReceiverBackLeft";
	public static final String RECEIVER_BACK_RIGHT = "IRReceiverBackRight";
	public static final String SENDER_FRONT_RIGHT = "IRSenderFrontRight";
	public static final String SENDER_FRONT_LEFT = "IRSenderFrontLeft";
	public static final String SENDER_BACK_LEFT = "IRSenderBackLeft";
	public static final String SENDER_BACK_RIGHT = "IRSenderBackRight";

	public boolean hasCollisionFrontRight();

	public boolean hasCollisionFrontLeft();

	public boolean hasCollisionBackLeft();

	public boolean hasCollisionBackRight();

}
