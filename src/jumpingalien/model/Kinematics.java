package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public abstract class Kinematics {
	
	@Basic
	public abstract double getHorizontalAcceleration();

	@Basic
	public abstract double getVerticalAcceleration();

	public abstract void setHorizontalAcceleration(double x);

	public abstract void setVerticalAcceleration(double y);
	
	@Basic
	public abstract double getHorizontalVelocity();

	@Basic
	public abstract double getVerticalVelocity();

	public abstract void setHorizontalVelocity(double x);

	public abstract void setVerticalVelocity(double y);
	
	@Basic
	public abstract double getMinimumHorizontalVelocity();

	public abstract void setMinimumHorizontalVelocity(double min);
	
	@Basic
	public abstract double getMaximumHorizontalVelocity();

	public abstract void setMaximumHorizontalVelocity(double max);
	
	public abstract void setHorizontalBoundaries(double min, double max);
	
	public abstract void enforceHorizontalBoundaries();
	
	public abstract void updateHorizontalVelocity(double time);
	
	public abstract void updateVerticalVelocity(double time);
	
}
