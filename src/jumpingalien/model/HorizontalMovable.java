package jumpingalien.model;

public interface HorizontalMovable {

	boolean isMovingLeft();
	boolean isMovingRight();
	void startMoveLeft() throws AssertionError;
	void startMoveRight() throws AssertionError;
	void endMove() throws AssertionError;
	default boolean isMovingHorizontally(){
		return isMovingLeft() || isMovingRight();
	}
	boolean canMoveLeft();
	boolean canMoveRight();

}
