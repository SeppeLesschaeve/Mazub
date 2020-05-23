package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public abstract class Acceleration {

	@Basic
	public abstract double getX();

	@Basic
	public abstract double getY();

	protected abstract void setX(double x);

	protected abstract void setY(double y);
		
}
