package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;

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
public abstract class Organism extends GameObject implements Movable{
	
	private HitPoint hitPoint;
	private double delay = 0.0;
	protected Kinematics kinematics = new Kinematics() ;

	protected Organism(int x, int y, int initPoints, int minPoints, int maxPoints, Sprite... sprites) throws IllegalArgumentException{
		super(x,y, sprites);
		this.setHitPoint(initPoints, minPoints, maxPoints);
	}
	
	public HitPoint getHitPoints() {
		return hitPoint.clone();
	}

	public int getPoints() {
		return hitPoint.getPoints();
	}
	
	public void setHitPoint(int initPoints, int minPoints, int maxPoints) {
		this.hitPoint = new HitPoint(initPoints, minPoints, maxPoints);
	}
	
	public void updateHitPoints(int points) {
		this.hitPoint.updatePoints(points);
	}
	
	public double[] getAcceleration() {
		return new double[] {kinematics.getXAcceleration(), kinematics.getYAcceleration()};
	}
	
	public double[] getVelocity() {
		return new double[] {kinematics.getXVelocity(), kinematics.getYVelocity()};
	}	
	
	public double getDelay() {
		return Double.valueOf(delay);
	}

	public void setDelay(double delay) {
		this.delay += delay;
		if(this.delay > Constant.REMOVE_DELAY.getValue()) 
			this.delay = Constant.REMOVE_DELAY.getValue();
	}
	
	public abstract boolean isDead();
	
	public void terminate() {
		if(this instanceof Slime && ((Slime) this).getSchool() != null) {
			((Slime) this).getSchool().removeSlime(((Slime) this));
		}
		if(getPoints() != 0) updateHitPoints(-getPoints());
		if(getWorld() == null) return;
		if(this == getWorld().getPlayer()) this.getWorld().unsetPlayer();
		this.getWorld().removeGameObject(this);
	}

	protected Rectangle getUpBorder() {
		Rectangle up = null;
		if(kinematics.getXVelocity() < 0) {
			up = new Rectangle(getRectangle().getX()-1, getRectangle().getY()+getRectangle().getHeight(), getRectangle().getWidth(), 1);
		}else if(kinematics.getXVelocity() == 0) {
			up = new Rectangle(getRectangle().getX(), getRectangle().getY()+getRectangle().getHeight(), getRectangle().getWidth(), 1);
		}else if(kinematics.getXVelocity() > 0) {
			up = new Rectangle(getRectangle().getX()+1, getRectangle().getY()+getRectangle().getHeight(), getRectangle().getWidth(), 1);
		}
		return up;
	}
	
	protected Rectangle getDownBorder() {
		Rectangle down = null;
		if(kinematics.getXVelocity() < 0) {
			down = new Rectangle(getRectangle().getX()-1, getRectangle().getY()-1, getRectangle().getWidth(), 1);
		}else if(kinematics.getXVelocity() == 0) {
			down = new Rectangle(getRectangle().getX(), getRectangle().getY()-1, getRectangle().getWidth(), 1);
		}else if(kinematics.getXVelocity() > 0) {
			down = new Rectangle(getRectangle().getX()+1, getRectangle().getY()-1, getRectangle().getWidth(), 1);
		}
		return down;
	}
	
	protected Rectangle getLeftBorder() {
		return new Rectangle(getRectangle().getX()-1, getRectangle().getY()+1, 1, getRectangle().getHeight()-1);
	}

	protected Rectangle getRightBorder() {
		return new Rectangle(getRectangle().getX()+getRectangle().getWidth(), getRectangle().getY()+1, 1, getRectangle().getHeight()-1);
	}
	
	public boolean isInside() {
		if(this.getWorld() == null) return true;
		return this.getWorld().getGameWorld().contains(getRectangle().getOrigin());
	}
	
	public void changeActualPosition(double[] newPosition){
		if(newPosition == null || newPosition.length != 2 || (((Double) newPosition[0]).isNaN() || ((Double) newPosition[1]).isNaN()))
			throw new IllegalArgumentException("The desired position can not be null, must be two-dimensional and not NaN values.");
		if(getWorld() != null && !(this instanceof Plant)) {
			Rectangle newPlace = new Rectangle((int)(newPosition[0]/0.01), (int)(newPosition[1]/0.01), getSprite().getWidth(), getSprite().getHeight()); 
			if(!getWorld().shallBePassable(newPlace)) throw new IllegalArgumentException("You can not place the Game Object in Solid Ground");
		}
		super.updatePosition(newPosition[0], newPosition[1]);
		if(getWorld() != null && !getWorld().getGameWorld().contains(getRectangle().getOrigin())) terminate();
	}
	
	protected Set<Organism> getCollidingCreatures(){
		Set<Organism> overlappingObjects = new HashSet<>();
		if(super.isStillNotInAGameWorld()) return overlappingObjects;
		Rectangle fullOuterRectangle = new Rectangle(getRectangle().getX()-1,getRectangle().getY()-1, getSprite().getWidth()+2,getSprite().getHeight()+2);
		for(Object object: this.getWorld().getGameObjects()) {
			if(fullOuterRectangle.overlaps(((Organism) object).getRectangle()) && object != this) {
				overlappingObjects.add((Organism) object);
			}
		}
		return overlappingObjects;
	}
	
	protected Set<Organism> getOverlappingPlants(){
		Set<Organism> overlappingPlants = new HashSet<>();
		if(super.isStillNotInAGameWorld()) return overlappingPlants;
		for(Object object: this.getWorld().getGameObjects()) {
			if(object instanceof Plant && ((Plant) object).getRectangle().overlaps(getRectangle())) {
				overlappingPlants.add((Plant) object);
			}
		}
		return overlappingPlants;
	}
	
	protected Set<Organism> getCollidingCreatures(Rectangle border){
		Set<Organism> overlappingObjects = new HashSet<>();
		if(super.isStillNotInAGameWorld()) return overlappingObjects;
		for(Object object: this.getWorld().getGameObjects()) {
			if(border.overlaps(((Organism) object).getRectangle()) && object != this) {
				overlappingObjects.add((Organism) object);
			}
		}
		return overlappingObjects;
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
