package jumpingalien.model;

import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
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
public class Spider extends Organism implements Run, Jump{

	private int legs;
	private double featureTime;
	private double time;
	private static final double JUMP_ACC = 0.5;
	private static final double JUMP_VEL = 1.0;
	
	private SpiderHitHandler hitHandler = new SpiderHitHandler() {

		@Override
		public void arrangeMazubHit(double dt) {
			setLegs(getLegs() - 1);
			if(isRunning()) {
				kinematics.setHorizontalVelocity(0);
			}
			if(isJumping() || isFalling()) {
				kinematics.setVerticalAcceleration(0);
				kinematics.setVerticalVelocity(0);
			}
		}

		@Override
		public void arrangeSlimeHit(double dt) {
			setTime(getTime() + dt);
			if(getTime() >= 0.5) {
				setLegs(getLegs() + 1);
				setTime(0.0);
			}
			
		}

		@Override
		public void arrangeSharkHit(double dt) {
			if(getBlockTime() == 0)setLegs(getLegs()/2);
			setBlockTime(getBlockTime() + dt);
			if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
				setBlockTime(0.0);
			}
			if(isRunning()) {
				kinematics.setHorizontalVelocity(0);
			}
			if(isJumping() || isFalling()) {
				kinematics.setVerticalAcceleration(0);
				kinematics.setVerticalVelocity(0);
			}
		}
		
	};
	

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
	public Spider(int nbLegs, int pixelLeftX, int pixelBottomY, Sprite... sprites) throws IllegalArgumentException{
		super(pixelLeftX, pixelBottomY, sprites);
		if(sprites.length != 3 || nbLegs < 0) throw new IllegalArgumentException();
		this.legs = nbLegs;
	}
	
	/**
	 * This method returns the number of legs
	 * 	
	 * @return ...
	 * 		| result == legs.getLegs()
	 */
	public int getLegs() {
		return Integer.valueOf(legs);
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
		this.legs = legs;
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
	}

	/**
	 * This method is used to manage to vertical movement of the spider over interval deltaT
	 * 
	 * @param deltaT
	 * 			This parameter is used as interval
	 * 
	 * @post ...
	 * 		|if(kinematics.getVerticalVelocity() > 0) then 
	 * 		| 	super.getVelocity().setMaxY(JUMP_VEL + 0.25*getLegs()) && setSprite(1)
	 *		|else if(kinematics.getVerticalVelocity() < 0) then  
	 *		|	super.getVelocity().setMaxY(-(JUMP_VEL + 0.25*getLegs())) && setSprite(2)
	 *@post ...
	 *		| double newY = this.getPosition().getY() + (kinematics.getVerticalVelocity()*deltaT) + (acceleration.getY()*deltaT*deltaT/2)
	 *		| super.getVelocity().accelerateY(getAcceleration(), deltaT)
	 *		| super.getPosition().setY(newY) && super.getOrigin().setY((int)(newY/0.01))
	 *
	 */
	@Override @Raw
	public void jump(double deltaT){
		if(kinematics.getVerticalVelocity() > 0) { 
			kinematics.setVerticalVelocity(JUMP_VEL + 0.25*getLegs());
			setSprite(1);
		}else if(kinematics.getVerticalVelocity() < 0){ 
			kinematics.setVerticalVelocity(-(JUMP_VEL + 0.25*getLegs()));
			setSprite(2);
		}
		double newY = this.getPosition().getY() + (kinematics.getVerticalVelocity()*deltaT) + (kinematics.getVerticalAcceleration()*deltaT*deltaT/2);
		kinematics.updateVerticalVelocity(deltaT);
		super.getPosition().setY(newY);
		super.getRectangle().setOrigin(getRectangle().getXCoordinate(), (int)(super.getPosition().getY()/0.01));
	}

	/**
	 * This method is used to end the vertical movement and to set ready for another movement
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented from interface Jump
	 * 
	 * @post ...
	 * 		|if(kinematics.getVerticalVelocity() > 0) then 
	 *		|		getVelocity().setY(-kinematics.getVerticalVelocity()) && getAcceleration().setY(-JUMP_ACC)
	 *		|		&& setSprite(2)
	 *		|else if(kinematics.getVerticalVelocity() < 0) then 
	 *		|		getVelocity().setY(-kinematics.getVerticalVelocity()) && getAcceleration().setY(JUMP_ACC)
	 *		|		&& setSprite(1)
	 * 
	 */
	@Override @Raw
	public void endJump(double deltaT){
		if(kinematics.getVerticalVelocity() > 0) { 
			
			kinematics.setVerticalAcceleration(-JUMP_ACC);
			setSprite(2);
		}else if(kinematics.getVerticalVelocity() < 0){
			kinematics.setVerticalAcceleration(JUMP_ACC);
			setSprite(1);
		}
		kinematics.setVerticalVelocity(-kinematics.getVerticalVelocity()); 
	}

	/**
	 * This method returns whether the spider can jump or not
	 * 
	 * @return ...
	 * 		| result == ( getWorld() == null || ( super.overlappingGameObject(getUpBorder()).isEmpty() 
	 *		|	&& super.getWorld().shallBePassable(getUpBorder()) && isAbleToJump()) )
	 *
	 */
	public boolean canJump() {
		return getWorld() == null || ( super.overlappingGameObject(getUpBorder()).isEmpty() 
				&& super.getWorld().shallBePassable(getUpBorder()) && isAbleToJump());
	}
	
	/**
	 * This method returns whether the spider can fall or not
	 * 
	 * @return ...
	 * 		| result == ( getWorld() == null || ( super.overlappingGameObject(getDownBorder()).isEmpty() 
	 *		|	&& super.getWorld().shallBePassable(getDown()) && isAbleToJump()) )
	 *
	 */
	public boolean canFall() {
		return getWorld() == null || ( super.overlappingGameObject(getDownBorder()).isEmpty() 
				&& super.getWorld().shallBePassable(getDownBorder()) && isAbleToJump());
	}
	
	/**
	 * This method is used to indicate whether the spider is jumping or not
	 * 
	 *@return ...
	 *		| result == super.kinematics.getVerticalVelocity() > 0
	 */
	public boolean isJumping() {
		return kinematics.getVerticalVelocity() > 0;
	}
	
	/**
	 * This method is used to indicate whether the spider is falling or not
	 * 
	 * @return ...
	 * 		| result == super.kinematics.getVerticalVelocity() <= 0 && getAcceleration().getY() != 0
	 */
	public boolean isFalling() {
		return kinematics.getVerticalVelocity() <= 0 && kinematics.getVerticalAcceleration() != 0;
	}
	
	/**
	 * This method is used to indicate whether the spider is able to jump; 
	 * this is the main condition for the jumping movement
	 *  
	 * @return ...
	 * 		| result == (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()))
	 */
	public boolean isAbleToJump() {
		return (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()));
	}

	/**
	 * This method is used to make the horizontal movement of the spider over interval deltaT
	 * 
	 * @param deltaT
	 * 			This parameter is used as interval
	 * 
	 * @post ...
	 * 		|double newX = this.getPosition().getX() + super.kinematics.getHorizontalVelocity()*deltaT
	 *		|super.getPosition().setX(newX) && super.getOrigin().setX((int)(newX/0.01))
	 *
	 */
	@Override @Raw
	public void run(double deltaT) {
		double newX = this.getPosition().getX() + super.kinematics.getHorizontalVelocity()*deltaT;
		super.getPosition().setX(newX);
		super.getRectangle().setOrigin((int)(super.getPosition().getX()/0.01), getRectangle().getYCoordinate());
	}

	/**
	 * This method is used to end the horizontal movement and to set ready for another movement
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented from interface Run
	 * 
	 * @post ...
	 * 		|if(super.kinematics.getHorizontalVelocity() > 0) then super.getVelocity().setX(getLegs()*-0.15)
	 *		|else if(super.kinematics.getHorizontalVelocity() < 0) then super.getVelocity().setX(getLegs()*0.15)
	 * 
	 */
	@Override
	public void endRun(double deltaT) {
		if(kinematics.getHorizontalVelocity() > 0) kinematics.setHorizontalVelocity(getLegs()*-0.15);
		else if(kinematics.getHorizontalVelocity() < 0) kinematics.setHorizontalVelocity(getLegs()*0.15);
	}
	
	/**
	 * This method returns whether the spider can run or not
	 * 
	 * @return ...
	 * 		| if(getWorld() == null) then result == true
	 *         ...
	 *      | if(getOrientation() == Orientation.NEGATIVE.getValue()) then 
	 *		|	result == super.overlappingGameObject(getLeftBorder()).isEmpty()
	 *		|		&& super.getWorld().shallBePassable(getLeftBorder()) && isAbleToRun()
	 *		   ...
	 *		| if(getOrientation() == Orientation.POSITIVE.getValue()) then
	 *		|	result == super.overlappingGameObject(getRightBorder()).isEmpty() 
	 *		|		&& super.getWorld().shallBePassable(getRightBorder()) && isAbleToRun()
	 *			...
	 *		| result == false
	 */
	public boolean canRun() {
		if(getWorld() == null) return true;
		if(getOrientation() == -1) {
			return super.overlappingGameObject(getLeftBorder()).isEmpty()
					&& super.getWorld().shallBePassable(getLeftBorder()) && isAbleToRun();
		}
		if(getOrientation() == 1) {
			return super.overlappingGameObject(getRightBorder()).isEmpty() 
					&& super.getWorld().shallBePassable(getRightBorder()) && isAbleToRun();
		}
		return false;
	}


	/**
	 * This method is used to indicate whether the spider is able to jump; 
	 * this is the main condition for the jumping movement
	 *  
	 * @return ...
	 * 		| result == (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()))
	 */
	public boolean isAbleToRun() {
		return (!super.getWorld().shallBePassable(getDownBorder()) || !super.getWorld().shallBePassable(getUpBorder()));
	}

	/**
	 * This method is used to advance the state of the spider over interval deltaT
	 * 
	 * @param deltaT
	 * 			This parameter is used as interval
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		|Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)
	 * @post ...
	 * 		|if(getAcceleration().getY() == 0.0 && isAbleToJump()) then
	 *		|	getVelocity().setY(JUMP_VEL) && getAcceleration().setY(JUMP_ACC)
	 *		|if(kinematics.getHorizontalVelocity() == 0.0 && isAbleToRun()) then getVelocity().setX(-(0.15*getLegs()))
	 * @post ...
	 * 		|for(double time = 0.0, dt = updateDt(deltaT, time); time < deltaT; time += dt, dt = updateDt(deltaT, time))
	 *		|	if(isDead()) then super.setDelay(getDelay() + dt) 
	 *		|	if(getDelay() >= REMOVE_DELAY) then terminate()
	 *		|	arrangeFeatureHit(dt)
	 *		|	arrangeObjectHit(dt)
	 *		|	arrangeMovement(dt)
	 *		|	setBorders()
	 */
	@Override
	public void advanceTime(double deltaT) throws IllegalArgumentException{
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		if(kinematics.getVerticalAcceleration() == 0.0 && isAbleToJump()) {
			kinematics.setVerticalVelocity(JUMP_VEL); 
			kinematics.setVerticalAcceleration(JUMP_ACC);
		}
		if(kinematics.getHorizontalVelocity() == 0.0 && isAbleToRun()) kinematics.setHorizontalVelocity(-(0.15*getLegs()));
		for(double time = 0.0, dt = updateDt(deltaT, time); time < deltaT; time += dt, dt = updateDt(deltaT, time)) {
			if(isDead()) super.setDelay(getDelay() + dt); 
			if(getDelay() >= REMOVE_DELAY) terminate();
			arrangeFeatureHit(dt);
			arrangeObjectHit(dt);
			arrangeMovement(dt);
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
	private void arrangeMovement(double dt) {
		if( isDead() ) return;
		if((isJumping() && canJump()) || (isFalling() && canFall())) jump(dt); else endJump(dt);
		if(isRunning() && canRun()) run(dt); else endRun(dt);
		if(!super.isInside()) terminate();
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
	private void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingObjects();
		if(getWorld() != null) {
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 0:hitHandler.arrangeMazubHit(dt);break;
				case 3:hitHandler.arrangeSlimeHit(dt);break;
				case 4:hitHandler.arrangeSharkHit(dt);break;
				case 5:if(object instanceof Spider && object.getBlockTime() == 0) {
					if(isRunning()) endRun(dt);
					if(isJumping() || isFalling()) endJump(dt);
				}break;
				default: break;
				}
			}
		}
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
	private void arrangeFeatureHit(double dt) {
		if(isInContactWithWaterOrIce()) setLegs(0);
		if(isInContactWithMagma()) {
			if(getFeatureTime() == 0.0) {
				setLegs(getLegs() - 2);
			}
			setFeatureTime(getFeatureTime() + dt);
			if(getFeatureTime() >= 0.6) {
				setLegs(getLegs() - 2); setFeatureTime(getFeatureTime() - 0.6);
			}
		}else setFeatureTime(0.0);
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
		for(int pixelX = super.getRectangle().getXCoordinate() - 1; pixelX <= super.getRectangle().getXCoordinate()+super.getRectangle().getWidth(); pixelX++) {
			for(int pixelY= super.getRectangle().getYCoordinate() - 1; pixelY <= super.getRectangle().getYCoordinate()+super.getRectangle().getHeight(); pixelY++) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER || getWorld().getTileFeature(pixelX, pixelY) == Feature.ICE) {return true;}
			}
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
		for(int pixelX = super.getRectangle().getXCoordinate() - 1; pixelX <= super.getRectangle().getXCoordinate()+super.getRectangle().getWidth(); pixelX++) {
			for(int pixelY= super.getRectangle().getYCoordinate() - 1; pixelY <= super.getRectangle().getYCoordinate()+super.getRectangle().getHeight(); pixelY++) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) {return true;}
			}
		}
		return false;
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

	/**
	 * This method returns the orientation of the spider
	 * 
	 * @return ...
	 * 		| if(super.kinematics.getVerticalVelocity() < 0 || super.kinematics.getHorizontalVelocity() < 0) then
	 *		|	result == Orientation.NEGATIVE.getValue()
	 * 		   ...
	 * 		| if(super.kinematics.getVerticalVelocity() > 0 || super.kinematics.getHorizontalVelocity() > 0) then
	 *		| 	result == Orientation.POSITIVE.getValue()
	 */
	@Override
	public int getOrientation() {
		if(kinematics.getVerticalVelocity() < 0 || kinematics.getHorizontalVelocity() < 0) {
			return -1;
		}
		if(kinematics.getVerticalVelocity() > 0 || kinematics.getHorizontalVelocity() > 0){
			return 1;
		}
		return 0;
	}

	/**
	 * This method is used to return a new time slice for the advancement of the state of the spider
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time that must be passed in total
	 * @param time
	 * 			This parameter is used as the time that has already been passed
	 * 
	 * @post ...
	 * 		|double result =  0.01 / ( Math.sqrt( Math.pow(kinematics.getHorizontalVelocity(), 2) + Math.pow(kinematics.getVerticalVelocity(), 2) ) + 
			|	( Math.sqrt(deltaT) * Math.pow(acceleration.getY(), 2) ))
	 * @post ...
	 * 		|if(Double.isInfinite(result) || result > 0.01) then result = 0.01
	 * 		|if(time + result > deltaT) then result = deltaT-time
	 *		|result.equals(result)
	 *	
	 */
	protected double updateDt(double deltaT, double time) {
		double result = 0.01 / ( Math.sqrt( Math.pow(kinematics.getHorizontalVelocity(), 2) + Math.pow(kinematics.getVerticalVelocity(), 2) ) + 
				( Math.sqrt(deltaT) * Math.pow(kinematics.getVerticalAcceleration(), 2) ));
		if(Double.isInfinite(result) || result > 0.01) {
			result = 0.01;
		}
		if(time + result > deltaT) {
			result = deltaT-time;
		}
		return result;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
}
