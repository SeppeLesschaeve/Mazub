package jumpingalien.model;

import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

/**
 * This class builds a shark that can run and jump 
 * the movement of this shark starts orientated to the left and will move for 0.5 seconds, 
 * then one second resting that means that the shark will not move horizontally,
 * then 0.5 seconds to the right and then again one second of rest;
 * this movement will be repeated until the shark is dead
 * 
 * @invar ...
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public class Shark extends Creature implements TwoDimensionMovable{
	
	private double featureTime = 0.0;
	private double mazubTime = 0.0;
	private double moveTime = 0.0;
	private double restTime = 0.0;
	private int newIndex = 1;
	
	/**This constructor will set the initial Pixel Position, Actual Position, Dimension and the images to show the animation
	 * 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 		| (sprites.length != 3 || for (int index = 0; index < sprites.length; index++) sprites[index] == null)
	 * 
	 * @effect ...
	 * 		|super(pixelLeftX, 100, 0, Integer.MAX_VALUE, pixelBottomY, sprites)
	 * @post ...
	 * 		|super.getAcceleration().setX(-1.5)
	 *		|super.getAcceleration().setY(Y_ACC)
	 * 
	 */
	public Shark(int pixelLeftX, int pixelBottomY, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites);
		if(sprites.length != 3) {
			throw new IllegalArgumentException("You can not have more or less than three sprites for a shark");
		}
		kinematics.setXVelocityBounds(0.0, Double.POSITIVE_INFINITY);
		kinematics.setXAcceleration(-1.5);
		kinematics.setYAcceleration(-10.0);
	}
	
	/**
	 * This method returns Out Water Time
	 * 
	 * @return ...
	 * 		|result == outWaterTime
	 * 
	 */
	@Basic
	public double getOutWaterTime() {
		return featureTime;
	}

	/**
	 * This method is used to set the Out Water Time
	 * 
	 * @param outWaterTime
	 * 			This parameter is used to set the Out Water Time
	 * 
	 * @post ...
	 * 		|(new this).outWaterTime = outWaterTime
	 * 
	 */
	protected void setOutWaterTime(double outWaterTime) {
		this.featureTime = outWaterTime;
	}
	
	public double getMazubTime() {
		return mazubTime;
	}

	public void setMazubTime(double mazubTime) {
		this.mazubTime = mazubTime;
	}

	/**
	 * This method returns Move Time
	 * 
	 * @return ...
	 * 		|result == moveTime
	 * 
	 */
	@Basic
	public double getMoveTime() {
		return moveTime;
	}
	
	/**
	 * This method is used to set the Move Time
	 * 
	 * @param moveTime
	 * 			This parameter is used to set the Move Time
	 * 
	 * @post ...
	 * 		|(new this).moveTime = moveTime
	 * 
	 */
	protected void setMoveTime(double moveTime) {
		this.moveTime = moveTime;
	}
	
	/**
	 * This method returns Rest Time
	 * 
	 * @return ...
	 * 		|result == restTimer
	 * 
	 */
	@Basic
	public double getRestTime() {
		return restTime;
	}

	/**
	 * This method is used to set the Rest Time
	 * 
	 * @param restTime
	 * 			This parameter is used to set the Rest Time
	 * 
	 * @post ...
	 * 		|(new this).restTime = restTime
	 * 
	 */
	protected void setRestTime(double restTime) {
		this.restTime = restTime;
	}
	
	/**
	 * This method returns the new index
	 * 
	 * @return ...
	 * 		| result == newIndex
	 */
	public int getNewIndex() {
		return newIndex;
	}

	/**
	 * This method sets the new index
	 * 
	 * @post ...
	 * 		| if(getNewIndex() == 1) then (new this).newIndex = 2
	 *		|else (new this).newIndex = 1
	 *		|super.setSprite(newIndex)
	 */
	public void setNewIndex() {
		if(newIndex == 1) this.newIndex = 2;
		else if(newIndex == 2) this.newIndex = 1;
		else this.newIndex = 0;
		super.setSprite(newIndex);
	}
	
	@Override
	protected boolean canMoveInCurrentState() {
		return canJumpInCurrentState() && canRunInCurrentState();
	}
	
	@Override
	protected void setNewState() {
		if(isMoving()) endRun();
		if(isJumping()) endJump();
	}
	
	/**
	 * This method is returns whether the shark is located in water or not
	 * 
	 * @return ...
	 * 		| if(getWorld() == null) then result == false
	 *		|for(int pixelX = super.getOrigin().getX(); pixelX < super.getOrigin().getX()+super.getRectangle().getDimension().getWidth(); pixelX++)
	 *		|	for(int pixelY= super.getOrigin().getY(); pixelY < super.getOrigin().getY()+super.getRectangle().getDimension().getHeight(); pixelY++)
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) then result == true
	 *		|result == false
	 *
	 */
	@Basic
	public boolean swimsInWater() {
		if(getWorld() == null) return false;
		int tileLength = super.getWorld().getTileLength();
		int newX = 0;
		int newY = 0;
		for(int pixelX = super.getRectangle().getX(); pixelX < super.getRectangle().getX()+super.getRectangle().getWidth()-1; pixelX+=newX) {
			for(int pixelY= super.getRectangle().getY(); pixelY < super.getRectangle().getY()+super.getRectangle().getHeight()-1; pixelY+=newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) return true;
				newY = Math.min(tileLength, super.getRectangle().getY() + super.getRectangle().getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, super.getRectangle().getX() + super.getRectangle().getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return false;
	}
	
	public boolean isFullySwimming() {
		if(getWorld() == null) return false;
		Rectangle upper = new Rectangle(getRectangle().getX(), getRectangle().getY()+getRectangle().getHeight()-1,getRectangle().getWidth(),1);
		int tileLength = super.getWorld().getTileLength();
		int newX = 0;
		int newY = 0;
		for(int pixelX = upper.getX(); pixelX < upper.getX()+upper.getWidth()-1; pixelX+=newX) {
			for(int pixelY= upper.getY(); pixelY < upper.getY()+upper.getHeight()-1; pixelY+=newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) != Feature.WATER) return false;
				newY = Math.min(tileLength, super.getRectangle().getY() + super.getRectangle().getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, super.getRectangle().getX() + super.getRectangle().getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return true;
		
	}
	
	/**
	 * This method is used to arrange the hitPoints because of being out of water based on interval dt
	 *
	 * @param dt
	 * 		This parameter is used as interval
	 * 
	 * @post ...
	 * 		| if(!swimsInWater()) then 
	 *		|	setOutWaterTime(getOutWaterTime() + dt)
	 *		|	and if(getOutWaterTime() >= Constant.SHARK_OUT_WATER_TIME.getValue()) then 
	 *		|		super.getHitPoint().setPoint(Constant.SHARK_OUT_WATER.getValue()) && setOutWaterTime(0.0)
	 *		| else setOutWaterTime(0.0)
	 * 
	 */
	protected void arrangeFeatureHit(double dt) {
		if(!swimsInWater()) {
			setOutWaterTime(getOutWaterTime() + dt);
			if(getOutWaterTime() >= Constant.SHARK_OUT_WATER_TIME.getValue()) {
				super.updateHitPoints((int) Constant.SHARK_OUT_WATER.getValue());
				setOutWaterTime(0.0);
			}
		}else {
			setOutWaterTime(0.0);
		}
	}

	/**
	 * This method is used to arrange the Hit Points because of hit with objects using dt as time
	 * 
	 * @param dt
	 * 			This parameter is used as time
	 * 
	 * @post ...
	 * 		| Set<GameObject> objects = getCollidingObjects()
	 *		| if(getWorld() != null) 
	 *		|	for(GameObject object: objects) 
	 *		|		if(object instanceof Shark && object.getBlockTime() == 0) then arrangeSwitch(this)
	 *		|		else getWorld().handleImpact(this, object, dt)
	 *
	 */
	protected void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingCreatures();
		if(mazubTime != 0) setMazubTime(mazubTime + dt);
		if(getWorld() != null) {
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 0:if(!object.isDead())arrangeMazubHit(dt);break;
				case 3:if(!object.isDead())arrangeSlimeHit();break;
				case 4:arrangeSwitch(this);break;
				default: break;
				}
			}
		}
	}
	
	public void arrangeMazubHit(double dt) {
		if(mazubTime == 0) {
			updateHitPoints((int) Constant.SHARK_MAZUB.getValue());
			setMazubTime(mazubTime + dt);
		}
	}
	
	public void arrangeSlimeHit() {
		updateHitPoints((int) Constant.SHARK_SLIME.getValue());
	}
	
	/**
	 * This method is used to advance the shark in the current state over interval
	 * 
	 * @param dt
	 * 		This parameter is used as interval
	 * 
	 * @post ...
	 * 		| if(isDead()) then return
	 * @post ...
	 * 		|if(isRunning()) then if(canRun()) then run(dt) else endRun(dt)
	 *		|if(isJumping()) then if(canJump()) then jump(dt) else endJump(dt)
	 * @post ...
	 * 		| if(getMoveTime() < Constant.SHARK_SWITCH_TIME.getValue()) then setMoveTime(getMoveTime() + dt)
	 *		| else 
	 *		|	if(getRestTime() < Constant.REST_TIME.getValue()) then setRestTime(getRestTime() + dt)
	 *		|	super.setSprite(0) && super.getAcceleration().setX(0.0) && super.getVelocity().setX(0.0)
	 *		|	if(getRestTime() >= Constant.REST_TIME.getValue()) then
	 *		|		arrangeNewMoveTime() && setRestTime(0.0) && setMoveTime(0.0)
	 * @post ...
	 * 		| if(!super.isInside()) then terminate()
	 */
	protected void arrangeMovement(double dt) {
		if(kinematics.isStationary() && moveTime == 0 && canStartJump()) startJump();
		if(kinematics.isStationary() && canStartFall()) startFall();
		if(isMoving()) run(dt);
		if(isJumping()) jump(dt);
		if(getMoveTime() < Constant.SHARK_SWITCH_TIME.getValue()) {
			setMoveTime(getMoveTime() + dt);
		}else {
			if(getRestTime() < Constant.SHARK_REST_TIME.getValue()) setRestTime(getRestTime() + dt);
			super.setSprite(0); 
			kinematics.setXAcceleration(0.0); 
			kinematics.setXVelocity(0.0);
			if(getRestTime() >= Constant.SHARK_REST_TIME.getValue()) {
				arrangeNewMoveTime();
				setRestTime(0.0); 
				setMoveTime(0.0);
			}
		}
		if(!super.isInside()) terminate();
	}

	/**
	 * This method is used to arrange the new movement
	 * 
	 * @effect ...
	 * 		|setNewIndex()
	 * @post ...
	 * 		|if(getNewIndex() == 2) then super.getAcceleration().setX(1.5) 
	 *		|else super.getAcceleration().setX(-1.5)
	 *		|if(canStartJump()) then super.getAcceleration().setY(-10.0) && super.getVelocity().setY(Y_VEL)
	 */
	private void arrangeNewMoveTime() {
		setNewIndex();
		if(newIndex == 2) startRunRight();
		else if(newIndex == 1)startRunLeft();
		if(canStartJump() && !isGoingDown()) startJump();
	}
	
	/**
	 * This method is used to block this shark and another shark
	 * 
	 * @param shark
	 * 			This parameter is used as other shark
	 * 
	 * @post ...
	 * 		| shark.getAcceleration().setX(0.0) && shark.setNewIndex() && shark.getVelocity().setX(0.0)
	 * @post ...
	 * 		| this.getAcceleration().setX(0.0) && this.setNewIndex() && this.getVelocity().setX(0.0)
	 */
	private void arrangeSwitch(Shark shark) {
		if(shark.isRunningLeft()) {
			shark.startRunLeft(); 
			startRunRight();
		}else {
			startRunLeft(); 
			shark.startRunRight();
		}
		shark.setNewIndex();
		setNewIndex();
	}

	@Override
	public boolean canStartJump() {
		return swimsInWater()  || !super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean canJump() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder());
	}

	@Override
	public boolean isGoingUp() {
		return kinematics.getYVelocity() > 0 && kinematics.getYAcceleration() < 0;
	}

	@Override
	public boolean canStartFall() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return false;
		return super.getCollidingCreatures(getDownBorder()).isEmpty()  && super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean canFall() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getDownBorder()).isEmpty() && super.getWorld().shallBePassable(getDownBorder()) && !isFullySwimming();
	}

	@Override
	public boolean isGoingDown() {
		return kinematics.getYVelocity() <= 0 && kinematics.getYAcceleration() < 0;
	}

	@Override
	public void startJump() {
		if(!canStartJump() || isJumping() || isDead()) throw new IllegalStateException("Shark can not start jumping if he did jump before it.");
		kinematics.setYVelocity(2.0);
		kinematics.setYAcceleration(-10.0);
	}

	@Override
	public void endGoingUp() {
		if(kinematics.getYVelocity() > 0) kinematics.setYVelocity(0.0);
		kinematics.setYAcceleration(0.0);	
	}

	@Override
	public void startFall() {
		if(!canStartFall() || isDead()) throw new IllegalStateException();
		kinematics.setYVelocity(0.0);
		kinematics.setYAcceleration(-10.0);
	}

	@Override
	public void endGoingDown() {
		kinematics.setYVelocity(0.0);
		kinematics.setYAcceleration(0.0);
	}

	@Override
	public void jump(double deltaT) {
		super.updateVerticalComponent(this.getPosition().getY() + (kinematics.getYVelocity()*deltaT) + (kinematics.getYAcceleration()*deltaT*deltaT/2));
		kinematics.updateYVelocity(deltaT);	
	}

	@Override
	public boolean canStartRunRight() {
		return canRunRight();
	}

	@Override
	public boolean canRunRight() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
	}

	@Override
	public boolean isRunningRight() {
		return kinematics.getXVelocity() > 0 || kinematics.getXAcceleration() > 0;
	}

	@Override
	public boolean canStartRunLeft() {
		return canRunLeft();
	}

	@Override
	public boolean canRunLeft() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
	}

	@Override
	public boolean isRunningLeft() {
		return kinematics.getXVelocity() < 0 || kinematics.getXAcceleration() < 0;
	}

	@Override
	public void startRunRight() {
		assert(!isMoving() && !isDead() && canStartRunRight());
		kinematics.setXVelocity( 1.0);
		kinematics.setXVelocityBounds(1.0, 3.0);
		kinematics.setXAcceleration(0.9);	
	}

	@Override
	public void endRunRight() {
		assert(isRunningRight() &&  !isDead());
		kinematics.setXVelocity(0.0);
		kinematics.setXVelocityBounds(0.0, 0.0);
		kinematics.setXAcceleration(0.0);
	}

	@Override
	public void startRunLeft() {
		assert(!isMoving() && !isDead() && canStartRunLeft());
		kinematics.setXVelocity( -1.0);
		kinematics.setXVelocityBounds(-1.0, -3.0);
		kinematics.setXAcceleration(-0.9);
	}

	@Override
	public void endRunLeft() {
		assert(isRunningLeft() &&  !isDead());
		kinematics.setXVelocity(0.0);
		kinematics.setXVelocityBounds(0.0, 0.0);
		kinematics.setXAcceleration(0.0);
	}

	@Override
	public void run(double deltaT) {
		super.updateHorizontalComponent(this.getPosition().getX() + (kinematics.getXVelocity()*deltaT)+ (kinematics.getXAcceleration()*deltaT*deltaT/2));
		kinematics.updateXVelocity(deltaT);
	}
	
}
