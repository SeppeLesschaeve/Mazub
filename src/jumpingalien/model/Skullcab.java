package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * This class builds a Skullcab that can jump at constant velocity
 * 
 * @invar ...
 * 		| getVelocity().getY() <= getVelocity().getMaxY()
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
		if(super.getVelocity().getY() < 0) {
			return Orientation.NEGATIVE.getValue();
		}
		if(super.getVelocity().getY() > 0) {
			return Orientation.POSITIVE.getValue();
		}
		return 0;
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
		super.getVelocity().setX(0.0);
		super.getVelocity().setY(Y_VELOCITY);
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
		for(double time = 0.0, dt = updateDt(deltaT, time); time < deltaT; time += dt, dt = updateDt(deltaT, time)) {
			this.setAge(getAge() + dt);
			if(getAge() < SKULL_AGE && getTimer() + dt > Constant.PLANT_SWITCH_TIME.getValue() && !isDead()) { 
				arrangeOvershoot(Constant.PLANT_SWITCH_TIME.getValue() - getTimer(), dt);
			}else if(getAge() < SKULL_AGE && !isDead()) {
				arrangeMove(dt);
			}else {
				if(getWorld() != null && getWorld().getPlayer() != null) {
					getWorld().handleCollision(getWorld().getPlayer(), this, deltaT);
				}
				super.setDelay(dt);
			}
		}
		if(super.getDelay() >= REMOVE_DELAY || !isInside()) terminate();
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
		jump(deltaT);
		if(!super.isInside()) {
			terminate(); return;
		}
		if(getWorld() != null && getWorld().getPlayer() != null) {
			getWorld().handleCollision(getWorld().getPlayer(), this, deltaT);
		}
	}

	/**
	 * This method updates the position using dt as time and remainder as remainder up to 0.5 seconds to the switch 
	 * 
	 * @param dt
	 * 			This parameter is used to update the position
	 * @param remainder
	 * 			This parameter is used as remainder to update the position
	 * 
	 * @post ...
	 * 		| arrangeMove(remainder)
	 *		| endJump(dt)
	 *		|arrangeMove(dt - remainder)
	 *
	 */
	@Override
	protected void arrangeOvershoot(double remainder, double dt){
		arrangeMove(remainder);
		endJump(dt);
		arrangeMove(dt - remainder);
	}

	/**
	 * This method is used to switch to movement 
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented of the interface Jump
	 * 
	 * @post ...
	 * 		| setTimer(0.0) && super.getVelocity().setY(-super.getVelocity().getY()) && super.setSprite(1-super.getIndex())
	 */
	@Override @Raw
	public void endJump(double deltaT){
		setTimer(0.0); 
		super.getVelocity().setY(-super.getVelocity().getY()); 
		super.setSprite(1-super.getIndex());
	}
	
	/**
	 * This method is used to arrange the Position using deltaT
	 * 
	 * @param deltaT
	 * 		This parameter is used as time
	 * 
	 * @post ...
	 * 		| super.getPosition().setY(super.getPosition().getY() + super.getVelocity().getY()*deltaT)
	 *		| super.getOrigin().setY((int)(super.getPosition().getY()/0.01)))
	 */
	@Override @Raw
	public void jump(double deltaT){
		super.getPosition().setY(super.getPosition().getY() + super.getVelocity().getY()*deltaT);
		super.getOrigin().setY((int)(super.getPosition().getY()/0.01));
	}

}