package jumpingalien.model;

public interface OnlyVerticalMovable extends Jump, Movable {

	default int getOrientation() {
		if(isGoingUp()) return 1;
		if(isGoingDown()) return -1;
		return 0;
	}

}
