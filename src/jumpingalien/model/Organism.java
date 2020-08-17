package jumpingalien.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * This class holds a couple of rectangles with the origin at the bottom-left that tells 
 * where the Game Object is located (the whole rectangle and its left-, right-, up- and down-border), 
 * images to show the animation of the Game Object and one of them is used to display the current pose;
 * a world where the Game Object can be attached to that will handle the collision with other Game Objects
 * 
 * @invar ...
 * 		| hasProperWorld()
 * 
 * @version 3.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Organism extends GameObject{
	
	protected HitPoint hitPoint;
	private double blockTime = 0.0;
	private double delay = 0.0;
	protected static final double REMOVE_DELAY = 0.6;
	protected Kinematics kinematics = new Kinematics() {
		
		private double horizontalAcceleration;
		private double verticalAcceleration;
		private double horizontalVelocity;
		private double verticalVelocity;
		private double minimumHorizontalVelocity;
		private double maximumHorizontalVelocity;

		@Override
		public double getHorizontalAcceleration() {
			return Double.valueOf(horizontalAcceleration);
		}

		@Override
		public double getVerticalAcceleration() {
			return Double.valueOf(verticalAcceleration);
		}

		@Override
		public void setHorizontalAcceleration(double x) {
			this.horizontalAcceleration = x;
		}

		@Override
		public void setVerticalAcceleration(double y) {
			this.verticalAcceleration = y;
		}

		@Override
		public double getHorizontalVelocity() {
			return Double.valueOf(horizontalVelocity);
		}

		@Override
		public double getVerticalVelocity() {
			return Double.valueOf(verticalVelocity);
		}

		@Override
		public void setHorizontalVelocity(double x) {
			this.horizontalVelocity = x;
		}

		@Override
		public void setVerticalVelocity(double y) {
			this.verticalVelocity = y;
		}

		@Override
		public double getMinimumHorizontalVelocity() {
			return Double.valueOf(minimumHorizontalVelocity);
		}

		@Override
		public void setMinimumHorizontalVelocity(double min) {
			this.minimumHorizontalVelocity = min;
		}

		@Override
		public double getMaximumHorizontalVelocity() {
			return Double.valueOf(maximumHorizontalVelocity);
		}

		@Override
		public void setMaximumHorizontalVelocity(double max) {
			if(max < getMinimumHorizontalVelocity()) this.maximumHorizontalVelocity = getMinimumHorizontalVelocity();
			else this.maximumHorizontalVelocity = max;
		}

		@Override
		public void setBoundsOfHorizontalBoundaries(double min, double max) {
			this.setMinimumHorizontalVelocity(min);
			this.setMaximumHorizontalVelocity(max);
		}
		
		@Override
		public void enforceHorizontalBoundaries() {
			if(Math.abs(horizontalVelocity) < getMinimumHorizontalVelocity()) {
				setHorizontalVelocity(getMinimumHorizontalVelocity()*Math.signum(horizontalVelocity));
			}
			if(Math.abs(horizontalVelocity) > getMaximumHorizontalVelocity()) {
				setHorizontalVelocity(getMaximumHorizontalVelocity()*Math.signum(horizontalVelocity));
			}
		}

		@Override
		public void updateHorizontalVelocity(double time) {
			horizontalVelocity += horizontalAcceleration*time;
			this.enforceHorizontalBoundaries();
		}

		@Override
		public void updateVerticalVelocity(double time) {
			verticalVelocity += verticalAcceleration*time;
			this.enforceHorizontalBoundaries();	
		}
		
	};

	@Model
	protected Organism(int x, int y, Sprite... sprites) throws IllegalArgumentException{
		super(x,y, sprites);
	}
	
	public double[] getAcceleration() {
		return new double[] {kinematics.getHorizontalAcceleration(), kinematics.getVerticalAcceleration()};
	}
	
	public double[] getVelocity() {
		return new double[] {kinematics.getHorizontalVelocity(), kinematics.getVerticalVelocity()};
	}
	
	protected void setSprite(int index){
		if(index < 8) {
			setSprite(getSprites()[index]);
		}else if(getOrientation() == 1 && index >= ((getSprites().length-8)/2)+8){
			index = 8;
		}else if(getOrientation() == -1 && index >= getSprites().length) {
			index = ((getSprites().length-8)/2)+8;
		}
		setSprite(getSprites()[index]);
		this.getRectangle().setWidth(getSprite().getWidth());
		this.getRectangle().setHeight(getSprite().getHeight());
	}
	
	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay += delay;
		if(this.delay > REMOVE_DELAY) this.delay = REMOVE_DELAY;
	}
	
	public double getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(double blockTime) {
		this.blockTime = blockTime;
	}

	public boolean isRunning() {
		return kinematics.getHorizontalVelocity() != 0;
	}

	public abstract int getOrientation();
	public abstract void advanceTime(double deltaT);
	public abstract boolean isDead();
	protected abstract double updateDt(double deltaT, double time);
	
	public void terminate() {
		if(this instanceof Slime && ((Slime) this).getSchool() != null) {
				((Slime) this).getSchool().removeSlime(((Slime) this));
		}
		if(this instanceof Creature && ((Creature)this).getHitPoints() != 0) 
			((Creature) this).setHitPoints(-((Creature)this).getHitPoints());
		if(this instanceof Plant && ((Plant)this).getHit() != 0) 
			((Plant) this).getHitPoint().setPoints(-((Plant)this).getHit());
		if(this instanceof Spider) ((Spider) this).setLegs(0);
		if(getWorld() == null) return;
		if(this == getWorld().getPlayer()) this.getWorld().unsetPlayer();
		this.getWorld().removeGameObject(this);
		this.setWorld(null);
	}

	public boolean isTerminated() {
		return (this.getWorld() == null && isDead());
	}

	protected Rectangle getUpBorder() {
		Rectangle up = null;
		if(getOrientation() == -1) {
			up = new Rectangle(getRectangle().getXCoordinate()-1, getRectangle().getYCoordinate()+getRectangle().getHeight(), getRectangle().getWidth(), 1);
		}else if(getOrientation() == 0) {
			up = new Rectangle(getRectangle().getXCoordinate(), getRectangle().getYCoordinate()+getRectangle().getHeight(), getRectangle().getWidth(), 1);
		}else if(getOrientation() == 1) {
			up = new Rectangle(getRectangle().getXCoordinate()+1, getRectangle().getYCoordinate()+getRectangle().getHeight(), getRectangle().getWidth(), 1);
		}
		return up;
	}
	
	protected Rectangle getDownBorder() {
		Rectangle down = null;
		if(getOrientation() == -1) {
			down = new Rectangle(getRectangle().getXCoordinate()-1, getRectangle().getYCoordinate()-1, getRectangle().getWidth(), 1);
		}else if(getOrientation() == 0) {
			down = new Rectangle(getRectangle().getXCoordinate(), getRectangle().getYCoordinate()-1, getRectangle().getWidth(), 1);
		}else if(getOrientation() == 1) {
			down = new Rectangle(getRectangle().getXCoordinate()+1, getRectangle().getYCoordinate()-1, getRectangle().getWidth(), 1);
		}
		return down;
	}
	
	protected Rectangle getLeftBorder() {
		return new Rectangle(getRectangle().getXCoordinate()-1, getRectangle().getYCoordinate(), 1, getRectangle().getHeight());
	}

	protected Rectangle getRightBorder() {
		return new Rectangle(getRectangle().getXCoordinate()+getRectangle().getWidth(), getRectangle().getYCoordinate(), 1, getRectangle().getHeight());
	}
	
	public boolean isInside() {
		if(this.getWorld() == null) {return true;}
		if(this.getPosition().getX() < 0 || this.getPosition().getY() < 0) return false;
		return this.getWorld().getGameWorld().contains(getRectangle().getOrigin());
	}
	
	public void changeActualPosition(double[] newPosition) throws IllegalArgumentException{
		if(newPosition == null || newPosition.length != 2 || (((Double) newPosition[0]).isNaN() || ((Double) newPosition[1]).isNaN()))
			throw new IllegalArgumentException("The desired position can not be null, must be two-dimensional and not NaN values.");
		if(getWorld() != null && !(this instanceof Plant)) {
			for(int newX = (int)(newPosition[0]/0.01), pixelX = newX; pixelX < newX+getSprite().getWidth(); pixelX+=getWorld().getTileLength()) {
				for(int newY = (int)(newPosition[1]/0.01), pixelY= newY; pixelY < newY+getSprite().getHeight(); pixelY+= getWorld().getTileLength()) {
					if(!getWorld().getTileFeature(pixelX, pixelY).isPassable()) 
						throw new IllegalArgumentException("You can not place the Game Object in Solid Ground");
				}
			}
		}
		getPosition().setPosition(newPosition[0], newPosition[1]);
		getRectangle().setOrigin((int)(newPosition[0]/0.01), (int)(newPosition[1]/0.01));
		if(getWorld() != null && !getWorld().getGameWorld().contains(getRectangle().getOrigin())) terminate();
	}
	
	protected Set<Organism> overlappingGameObject(Rectangle rect){
		Set<Organism> overlappingObjects = new HashSet<>();
		if(this.getWorld() == null || this.isTerminated() || rect == null) return overlappingObjects;
		for(Object object: this.getWorld().getGameObjects()) {
			if(!(object instanceof Plant) || this instanceof Mazub) {
				if(object instanceof Plant) {
					overlappingObjects.add((Plant) object);
				}else if(rect.overlaps(((Organism) object).getRectangle()) && object != this) {
					overlappingObjects.add((Organism) object);
				}
			}
		}
		return overlappingObjects;
	}
	
	protected Set<Organism> getCollidingObjects() {
		Set<Organism> objects;
		if(kinematics.getVerticalVelocity() > 0) {
			objects = overlappingGameObject(getUpBorder());
			if(getOrientation() ==  -1) objects.addAll(overlappingGameObject(getLeftBorder()));
			else if(getOrientation() == 1) objects.addAll(overlappingGameObject(getRightBorder()));
		}else {
			objects = overlappingGameObject(getDownBorder());
			if(getOrientation() ==  -1) objects.addAll(overlappingGameObject(getLeftBorder()));
			else if(getOrientation() == 1) objects.addAll(overlappingGameObject(getRightBorder()));
		}
		return objects;
	}
	
	protected int getGameObjectType(Organism gameObject) {
		if(gameObject instanceof Mazub) return 0;
		else if (gameObject instanceof Sneezewort) return 1;
		else if(gameObject instanceof Skullcab) return 2;
		else if(gameObject instanceof Slime) return 3;
		else if(gameObject instanceof Shark) return 4;
		else if(gameObject instanceof Spider) return 5;
		else return -1;
	}
}
