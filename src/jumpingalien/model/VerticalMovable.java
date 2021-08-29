package jumpingalien.model;

public interface VerticalMovable {

	boolean isJumping();
	boolean isFalling();
	void startJump() throws IllegalStateException;
	void startFall() throws IllegalStateException;
	void endJump() throws IllegalStateException;
	default boolean isMovingVertically(){
		return isJumping() || isFalling();
	}
	boolean canJump();
	boolean canFall();
	
}
