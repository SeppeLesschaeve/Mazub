package jumpingalien.model;

public interface OnlyHorizontalMovable extends Run, Movable {

	default int getOrientation() {
		if(isRunningRight()) return 1;
		if(isRunningLeft()) return -1;
		return 0;
	}

}
