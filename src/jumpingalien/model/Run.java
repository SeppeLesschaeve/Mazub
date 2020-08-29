package jumpingalien.model;

public interface Run {

	boolean canStartRunRight();
	
	boolean canRunRight();
	
	boolean isRunningRight();
	
	boolean canStartRunLeft();
	
	boolean canRunLeft();
	
	boolean isRunningLeft();
	
	default boolean isMoving() {
		return isRunningRight() || isRunningLeft();
	}
	
	default boolean canRunInCurrentState() {
		if(isRunningLeft()) return canRunLeft();
		if(isRunningRight()) return canRunRight();
		return true;
	}
	
	void startRunRight();
	
	void endRunRight();
	
	void startRunLeft();
	
	void endRunLeft();
	
	default void endRun() {
		if(!isMoving()) throw new AssertionError();
		if(isRunningRight()) endRunRight();
		if(isRunningLeft()) endRunLeft();
	}
	
	void run(double deltaT);

}
