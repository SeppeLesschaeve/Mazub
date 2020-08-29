package jumpingalien.model;

public interface Jump {
	
	boolean canStartJump();
	
	boolean canJump();
	
	boolean isGoingUp();
	
	boolean canStartFall();
	
	boolean canFall();
	
	boolean isGoingDown();
	
	default boolean isJumping() {
		return isGoingUp() || isGoingDown();
	}
	
	default boolean canJumpInCurrentState() {
		if(isGoingUp()) return canJump();
		if(isGoingDown()) return canFall();
		return true;
	}
	
	void startJump();
	
	void endGoingUp();
	
	void startFall();
	
	void endGoingDown();
	
	default void endJump() {
		if(!isJumping()) throw new IllegalStateException();
		if(isGoingUp()) endGoingUp();
		else endGoingDown();
	}
	
	void jump(double deltaT);
	
}
