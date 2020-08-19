package jumpingalien.model;

import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
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
public class Shark extends Creature implements Run, Jump{
	
	private double moveTime= 0.0;
	private double outWaterTime =0.0;
	private double restTime = 0.0;
	private int newIndex = 1;
	private static final  double X_ACC = 1.5;
	private static final double Y_ACC = -10.0;
	private static final double Y_VEL = 2.0;
	
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
	 * 		|super.getAcceleration().setX(-X_ACC)
	 *		|super.getAcceleration().setY(Y_ACC)
	 * 
	 */
	public Shark(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws IllegalArgumentException{
		super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites);
		for(int index = 0; index < sprites.length; index++) {
			if(sprites[index] == null) {
				throw new IllegalArgumentException("One of the given sprites is null and so can not be used");
			}
		}
		if(sprites.length != 3) {
			throw new IllegalArgumentException("You can not have more or less than three sprites for a shark");
		}
		kinematics.setXVelocityBounds(0.0, Double.POSITIVE_INFINITY);
		kinematics.setXAcceleration(-X_ACC);
		kinematics.setYAcceleration(Y_ACC);
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
	 * This method returns Out Water Time
	 * 
	 * @return ...
	 * 		|result == outWaterTime
	 * 
	 */
	@Basic
	public double getOutWaterTime() {
		return outWaterTime;
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
		this.outWaterTime = outWaterTime;
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
		if(getNewIndex() == 1) this.newIndex = 2;
		else this.newIndex = 1;
		super.setSprite(newIndex);
	}
	
	/**
	 * This method is used to arrange the vertical movement using deltaT as time
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time
	 * 
	 * @post ...
	 * 		|super.updateY(deltaT) && super.getVelocity().accelerateY(getAcceleration(), deltaT)
	 */
	@Override @Raw
	public void jump(double deltaT){
		super.updateY(deltaT); 
		kinematics.updateYVelocity(deltaT);
	}

	/**
	 * This method is used to stop the vertical movement
	 * 
	 * @param dt
	 * 			This parameter is unused but must be implemented from interface Jump
	 * 
	 * @post ...
	 * 		| if(kinematics.getVerticalVelocity() > 0) then getVelocity().setY(0.0)
	 *		| else getAcceleration().setY(0.0) && getVelocity().setY(0.0)
	 */
	@Raw
	public void endJump(){
		if(kinematics.getYVelocity() > 0.0) {
			kinematics.setYVelocity(0.0);
		}else {
			kinematics.setYAcceleration(0.0);
			kinematics.setYVelocity(0.0);
		}
	}

	/**
	 * This method is used to arrange the horizontal movement using deltaT as time
	 * 
	 * @param deltaT
	 * 			This parameter is used as time
	 * @post ...
	 * 		| super.updateX(deltaT) && super.getVelocity().accelerateX(getAcceleration(), deltaT)
	 *		|if(kinematics.getHorizontalVelocity() > 0) then super.setSprite(2)
	 *		|else if(kinematics.getHorizontalVelocity() < 0) then super.setSprite(1)
	 */
	@Override @Raw
	public void run(double deltaT) {
		super.updateX(deltaT); 
		kinematics.updateXVelocity(deltaT);
		if(kinematics.getXVelocity() > 0) super.setSprite(2);
		else if(kinematics.getXVelocity() < 0) super.setSprite(1);
	}

	/**
	 * This method is used to end the movement
	 * 
	 * @param dt
	 * 			This parameter is unused but must be implemented from interface Run
	 * 
	 * @post ...
	 * 		| super.getVelocity().setX(0.0) && super.getAcceleration().setX(0.0)
	 */
	public void endRun() {
		kinematics.setXAcceleration(0.0);
		kinematics.setXVelocity(0.0);
	}

	/**
	 * This method is used to update the state of the shark over an interval
	 * 
	 * @param deltaT
	 * 			This parameter is used as interval
	 *
	 * @throws IllegalArgumentException
	 * 			...
	 * 		| (Double.isNaN(deltaT) || deltaT < 0 || deltaT >= 0.2 || Double.isInfinite(deltaT))
	 * @post ...
	 * 		|if(getMoveTime() == 0 &&  getRestTime() == 0) then 
	 *		|	super.setSprite(1) && super.getAcceleration().setY(Y_ACC) 
	 *		|	and if(getWorld() == null || (!super.getWorld().shallBePassable(getDownBorder()) || swimsInWater())) 
	 *		|	then super.getVelocity().setY(Y_VEL) else super.getVelocity().setY(0.0)
	 * @post ...
	 * 		| if(mustFall()) then super.getAcceleration().setY(Y_ACC) 
	 *		| for(double time = 0.0, dt = super.updateDt(deltaT, time); time < deltaT; time += dt, dt = super.updateDt(deltaT, time)) 
	 *		|	if(isDead()) then super.setDelay(getDelay() + dt) 
	 *		|	if(getDelay() >= REMOVE_DELAY) then terminate()
	 *		|	arrangeFeatureHit(dt)
	 *		|	arrangeObjectHit(dt)
	 *		|	arrangeMovement(dt)
	 *		|	setBorders()
	 * 
	 */
	@Override
	public void advanceTime(double deltaT) throws IllegalArgumentException{
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT >= 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		if(getMoveTime() == 0 &&  getRestTime() == 0) {
			super.setSprite(1); kinematics.setYAcceleration(Y_ACC);
			if(getWorld() == null || (!super.getWorld().shallBePassable(getDownBorder()) || swimsInWater())) 
				kinematics.setYVelocity(Y_VEL); else kinematics.setYVelocity(0.0);
		}
		if(mustFall()) kinematics.setYAcceleration(Y_ACC);
		for(double time = 0.0, dt = super.updateDt(deltaT, time); time < deltaT; time += dt, dt = super.updateDt(deltaT, time)) {
			if(isDead()) super.setDelay(getDelay() + dt); 
			if(getDelay() >= REMOVE_DELAY) terminate();
			arrangeFeatureHit(dt);
			arrangeObjectHit(dt);
			arrangeMovement(dt);
		}
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
	private void arrangeMovement(double dt) {
		if(isDead()) { return;}
		if(isRunning()) { if(canRun()) run(dt); else endRun();}
		if(isJumping()) { if(canJump()) jump(dt); else endJump();}
		if(getMoveTime() < Constant.SHARK_SWITCH_TIME.getValue()) {
			setMoveTime(getMoveTime() + dt);
		}else {
			if(getRestTime() < Constant.SHARK_REST_TIME.getValue()) setRestTime(getRestTime() + dt);
			super.setSprite(0); 
			kinematics.setXAcceleration(0.0); 
			kinematics.setXVelocity(0.0);
			if(getRestTime() >= Constant.SHARK_REST_TIME.getValue()) {
				arrangeNewMoveTime();
				setRestTime(0.0); setMoveTime(0.0);
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
	 * 		|if(getNewIndex() == 2) then super.getAcceleration().setX(X_ACC) 
	 *		|else super.getAcceleration().setX(-X_ACC)
	 *		|if(canStartJump()) then super.getAcceleration().setY(Y_ACC) && super.getVelocity().setY(Y_VEL)
	 */
	private void arrangeNewMoveTime() {
		setNewIndex();
		if(getNewIndex() == 2) kinematics.setXAcceleration(X_ACC); 
		else kinematics.setXAcceleration(-X_ACC);
		if(canStartJump()) {
			kinematics.setYAcceleration(Y_ACC);
			kinematics.setYVelocity(Y_VEL);
		}
	}
	
	/**
	 * This method returns whether the shark can jump
	 * 
	 * @return ...
	 * 		| if(kinematics.getVerticalAcceleration() == 0) then result == false
	 * 		   ...
	 *		| if(kinematics.getVerticalVelocity() > 0)
	 *		|		result == super.overlappingGameObject(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder())
	 *		|else result == mustFall()
	 */
	@Override
	protected boolean canJump() {
		if(kinematics.getYAcceleration() == 0) return false;
		if(kinematics.getYVelocity() > 0)
			return super.overlappingGameObject(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder());
		else 
			return mustFall();
	}
	
	/**
	 * This method returns whether the shark is running or not
	 * 
	 * @return ...
	 * 		| result == kinematics.getHorizontalAcceleration() != 0
	 */
	@Override
	public boolean isRunning() {
		return kinematics.getXAcceleration() != 0;
	}
	
	/**
	 * This method is used to evaluate if the shark can start moving at current start
	 * 
	 * @return ...
	 * 		| result == (getWorld() != null && (swimsInWater() || !super.getWorld().shallBePassable(getDownBorder()))
	 */
	private boolean canStartJump() {
		if(getWorld() == null) return false;
		return swimsInWater() || !super.getWorld().shallBePassable(getDownBorder());
	}

	/**
	 * This method returns whether the shark is jumping or not
	 * 
	 * @return ...
	 * 		| result ==  kinematics.getVerticalAcceleration() != 0
	 */
	private boolean isJumping() {
		return kinematics.getYAcceleration() != 0;
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
	private void arrangeFeatureHit(double dt) {
		if(!swimsInWater()) {
			setOutWaterTime(getOutWaterTime() + dt);
			if(getOutWaterTime() >= Constant.SHARK_OUT_WATER_TIME.getValue()) {
				super.updateHitPoints((int) Constant.SHARK_OUT_WATER.getValue());
				setOutWaterTime(0.0);
			}
		}else setOutWaterTime(0.0);
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
	private void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingObjects();
		if(getWorld() != null) {
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 0:arrangeMazubHit(dt);break;
				case 3:arrangeSlimeHit(dt);break;
				case 4: if(object.getBlockTime() == 0)arrangeSwitch(this);break;
				default: break;
				}
			}
		}
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
		shark.kinematics.setXAcceleration(0.0);
		shark.setNewIndex();
		shark.kinematics.setXVelocity(0.0);
		kinematics.setXAcceleration(0.0);
		setNewIndex();
		kinematics.setXVelocity(0.0);
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
		if(getWorld() == null) {return false;}
		for(int pixelX = super.getRectangle().getXCoordinate(); pixelX <= super.getRectangle().getXCoordinate()+super.getRectangle().getWidth()-1; pixelX++) {
			for(int pixelY= super.getRectangle().getYCoordinate(); pixelY <= super.getRectangle().getYCoordinate()+super.getRectangle().getHeight()-1; pixelY++) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) return true;
			}
		}
		return false;
	}
	
	/**
	 * This method returns whether the shark must fall or not
	 * 
	 * @return ...
	 * 		| if(getWorld() == null || super.kinematics.getVerticalVelocity() > 0) then result == false 
	 *		| 	Rectangle up = super.getUp() && down = super.getDownBorder()
	 *		| 	for(int pixelX= rect.getOrigin().getX(); pixelX < rect.getOrigin().getX()+ rect.getDimension().getWidth(); pixelX++) 
	 *		|		if(getWorld().getTileFeature(pixelX, rect.getOrigin().getY()) == Feature.WATER) then result == false
	 *		| 	for(int pixelX = down.getOrigin().getX(); pixelX < down.getOrigin().getX()+down.getDimension().getWidth(); pixelX++)
	 *		|		if(!getWorld().getTileFeature(pixelX, down.getOrigin().getY()).isPassable()) then result == false
	 *		| result == super.overlappingGameObject(down).isEmpty()
	 */
	private boolean mustFall() {
		if(getWorld() == null || kinematics.getYVelocity() > 0) return false; 
		Rectangle up = super.getUpBorder();
		for(int pixelX = up.getOrigin().getX(); pixelX < up.getOrigin().getX()+ up.getWidth(); pixelX++) {
			if(getWorld().getTileFeature(pixelX, up.getOrigin().getY()) == Feature.WATER) return false;
		}
		Rectangle down = super.getDownBorder();
		for(int pixelX = down.getOrigin().getX(); pixelX < down.getOrigin().getX()+down.getWidth(); pixelX++) {
			if(!getWorld().getTileFeature(pixelX, down.getOrigin().getY()).isPassable()) return false;
		}
		return super.overlappingGameObject(down).isEmpty();
	}
	
	public void arrangeMazubHit(double dt) {
		if(getBlockTime() == 0) updateHitPoints((int) Constant.SHARK_MAZUB.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}
	
	public void arrangeSlimeHit(double dt) {
		if(getBlockTime() == 0) updateHitPoints((int) Constant.SHARK_SLIME.getValue());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}
	
}
