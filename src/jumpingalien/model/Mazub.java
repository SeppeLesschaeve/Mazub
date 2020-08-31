package jumpingalien.model;

import java.util.Set;

import annotate.Basic;
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
public class Mazub extends Creature implements TwoDimensionMovable{
	

	private double featureTime = 0.0;
	private double skullTime = 0.0;
	private double slimeTime = 0.0;
	private double sharkTime = 0.0;
	private double spiderTime = 0.0;
	private double spriteTime = 0.0;
	private double endMoveTime = 0.0;
	
	private Feature previousFeature;
	
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
	
	public double getFeatureTime() {
		return featureTime;
	}

	public void setFeatureTime(double featureTime) {
		this.featureTime = featureTime;
	}
	
	public double getSkullTime() {
		return skullTime;
	}

	public void setSkullTime(double skullTime) {
		this.skullTime = skullTime;
		if(this.skullTime >= Constant.TIMEOUT.getValue()) this.skullTime = 0.0;
	}

	public double getSlimeTime() {
		return slimeTime;
	}

	public void setSlimeTime(double slimeTime) {
		this.slimeTime = slimeTime;
		if(this.slimeTime >= Constant.TIMEOUT.getValue()) this.slimeTime = 0.0;
	}

	public double getSharkTime() {
		return sharkTime;
	}

	public void setSharkTime(double sharkTime) {
		this.sharkTime = sharkTime;
		if(this.sharkTime >= Constant.TIMEOUT.getValue()) this.sharkTime = 0.0;
	}

	public double getSpiderTime() {
		return spiderTime;
	}

	public void setSpiderTime(double spiderTime) {
		this.spiderTime = spiderTime;
		if(this.spiderTime >= Constant.TIMEOUT.getValue()) this.spiderTime = 0.0;
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
	 * This method returns an array of two integers representing the Pixel Position
	 * 
	 * @return the x and y coordinates of the Pixel Position
	 * 		| result == {super.getOrigin().getX(),super.getOrigin().getY()}
	 */
	@Basic
	public int[] getPixelPosition() {
		return new int[] {super.getRectangle().getX(),super.getRectangle().getY()};
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
	 * This method returns the orientation of the alien based on the velocity and the current sprite
	 * 
	 * @return the orientation of the alien
	 * 		| if(super.getIndex() == 2) result ==  Orientation.POSITIVE.getValue()
	 *		| if(super.getIndex() == 3) result ==  Orientation.NEGATIVE.getValue()
		    | result == super.getOrientation()
	 */
	@Basic @Override
	public int getOrientation() {
		if(super.getIndex() == 2 || isRunningRight()) return 1;
		if(super.getIndex() == 3 || isRunningLeft()) return -1;
		return 0;
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
				setVelocity(-1.0, kinematics.getYVelocity());
				kinematics.setXVelocityBounds(-1.0, -1.0);
			}else{ 
				setVelocity(1.0, kinematics.getYVelocity());
				kinematics.setXVelocityBounds(1.0, 1.0);
			}
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
		if(!canStopDuck()) return;
		setSpriteTime(0.0);
		if(super.getIndex() == 6) {
			endRunRight();
			startRunRight();
		}else if(super.getIndex() == 7) {
			endRunLeft();
			startRunLeft();
		}else {
			super.updateDimension(super.getSprites()[0].getWidth(), getSprites()[0].getHeight());
			super.setSprite(0);
			setVelocity(0.0, 0.0);
			setAcceleration(0.0, 0.0);
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
		int index = -1;
		if(super.getIndex() == 6) index = 8;
		else if(super.getIndex() == 7) index = ((super.getSprites().length-8)/2) + 8;
		else if(super.getIndex() == 1) index = 0;
		if(index == -1) return false;
		return (getWorld() == null || canChangeSprite(index));
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

	@Override
	public void advanceTime(double deltaT){
		Position<Integer> position = this.getRectangle().getOrigin();
		super.advanceTime(deltaT);
		if(getWorld() != null) super.getWorld().updateWindow(position);
	}

	@Override
	protected boolean canMoveInCurrentState() {
		if(kinematics.isStationary()) return canStartFall();
		return canJumpInCurrentState() && canRunInCurrentState();
	}
	
	@Override
	protected void setNewState() {
		if(kinematics.isStationary() && getOrientation() == 0) startFall();
		if(isMoving()) endRun();
		if(isJumping()) endGoingUp();
		if(isGoingDown()) endGoingDown();
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
		int tileLength = super.getWorld().getTileLength();
		int newX = 0;
		int newY = 0;
		for(int pixelX = super.getRectangle().getX(); pixelX < super.getRectangle().getX() + super.getRectangle().getWidth()-1; pixelX+=newX) {
			for(int pixelY= super.getRectangle().getY(); pixelY < super.getRectangle().getY() + super.getRectangle().getHeight()-1; pixelY+=newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) return Feature.MAGMA;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS) feature = Feature.GAS;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER && feature != Feature.GAS) feature = Feature.WATER;
				newY = Math.min(tileLength, super.getRectangle().getY() + super.getRectangle().getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, super.getRectangle().getX() + super.getRectangle().getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return feature;
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
		Set<Organism> objects = getCollidingCreatures();
		objects.addAll(getOverlappingPlants());
		if(skullTime != 0) setSkullTime(skullTime + dt);
		if(slimeTime != 0) setSlimeTime(slimeTime + dt);
		if(sharkTime != 0) setSharkTime(sharkTime + dt);
		if(spiderTime != 0) setSharkTime(spiderTime + dt);
		if(getWorld() != null) {
			for(Organism object : objects){
				int type = getGameObjectType(object);
				switch(type) {
				case 1:arrangeSneezeHit((Sneezewort) object); break;
				case 2:arrangeSkullHit((Skullcab) object, dt); break;
				case 3:if(!object.isDead())arrangeSlimeHit(dt); break;
				case 4:if(!object.isDead())arrangeSharkHit(dt); break;
				case 5:if(!object.isDead())arrangeSpiderHit(dt); break;
				default: break;
				}
			}
		}
	}
	
	public void arrangeSlimeHit(double dt) {
		if(slimeTime == 0) {
			updateHitPoints((int) Constant.MAZUB_SLIME.getValue());
			setSlimeTime(slimeTime + dt);
		}
	}
	
	public void arrangeSneezeHit(Sneezewort sneezewort) {
		if(getPoints() < getHitPoints().getMaximum() ) { 
			if(sneezewort.isDead()) updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			else updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
			sneezewort.arrangeEat(0.0);
		}
	}
	
	public void arrangeSkullHit(Skullcab skullcab, double dt) {
		if(skullTime == 0 && getPoints() < getHitPoints().getMaximum()) {
			if(skullcab.isDead()) updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			else updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
			skullcab.arrangeEat(dt);
			setSkullTime(skullTime + dt);
		}
	}
	
	public void arrangeSharkHit(double dt) {
		if(sharkTime == 0) {
			updateHitPoints((int) Constant.MAZUB_SHARK.getValue());
			setSharkTime(sharkTime + dt);
		}
	}

	public void arrangeSpiderHit(double dt) {
		if(spiderTime == 0) {
			updateHitPoints((int) Constant.MAZUB_SPIDER.getValue());
			setSpiderTime(spiderTime + dt);
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
		if( isDead() ) {
			this.setVelocity(0.0, 0.0);
			this.setAcceleration(0.0, 0.0);
		}
		if(kinematics.isStationary() && canStartFall()) startFall();
		if(isJumping() || isGoingDown()) jump(dt);
		if(isMoving()) run(dt);
		if(isMoving() && !isDucking() && !isJumping()) arrangeSpriteChange(dt);
		if(super.getIndex() == 2 || super.getIndex() == 3) arrangeEndMove(dt);
		if(!super.isInside()) terminate();
	}
	
	/**
	 * This method arrange the sprite change to show the running animation based on the interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * 
	 * @post After each 0.075 seconds the sprite will be updated will running
	 * 		|setSpriteTime(getSpriteTime() + dt)
	 *		|if(getSpriteTime() >= 0.075) then 
	 *		|	super.setSprite(super.getIndex() + 1) && super.getRectangle().setDimension(getWidth(), getHeight())
	 *		|    && setSpriteTime(getSpriteTime() - 0.075)
	 *
	 */
	private void arrangeSpriteChange(double dt) {
		setSpriteTime(getSpriteTime() + dt);
		int index = super.getIndex() + 1;
		if(getOrientation() == 1 && index >= ((getSprites().length-8)/2)+8){
			index = 8;
		}else if(getOrientation() == -1 && index >= getSprites().length) {
			index = ((getSprites().length-8)/2)+8;
		}
		if(getSpriteTime() >= 0.075 && canChangeSprite(index)) {
			setSprite(getSprites()[index]);
			super.updateDimension(getImageWidth(), getImageHeight());
			setSpriteTime(0.0);
		}
	}
	
	private boolean canChangeSprite(int index) {
		Rectangle newRect = new Rectangle(getRectangle().getOrigin(), super.getSprites()[index].getWidth(), super.getSprites()[index].getHeight());
		super.updatePosition(super.getRectangle().getX(), super.getRectangle().getY()+1);
		super.updateDimension(getSprites()[index].getWidth(), getSprites()[index].getHeight()-1);
		int currentIndex = super.getIndex();
		super.setSprite(index);
		if(getWorld() != null && getCollidingCreatures().isEmpty() && super.getWorld().shallBePassable(newRect)){
				super.updatePosition(getRectangle().getX(), getRectangle().getY()-1);
				super.updateDimension(getRectangle().getWidth(), getRectangle().getHeight()+1);
				super.setSprite(currentIndex);
				return true;
		}
		super.setSprite(currentIndex);
		super.updatePosition(getRectangle().getX(), getRectangle().getY()-1);
		super.updateDimension(getRectangle().getWidth(), getRectangle().getHeight()+1);
		return false;
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
		if(getEndMoveTime() >= Constant.MAZUB_END_MOVE_TIME.getValue() && canChangeSprite(0)) {
			super.setSprite(0);
			super.updateDimension(getImageWidth(), getImageHeight());
			setEndMoveTime(0.0);
		}
	}
	
	@Override
	public boolean canStartJump() {
		return true;
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
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getDownBorder()).isEmpty()  && super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean canFall() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getDownBorder()).isEmpty() && super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean isGoingDown() {
		return kinematics.getYVelocity() <= 0 && kinematics.getYAcceleration() < 0;
	}
	
	@Override
	public boolean isJumping() {
		return isGoingUp();
	}
	
	@Override
	public void endJump() {
		if(!isGoingUp() && !isGoingDown()) throw new IllegalStateException();
		if(isGoingUp()) endGoingUp();
	}

	@Override
	public void startJump() {
		if(!canStartJump() || isJumping() || isDead()) throw new IllegalStateException("Mazub can not start jumping if he did jump before it.");
		if(getOrientation() == -1)  super.setSprite(5);
		if(getOrientation() == 1) super.setSprite(4);
		if(getOrientation() == 0) super.setSprite(0);
		setVelocity(kinematics.getXVelocity(), 8.0);
		setAcceleration(kinematics.getXAcceleration(), -10.0);
	}

	@Override
	public void endGoingUp() {
		if(getOrientation() == -1) super.setSprite(((super.getSprites().length-8)/2) +8);
		if(getOrientation() == 1) super.setSprite(8);
		if(getOrientation() == 0) super.setSprite(0);
		setSpriteTime(0.0);
		if(kinematics.getYVelocity() > 0) setVelocity(kinematics.getXVelocity(), 0.0);
		setAcceleration(kinematics.getXAcceleration(), 0.0);	
	}

	@Override
	public void startFall() {
		if(getOrientation() == -1)  super.setSprite(5);
		if(getOrientation() == 1) super.setSprite(4);
		if(getOrientation() == 0) super.setSprite(0);
		setVelocity(kinematics.getXVelocity(), 0.0);
		setAcceleration(kinematics.getXAcceleration(), -10.0);
	}

	@Override
	public void endGoingDown() {
		if(canFall()) return;
		if(getOrientation() == -1) super.setSprite(((super.getSprites().length-8)/2) +8);
		if(getOrientation() == 1) super.setSprite(8);
		if(getOrientation() == 0) super.setSprite(0);
		setSpriteTime(0.0);
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
		super.setSprite(8);
		setSpriteTime(0.0);
		setVelocity( 1.0 , kinematics.getYVelocity());
		kinematics.setXVelocityBounds(1.0, 3.0);
		setAcceleration(0.9, kinematics.getYAcceleration());	
	}

	@Override
	public void endRunRight() {
		assert(isRunningRight() &&  !isDead());
		super.setSprite(2);
		setVelocity(0.0, kinematics.getYVelocity());
		kinematics.setXVelocityBounds(0.0, 0.0);
		setAcceleration(0.0, kinematics.getYAcceleration());
	}

	@Override
	public void startRunLeft() {
		assert(!isMoving() && !isDead() && canStartRunLeft());
		setSpriteTime(0.0);
		super.setSprite(((super.getSprites().length - 8) /2 ) + 8);
		setVelocity( -1.0 , kinematics.getYVelocity());
		kinematics.setXVelocityBounds(-1.0, -3.0);
		setAcceleration(-0.9, kinematics.getYAcceleration());
	}

	@Override
	public void endRunLeft() {
		assert(isRunningLeft() &&  !isDead());
		super.setSprite(3);
		setVelocity(0.0, kinematics.getYVelocity());
		kinematics.setXVelocityBounds(0.0, 0.0);
		setAcceleration(0.0, kinematics.getYAcceleration());
	}

	@Override
	public void run(double deltaT) {
		super.updateHorizontalComponent(this.getPosition().getX() + (kinematics.getXVelocity()*deltaT) + (kinematics.getXAcceleration()*deltaT*deltaT/2));
		kinematics.updateXVelocity(deltaT);
	}
	
}
