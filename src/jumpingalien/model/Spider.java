package jumpingalien.model;

import java.util.Set;

import jumpingalien.util.Sprite;

/**
 * This class builds a spider that can run and jump
 * and with legs that can increase or decrease in amount because of collision with features and other Game Object
 *  
 * @invar ...
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 *
 * @author Seppe Lesschaeve (Informatica)
 * @version 1.0
 *
 */
public class Spider extends Creature implements TwoDimensionMovable{

	private double featureTime;
	private double mazubTime;
	private double slimeTime;
	private double sharkTime;
	
	/**
	 * 
	 * This constructor will set the Legs, Pixel Position, Actual Position, Dimension and the images to show the animation
	 * 
	 * @param nbLegs
	 * 			This parameter is used as the number of legs
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as the images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		| sprites.length != 3 || nbLegs < 0
	 * @effect ...
	 * 	  	| super(pixelLeftX, pixelBottomY, sprites)
	 * @post ...
	 * 		| this.legs = new Legs(0, nbLegs)
	 */
	public Spider(int nbLegs, int pixelLeftX, int pixelBottomY, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, nbLegs, 2, nbLegs, sprites);
		if(sprites.length != 3 || nbLegs < 0) throw new IllegalArgumentException();
	}
	
	/**
	 * This method returns the number of legs
	 * 	
	 * @return ...
	 * 		| result == legs.getLegs()
	 */
	public int getLegs() {
		return super.getPoints();
	}
	
	/**
	 * This method is used to set the number of legs
	 * 
	 * @param legs
	 * 			This parameter is used as the number of legs
	 * 
	 * @post ...
	 * 		| this.legs.setLegs(legs)
	 */
	public void setLegs(int legs) {
		super.updateHitPoints(legs);
	}
	
	/**
	 * This method returns the Feature Time
	 * 
	 * @return ...
	 * 		|result == featureTime
	 */
	@Basic
	public double getFeatureTime() {
		return featureTime;
	}

	/**
	 * This method is used to set the Feature Time
	 * 
	 * @param featureTime
	 * 			This parameter is used to set the Feature Time
	 * 
	 * @post ...
	 * 		|(new this).featureTime = featureTime
	 * 
	 */
	private void setFeatureTime(double featureTime) {
		this.featureTime = featureTime;
		if(featureTime >= 0.6) this.featureTime = 0.0;
	}

	public double getMazubTime() {
		return mazubTime;
	}

	public void setMazubTime(double mazubTime) {
		this.mazubTime = mazubTime;
		if(this.mazubTime >= Constant.TIMEOUT.getValue()) this.mazubTime = 0.0;
	}

	public double getSlimeTime() {
		return slimeTime;
	}

	public void setSlimeTime(double slimeTime) {
		this.slimeTime = slimeTime;
		if(this.slimeTime >= 0.5) this.slimeTime = 0.0;
	}

	public double getSharkTime() {
		return sharkTime;
	}

	public void setSharkTime(double sharkTime) {
		this.sharkTime = sharkTime;
		if(this.sharkTime >= Constant.TIMEOUT.getValue()) this.sharkTime = 0.0;
	}

	/**
	 * This method is used to indicate whether the spider is considered dead
	 * 
	 * @return ...
	 * 		| result == getLegs() < 2
	 */
	@Override
	public boolean isDead() {
		return getLegs() < 2;
	}


	protected void arrangeInitialMovement() {
		if(kinematics.getYAcceleration() == 0.0 && canStartJump()) startJump();
		else if(kinematics.getYAcceleration() == 0.0 && canStartFall()) startFall();
		else if(kinematics.getXAcceleration() == 0.0 && canStartRunLeft()) startRunLeft();
		else if(kinematics.getXAcceleration() == 0.0 && canStartRunRight()) startRunRight();	
	}

	@Override
	protected boolean canMoveInCurrentState() {
		if(kinematics.isStationary()) return canStartJump() || canStartRunLeft();
		return canJumpInCurrentState() && canRunInCurrentState();
	}
	
	@Override
	protected void setNewState() {
		if(kinematics.isStationary() && canStartFall()) startFall();
		if(kinematics.isStationary() && canStartRunRight()) startRunRight();
		if(isMoving()) endRun();
		if(isJumping()) endJump();
	}
	
	/**
	 * This method is used to indicate whether the spider is in contact with water or ice
	 * 
	 * @return ...
	 * 		| if(getWorld() == null) then result == false
	 * 		   ...
	 * 		|for(int pixelX = super.getOrigin().getX() - 1; pixelX <= super.getOrigin().getX()+super.getRectangle().getDimension().getWidth(); pixelX++) 
	 *		|	for(int pixelY= super.getOrigin().getY() - 1; pixelY <= super.getOrigin().getY()+super.getRectangle().getDimension().getHeight(); pixelY++)
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER || getWorld().getTileFeature(pixelX, pixelY) == Feature.ICE) 
	 *		|			then result == true
	 * 		   ...
	 * 		| result == false
	 */
	@Basic
	public boolean isInContactWithWaterOrIce() {
		if(getWorld() == null) {return false;}
		int tileLength = super.getWorld().getTileLength();
		int newX = 0;
		int newY = 0;
		for(int pixelX = super.getRectangle().getX(); pixelX < super.getRectangle().getX()+super.getRectangle().getWidth()-1; pixelX+=newX) {
			for(int pixelY= super.getRectangle().getY(); pixelY < super.getRectangle().getY()+super.getRectangle().getHeight()-1; pixelY+=newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER || getWorld().getTileFeature(pixelX, pixelY) == Feature.ICE) return true;
				newY = Math.min(tileLength, super.getRectangle().getY() + super.getRectangle().getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, super.getRectangle().getX() + super.getRectangle().getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return false;
	}
	
	/**
	 * This method is used to indicate whether the spider is in contact with magma
	 * 
	 * @return ...
	 * 		| if(getWorld() == null) then result == false
	 * 		   ...
	 * 		|for(int pixelX = super.getOrigin().getX() - 1; pixelX <= super.getOrigin().getX()+super.getRectangle().getDimension().getWidth(); pixelX++) 
	 *		|	for(int pixelY= super.getOrigin().getY() - 1; pixelY <= super.getOrigin().getY()+super.getRectangle().getDimension().getHeight(); pixelY++)
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) then result == true
	 * 		   ...
	 * 		| result == false
	 */
	@Basic
	public boolean isInContactWithMagma() {
		if(getWorld() == null) {return false;}
		int tileLength = super.getWorld().getTileLength();
		int newX = 0;
		int newY = 0;
		for(int pixelX = super.getRectangle().getX(); pixelX < super.getRectangle().getX()+super.getRectangle().getWidth()-1; pixelX+=newX) {
			for(int pixelY= super.getRectangle().getY(); pixelY < super.getRectangle().getY()+super.getRectangle().getHeight()-1; pixelY+=newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) return true;
				newY = Math.min(tileLength, super.getRectangle().getY() + super.getRectangle().getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, super.getRectangle().getX() + super.getRectangle().getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return false;
	}

	/**
	 * This method is used to arrange the Hit Points because of hit with features using dt as time
	 * 
	 * @param dt
	 * 			This parameter is used as time
	 * 
	 * @post ...
	 * 		|if(isInContactWithWaterOrIce()) setLegs(0);
	 * @post ...
	 * 		|if(isInContactWithMagma()) then 
	 *		|	if(getFeatureTime() == 0.0) then setLegs(getLegs() - 2.0)
	 *		|	setFeatureTime(getFeatureTime() + dt)
	 *		|	if(getFeatureTime() >= 0.6) then 
	 *		|		setLegs(getLegs() - 2.0) && setFeatureTime(getFeatureTime() - 0.6)
	 *		|else setFeatureTime(0.0)
	 *
	 */
	protected void arrangeFeatureHit(double dt) {
		if(isInContactWithWaterOrIce()) setLegs(-getLegs());
		if(isInContactWithMagma()) {
			if(getFeatureTime() == 0.0) {
				setLegs(-2);
			}
			setFeatureTime(getFeatureTime() + dt);
		}else setFeatureTime(0.0);
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
	 *		|		if(object instanceof Spider && object.getBlockTime() == 0) 
	 *		|			then if(isRunning()) then endRun(dt)
	 *		|			and then if(isJumping() || isFalling()) then endJump(dt)
	 *		|		else getWorld().handleImpact(this, object, dt)
	 *
	 */
	protected void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingCreatures();
		if(mazubTime != 0) setMazubTime(mazubTime + dt);
		if(slimeTime != 0) setSlimeTime(slimeTime + dt);
		if(sharkTime != 0) setSharkTime(sharkTime + dt);
		if(getWorld() != null) {
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 0:arrangeMazubHit(dt);break;
				case 3:arrangeSlimeHit(dt);break;
				case 4:arrangeSharkHit(dt);break;
				case 5:arrangeSwitch((Spider) object); break;
				default: break;
				}
			}
		}
	}
	
	public void arrangeMazubHit(double dt) {
		if(mazubTime == 0) {
			setLegs(-1);
			setMazubTime(mazubTime + dt);
		}
	}

	public void arrangeSlimeHit(double dt) {
		if(slimeTime == 0) {
			setLegs(+1);
			setSlimeTime(slimeTime + dt);
		}
	}

	public void arrangeSharkHit(double dt) {
		if(sharkTime == 0) {
			setLegs(-(getLegs()/2));
			setSharkTime(sharkTime + dt);
		}
	}

	private void arrangeSwitch(Spider spider) {
		if(spider.isMoving()) {
			if(spider.isRunningRight()) {
				spider.startRunLeft(); 
				startRunRight();
			}else {
				startRunLeft(); 
				spider.startRunRight();
			}
		}
		if(spider.isJumping()){
			if(spider.isGoingUp()) {
				spider.startFall(); 
				startJump();
			}else {
				startFall(); 
				spider.startJump();
			}
		}
	}
	/**
	 * This method is used to arrange the movements of the spider over interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * 
	 * @post ...
	 * 	   	| if( isDead() ) then return
	 * @post ...
	 * 		| if((isJumping() && canJump()) || (isFalling() && canFall())) then jump(dt) else endJump(dt)
	 *		| if(isRunning() && canRun()) then run(dt) else endRun(dt)
	 *		| if(!super.isInside()) terminate()
	 */
	protected void arrangeMovement(double dt) {
		if(kinematics.isStationary()) arrangeInitialMovement();
		if(isJumping()) jump(dt);
		if(isMoving()) run(dt);
		if(!super.isInside()) terminate();
	}

	@Override
	public boolean canStartJump() {
		return canJump();
	}

	@Override
	public boolean canJump() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return getCollidingCreatures(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder()) 
				&& (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()));
	}

	@Override
	public boolean isGoingUp() {
		return kinematics.getYVelocity() > 0 && kinematics.getYAcceleration() < 0;
	}

	@Override
	public boolean canStartFall() {
		return canFall();
	}

	@Override
	public boolean canFall() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		return getCollidingCreatures(getDownBorder()).isEmpty() && super.getWorld().shallBePassable(getDownBorder()) 
				&& (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()));
	}

	@Override
	public boolean isGoingDown() {
		return kinematics.getYVelocity() < 0 || kinematics.getYAcceleration() < 0;
	}

	@Override
	public void startJump() {
		if(isJumping() || isDead()) throw new IllegalStateException("Spider can not start jumping if he did jump before it.");
		super.setSprite(1);
		kinematics.setYVelocity(1.0);
		kinematics.setYAcceleration(0.5);
		kinematics.setMaxYVelocity(1.0 +0.5*0.5*getLegs());
	}

	@Override
	public void endGoingUp() {
		startFall();
	}

	@Override
	public void startFall() {
		if(isJumping() || isDead()) throw new IllegalStateException("Spider can not start jumping if he did jump before it.");
		super.setSprite(2);
		kinematics.setYVelocity(1.0);
		kinematics.setYAcceleration(-0.5);
		kinematics.setMaxYVelocity(-1.0-(0.5*0.5)*getLegs());
	}

	@Override
	public void endGoingDown() {
		startJump();
	}

	@Override
	public void jump(double deltaT) {
		super.updateVerticalComponent(this.getPosition().getY() + (kinematics.getYVelocity()*deltaT) + 
				(kinematics.getYAcceleration()*deltaT*deltaT/2));
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
		return getCollidingCreatures(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder())
				&& (!super.getWorld().shallBePassable(getUpBorder()) || !super.getWorld().shallBePassable(getDownBorder()));
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
		return getCollidingCreatures(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder()) &&
				(!super.getWorld().shallBePassable(getUpBorder()) || !super.getWorld().shallBePassable(getDownBorder()));
	}

	@Override
	public boolean isRunningLeft() {
		return kinematics.getXVelocity() < 0 || kinematics.getXAcceleration() < 0;
	}

	@Override
	public void startRunRight() {
		assert(!isMoving() && !isDead());
		super.setSprite(0);
		kinematics.setXAcceleration(0.15);
		kinematics.setMaxXVelocity(0.15*0.15*getLegs());
	}

	@Override
	public void endRunRight() {
		assert(isRunningRight() && !isDead());
		kinematics.setXAcceleration(0.0);
		kinematics.setMaxXVelocity(0.0);
	}

	@Override
	public void startRunLeft() {
		assert(!isMoving() && !isDead());
		super.setSprite(0);
		kinematics.setXAcceleration(-0.15);
		kinematics.setMaxXVelocity(-0.15*0.15*getLegs());
		
	}

	@Override
	public void endRunLeft() {
		assert(isRunningLeft() && !isDead());
		kinematics.setXAcceleration(0.0);
		kinematics.setMaxXVelocity(0.0);
	}

	@Override
	public void run(double deltaT) {
		super.updateHorizontalComponent(this.getPosition().getX() + super.kinematics.getXVelocity()*deltaT + 
				(kinematics.getXAcceleration()*deltaT*deltaT/2));
		kinematics.updateXVelocity(deltaT);
	}	
	
}
