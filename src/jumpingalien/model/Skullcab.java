package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * This class builds a Skullcab that can jump at constant velocity
 * 
 * @invar ...
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public class Skullcab extends Plant implements Jump{
	
	private static final double Y_VELOCITY = 0.5;
	
	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoint and the images to show the animation
	 * 
	 * @param xPixelLeft
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param yBottomLeft
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 *  
	 *  @throws IllegalArgumentException
	 * 		...
	 * 		| (sprites.length != 2)
	 * @effect ...
	 * 		| super(xPixelLeft, yBottomLeft, 3, sprites)
	 * @post ...
	 * 		| startMove()
	 * 
	 */
	public Skullcab(int xPixelLeft, int yBottomLeft, Sprite... sprites) throws IllegalArgumentException{
		super(xPixelLeft, yBottomLeft, 3, sprites);
		if(sprites.length != 2) throw new IllegalArgumentException("You must have exactly two images");
		startMove();
	}
	
	/**
	 * This method returns the orientation of the Skullcab using the sprite and velocity
	 * 
	 * @return ....
	 * 		| if(super.getVelocity().getX() < 0) then result == Orientation.NEGATIVE.getValue()
	 *		| if(super.getVelocity().getX() > 0) then result == Orientation.POSITIVE.getValue()
	 */
	@Basic
	public int getOrientation() {
		if(kinematics.getYVelocity() < 0) return -1;
		else if(kinematics.getYVelocity() > 0) return 1;
		else return 0;
	}
	
	/**
	 * This method will set the velocity to start moving
	 * 
	 * @post ...
	 * 		|super.getVelocity().setX(0.0)
	 *		|super.getVelocity().setY(Y_VELOCITY)
	 */
	@Override
	protected void startMove() {
		kinematics.setYVelocity(Y_VELOCITY);
	}
	
	/**
	 * This method will update the situation of the Skullcab using deltaT
	 * 
	 * @param deltaT
	 * 			This parameter is used as time to be passed
	 * @throws IllegalArgumentException
	 * 			...
	 * 		|Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)
	 * @post ...
	 * 		|	for(double time = 0.0, dt = updateDt(deltaT, time); time < deltaT; time += dt, dt = updateDt(deltaT, time)) 
	 *		|		this.setAge(getAge() + dt)
	 *		|		if(getAge() < SKULL_AGE && getTimer() + dt > Constant.PLANT_SWITCH_TIME.getValue() && !isDead())  
	 *		|			then arrangeOvershoot(Constant.PLANT_SWITCH_TIME.getValue() - getTimer(), dt)
	 *		|		else if(getAge() < SKULL_AGE && !isDead())  then arrangeMove(dt)
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
			if(!isDead()) arrangeMove(dt);
			else {
				super.setDelay(dt);
				if(getWorld() != null && getWorld().getPlayer() != null)getWorld().getPlayer().arrangeObjectHit(dt);
			}
			this.setAge(getAge() + dt);
			time+=dt;
		}
		if(super.getDelay() >= Constant.REMOVE_DELAY.getValue() || !isInside()) terminate();
	}
	
	/**
	 * This method will update the position using deltaT, velocity and the current sprite when not dead 
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time to update the position
	 * @post ...
	 * 		| setTimer(getTimer() + deltaT) && jump(deltaT)
	 * @post ...
	 *		|if(!super.isInside()) then terminate() && return
	 * @post ...
	 *		|if(getWorld() != null && getWorld().getPlayer() != null) then getWorld().handleImpact(getWorld().getPlayer(), this, deltaT)
	 * 
	 */
	@Override
	protected void arrangeMove(double deltaT){
		setTimer(getTimer() + deltaT);
		double overshoot = getTimer()-Constant.PLANT_SWITCH_TIME.getValue();
		if(getTimer() >= Constant.PLANT_SWITCH_TIME.getValue()) {
			jump(deltaT-overshoot);
			if(getWorld() != null && getWorld().getPlayer() != null)getWorld().getPlayer().arrangeObjectHit(deltaT-overshoot);
			endJump();
			jump(overshoot);
			if(getWorld() != null && getWorld().getPlayer() != null)getWorld().getPlayer().arrangeObjectHit(overshoot);
		}else {
			jump(deltaT);
			if(getWorld() != null && getWorld().getPlayer() != null)getWorld().getPlayer().arrangeObjectHit(deltaT);
			
		}
		if(!super.isInside()) terminate();
	}
/**
	 * This method is used to switch to movement 
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented of the interface Jump
	 * 
	 * @post ...
	 * 		| setTimer(0.0) && super.getVelocity().setY(-super.kinematics.getVerticalVelocity()) && super.setSprite(1-super.getIndex())
	 */
	@Raw
	public void endJump(){
		setTimer(getTimer()-Constant.PLANT_SWITCH_TIME.getValue()); 
		kinematics.setYVelocity(-kinematics.getYVelocity()); 
		super.setSprite(1-super.getIndex());
	}
	
	/**
	 * This method is used to arrange the Position using deltaT
	 * 
	 * @param deltaT
	 * 		This parameter is used as time
	 * 
	 * @post ...
	 * 		| super.getPosition().setY(super.getPosition().getY() + super.kinematics.getVerticalVelocity()*deltaT)
	 *		| super.getOrigin().setY((int)(super.getPosition().getY()/0.01)))
	 */
	@Override @Raw
	public void jump(double deltaT){
		super.updateVerticalComponent(super.getPosition().getY() + kinematics.getYVelocity()*deltaT);
	}

	protected void arrangeEat(double dt) {
		if(getWorld().getPlayer().getPoints() < getWorld().getPlayer().getHitPoints().getMaximum() && getHitTime() == 0 && getPoints() != 0) updateHitPoints(-1);
		setHitTime(getHitTime() + dt);
		if(getHitTime()  >= Constant.TIMEOUT.getValue()) setHitTime(0);
		if(getPoints() == 0) terminate();
	}

}
