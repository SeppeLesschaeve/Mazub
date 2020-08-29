package jumpingalien.model;

public interface TwoDimensionMovable extends Run, Jump, Movable {
	
	default int getOrientation() {
		if(isGoingUp() || isRunningRight()) return 1;
		if(isGoingDown() || isRunningLeft()) return -1;
		return 0;
	}

}
