package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import jumpingalien.util.Sprite;

/**
 * This class builds a Creature with Hit Points as health
 *  
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Creature extends GameObject {
	
	private HitPoint hitPoint;
	private Acceleration acceleration = new Acceleration() {
		private double x = 0.0;
		private double y = 0.0;

		@Override
		public double getX() {
			return Double.valueOf(x);
		}

		@Override
		protected void setX(double x) {
			this.x = x;
		}

		@Override
		public double getY() {
			return Double.valueOf(y);
		}

		@Override
		protected void setY(double y) {
			this.y = y;
		}
	};
	
	@Model
	protected Creature(int pixelLeftX, int pixelBottomY, int initialHit, int minHit, int maxHit, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, sprites);
		this.hitPoint = new HitPoint(initialHit, minHit, maxHit);
	}
	
	public HitPoint getHitPoint() {
		return hitPoint.clone();
	}
	
	@Basic
	public int getHitPoints() {
		return getHitPoint().getPoints();
	}
	
	protected void setHitPoints(int points) {
		this.hitPoint.setPoints(points);
	}
	
	public Acceleration getAcceleration() {
		return acceleration;
	}
	
	@Basic @Override
	public int getOrientation() {
		if(getVelocity().getX() < 0 || acceleration.getX() < 0) {
			return Orientation.NEGATIVE.getValue();
		}else if (getVelocity().getX() > 0 || acceleration.getX() > 0) {
			return Orientation.POSITIVE.getValue();
		}
		return Orientation.NEUTRAL.getValue();
	}

	@Override
	protected double updateDt(double deltaT, double time) {
		double result = 0.01 / ( Math.sqrt( Math.pow(getVelocity().getX(), 2) + Math.pow(getVelocity().getY(), 2) ) + 
				(Math.sqrt( Math.pow(acceleration.getX(), 2) + Math.pow(acceleration.getY(), 2) ) * deltaT) );
		if(Double.isInfinite(result) || result > 0.01) {
			result = 0.01;
		}
		if(time + result > deltaT) {
			result = deltaT-time;
		}
		return result;
	}
	
	protected void updateX(double dt) {
		double newX = this.getPosition().getX() + (getVelocity().getX()*dt)+ (acceleration.getX()*dt*dt/2);
		super.getPosition().setX(newX);
		this.getOrigin().setX((int)(newX/0.01));
	}
	
	protected void updateY(double dt) {
		double newY = this.getPosition().getY() + (getVelocity().getY()*dt) + (acceleration.getY()*dt*dt/2);
		super.getPosition().setY(newY);
		this.getOrigin().setY((int)(newY/0.01));
	}
	
	@Override
	public boolean isDead() {
		return getHitPoints() == 0;
	}
	
	protected boolean canStopRun() {
		Rectangle endRun = new Rectangle(getOrigin(), new Dimension(getSprites()[0].getWidth(), getSprites()[0].getHeight()));
		if(getWorld() != null) {
			return super.getWorld().shallBePassable(endRun) && overlappingGameObject(endRun).isEmpty();
		}
		return true;
	}
	
	protected boolean canFall() {
		if(getWorld() == null) return false;
		for(int x = super.getDown().getOrigin().getX(); x < super.getDown().getOrigin().getX() + super.getDown().getDimension().getWidth(); x++)
			if(!getWorld().getTileFeature(x, super.getDown().getOrigin().getY()).isPassable()) return false;
		return(overlappingGameObject(getDown()).isEmpty());
	}

	protected boolean canRun() {
		if(getWorld()  == null) return true;
		if(getOrientation() == Orientation.NEGATIVE.getValue())
			return overlappingGameObject(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
		else if(getOrientation() == Orientation.POSITIVE.getValue())
			return overlappingGameObject(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
		return false;
	}
	
	protected abstract boolean canJump();

}
