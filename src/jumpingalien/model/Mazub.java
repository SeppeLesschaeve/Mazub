package jumpingalien.model;

import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * This class holds a (playable) alien that is able to run, duck and jump
 * 
 * @invar The horizontal velocity will never be bigger than the maximum horizontal velocity
 * 		| kinematics.getHorizontalVelocity() <= Math.abs(getVelocity().getMaxX())
 * @invar The vertical velocity will never be bigger than the maximum vertical velocity
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 *                     
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 * 
 */
public class Mazub extends Creature implements Run, Jump{
	
	private double spriteTime = 0.0;
	private double endMoveTime = 0.0;
	private double eatTime = 0.0;
	private double featureTime;
	private Feature previousFeature;

	private static final double MIN_X_VELOCITY = 1.0;
	private static final double MAX_X_VELOCITY = 3.0;
	private static final double Y_VELOCITY = 8.0;
	private static final double X_ACC = 0.9; 
	private static final double Y_ACC = -10.0;
	private static final double SPRITE_CHANGE = 0.075;
	
	/**
	 * This constructor will set the HitPoint, Pixel Position, Actual Position, Dimension and the images to show the animation
	 * 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @throws IllegalArgumentException 
	 * 			The sprites must be an array of at least eight even numbered images
	 * 			| (sprites.length < 8 || sprites.length%2 != 0)
	 * 		
	 * @post The sprites and Positions and HitPoint will be set using the superclasses Creature and GameObject
	 * 		| super(pixelLeftX, pixelBottomY, 100, 0, 500, sprites)
	 */
	public Mazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws IllegalArgumentException{
		super(pixelLeftX, pixelBottomY, 100, 0, 500, sprites);
		if(sprites.length < 8 || sprites.length%2 != 0)
			throw new IllegalArgumentException("This is not a valid amount of sprites.");
	}
	
	/**
	 * This method returns an array of two integers representing the Pixel Position
	 * 
	 * @return the x and y coordinates of the Pixel Position
	 * 		| result == {super.getOrigin().getX(),super.getOrigin().getY()}
	 */
	@Basic
	public int[] getPixelPosition() {
		return new int[] {super.getRectangle().getOrigin().getX(),super.getRectangle().getOrigin().getY()};
	}
	
	/**
	 * This method is used to set the coordinates of the Pixel Position
	 * 
	 * @param x
	 * 		This parameter represents the x-coordinate of the Pixel Position
	 * @param y
	 * 		This parameter represents the y-coordinate of the Pixel Position
	 * 
	 * @throws IllegalArgumentException
	 * 		If one of the coordinates is smaller than zero
	 * 		| ( x < 0 || y < 0 )
	 * 
	 * @post The Pixel Position will be set
	 * 		| super.getOrigin().setPosition(x, y)
	 */
	protected void setPixelPosition(int x, int y) throws IllegalArgumentException{
		if( x < 0 || y < 0) throw new IllegalArgumentException("The coordinates must be positive");
		super.updatePosition(x, y);
	}
	
	/**
	 * This method returns an array of two double precision floating-point numbers representing the Actual Position
	 * 
	 * @return the x and y coordinates of the Actual Position
	 * 		| result == {super.getPosition().getX(), super.getPosition().getY()}		
	 */
	@Basic
	public double[] getActualPosition() {
		return new double[] {super.getPosition().getX(), super.getPosition().getY()};
	}

	/**
	 * This method returns the orientation of the alien based on the velocity and the current sprite
	 * 
	 * @return the orientation of the alien
	 * 		| if(super.getIndex() == 2) result ==  Orientation.POSITIVE.getValue()
	 *		| if(super.getIndex() == 3) result ==  Orientation.NEGATIVE.getValue()
		    | result == super.getOrientation()
	 */
	@Basic @Override
	public int getOrientation() {
		if(super.getIndex() == 2) return 1;
		if(super.getIndex() == 3) return  -1;
		return super.getOrientation();
	}
	
	/**
	 * This method is used to set the velocity using two double precision floating-point numbers
	 * 
	 * @param x 
	 * 			This parameter is used as x-component of the velocity
	 * @param y
	 * 			This parameter is used as y-component of the velocity
	 * 
	 * @post The velocity will be set
	 * 		| getVelocity().setX(x)
	 * 		| getVelocity().setY(y)
	 */
	public void setVelocity(double x, double y) {
		kinematics.setXVelocity(x);
		kinematics.setYVelocity(y);
	}
	
	/**
	 * This method is used to set the acceleration using two double precision floating-point numbers
	 * 
	 * @param x
	 * 			This parameter is used as x-component of the acceleration
	 * @param y
	 * 			This parameter is used as y-component of the acceleration
	 * 
	 * @post The acceleration will be set
	 * 		|getAcceleration().setX(x)
	 *		|getAcceleration().setY(y)
	 */
	public void setAcceleration(double x, double y) {
		kinematics.setXAcceleration(x);
		kinematics.setYAcceleration(y);
	}
	
	/**
	 * This method is used to start the right movement of the alien
	 * 
	 * @pre The alien is not moving and is not dead
	 * 		| !isRunning() && !isDead()
	 * @post The velocity, acceleration and the sprite will be set
	 * 		| super.setSprite(8)
	 *		| setAcceleration(X_ACC, 0.0)
	 *   	| setVelocity( MIN_X_VELOCITY , 0.0)
	 * 		| super.setBorders()
	 */
	public void startRunRight() {
		assert(!isRunning() && !isDead());
		setAcceleration(X_ACC, kinematics.getYAcceleration());
		setVelocity( MIN_X_VELOCITY , kinematics.getYVelocity());
		kinematics.setXVelocityBounds(MIN_X_VELOCITY, MAX_X_VELOCITY);
		super.setSprite(8);
	}
	
	/**
	 * This method is used to start the left movement of the alien
	 * 
	 * @pre The alien is not moving and is not dead
	 * 		| !isRunning()  && !isDead()
	 * @post The velocity, acceleration and the sprite will be set
	 * 		| super.setSprite(((sprites.length-8)/2)+8)
	 *		| setAcceleration(-X_ACC, 0.0)
	 *		| setVelocity( -MIN_X_VELOCITY , 0.0)
	 *		| super.setBorders()
	 */
	public void startRunLeft() {
		assert(!isRunning() && !isDead());
		setAcceleration(-X_ACC, kinematics.getYAcceleration());
		setVelocity( -MIN_X_VELOCITY , kinematics.getYVelocity());
		kinematics.setXVelocityBounds(-MIN_X_VELOCITY, -MAX_X_VELOCITY);
		super.setSprite(((super.getSprites().length - 8) /2 ) + 8);
	}
	
	/**
	 * This method is used to stop the movement of the alien 
	 * 
	 * @pre The alien is running and is not dead
	 * 		| isRunning() &&  !isDead()
	 * @post The velocity, acceleration and the sprite will be set on the orienation
	 * 		| if(getOrientation() == Orientation.NEGATIVE) then super.setSprite(3)
	 *		| if(getOrientation() == Orientation.POSITIVE) then super.setSprite(2)
	 *		| if(getOrientation() == Orientation.NEUTRAL) then super.setSprite(0)
	 *		| setVelocity(0.0, kinematics.getVerticalVelocity())
	 *		| setAcceleration(0.0, kinematics.getVerticalAcceleration())
	 *		| super.setBorders()
	 */
	public void endRun() {
		assert(isRunning() &&  !isDead());
		if(getOrientation() == -1)  super.setSprite(3);
		if(getOrientation() == 1) super.setSprite(2);
		if(getOrientation() == 0) super.setSprite(0);
		setVelocity(0.0, kinematics.getYVelocity());
		setAcceleration(0.0, kinematics.getYAcceleration());
		kinematics.setXVelocityBounds(0.0, 0.0);
	}
	
	/**
	 * This method is used to start the jump movement of the alien
	 * 
	 * @throws IllegalStateException
	 * 		If Mazub is considered jumping or dead
	 * 		|(isMovingVertically() || getHit() == 0) 
	 * @post The velocity, accceleration and sprite will be set based on the orientation
	 * 		| if(getOrientation() == Orientation.NEGATIVE) then super.setSprite(5)
	 *		| if(getOrientation() == Orientation.POSITIVE) then super.setSprite(4)
	 *		| if(getOrientation() == Orientation.NEUTRAL) then super.setSprite(0)
	 *		| setVelocity(kinematics.getHorizontalVelocity(), Y_VELOCITY)
	 *	    | setAcceleration(kinematics.getHorizontalAcceleration(), Y_ACC)
	 *		| super.setBorders()
	 * 
	 */
	public void startJump() throws IllegalStateException{
		if(isMovingVertically() || isDead()) throw new IllegalStateException("Mazub can not start jumping if he did jump before it.");
		if(getOrientation() == -1)  super.setSprite(5);
		if(getOrientation() == 1) super.setSprite(4);
		if(getOrientation() == 0) super.setSprite(0);
		setVelocity(kinematics.getXVelocity(), Y_VELOCITY);
		setAcceleration(kinematics.getXAcceleration(), Y_ACC);
	}
	
	/**
	 * This method is used to stop the jump movement of the alien
	 * 
	 * @throws IllegalStateException
	 * 		If Mazub is considered not jumping or dead
	 * 		| (!isMovingVertically() || isDead())
	 * @post The velocity, acceleration and sprite will be set based on the orientation
	 * 		| if(super.kinematics.getVerticalVelocity() > 0) then super.setVelocity(super.kinematics.getHorizontalVelocity(), 0.0)
	 *		| if(getOrientation() == Orientation.NEGATIVE) then super.setSprite(getSprites().length-8)/2)+8)
	 *		| if(getOrientation() == Orientation.POSITIVE) then super.setSprite(8)
	 *		| if(getOrientation() == Orientation.NEUTRAL) then super.setSprite(0)
	 *		| setAcceleration(kinematics.getHorizontalAcceleration(), 0.0)
	 *		| super.setBorders()
	 * 
	 */
	public void endJump(){
		if(!isMovingVertically() || isDead()) throw new IllegalStateException("Mazub can not stop jumping if he didn' t jump before it.");
		setSpriteTime(0.0);
		if(kinematics.getYVelocity() > 0) {
			setVelocity(kinematics.getXVelocity(), 0.0);
		}
		if(getOrientation() == -1) super.setSprite(((super.getSprites().length-8)/2) +8);
		if(getOrientation() == 1) super.setSprite(8);
		if(getOrientation() == 0) super.setSprite(0);
		setAcceleration(kinematics.getXAcceleration(), 0.0);
	}
	
	/**
	 * This method returns whether the alien is moving vertically or not
	 * 
	 * @return True if and only if the alien is jumping or falling
	 * 			| result == isJumping() || isFalling()
	 */
	public boolean isMovingVertically() {
		return isJumping() || isFalling();
	}
	
	/**
	 * This method returns whether the alien is jumping or not
	 * 
	 * @return True if and only is the alien is moving upwards
	 * 		| result == super.kinematics.getVerticalVelocity() > 0
	 */
	@Basic
	public boolean isJumping() {
		return kinematics.getYVelocity() > 0;
	}
	
	/**
	 * This method returns whether the alien is falling or not
	 * 
	 * @return True if and only is the alien is moving downwards
	 * 		| result == super.kinematics.getVerticalVelocity() <= 0 && super.kinematics.getVerticalAcceleration() != 0
	 */
	@Basic
	public boolean isFalling() {
		return kinematics.getYVelocity() <= 0 && kinematics.getYAcceleration() != 0;
	}
	
	/**
	 * This method is used to start the duck movement of the alien
	 * 
	 * @post The velocity, acceleration and the sprite will be set based on the orientation
	 *   | if(super.kinematics.getHorizontalVelocity() != 0) 
	 *	 |	  if(kinematics.getHorizontalVelocity() < 0) 
	 *	 |		 then setVelocity(-MIN_X_VELOCITY, kinematics.getVerticalVelocity())
	 *	 |	  else setVelocity(MIN_X_VELOCITY, kinematics.getVerticalVelocity())
	 *	 | if(getOrientation() == Orientation.NEUTRAL) then setSprite(1)
	 *	 | if (getOrientation() == Orientation.POSITIVE) then setSprite(6)
	 *	 | if (getOrientation() == Orientation.NEGATIVE) then  setSprite(7)
	 *	 | setAcceleration(0.0, kinematics.getVerticalAcceleration())
	 *	 | super.setborders()
	 *
	 */
	public void startDuck() {
		if(kinematics.getXVelocity() != 0) {
			if(kinematics.getXVelocity() < 0) {
				setVelocity(-MIN_X_VELOCITY, kinematics.getYVelocity());
			}else{ 
				setVelocity(MIN_X_VELOCITY, kinematics.getYVelocity());
			}
			kinematics.setXVelocityBounds(MIN_X_VELOCITY, MIN_X_VELOCITY);
		}
		if(getOrientation() == 0)  super.setSprite(1);
		if (getOrientation() == 1) super.setSprite(6);
		if (getOrientation() == -1)  super.setSprite(7);
		setAcceleration(0.0, kinematics.getYAcceleration());
	}
	
	/**
	 * This method stops the duck movement of the alien when he can stop ducking
	 * 
	 * @post The velocity, acceleration and the sprite will be set based on the orientation
	 * 		| if (getOrientation() == Orientation.POSITIVE)  then
	 *		| 	if(canStopDuck()) then super.setSprite(8) else super.setSprite(6)
	 *		|   setVelocity(MIN_X_VELOCITY, 0.0)
	 *		|   setAcceleration(X_ACC, 0.0)
	 *	    | if (getOrientation() == Orientation.NEGATIVE)  then
	 *		|	if(canStopDuck()) then super.setSprite((getSprites().length-8)/2)+8) else super.setSprite(7)
	 *		|   setVelocity(-MIN_X_VELOCITY, 0.0)
	 *		|   setAcceleration(-X_ACC, 0.0)
	 *		| if (getOrientation() == Orientation.NEUTRAL)  then
	 *		|	if(canStopDuck()) then super.setSprite(0) else super.setSprite(1)
	 *		|   setAcceleration(0.0, 0.0)
	 *		|   setVelocity(0.0, 0.0)
	 *		| super.setBorders()
	 *
	 */
	public void endDuck() {
		if(!canStopDuck()) return;
		setSpriteTime(0.0);
		if (getOrientation() == 1) {
			super.setSprite(8);
			setVelocity(MIN_X_VELOCITY, 0.0);
			kinematics.setXVelocityBounds(MIN_X_VELOCITY, MAX_X_VELOCITY);
			setAcceleration(X_ACC, 0.0);
		}
		if (getOrientation() == -1) {
			super.setSprite(((super.getSprites().length-8)/2) + 8);
			setVelocity(-MIN_X_VELOCITY, 0.0);
			kinematics.setXVelocityBounds(-MIN_X_VELOCITY, -MAX_X_VELOCITY);
			setAcceleration(-X_ACC, 0.0);
		}
		if (getOrientation() == 0) {
			super.setSprite(0);
			setAcceleration(0.0, 0.0);
			setVelocity(0.0, 0.0);
		}
	}
	
	/**
	 * This method returns whether the alien can stop ducking
	 * 
	 * @return True if the wanted rectangle will not overlap any GameObject in the World and shall be located passable in the World
	 * 		|  Rectangle endDuck = new Rectangle(new Position<Integer>(0,0), new Dimension(0,0))
	 *		| int toLeft = ((super.getSprites().length-8)/2) + 8
	 *		| if(super.getIndex() == 6) then 
	 *		|	endDuck = new Rectangle(getOrigin().getX(), getOrigin().getY(), super.getSprites()[8].getWidth(), super.getSprites()[8].getHeight())
	 *		| if(super.getIndex() == 7) then 
	 *		|	endDuck = new Rectangle(getOrigin().getX(), getOrigin().getY(), getSprites()[toLeft].getWidth(), getSprites()[toLeft].getHeight())
	 *		| if(super.getIndex() == 1) then
	 *		|	endDuck = new Rectangle(getOrigin().getX(), getOrigin().getY(), super.getSprites()[0].getWidth(), getSprites()[0].getHeight())
	 *		|if(getWorld() != null) then result == isEmpty(overlappingGameObject(endDuck)) && super.getWorld().shallBePassable(endDuck)
	 *		|result == true
	 */
	public boolean canStopDuck() {
		Rectangle endDuck = null;
		int toLeft = ((super.getSprites().length-8)/2) + 8;
		if(super.getIndex() == 6)
			endDuck = new Rectangle(getRectangle().getXCoordinate(), getRectangle().getYCoordinate(), super.getSprites()[8].getWidth(), super.getSprites()[8].getHeight());
		if(super.getIndex() == 7)
			endDuck = new Rectangle(getRectangle().getXCoordinate(), getRectangle().getYCoordinate(), getSprites()[toLeft].getWidth(), getSprites()[toLeft].getHeight());
		if(super.getIndex() == 1)
			endDuck = new Rectangle(getRectangle().getXCoordinate(), getRectangle().getYCoordinate(), super.getSprites()[0].getWidth(), getSprites()[0].getHeight());
		if(getWorld() != null) {
			return isEmpty(overlappingGameObject(endDuck)) && super.getWorld().shallBePassable(endDuck);
		}
		return true;
	}
	
	/**
	 * This method returns whether the alien is ducking or not
	 * 
	 * @return True if and only is the alien is ducking
	 * 		| result == getHeight() == super.getSprites()[1].getHeight() 
	 * 
	 */
	@Basic
	public boolean isDucking() {
		return getImageHeight() == super.getSprites()[1].getHeight();
	}

	/**
	 * This method returns whether the given set does include only plants, because the overlapping Set of Game Object is considered empty
	 * 
	 * @param objects
	 * 			This parameter is used as the set of Game Object
	 * 
	 * @return True if and only if the set does include only plants
	 * 		| for(GameObject object: objects)
	 *		|	if(!(object instanceof Plant)) then result == false
	 *		| result == true
	 * 
	 */
	private boolean isEmpty(Set<Organism> objects) {
		for(GameObject object: objects) {
			if(!(object instanceof Plant)) return false;
		}
		return true;
	}
	
	/**
	 * This method stops the running sprite movement based on the interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * 
	 * @post After Constant.END_MOVE_TIME.getValue() seconds, the sprite will be set as soon as possible
	 * 		| setEndMoveTime(getEndMoveTime() + dt)
	 *		| if(getEndMoveTime() >= Constant.END_MOVE_TIME.getValue()) then
	 *		|	if(super.canStopRun()) then super.setSprite(0) && setEndMoveTime(0.0)
	 *		|	super.getRectangle().setDimension(getWidth(), getHeight())
	 */
	private void arrangeEndMove(double dt) {
		setEndMoveTime(getEndMoveTime() + dt);
		if(getEndMoveTime() >= Constant.MAZUB_END_MOVE_TIME.getValue()) {
			Rectangle endRun = new Rectangle(getRectangle().getOrigin(), getSprites()[0].getWidth(), getSprites()[0].getHeight());
			if(getWorld() == null || (super.getWorld().shallBePassable(endRun) && overlappingGameObject(endRun).isEmpty())) {
				super.setSprite(0);
				setEndMoveTime(0.0);
			}
			super.updateDimension(getImageWidth(), getImageHeight());
		}
	}
	
	/**
	 * This method arrange the sprite change to show the running animation based on the interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * 
	 * @post After each SPRITE_CHANGE seconds the sprite will be updated will running
	 * 		|setSpriteTime(getSpriteTime() + dt)
	 *		|if(getSpriteTime() >= SPRITE_CHANGE) then 
	 *		|	super.setSprite(super.getIndex() + 1) && super.getRectangle().setDimension(getWidth(), getHeight())
	 *		|    && setSpriteTime(getSpriteTime() - SPRITE_CHANGE)
	 *
	 */
	private void arrangeSpriteChange(double dt) {
		setSpriteTime(getSpriteTime() + dt);
		if(getSpriteTime() >= SPRITE_CHANGE) {
			super.setSprite(super.getIndex() + 1);
			super.updateDimension(getImageWidth(), getImageHeight());
			setSpriteTime(getSpriteTime() - SPRITE_CHANGE);
		}
	}

	public void advanceTime(double deltaT) throws IllegalArgumentException{
		Position<Integer> position = new Position<>(getRectangle().getXCoordinate(), getRectangle().getYCoordinate());
		super.advanceTime(deltaT);
		if(getWorld() != null) super.getWorld().updateWindow(position);
		arrangeObjectHit(0.0); 
	}
	
	/**
	 * This method is used to update the state of the alien if the alien can not advance in current state
	 * 
	 * @post If he was running he will stop run and if he was jumping he will stop jumping
	 * 		| if(super.kinematics.getHorizontalAcceleration() == 0.0 && super.kinematics.getVerticalAcceleration() == 0.0) then return
	 *		| if(isRunning() && getOrientation() == Orientation.NEGATIVE.getValue() && !canRunLeft()) then endRun(0.0)
	 *		| if(isRunning() && getOrientation() == Orientation.POSITIVE.getValue() && !canRunRight()) then endRun(0.0)
	 *		| if((isJumping() && !canJump()) || (isFalling() && !canFall())) then endJump(0.0)
	 *
	 */
	protected void setNewState() {
		if(kinematics.getXAcceleration() == 0.0 && kinematics.getYAcceleration() == 0.0) return;
		if(isRunning() && ((getOrientation() == -1 && !canRunLeft())||(getOrientation() == 1 && !canRunRight()))) endRun();
		if((isJumping() && !canJump()) || (isFalling() && !canFall())) endJump();
		arrangeFallNotJumping();
	}

	/**
	 * This method returns if the alien can run to the left
	 * 
	 * @return True if and only if the alien will not be located in another GameObject or in impassable terrain
	 * 		| if(getWorld() == null) result == true
	 *		|result ==  isEmpty(super.overlappingGameObject(getLeftBorder())) && super.getWorld().shallBePassable(getLeftBorder())
	 * 
	 */
	private boolean canRunLeft() {
		if(getWorld() == null) return true;
		return isEmpty(super.overlappingGameObject(getLeftBorder())) && super.getWorld().shallBePassable(getLeftBorder());
	}
	
	/**
	 * This method returns if the alien can run to the right
	 * 
	 * @return True if and only if the alien will not be located in another GameObject or in impassable terrain
	 * 		| if(getWorld() == null) result == true
	 *		|result ==  isEmpty(super.overlappingGameObject(getRightBorder())) && super.getWorld().shallBePassable(getRightBorder())
	 * 
	 */
	private boolean canRunRight() {
		if(getWorld() == null) return true;
		return isEmpty(super.overlappingGameObject(getRightBorder())) && super.getWorld().shallBePassable(getRightBorder());
	}
	
	/**
	 * This method returns if the alien can jump
	 * 
	 * @return True if and only if the alien will not be located in another GameObject or in impassable terrain
	 * 		| if(getWorld() == null) result == true
	 *		|result ==  isEmpty(super.overlappingGameObject(getUpBorder())) && super.getWorld().shallBePassable(getUpBorder())
	 * 
	 */
	@Override
	public boolean canJump() {
		if(getWorld() == null) return true;
		return isEmpty(super.overlappingGameObject(getUpBorder())) && super.getWorld().shallBePassable(getUpBorder());
	}
	
	/**
	 * This method returns if the alien can fall
	 * 
	 * @return True if and only if the alien will not be located in another GameObject or in impassable terrain
	 * 		| if(getWorld() == null) result == true
	 *		|result ==  isEmpty(super.overlappingGameObject(getDownBorder())) && super.getWorld().shallBePassable(getDown())
	 * 
	 */
	public boolean canFall() {
		if(getWorld() == null) return true;
		return isEmpty(super.overlappingGameObject(getDownBorder())) && super.getWorld().shallBePassable(getDownBorder());
	}
	
	/**
	 * This method returns whether the alien can advance in the current state
	 * 
	 * @return If he is running canRunLeft() or canRunRight() will be evaluated based on the orientation 
	 * 		|if(isRunning() && getOrientation() == Orientation.NEGATIVE.getValue()) then result == canRunLeft()	
	 *		|if(isRunning() && getOrientation() == Orientation.POSITIVE.getValue()) then result == canRunRight()
	 *			If he is jumping canJump() will be evaluated or canFall() if he is falling
	 *		|if(isJumping()) then result == canJump()
	 *		|if(isFalling()) result == canFall()
	 */
	protected boolean canMoveInCurrentState() {
		if(isRunning() && getOrientation() == -1) return canRunLeft();
		if(isRunning() && getOrientation() == 1) return canRunRight();
		if(isJumping()) return canJump();
		if(isFalling()) return canFall();
		return false;
	}

	/**
	 * This method is used to arrange the Hit Points because of hit with objects using dt as time
	 * 
	 * @param dt
	 * 			This parameter is used as time
	 * 
	 * @post The Hit Points will be arranged for each colliding object
	 * 		| Set<GameObject> objects = getCollidingObjects()
	 *		| if(getWorld() != null) 
	 *		|	for(GameObject object: objects) 
	 *		|		getWorld().handleImpact(this, object, dt)
	 *
	 */
	protected void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingObjects();
		if(getWorld() != null) {
			for(Organism object : objects){
				int type = getGameObjectType(object);
				switch(type) {
				case 1:arrangeSneezeHit((Sneezewort) object, dt); break;
				case 2:arrangeSkullHit((Skullcab) object, dt); break;
				case 3:arrangeSlimeHit(dt); break;
				case 4:arrangeSharkHit(dt); break;
				case 5:arrangeSpiderHit(dt); break;
				default: break;
				}
			}
		}
	}

	/**
	 * This method is used to advance the alien in the current state over an interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * @post If he is not dead he will advance over interval dt else he will stop his movement
	 * 		|if( !isDead() ) then
	 *		|	if(isMovingVertically()) then jump(dt)
	 *		|	if(isRunning()) then run(dt)
	 *		| else endRun(dt) && endJump(dt)
	 * @post If he is not in the Game World anymore he will be terminated 
	 *		  and if he is just moving the sprite will be updated
	 *		|if(!super.isInside()) then terminate()
	 *		|if(isRunning() && !isDucking() && kinematics.getVerticalVelocity() <= 0) then arrangeSpriteChange(dt)
	 *		
	 */
	protected void arrangeMovement(double dt) {
		if( isDead() ) return;
		if(isMovingVertically()) jump(dt);
		if(isRunning()) run(dt);
		if(!super.isInside()) terminate();
		if(isRunning() && !isDucking() && kinematics.getYVelocity() <= 0) arrangeSpriteChange(dt);
		if(super.getIndex() == 2 || super.getIndex() == 3) arrangeEndMove(dt);
	}
	
	/**
	 * This method arrange the falling movement if the alien is not considered jumping
	 * 
	 * @post If the alien is not jumping and not standing on impassable terrain or another GameObject, the alien will fall
	 * 		|if(getWorld() != null && !isJumping()) 
	 *		|	if(super.getWorld().shallBePassable(getDownBorder()) && 
	 *		|		isEmpty(super.overlappingGameObject(getDownBorder())))
	 *		|		then setAcceleration(kinematics.getHorizontalAcceleration(), Y_ACC)
	 *		|else setAcceleration(kinematics.getHorizontalAcceleration(), 0.0)
	 *
	 */
	private void arrangeFallNotJumping() {
		if(getWorld() != null && !isJumping()) {
			if(super.getWorld().shallBePassable(getDownBorder()) && isEmpty(super.overlappingGameObject(getDownBorder())))
				setAcceleration(kinematics.getXAcceleration(), Y_ACC);
			else setAcceleration(kinematics.getXAcceleration(), 0.0);
		}
	}
	
	/**
	 * This method returns which feature is dominant where the player is situated
	 * 
	 * @return
	 * 		If the world equals null AIR, else which feature is dominant where the player is situated
	 *  	|if(getWorld() == null) result ==  Feature.AIR
	 *		|Feature feat = Feature.AIR;
	 *		|for(int pixelX = super.getOrigin().getX(); pixelX < super.getOrigin().getX() + super.getRectangle().getDimension().getWidth(); pixelX++)
	 *		|	for(int pixelY = super.getOrigin().getY(); pixelY < super.getOrigin().getY() + super.getRectangle().getDimension().getHeight(); pixelY++)
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) then result == Feature.MAGMA
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS) then feat = Feature.GAS
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER && feat != Feature.GAS) then  feat = Feature.WATER
	 *		|result == feat
	 *
	 */
	public Feature getDominantFeature() {
		if(getWorld() == null) return Feature.AIR;
		Feature feature = Feature.AIR;
		for(int pixelX = super.getRectangle().getXCoordinate(); pixelX <= super.getRectangle().getXCoordinate()+super.getRectangle().getWidth()-1; pixelX++) {
			for(int pixelY= super.getRectangle().getYCoordinate(); pixelY <= super.getRectangle().getYCoordinate()+super.getRectangle().getHeight()-1; pixelY++) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) return Feature.MAGMA;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS) feature = Feature.GAS;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER && feature != Feature.GAS) feature = Feature.WATER;
			}
		}
		return feature;
	}
	
	/**
	 * This method returns the End Move Time
	 * 
	 * @return the End Move Time will be returned
	 * 		|result == endMoveTime
	 */
	public double getEndMoveTime() {
		return endMoveTime;
	}

	/**
	 * This method sets the End Move Time
	 * 
	 * @param endMoveTime
	 * 			This parameter is used as time
	 * 
	 * @post The End Move Time will be set
	 * 		|(new this).endMoveTime = endMoveTime
	 */
	public void setEndMoveTime(double endMoveTime) {
		this.endMoveTime = endMoveTime;
	}

	/**
	 * This method returns the Sprite Time
	 * 
	 * @return ...
	 * 		|result.equals(spriteTime)
	 * 
	 */
	@Basic
	public double getSpriteTime() {
		return Double.valueOf(spriteTime);
	}

	/**
	 * This method is used to set the Sprite Time
	 * 
	 * @param spriteTime
	 * 			This parameter is used to set the SpriteTime
	 * 
	 * @post The Sprite Time will be set
	 * 		|(new this).spriteTime = spriteTime
	 * 
	 */
	protected void setSpriteTime(double spriteTime) {
		this.spriteTime = spriteTime;
	}

	public double getEatTime() {
		return Double.valueOf(eatTime);
	}

	public void setEatTime(double eatTime) {
		this.eatTime = eatTime;
	}

	/**
	 * This method returns the width of the current sprite
	 * 
	 * @return the width of the current sprite will be returned
	 * 		| result == super.getSprite().getWidth()
	 */
	@Basic 
	public int getImageWidth() {
		return super.getSprite().getWidth();
	}
	
	/**
	 * This method returns the height of the current sprite
	 * 
	 * @return the height of the current sprite will be returned
	 * 		| result == super.getSprite().getHeight()
	 */
	@Basic 
	public int getImageHeight() {
		return super.getSprite().getHeight();
	}
	
	/**
	 * This method is used to update the left movement using deltaT as time
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time
	 * 
	 * @post The x-coordinate on the x-component of the velocity will be updated when not ducking
	 * 		| updateX(deltaT)
	 * 		|if(!isDucking()) then super.getVelocity().accelerateX(getAcceleration(), deltaT)
	 */
	@Override @Raw
	public void run(double deltaT) {
		updateX(deltaT); 
		if(!isDucking())kinematics.updateXVelocity(deltaT);
	}


	/**
	 * This method is used to update the vertical movement using deltaT as time
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time
	 * 
	 * @post The y-coordinate and the y-component of the velocity will be changed
	 * 		| updateY(deltaT) && super.getVelocity().accelerateY(getAcceleration(), deltaT)			
	 */
	@Override @Raw
	public void jump(double deltaT){
		updateY(deltaT);  
		kinematics.updateYVelocity(deltaT);
	}

	public void arrangeSlimeHit(double dt) {
		if(getBlockTime() == 0) updateHitPoints((int) Constant.MAZUB_SLIME.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) setBlockTime(0.0);
	}
	
	public void arrangeSneezeHit(Sneezewort sneezewort, double dt) {
		if(getPoints() < getHitPoints().getMaximum() ) { 
			if(sneezewort.isDead()) updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			else updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
			sneezewort.terminate();
		}
	}
	
	public void arrangeSkullHit(Skullcab skullcab, double dt) {
		if(skullcab.getHitTime() == 0 && skullcab.getPoints() != 0 && getPoints() < getHitPoints().getMaximum()) {
			if(skullcab.isDead()) updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			else updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
		}
		skullcab.arrangeEat(dt);
		
	}
	
	public void arrangeSharkHit(double dt) {
		if(getBlockTime() == 0) updateHitPoints((int) Constant.MAZUB_SHARK.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}

	public void arrangeSpiderHit(double dt) {
		if(getBlockTime() == 0) updateHitPoints((int) Constant.MAZUB_SPIDER.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}
	
	public void arrangeFeatureHit(double time) {
		if(getWorld() == null || isDead()) return;
		Feature feature = getDominantFeature();
		if(feature != previousFeature) featureTime = 0;
		if(feature == Feature.MAGMA) handleMagmaHit(time);
		if(feature == Feature.GAS) handleGasHit(time);
		if(feature == Feature.WATER) handleWaterHit(time);
		this.previousFeature = feature;
		if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) 
			featureTime -= Constant.MAZUB_FEATURE_TIME.getValue();
	}

	public void handleMagmaHit(double time) {
		if(featureTime == 0.0 && previousFeature != Feature.MAGMA) {
			updateHitPoints((int) Constant.MAZUB_MAGMA.getValue());
			featureTime += time;
		}else {
			featureTime += time; 
			if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) 
				updateHitPoints((int) Constant.MAZUB_MAGMA.getValue());
		}
	}

	public void handleGasHit(double time) {
		if(featureTime == 0.0 && previousFeature != Feature.GAS) {
			updateHitPoints((int) Constant.MAZUB_GAS.getValue());
			featureTime += time;
		}else {
			featureTime += time; 
			if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) 
				updateHitPoints((int) Constant.MAZUB_GAS.getValue());
		}
	}

	public void handleWaterHit(double time) {
		featureTime += time; 
		if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) 
			updateHitPoints((int) Constant.MAZUB_WATER.getValue());
	}

	@Override
	protected void arrangeInitialMovement() {
		if(!isJumping()) arrangeFallNotJumping();
	}


}
