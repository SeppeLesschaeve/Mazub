package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

/**
 * This class builds a slime that is able to run
 * 
 * @invar ...
 * 		| hasProperSchool()
 * @invar ...
 * 		| getVelocity().getX() <= getVelocity().getMaxX())
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 * 
 */
public class Slime extends Creature implements Run{
	
	private School school;
	private double gasTime = 0.0;
	private double waterTime = 0.0;
	private long id;
	private static final double X_ACC = 0.7;
	private static final double X_MAX_VELOCITY = 2.5;
	
	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoints and the images to show the animation
	 * 
	 * @param ids 
	 * 			This set is used to check if it does not contain the id 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param id
	 * 			This parameter is used as id
	 * @param school
	 * 			This parameter is used as school
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 			...
	 * 		| sprites.length != 2 || id < 0 || ids.contains(id)
	 * @effect ...
	 * 		| super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites)
	 * @post ...
	 * 		| this.setId(id) && if(getSchool() != null) then getSchool().addSlime(this)
	 * @post ...
	 * 		|	if(school != null) then school.addSlime(this)
	 * @post ...
	 * 		| super.getAcceleration().setX(X_ACC) && super.getAcceleration().setY(0.0) && 
	 * 		| super.getVelocity().setMinX(0.0) && super.getVelocity().setMaxX(X_MAX_VELOCITY)
	 * 
	 */
	public Slime(Set<Long> ids, int pixelLeftX, int pixelBottomY, long id, School school, Sprite... sprites) throws IllegalArgumentException{
		super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites);
		if(sprites.length != 2 || id < 0 || ids.contains(id)) throw new IllegalArgumentException("You must have exactly two images");
		this.setId(id);
		kinematics.setHorizontalAcceleration(X_ACC);
		kinematics.setMaximumHorizontalVelocity(X_MAX_VELOCITY);
		if(school != null) {
			school.addSlime(this);
		}
	}
	
	/**
	 * This method returns the id
	 * 
	 * @return ...
	 * 		|result == id
	 * 
	 */
	@Basic
	public long getId() {
		return Long.valueOf(id);
	}
	
	/**
	 * This method is used to set the id
	 * 
	 * @param id
	 * 		This parameter is used as the id
	 * 
	 * @post ...
	 * 		|(new this).id = id
	 * 
	 */
	private void setId(long id) {
		this.id = id;
	}

	/**
	 * This method returns the Gas Time
	 * 
	 * @return ...
	 * 		| result == gasTime
	 */
	public double getGasTime() {
		return Double.valueOf(gasTime);
	}

	/**
	 * This method is used to set the Gas Time
	 * 
	 * @param gasTime
	 * 		This parameter is used as the Gas Time
	 * 
	 * @post ...
	 * 		|(new this).gasTime = gasTime
	 * 
	 */
	private void setGasTime(double gasTime) {
		this.gasTime = gasTime;
	}

	/**
	 * This method returns the Water Time
	 * 
	 * @return ...
	 * 		| result == waterTime
	 */
	public double getWaterTime() {
		return Double.valueOf(waterTime);
	}

	/**
	 * This method is used to set the Water Time
	 * 
	 * @param waterTime
	 * 		This parameter is used as the Water Time
	 * 
	 * @post ...
	 * 		|(new this).waterTime = waterTime
	 * 
	 */
	private void setWaterTime(double waterTime) {
		this.waterTime = waterTime;
	}

	/**
	 * This method will return if the slime can jump
	 * 
	 * @return ...
	 * 		|result == false
	 */
	@Override
	protected boolean canJump() {
		return false;
	}
	
	/**
	 * This method returns whether a school can be used or not
	 * 
	 * @param school
	 * 		This parameter is used as school
	 * 
	 * @return ...
	 * 		| school == null || !school.isTerminated()
	 * 
	 */
	@Basic
	public boolean canHaveAsSchool(School school) {
		return school == null || !school.isTerminated();
	}
	
	/**
	 * This method returns whether this slime is attached to a school
	 * 
	 * @return ...
	 * 		| result == canHaveAsSchool(getSchool()) && getSchool() != null &&  getSchool().getSlimes().contains(this)
	 */
	public boolean hasProperSchool() {
		return canHaveAsSchool(getSchool()) && (getSchool() == null ||  getSchool().hasProperSlime(this));
	}

	/**
	 * This method returns the school
	 * 
	 * @return ...
	 * 		|result == school
	 * 
	 */
	@Basic
	public School getSchool() {
		return this.school;
	}
	
	/**
	 * This method is used to set the school
	 * 
	 * @param school
	 * 		This parameter is used as the school
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		| !canHaveAsSchool(school)
	 * @post ...
	 * 		|(new this).school = school
	 * 
	 */
	public void setSchool(School school)  throws IllegalArgumentException{
		if(!canHaveAsSchool(school)) throw new IllegalArgumentException();
		this.school = school;
	}
	
	/**
	 * This method return whether the new school can be used to place the slime
	 * 
	 * @param school
	 * 			This parameter is used as school
	 * 
	 * @return ...
	 * 			|result == (getSchool() != null && !isTerminated() && !school.isTerminated())
	 */
	public boolean canHaveAsNewSchool(School school) {
		return (getSchool() != null && !isTerminated() && !school.isTerminated());
	}
	
	/**
	 * This method is used to arrange the switch of a slime to a new school
	 * 
	 * @param school
	 * 			This parameter is used as new school
	 * 
	 * @throws IllegalArgumentException 
	 * 		 ...
	 * 		|!canHaveAsNewSchool(school)
	 * @post ...
	 * 		| for(Slime oldSchoolSlime: this.getSchool().getSlimes()) 
	 *		|	if(oldSchoolSlime != this) then 
	 *		|		this.getHitPoints().setPoint(-1) && oldSchoolSlime.getHitPoints().setPoint(+1)
	 * @post ...
	 *		|if(getWorld() != null) then getWorld().transfer(this, this.getSchool(), school)
	 * @post ...
	 *		|for(Slime newSchoolSlime: this.getSchool().getSchool()) 
	 *		|	if(newSchoolSlime != this) then
	 *		|		newSchoolSlime.getHitPoints().setPoint(-1) && this.getHitPoints().setPoint(+1)
	 *
	 */
	@Raw
	public void setNewSchool(School school) throws IllegalArgumentException{
		if(!canHaveAsNewSchool(school)) throw new IllegalArgumentException("You can not use this school as new school for this slime");
		for(Slime oldSchoolSlime: this.getSchool().getSlimes()) {
			if(oldSchoolSlime != this) {
				this.updateHitPoints(-1);
				oldSchoolSlime.updateHitPoints(1);
			}
		}
		if(getWorld() != null) {
			getWorld().transfer(this, this.getSchool(), school);
		}
		for(Slime newSchoolSlime: this.getSchool().getSlimes()) {
			if(newSchoolSlime != this) {
				newSchoolSlime.updateHitPoints(-1);
				this.updateHitPoints(1);
			}
		}
	}
	
	/**
	 * This method is used to clear the id
	 * 
	 * @effect ...
	 * 		| this.setId(0L)
	 */
	public void clearId() {
		this.setId(0L);
	}
	
	/**
	 * This method is used to synchronize the school
	 * 
	 * @post...
	 * 		| if(getSchool() == null) then return
	 *		| getSchool().getSlimes().stream().filter(slime -> slime != this).forEach(slime -> slime.setHit(-1))
	 *
	 */
	protected void synchronizeSchool() {
		if(getSchool() == null) return;
		getSchool().getSlimes().stream().filter(slime -> slime != this).forEach(slime -> slime.updateHitPoints(-1));
	}

	/**
	 * This method is used to arrange the horizontal movement using deltaT as time
	 * 
	 * @param deltaT
	 * 			This parameter is used as time
	 * 
	 * @post...
	 * 		| super.updateX(deltaT) && super.getVelocity().accelerateX(getAcceleration(), deltaT)
	 * 
	 */
	@Override @Raw
	public void run(double deltaT) {
		super.updateX(deltaT); kinematics.updateHorizontalVelocity(deltaT);
	}
	
	/**
	 * This method will set to time elements to end the movement
	 * 
	 * @param deltaT
	 * 			This parameter is unused but must be implemented of interface Run
	 * 
	 * @post ...
	 * 		|  if(getOrientation() == Orientation.NEGATIVE.getValue() && !super.getWorld().shallBePassable(super.getLeftBorder())||
	 *		|		getOrientation() == Orientation.POSITIVE.getValue() && !super.getWorld().shallBePassable(super.getRightBorder()))
	 *		| then super.getAcceleration().setX(0.0); super.getVelocity().setX(0.0)
	 *@post ...
	 *		| Set<GameObject> objects = new HashSet<>()
	 *		| if(getOrientation() == Orientation.NEGATIVE.getValue()) then objects = super.overlappingGameObject(super.getLeftBorder())
	 *		| if(getOrientation() == Orientation.POSITIVE.getValue()) then objects = super.overlappingGameObject(super.getRightBorder())
	 *		| for(GameObject object: objects) 
	 *		|	if(object instanceof Slime && object != this) then arrangeSwitch((Slime)object) && break
	 */
	public void endRun() {
		if((getOrientation() == -1 && !super.getWorld().shallBePassable(super.getLeftBorder()))||
				(getOrientation() == 1 && !super.getWorld().shallBePassable(super.getRightBorder()))) {
			kinematics.setHorizontalAcceleration(0.0);
			kinematics.setHorizontalVelocity(0.0);
		} 
		Set<Organism> objects = new HashSet<>();
		if(getOrientation() == -1) objects = super.overlappingGameObject(super.getLeftBorder());
		if(getOrientation() == 1) objects = super.overlappingGameObject(super.getRightBorder());
		for(Organism object: objects) {
			if(object instanceof Slime && object != this) {
				arrangeSwitch((Slime)object); break;
			}else {
				kinematics.setHorizontalVelocity(0.0);
			}
		}
	}
	
	/**
	 * This method is used to arrange the school switch of a slime
	 * 
	 * @param slime
	 * 			This parameter is used as slime
	 * 
	 * @post ...
	 *		|if(slime.getSchool().getSchool().size() < this.getSchool().getSchool().size()) then
	 *		|	setNewSchool(slime, this.getSchool())
	 *		|else if( this.getSchool().getSchool().size() < slime.getSchool().getSchool().size()) then 
	 *		|	setNewSchool(this, slime.getSchool())
	 *@post ... 
	 *		|slime.getAcceleration().setX(-slime.kinematics.getHorizontalAcceleration())
	 *		|this.getAcceleration().setX(-kinematics.getHorizontalAcceleration())
	 *		|slime.getVelocity().setX(0.0)
	 *		|this.getVelocity().setX(0.0)
	 *		|setSprite(1- getIndex())
	 *		|slime.setSprite(1- slime.getIndex())
	 */
	private void arrangeSwitch(Slime slime) {
		slime.kinematics.setHorizontalAcceleration(-slime.kinematics.getHorizontalAcceleration());
		this.kinematics.setHorizontalAcceleration(-kinematics.getHorizontalAcceleration());
		slime.kinematics.setHorizontalVelocity(0.0); 
		this.kinematics.setHorizontalVelocity(0.0);
		if(slime.getSchool() != null) {
			if(slime.getSchool().getSlimes().size() < getSchool().getSlimes().size()) {
				slime.setNewSchool(this.getSchool());
			}else if(getSchool().getSlimes().size() < slime.getSchool().getSlimes().size()) {
				setNewSchool(slime.getSchool());
			}
		}
		slime.setSprite(1- slime.getIndex());
		this.setSprite(1- getIndex());
	}
	
	/**
	 * This method is used to update the situation of the slime using deltaT as passing time 
	 * 
	 * @param deltaT
	 * 			This parameter is used as time
	 * 
	 * @throws IllegalArgumentException
	 * 			...
	 * 		|Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)
	 * @post ...
	 * 		| for(double time = 0.0, dt = super.updateDt(deltaT, time); time < deltaT; time += dt, dt = super.updateDt(deltaT, time)) 
	 *		|	if(isDead()) then super.setDelay(getDelay() + dt)
	 *		|	if(getDelay() >= REMOVE_DELAY) then terminate()
	 *		|	arrangeFeatureHit(dt)
	 *		|	arrangeObjectHit(dt)
	 *		|	arrangeMovement(dt)
	 *		|	setBorders()
	 *
	 */
	public void advanceTime(double deltaT) throws IllegalArgumentException{
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		for(double time = 0.0, dt = super.updateDt(deltaT, time); time < deltaT; time += dt, dt = super.updateDt(deltaT, time)) {
			if(isDead()) super.setDelay(getDelay() + dt); 
			if(getDelay() >= REMOVE_DELAY) terminate();
			arrangeFeatureHit(dt);
			arrangeObjectHit(dt);
			arrangeMovement(dt);
		}
	}

	/**
	 * This method arrange the movement of the slime over interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * 
	 * @post ...
	 * 		| if(canRun()) then run(dt) else endRun(dt)
	 *		| if(!super.isInside()) then terminate()
	 *
	 */
	private void arrangeMovement(double dt) {
		if(canRun()) run(dt); else endRun();
		if(!super.isInside()) terminate();
	}

	/**
	 * This method arrange the hitPoint due to features over interval dt
	 * 
	 * @param dt
	 * 			This parameter is used as interval
	 * 
	 * @post ...
	 * 		|if(getHit() == 0) return
	 * 		|Boolean[] features =  getFeatureScore();
	 *		|if(features[0]) then super.getHitPoint().setPoint(-getHit()) && return
	 *@post ...
	 *		|if(features[1]) then setGasTime(getGasTime() + dt) 
	 *		|	and if(getGasTime() >= Constant.SLIME_GAS_TIME.getValue()) then super.getHitPoint().setPoint(Constant.SLIME_GAS.getValue())
	 *		|else setGasTime(0.0)
	 *		|if(getGasTime() >= Constant.SLIME_GAS_TIME.getValue()) then setGasTime(0.0)
	 *@post ...
	 *		|if(features[2]) then setWaterTime(getWaterTime() + dt) 
	 *		|	and if(getWaterTime() >= Constant.SLIME_WATER_TIME.getValue()) then super.getHitPoint().setPoint(Constant.SLIME_WATER.getValue())
	 *		|else setWaterTime(0.0)
	 *		|if(getWaterTime() >= Constant.SLIME_WATER_TIME.getValue()) then setWaterTime(0.0)
	 */
	private void arrangeFeatureHit(double dt) {
		if(getHitPoints() == 0) return;
		Boolean[] features =  getFeatureScore();
		if(features[0]) {super.updateHitPoints(-getHitPoints()); return;}
		if(features[1]) {
			setGasTime(getGasTime() + dt); 
			if(getGasTime() >= Constant.SLIME_GAS_TIME.getValue()) super.updateHitPoints((int) Constant.SLIME_GAS.getValue());
		}else { setGasTime(0.0);}
		if(features[2]) {
			setWaterTime(getWaterTime() + dt); 
				if(getWaterTime() >= Constant.SLIME_WATER_TIME.getValue()) { 
					super.updateHitPoints((int) Constant.SLIME_WATER.getValue()); synchronizeSchool();}
		}else { setWaterTime(0.0);}
		if(getGasTime() >= Constant.SLIME_GAS_TIME.getValue()) setGasTime(0.0);
		if(getWaterTime() >= Constant.SLIME_WATER_TIME.getValue()) setWaterTime(0.0);
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
	 *		|		getWorld().handleImpact(this, object, dt)
	 *
	 */
	private void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingObjects();
		if(getWorld() != null) {
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 0: arrangeMazubHit(dt);break;
				case 4: arrangeSharkHit(dt);break;
				default:break;
				}
			}
		}
	}

	/**
	 * This method returns the indication of the features where the slime is located
	 * 
	 * @return ...
	 * 		|Boolean[] features = new Boolean[] {false, false, false}
	 *		|if(getWorld() == null) return features
	 *		...
	 *		|for(int pixelX = super.getOrigin().getX(); pixelX < super.getOrigin().getX()+super.getRectangle().getDimension().getWidth(); pixelX++)
	 *		|	for(int pixelY= super.getOrigin().getY(); pixelY < super.getOrigin().getY()+super.getRectangle().getDimension().getHeight(); pixelY++)
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA)  features[0] = true
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS)  features[1] = true
	 *		|		if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER)  features[2] = true
	 *		|return features
	 *
	 */
	@Basic
	public Boolean[] getFeatureScore() {
		Boolean[] features = new Boolean[] {false, false, false};
		if(getWorld() == null) return features;
		for(int pixelX = super.getRectangle().getXCoordinate(); pixelX <= super.getRectangle().getXCoordinate()+super.getRectangle().getWidth()-1; pixelX++) {
			for(int pixelY= super.getRectangle().getYCoordinate(); pixelY <= super.getRectangle().getYCoordinate()+super.getRectangle().getHeight()-1; pixelY++) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) features[0] = true;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS)   features[1] = true;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) features[2] = true;
			}
		}
		return features;
	}
	
	public void arrangeMazubHit(double dt) {
		if(getBlockTime() == 0) {
			updateHitPoints((int) Constant.SLIME_MAZUB.getValue());
			synchronizeSchool();
		}
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}

	public void arrangeSharkHit(double dt) {
		if(getBlockTime() == 0) updateHitPoints(-getHitPoints());
		setBlockTime(getBlockTime() + dt);
		if(getBlockTime() >= Constant.TIMEOUT.getValue()) {
			setBlockTime(0.0);
		}
	}
	
}
