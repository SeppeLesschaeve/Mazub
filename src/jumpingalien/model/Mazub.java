package jumpingalien.model;

import annotate.Basic;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.animation.MazubVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.collision.MazubCollider;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.feature.MazubFeatureHandler;
import jumpingalien.model.kinematics.FullKinematics;
import jumpingalien.model.kinematics.Kinematics;
import jumpingalien.model.storage.GameObjectStorage;
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
public class Mazub extends Creature implements VerticalMovable {

	private double spiderTime = 0.0;
	private double endMoveTime = 0.0;
	private double blockTime = 0.0;
	private boolean isAdvancedByWorld = false;
	
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
	public Mazub(int pixelLeftX, int pixelBottomY, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, 100, 0, 500, sprites);
		if(sprites.length < 8 || sprites.length%2 != 0)
			throw new IllegalArgumentException("This is not a valid amount of sprites.");
	}

	public void setSpiderTime(double spiderTime) {
		this.spiderTime = spiderTime;
		if(this.spiderTime >= Constant.TIMEOUT.getValue()) this.spiderTime = 0.0;
	}

	public double getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(double blockTime) {
		this.blockTime = blockTime;
		if(this.blockTime >= Constant.TIMEOUT.getValue()) this.blockTime = 0.0;
	}

	public boolean isAdvancedByWorld() {
		return isAdvancedByWorld;
	}

	public void setAdvancedByWorld(boolean advancedByWorld) {
		isAdvancedByWorld = advancedByWorld;
	}

	public double getSpiderTime() {
		return spiderTime;
	}

	public double getEndMoveTime() {
		return endMoveTime;
	}

	public void setEndMoveTime(double endMoveTime) {
		this.endMoveTime = endMoveTime;
	}

	@Override
	protected Collidable initializeCollider() {
		return new MazubCollider(this);
	}

	@Override
	protected FeatureHandler initializeFeatureHandler() {
		return new MazubFeatureHandler(this);
	}

	@Override
	protected Kinematics initializeKinematics() {
		return new FullKinematics();
	}

	@Override
	protected GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		return new MazubVisualizer(this, xCoordinate, yCoordinate, sprites);
	}

	@Override
	public double[] getAcceleration() {
		return new double[]{((FullKinematics) getKinematics()).getXAcceleration(), ((FullKinematics)getKinematics()).getYAcceleration()};
	}

	@Override
	public double[] getVelocity() {
		return new double[]{((FullKinematics) getKinematics()).getXVelocity(), ((FullKinematics)getKinematics()).getYVelocity()};
	}

	@Override
	public int getOrientation(){
		if(isMovingLeft() || ((MazubVisualizer) getVisualizer()).isStillMovingLeft()) return -1;
		if(isMovingRight() || ((MazubVisualizer) getVisualizer()).isStillMovingRight()) return 1;
		return 0;
	}

	/**
	 * This method returns an array of two integers representing the Pixel Position
	 * 
	 * @return the x and y coordinates of the Pixel Position
	 * 		| result == {super.getOrigin().getX(),super.getOrigin().getY()}
	 */
	@Basic
	public int[] getPixelPosition() {
		return new int[] {getXCoordinate(), getYCoordinate()};
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
	protected void setPixelPosition(int x, int y){
		if( x < 0 || y < 0) throw new IllegalArgumentException("The coordinates must be positive");
		setXCoordinate(x);
		setYCoordinate(y);
	}
	
	/**
	 * This method returns an array of two double precision floating-point numbers representing the Actual Position
	 * 
	 * @return the x and y coordinates of the Actual Position
	 * 		| result == {super.getPosition().getX(), super.getPosition().getY()}		
	 */
	@Basic
	public double[] getActualPosition() {
		return new double[] {getXPosition(), getYPosition()};
	}


	@Override
	public void addToStorage(GameObjectStorage worldStorage) {
		worldStorage.addMazub(this);
	}

	@Override
	public void removeFromStorage(GameObjectStorage worldStorage) {
		worldStorage.removeMazub(this);
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
		if(isDucking()) return;
		if(!((MazubVisualizer)getVisualizer()).canStartDucking()) return;
		if(isMovingLeft()) {
			((FullKinematics) getKinematics()).setXVelocity(-1.0);
			((FullKinematics) getKinematics()).setXVelocityBounds(-1.0, -1.0);
		}else if(isMovingRight()){
			((FullKinematics) getKinematics()).setXVelocity(1.0);
			((FullKinematics) getKinematics()).setXVelocityBounds(1.0, 1.0);
		}else{
			((FullKinematics) getKinematics()).setXVelocity(0.0);
			((FullKinematics) getKinematics()).setXVelocityBounds(0.0, 0.0);
		}
		((FullKinematics) getKinematics()).setXAcceleration(0.0);
		((MazubVisualizer)getVisualizer()).updateImageBeginDucking();
	}
	
	/**
	 * This method stops the duck movement of the alien when he can stop ducking
	 * 
	 * @post The velocity, acceleration and the sprite will be set based on the orientation
	 * 		| if (getOrientation() == Orientation.POSITIVE)  then
	 *		| 	if(canStopDuck()) then super.setSprite(8) else super.setSprite(6)
	 *		|   setVelocity(1.0, 0.0)
	 *		|   setAcceleration(1.0, 0.0)
	 *	    | if (getOrientation() == Orientation.NEGATIVE)  then
	 *		|	if(canStopDuck()) then super.setSprite((getSprites().length-8)/2)+8) else super.setSprite(7)
	 *		|   setVelocity(-1.0, 0.0)
	 *		|   setAcceleration(-1.0, 0.0)
	 *		| if (getOrientation() == Orientation.NEUTRAL)  then
	 *		|	if(canStopDuck()) then super.setSprite(0) else super.setSprite(1)
	 *		|   setAcceleration(0.0, 0.0)
	 *		|   setVelocity(0.0, 0.0)
	 *		| super.setBorders()
	 *
	 */
	public void endDuck() {
		if(!isDucking()) return;
		if(!((MazubVisualizer) getVisualizer()).canStopDucking()) return;
		((MazubVisualizer)getVisualizer()).updateImageAfterDucking();
		if(isMovingRight()) {
			((FullKinematics)getKinematics()).setXAcceleration(0.9);
			((FullKinematics)getKinematics()).setXVelocityBounds(1.0,3.0);
		}else if(isMovingLeft()) {
			((FullKinematics)getKinematics()).setXAcceleration(-0.9);
			((FullKinematics)getKinematics()).setXVelocityBounds(-1.0,-3.0);
		}else{
			((FullKinematics)getKinematics()).setXAcceleration(0.0);
			((FullKinematics)getKinematics()).setXVelocityBounds(Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);
		}
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
		return ((FullKinematics)getKinematics()).getMinXVelocity() ==
				((FullKinematics)getKinematics()).getMaxXVelocity();
	}

	@Override
	public void advanceTime(double deltaT){
		Position<Integer> previousPosition = new Position<>(getXCoordinate(), getYCoordinate());
		super.advanceTime(deltaT);
		if(getWorld() != null) super.getWorld().updateWindow(previousPosition);
	}

	@Override
	protected void performDuringTimeStep() {
		if(getBlockTime() != 0) setBlockTime(getBlockTime() + getTimeStep());
		updateHorizontalMovement();
		updateVerticalMovement();
		getKinematics().updatePosition(getTimeStep(), this);
		if(!isInside()) terminate();
		if(!isTerminated() && !isStillNotInAGameWorld())handlePlants();
		super.getFeatureHandler().handleFeature();
		super.updateImage();
		if(isDead()){
			setDelay(getDelay() + getTimeStep());
			if(getDelay() == Constant.REMOVE_DELAY.getValue()) terminate();
		}
	}

	private void updateVerticalMovement() {
		if(getKinematics().isStationary() && canFall()) {
			startFall();
			return;
		}
		if(isJumping() && !canJump()){
			for(GameObject gameObject : getCollidingCreatures(getUpBorder())){
				gameObject.accept(super.getCollider());
			}
			endJump();
			if(canFall()) startFall();
		}else if(isFalling() && !canFall()){
			for(GameObject gameObject : getCollidingCreatures(getDownBorder())){
				gameObject.accept(super.getCollider());
			}
			((FullKinematics) getKinematics()).setYVelocity(0.0);
			((FullKinematics) getKinematics()).setYAcceleration(0.0);
		}
	}

	private void handlePlants() {
		for(Plant plant : getOverlappingPlants()){
			if(plant.getEatTime() != 0) plant.setEatTime(plant.getEatTime() + getTimeStep());
			plant.accept(super.getCollider());
		}
		if(getOverlappingPlants().isEmpty()){
			for (Plant plant : getWorld().getPlants()){
				plant.setEatTime(0.0);
			}
		}
	}

	@Override
	protected void accept(Collidable collidable) {
		collidable.collideWithMazub(this);
	}

	@Override
	public boolean canJump() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder());
	}

	@Override
	public boolean isJumping() {
		return ((FullKinematics) getKinematics()).getYVelocity() > 0 && ((FullKinematics) getKinematics()).getYAcceleration() < 0;
	}

	@Override
	public boolean canFall() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getDownBorder()).isEmpty()  && super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean isFalling() {
		return ((FullKinematics) getKinematics()).getYVelocity() <= 0 && ((FullKinematics) getKinematics()).getYAcceleration() < 0;
	}
	
	@Override
	public void endJump() {
		if(!isMovingVertically() || isDead()) throw new IllegalStateException();
		if(((FullKinematics) getKinematics()).getYVelocity() > 0) {
			((FullKinematics) getKinematics()).setYVelocity(0.0);
		}
		((FullKinematics) getKinematics()).setYAcceleration(0.0);
		((MazubVisualizer)getVisualizer()).updateImageBeginRunning();
	}

	@Override
	public void startJump() {
		if(!canJump() || isJumping()) throw new IllegalStateException("Mazub can not start jumping if he did jump before it.");
		((FullKinematics) getKinematics()).setMaxYVelocity(8.0);
		((FullKinematics) getKinematics()).setYVelocity(8.0);
		((FullKinematics) getKinematics()).setYAcceleration(-10.0);
		((MazubVisualizer)getVisualizer()).updateImageBeginJumping();
	}

	@Override
	public void startFall() {
		if(!canFall() || isFalling()) throw new IllegalStateException("Mazub can not start falling if he did fall before it.");
		((FullKinematics) getKinematics()).setYVelocity(0.0);
		((FullKinematics) getKinematics()).setYAcceleration(-10.0);
	}

	@Override
	public boolean canMoveRight() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
	}

	@Override
	public boolean isMovingRight() {
		return ((FullKinematics) getKinematics()).getXVelocity() > 0 || ((FullKinematics) getKinematics()).getXAcceleration() > 0;
	}

	@Override
	public boolean canMoveLeft() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
	}

	@Override
	public boolean isMovingLeft() {
		return ((FullKinematics) getKinematics()).getXVelocity() < 0 || ((FullKinematics) getKinematics()).getXAcceleration() < 0;
	}

	@Override
	public void startMoveRight() {
		assert(!isMovingHorizontally() && !isDead());
		((FullKinematics) getKinematics()).setXVelocity(1.0);
		((FullKinematics) getKinematics()).setXVelocityBounds(1.0, 3.0);
		((FullKinematics) getKinematics()).setXAcceleration(0.9);
		((MazubVisualizer)getVisualizer()).updateImageBeginRunning();
	}

	@Override
	public void endMove() {
		assert(isMovingHorizontally() &&  !isDead());
		((MazubVisualizer)getVisualizer()).updateImageAfterRunning();
		((FullKinematics) getKinematics()).setXVelocity(0.0);
		((FullKinematics)getKinematics()).setXAcceleration(0.0);
		((FullKinematics)getKinematics()).setXVelocityBounds(Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);

	}

	@Override
	public void startMoveLeft() {
		assert(!isMovingHorizontally() && !isDead());
		((FullKinematics) getKinematics()).setXVelocity(-1.0);
		((FullKinematics) getKinematics()).setXVelocityBounds(-1.0, -3.0);
		((FullKinematics) getKinematics()).setXAcceleration(-0.9);
		((MazubVisualizer)getVisualizer()).updateImageBeginRunning();
	}

}
