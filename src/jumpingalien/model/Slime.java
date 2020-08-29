package jumpingalien.model;

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
public class Slime extends Creature implements OnlyHorizontalMovable{
	
	private School school;
	private long id;
	private double gasTime = 0.0;
	private double waterTime = 0.0;
	private double mazubTime = 0.0;
	
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
	 * 		| super.getAcceleration().setX(0.7) && super.getAcceleration().setY(0.0) && 
	 * 		| super.getVelocity().setMinX(0.0) && super.getVelocity().setMaxX(X_MAX_VELOCITY)
	 * 
	 */
	public Slime(Set<Long> ids, int pixelLeftX, int pixelBottomY, long id, School school, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites);
		if(sprites.length != 2 || id < 0 || ids.contains(id)) throw new IllegalArgumentException("You must have exactly two images");
		this.setId(id);
		kinematics.setXAcceleration(0.7);
		kinematics.setMaxXVelocity(2.5);
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
	 * This method is used to clear the id
	 * 
	 * @effect ...
	 * 		| this.setId(0L)
	 */
	public void clearId() {
		this.setId(0L);
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
	

	public double getMazubTime() {
		return mazubTime;
	}

	public void setMazubTime(double mazubTime) {
		this.mazubTime = mazubTime;
		if(this.mazubTime >= Constant.TIMEOUT.getValue()) this.mazubTime = 0.0;
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
	public void setSchool(School school){
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
	public void setNewSchool(School school) {
		if(!canHaveAsNewSchool(school)) throw new IllegalArgumentException("You can not use this school as new school for this slime");
		Transferrator transferrator = new Transferrator() {
			
			@Override
			public void transfer(School nextSchool) {
				for(Slime oldSchoolSlime: Slime.this.getSchool().getSlimes()) {
					if(oldSchoolSlime != Slime.this) {
						Slime.this.updateHitPoints(-1);
						oldSchoolSlime.updateHitPoints(1);
					}
				}
				Slime.this.getSchool().removeSlime(Slime.this);
				nextSchool.addSlime(Slime.this);
				Slime.this.setSchool(nextSchool);
				for(Slime newSchoolSlime: Slime.this.getSchool().getSlimes()) {
					if(newSchoolSlime != Slime.this) {
					newSchoolSlime.updateHitPoints(-1);
					Slime.this.updateHitPoints(1);
					}
				}
			}
		};
		transferrator.transfer(school);
	}

	@Override
	protected boolean canMoveInCurrentState() {
		if(isRunningLeft()) return canRunLeft();
		if(isRunningRight()) return canRunRight();
		return false;
	}
	
	@Override
	protected void setNewState() {
		if(isMoving()) endRun();
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
		int tileLength = super.getWorld().getTileLength();
		int newX =0;
		int newY = 0;
		for(int pixelX = super.getRectangle().getX(); pixelX < super.getRectangle().getX()+super.getRectangle().getWidth()-1; pixelX+=newX) {
			for(int pixelY= super.getRectangle().getY(); pixelY < super.getRectangle().getY()+super.getRectangle().getHeight()-1; pixelY+=newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) features[0] = true;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS)   features[1] = true;
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) features[2] = true;
				newY = Math.min(tileLength, super.getRectangle().getY() + super.getRectangle().getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, super.getRectangle().getX() + super.getRectangle().getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return features;
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
	protected void arrangeFeatureHit(double dt) {
		if(getPoints() == 0) return;
		Boolean[] features =  getFeatureScore();
		if(Boolean.TRUE.equals(features[0])) {super.updateHitPoints(-getPoints()); synchronizeSchool();}
		if(Boolean.TRUE.equals(features[1])) {
			setGasTime(getGasTime() + dt); 
			if(getGasTime() >= Constant.SLIME_GAS_TIME.getValue()) 
				super.updateHitPoints((int) Constant.SLIME_GAS.getValue());
		}else { setGasTime(0.0);}
		if(Boolean.TRUE.equals(features[2])) {
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
	protected void arrangeObjectHit(double dt) {
		Set<Organism> objects = getCollidingCreatures();
		if(mazubTime != 0) setMazubTime(mazubTime + dt);
		if(getWorld() != null) {
			for(Organism object: objects) {
				int type = getGameObjectType(object);
				switch(type) {
				case 0: arrangeMazubHit(dt);break;
				case 3: arrangeSwitch((Slime) object); break;
				case 4: arrangeSharkHit();break;
				default:break;
				}
			}
		}
	}
	
	public void arrangeMazubHit(double dt) {
		if(mazubTime == 0) {
			updateHitPoints((int) Constant.SHARK_MAZUB.getValue());
			setMazubTime(mazubTime + dt);
			synchronizeSchool();
		}
	}

	public void arrangeSharkHit() {
		updateHitPoints(-getPoints());
		synchronizeSchool();
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
		if(slime.isRunningLeft()) {
			slime.startRunLeft(); 
			startRunRight();
		}else {
			startRunLeft(); 
			slime.startRunRight();
		}
		School newSchool = null;
		if(slime.getSchool().getSlimes().size() < getSchool().getSlimes().size()) {
			newSchool = this.getSchool();
		}else if(getSchool().getSlimes().size() < slime.getSchool().getSlimes().size()) {
			newSchool = slime.getSchool();
			slime = this;
		}
		if(newSchool != null) slime.setNewSchool(newSchool);
		slime.setSprite(1 - slime.getIndex());
		this.setSprite(1 - getIndex());
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
	protected void arrangeMovement(double dt) {
		if(isMoving()) run(dt);
		if(!super.isInside()) terminate();
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
		assert(!isDead() && canStartRunRight());
		super.setSprite(0);
		kinematics.setXAcceleration(0.7);	
	}

	@Override
	public void endRunRight() {
		assert(isRunningRight() &&  !isDead());
		kinematics.setXVelocity(0.0);
		kinematics.setXAcceleration(0.0);
	}

	@Override
	public void startRunLeft() {
		assert(!isDead() && canStartRunLeft());
		super.setSprite(1);
		kinematics.setXAcceleration(-0.7);
	}

	@Override
	public void endRunLeft() {
		assert(isRunningLeft() &&  !isDead());
		kinematics.setXVelocity(0.0);
		kinematics.setXAcceleration(0.0);
	}

	@Override
	public void run(double deltaT) {
		super.updateHorizontalComponent(this.getPosition().getX() + (kinematics.getXVelocity()*deltaT)+ (kinematics.getXAcceleration()*deltaT*deltaT/2));
		kinematics.updateXVelocity(deltaT);
	}
	
}
