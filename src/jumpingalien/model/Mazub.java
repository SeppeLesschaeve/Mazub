package jumpingalien.model;

import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
import be.kuleuven.cs.som.taglet.*;
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
		super.getRectangle().getOrigin().setPosition(x, y);
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
		kinematics.setHorizontalVelocity(x);
		kinematics.setVerticalVelocity(y);
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
		kinematics.setHorizontalAcceleration(x);
		kinematics.setVerticalAcceleration(y);
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
		super.setSprite(8);
		setAcceleration(X_ACC, kinematics.getVerticalAcceleration());
		setVelocity( MIN_X_VELOCITY , kinematics.getVerticalVelocity());
		kinematics.setBoundsOfHorizontalBoundaries(MIN_X_VELOCITY, MAX_X_VELOCITY);
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
		setAcceleration(-X_ACC, kinematics.getVerticalAcceleration());
		setVelocity( -MIN_X_VELOCITY , kinematics.getVerticalVelocity());
		kinematics.setBoundsOfHorizontalBoundaries(MIN_X_VELOCITY, MAX_X_VELOCITY);
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
		setVelocity(0.0, kinematics.getVerticalVelocity());
		setAcceleration(0.0, kinematics.getVerticalAcceleration());
		kinematics.setBoundsOfHorizontalBoundaries(0.0, 0.0);
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
		setVelocity(kinematics.getHorizontalVelocity(), Y_VELOCITY);
		setAcceleration(kinematics.getHorizontalAcceleration(), Y_ACC);
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
		if(kinematics.getVerticalVelocity() > 0) setVelocity(kinematics.getHorizontalVelocity(), 0.0);
		if(getOrientation() == -1) super.setSprite(((super.getSprites().length-8)/2) +8);
		if(getOrientation() == 1) super.setSprite(8);
		if(getOrientation() == 0) super.setSprite(0);
		setAcceleration(kinematics.getHorizontalAcceleration(), 0.0);
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
		return kinematics.getVerticalVelocity() > 0;
	}
	
	/**
	 * This method returns whether the alien is falling or not
	 * 
	 * @return True if and only is the alien is moving downwards
	 * 		| result == super.kinematics.getVerticalVelocity() <= 0 && super.kinematics.getVerticalAcceleration() != 0
	 */
	@Basic
	public boolean isFalling() {
		return kinematics.getVerticalVelocity() <= 0 && kinematics.getVerticalAcceleration() != 0;
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
		if(kinematics.getHorizontalVelocity() != 0) {
			if(kinematics.getHorizontalVelocity() < 0) {
				setVelocity(-MIN_X_VELOCITY, kinematics.getVerticalVelocity());
			}else{ setVelocity(MIN_X_VELOCITY, kinematics.getVerticalVelocity());}
			kinematics.setBoundsOfHorizontalBoundaries(MIN_X_VELOCITY, MIN_X_VELOCITY);
		}
		if(getOrientation() == 0)  super.setSprite(1);
		if (getOrientation() == 1) super.setSprite(6);
		if (getOrientation() == -1)  super.setSprite(7);
		setAcceleration(0.0, kinematics.getVerticalAcceleration());
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
			if(canStopDuck()) super.setSprite(8); else super.setSprite(6);
			setVelocity(MIN_X_VELOCITY, 0.0);
			kinematics.setBoundsOfHorizontalBoundaries(MIN_X_VELOCITY, MAX_X_VELOCITY);
			setAcceleration(X_ACC, 0.0);
		}
		if (getOrientation() == -1) {
			if(canStopDuck()) super.setSprite(((super.getSprites().length-8)/2) + 8); else super.setSprite(7);
			setVelocity(-MIN_X_VELOCITY, 0.0);
			kinematics.setBoundsOfHorizontalBoundaries(MIN_X_VELOCITY, MAX_X_VELOCITY);
			setAcceleration(-X_ACC, 0.0);
		}
		if (getOrientation() == 0) {
			if(canStopDuck()) super.setSprite(0); else super.setSprite(1);
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
			if(super.canStopRun()) {
				super.setSprite(0);
				setEndMoveTime(0.0);
			}
			super.getRectangle().setDimension(getImageWidth(), getImageHeight());
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
			super.getRectangle().setDimension(getImageWidth(), getImageHeight());
			setSpriteTime(getSpriteTime() - SPRITE_CHANGE);
		}
	}

	public void advanceTime(double deltaT) throws IllegalArgumentException{
		Position<Integer> position = new Position<>(getRectangle().getXCoordinate(), getRectangle().getYCoordinate());
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT >= 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		if(!isJumping()) needToFallNotJumping();
		for(double time = 0.0, dt = super.updateDt(deltaT, time); time < deltaT; time += dt, dt = super.updateDt(deltaT, time)) {
			if(isDead()) super.setDelay(getDelay() + dt); 
			if(getDelay() >= REMOVE_DELAY) terminate();
			arrangeFeatureHit(dt);
			arrangeObjectHit(dt);
			if(!canMoveInCurrentState()) setNewState();
			arrangeMovement(dt);
			if(super.getIndex() == 2 || super.getIndex() == 3) arrangeEndMove(dt);
		}
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
	private void setNewState() {
		if(kinematics.getHorizontalAcceleration() == 0.0 && kinematics.getVerticalAcceleration() == 0.0) return;
		if(isRunning() && getOrientation() == -1 && !canRunLeft()) endRun(0.0);
		if(isRunning() && getOrientation() == 1 && !canRunRight()) endRun(0.0);
		if((isJumping() && !canJump()) || (isFalling() && !canFall())) endJump(0.0);
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
	@Override
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
	private boolean canMoveInCurrentState() {
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
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 1:arrangeSneezeHit((Sneezewort) object, dt); break;
				case 2:arrangeSkullHit((Skullcab) object, dt); break;
				case 3:arrangeSlimeHit(dt); break;
				case 4:arrangeSharkHit(dt); break;
				case 5:arrangeSpiderHit(); break;
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
	private void arrangeMovement(double dt) {
		if( !isDead() ) {
			if(isMovingVertically()) jump(dt);
			if(isRunning()) run(dt);
		}else{
			endRun(dt); endJump(dt);
		}
		if(!super.isInside()) terminate();
		if(isRunning() && !isDucking() && kinematics.getVerticalVelocity() <= 0) arrangeSpriteChange(dt);
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
	private void needToFallNotJumping() {
		if(getWorld() != null && !isJumping()) {
			if(super.getWorld().shallBePassable(getDownBorder()) && 
					isEmpty(super.overlappingGameObject(getDownBorder())))
					setAcceleration(kinematics.getHorizontalAcceleration(), Y_ACC);
			else setAcceleration(kinematics.getHorizontalAcceleration(), 0.0);
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
	 * This method is used to arrange the Hit Points of the alien because of features using dt as time to determine whether Hit Points are to be changed.
	 *
	 * @param dt
	 * 			This parameter is used as time
	 * @effect If the alien is attached to a world and he the alien is not considered dead, the world will handle the feature hit
	 * 		| if getWorld() != null && !isDead() then getWorld().handleFeatureHit(this, dt)
	 *
	 */
	private void arrangeFeatureHit(double dt) {
		if(getWorld() != null && !isDead()) {
			getWorld().handleFeatureHit(this, dt);
		}
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
		if(!isDucking())kinematics.updateHorizontalVelocity(deltaT);
	}

	/**
	 * This method is used to stop the horizontal movement
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented from interface Run
	 * 
	 * @post The sprite will be set and the velocity as well as the acceleration to zero
	 * 		| getAcceleration().setX(0.0) && getVelocity().setX(0.0)
	 * 		| if(getOrientation() == Orientation.POSITIVE.getValue()) then setSprite(2)
	 *		| if(getOrientation() == Orientation.NEGATIVE.getValue()) then setSprite(3)
	 */
	@Override
	public void endRun(double deltaT) {
		if(getOrientation() == 1) setSprite(2);
		if(getOrientation() == -1) setSprite(3);
		kinematics.setHorizontalAcceleration(0.0);
		kinematics.setHorizontalVelocity(0.0);
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
		updateY(deltaT);  kinematics.updateVerticalVelocity(deltaT);
	}

	/**
	 * This method is used to stop the vertical movement
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented from interface Jump
	 * 
	 * @post The velocity and acceleration will be set based on the fact that he is jumping or not
	 * 		| if(kinematics.getVerticalVelocity() > 0) then getVelocity().setY(0.0)
	 *		| else getAcceleration().setY(0.0) && getVelocity().setY(0.0)
	 */
	@Override @Raw
	public void endJump(double deltaT){
		if(kinematics.getVerticalVelocity() > 0) 
			kinematics.setVerticalVelocity(0.0);
		else{ 
			kinematics.setVerticalAcceleration(0.0);
			kinematics.setVerticalVelocity(0.0);
		}
	}
	
	public void arrangeSlimeHit(double dt) {
		if(isRunning() && getBlockTime() == 0) setHitPoints((int) Constant.MAZUB_SLIME.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) setBlockTime(0.0);
	}
	
	public void arrangeSneezeHit(Sneezewort sneezewort, double dt) {
		if(getHitPoints() < hitPoint.getMaximum() && getRectangle().overlaps(sneezewort.getRectangle())) { 
			if(sneezewort.getAge() < Plant.SNEEZE_AGE && sneezewort.getHit() > 0) {
				setHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
			}else if(sneezewort.getAge() >= Plant.SNEEZE_AGE && sneezewort.getHit() > 0) {
				setHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			}
			sneezewort.terminate();
		}
	}
	
	public void arrangeSkullHit(Skullcab skullcab, double dt) {
		if(!skullcab.getRectangle().overlaps(getRectangle())) {
			skullcab.setHitTime(0); return;
		}
		if(skullcab.getHitTime() == 0 && skullcab.getHit() != 0 && getHitPoints() < hitPoint.getMaximum() ) {
			if(skullcab.isDead()) {
				setHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			}else {
				setHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
			}
			skullcab.getHitPoint().setPoints(-1);
		}
		skullcab.setHitTime(skullcab.getHitTime() + dt);
		if(skullcab.getHitTime()  >= 0.6) {
			skullcab.setHitTime(0);
		}
		if(skullcab.getHit() == 0) skullcab.terminate();
	}
	
	public void arrangeSharkHit(double dt) {
		if(getBlockTime() == 0) setHitPoints((int) Constant.MAZUB_SHARK.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}

	public void arrangeSpiderHit() {
		setHitPoints((int) Constant.MAZUB_SPIDER.getValue());
	}

}
