package jumpingalien.model;

import annotate.Basic;
import jumpingalien.util.Sprite;

/**
 * This class builds a Plant with Hit Points as health and with an age
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Plant extends Organism implements Movable{
	
	private double age  = 0.0;
	private double moveTime = 0.0;
	
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
	public double getMoveTime() {
		return moveTime;
	}

	/**
	 * This method is used to set the Timer
	 * 
	 * @param time
	 * 			This parameter is used to set the Timer
	 * 
	 * @post ...
	 * 		|(new this).timer = timer
	 * 
	 */
	protected void setMoveTime(double time) {
		this.moveTime = time;
	}
	
	/**
	 * This method will update the situation of the Sneezewort using deltaT
	 * 
	 * @param deltaT
	 * 			This parameter is used as time to be passed
	 * @throws IllegalArgumentException
	 * 			...
	 * 		|Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)
	 * @post ...
	 * 		|	for(double time = 0.0, dt = updateDt(deltaT, time); time < deltaT; time += dt, dt = updateDt(deltaT, time)) 
	 *		|		this.setAge(getAge() + dt)
	 *		|		if(getAge() < SNEEZE_AGE && getTimer() + dt > Constant.PLANT_SWITCH_TIME.getValue() && !isDead())  
	 *		|			then arrangeOvershoot(Constant.PLANT_SWITCH_TIME.getValue() - getTimer(), dt)
	 *		|		else if(getAge() < SNEEZE_AGE && !isDead())  then arrangeMove(dt)
	 *		|	else 
	 *		|		if(getWorld() != null && getWorld().getPlayer() != null) then 
	 *		|			getWorld().handleImpact(getWorld().getPlayer(), this, deltaT)
	 *		|		super.setDelay(dt)
	 *@post ...
	 *		| if(super.getDelay() >= REMOVE_DELAY || !isInside()) then terminate()
	 * 
	 */
	@Override
	public void advanceTime(double deltaT) throws IllegalArgumentException{
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		double dt = kinematics.calculateNewTimeSlice(deltaT, 0.0);
		for(double time = 0.0; time < deltaT; dt = kinematics.calculateNewTimeSlice(deltaT, time)) {
			if(moveTime + dt >= Constant.PLANT_SWITCH_TIME.getValue()) dt = Constant.PLANT_SWITCH_TIME.getValue() - moveTime;
			if(!isDead()) arrangeMove(dt);
			else super.setDelay(dt);
			this.setAge(getAge() + dt);
			time += dt;
		}
		if(super.getDelay() >= Constant.REMOVE_DELAY.getValue() || !isInside()) terminate();
	}
	
	public abstract boolean isDead();
	protected abstract void arrangeMove(double deltaT); //specification can be seen in the subclasses of this class
	protected abstract void arrangeEat(double dt);
}
