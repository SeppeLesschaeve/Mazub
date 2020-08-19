package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import jumpingalien.util.Sprite;

/**
 * This class builds a Plant with Hit Points as health and with an age
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Plant extends Organism{
	
	private double age  = 0.0;
	private double timer = 0.0;
	private double hitTime = 0.0;
	
	protected static final double SNEEZE_AGE = 10;
	protected static final double SKULL_AGE = 12;
	
	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoint and the images to show the animation
	 * 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @effect ...
	 * 		| super(pixelLeftX, pixelBottomY, sprites)
	 * @post ...
	 * 		| this.hitPoint = new HitPoint(point, 0, point)
	 */
	@Model
	protected Plant(int pixelLeftX, int pixelBottomY, int point, Sprite... sprites) {
		super(pixelLeftX, pixelBottomY, point, 0, point, sprites); 
	}


	/**
	 * This method returns Age
	 * 
	 * @return ...
	 * 		|result == age
	 * 
	 */
	@Basic
	public double getAge() {
		return this.age;
	}

	/**
	 * This method is used to set the Age
	 * 
	 * @param age
	 * 			This parameter is used to set the Age
	 * 
	 * @post ...
	 * 		|(new this).age = age
	 * 
	 */
	protected void setAge(double age) {
		this.age = age;
	}
	
	/**
	 * This method returns Timer
	 * 
	 * @return ...
	 * 		|result == timer
	 * 
	 */
	@Basic
	public double getTimer() {
		return timer;
	}

	/**
	 * This method is used to set the Timer
	 * 
	 * @param timer
	 * 			This parameter is used to set the Timer
	 * 
	 * @post ...
	 * 		|(new this).timer = timer
	 * 
	 */
	protected void setTimer(double timer) {
		this.timer = timer;
	}
	
	/**
	 * This method returns Hit Time
	 * 
	 * @return ...
	 * 		|result == hitTime
	 * 
	 */
	@Basic
	public double getHitTime() {
		return hitTime;
	}
	
	/**
	 * This method is used to set the Hit Time
	 * 
	 * @param hitTime
	 * 			This parameter is used to set the Hit Time
	 * 
	 * @post ...
	 * 		|(new this).hitTime = hitTime
	 * 
	 */
	protected void setHitTime(double hitTime) {
		this.hitTime = hitTime;
	}
	
	/**
	 * This method returns is used to return whether the Plant is dead
	 * 
	 * @return ...
	 * 		|if(this instanceof Sneezewort) then result == ( getHit() == 0 || getAge() >= SNEEZE_AGE)
	 * 			...
	 * 		|if(this instanceof Skullcab) result == ( getHit() == 0 || getAge() >= SKULL_AGE)
	 */
	@Override
	public boolean isDead() {
		if(this instanceof Sneezewort) return getPoints() == 0 || getAge() >= SNEEZE_AGE;
		if(this instanceof Skullcab)   return getPoints() == 0 || getAge() >= SKULL_AGE;
		return false;
	}
	
	protected abstract void arrangeMove(double deltaT); //specification can be seen in the subclasses of this class
	protected abstract void startMove(); //specification can be seen in the subclasses of this class
	protected abstract void arrangeEat(double deltaT);
}
