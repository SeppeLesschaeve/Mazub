package jumpingalien.model;

import annotate.Basic;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.storage.GameObjectStorage;
import jumpingalien.util.Sprite;
import jumpingalien.model.kinematics.Kinematics;
import java.util.HashSet;
import java.util.Set;

public abstract class GameObject{

	private Position<Double> position;
	private Collidable collider;
	private FeatureHandler featureHandler;
	private Kinematics kinematics;
	private GameObjectVisualizer visualizer;
	private World world;
	private double timeStep;
	private double delay;

	public GameObject(int xCoordinate, int yCoordinate, Sprite...sprites) {
		if(xCoordinate < 0 || yCoordinate < 0) throw new IllegalArgumentException("You can not create the alien outside the universe");
		for(Sprite sprite: sprites)
			if(sprite == null) throw new IllegalArgumentException();
		this.setCollider();
		this.setFeatureHandler();
		this.setKinematics();
		this.setVisualizer(xCoordinate, yCoordinate, sprites);
		this.position = new Position<>(xCoordinate*0.01, yCoordinate*0.01);
	}

	private void setCollider() {
		this.collider = initializeCollider();
	}

	protected abstract Collidable initializeCollider();

	private void setFeatureHandler(){
		this.featureHandler = initializeFeatureHandler();
	}

	protected abstract FeatureHandler initializeFeatureHandler();

	private void setKinematics() {
		this.kinematics = initializeKinematics();
	}

	protected abstract Kinematics initializeKinematics();

	private void setVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		this.visualizer = initializeVisualizer(xCoordinate, yCoordinate, sprites);
	}

	public Kinematics getKinematics(){
		return kinematics;
	}

	protected abstract GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites);

	public double getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(double timeStep) {
		this.timeStep = timeStep;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
		if(this.delay > Constant.REMOVE_DELAY.getValue())
			this.delay = Constant.REMOVE_DELAY.getValue();
	}

	public abstract double[] getAcceleration();
	public abstract double[] getVelocity();

	public boolean hasProperWorld() {
		return canHaveAsWorld(getWorld()) && (getWorld() ==  null || getWorld().hasProperGameObject(this));
	}
	
	public boolean canHaveAsWorld(World world) {
		if(world != null) return this.world == world;
		return !isTerminated();
	}

	public double getXPosition(){
		return position.getXCoordinate();
	}

	public int getXCoordinate(){
		return visualizer.getXCoordinate();
	}

	public void setXCoordinate(double xCoordinate) {
		if(xCoordinate >= 0){
			visualizer.setXCoordinate((int)(xCoordinate/0.01));
		}else{
			visualizer.setXCoordinate((int)((xCoordinate-0.01)/0.01));
		}
		position.setXCoordinate(xCoordinate);
	}

	public double getYPosition(){
		return position.getYCoordinate();
	}

	public int getYCoordinate(){
		return visualizer.getYCoordinate();
	}
	
	public void setYCoordinate(double yCoordinate) {
		if(yCoordinate >= 0){
			visualizer.setYCoordinate((int)(yCoordinate/0.01));
		}else{
			visualizer.setYCoordinate((int)((yCoordinate-0.01)/0.01));
		}
		position.setYCoordinate(yCoordinate);
	}

	public void changeActualPosition(double[] newPosition){
		if(newPosition == null || newPosition.length != 2 || (((Double) newPosition[0]).isNaN() || ((Double) newPosition[1]).isNaN()))
			throw new IllegalArgumentException("The desired position can not be null, must be two-dimensional and not NaN values.");
		setXCoordinate(newPosition[0]);
		setYCoordinate(newPosition[1]);
		if(getWorld() != null && !getWorld().isInside(getXCoordinate(), getYCoordinate())) terminate();
	}

	protected Rectangle getUpBorder() {
		Rectangle up = null;
		if(getVelocity()[0] < 0) {
			up = new Rectangle(getXCoordinate() - 1, getYCoordinate() + getImageHeight(), getImageWidth(), 1);
		}else if(getVelocity()[0] == 0) {
			up = new Rectangle(getXCoordinate(), getYCoordinate() + getImageHeight(), getImageWidth(), 1);
		}else if(getVelocity()[0] > 0) {
			up = new Rectangle(getXCoordinate() + 1, getYCoordinate() + getImageHeight(), getImageWidth(), 1);
		}
		return up;
	}

	protected Rectangle getDownBorder() {
		Rectangle down = null;
		if(getVelocity()[0] < 0) {
			down = new Rectangle(getXCoordinate() - 1, getYCoordinate() - 1, getImageWidth(), 1);
		}else if(getVelocity()[0] == 0) {
			down = new Rectangle(getXCoordinate(), getYCoordinate() - 1, getImageWidth(), 1);
		}else if(getVelocity()[0] > 0) {
			down = new Rectangle(getXCoordinate() + 1, getYCoordinate() - 1, getImageWidth(), 1);
		}
		return down;
	}

	protected Rectangle getLeftBorder() {
		return new Rectangle(getXCoordinate() - 1, getYCoordinate() + 1, 1, getImageHeight() - 1);
	}

	protected Rectangle getRightBorder() {
		return new Rectangle(getXCoordinate() + getImageWidth(), getYCoordinate() + 1, 1, getImageHeight() - 1);
	}
	
	@Basic
	public Sprite[] getImages() {
		return visualizer.getImages();
	}

	public int getImage() {
		return visualizer.getImage();
	}

	public void setImage(int index) {
		visualizer.setImage(index);
	}

	@Basic
	public Sprite getCurrentImage() {
		return visualizer.getCurrentImage();
	}

	public int getImageWidth(){
		return visualizer.getImageWidth();
	}

	public int getImageHeight(){
		return visualizer.getImageHeight();
	}

	@Basic
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		if(this.world != null && !this.world.equals(world))
			throw new IllegalArgumentException("Already placed in another World");
		this.world = world;
	}

	public void unSetWorld() {
		this.world = null;
	}
	
	public boolean isTerminated() {
		if(isStillNotInAGameWorld()) return false;
		return world == null && isDead();
	}

	public boolean isInside() {
		if(isStillNotInAGameWorld()) return true;
		return world != null && world.isInside(getXCoordinate(), getYCoordinate());
	}
	
	public boolean isStillNotInAGameWorld() {
		return world == null && !isDead();
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof GameObject && this.hashCode() == other.hashCode();
	}
	
	@Override
	public int hashCode() {
		return position.hashCode();
	}

	public void advanceTime(double deltaT) {
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT >= 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		for(double time = 0.0; time < deltaT; time += getTimeStep()){
			setTimeStep(kinematics.calculateNewTimeSlice(deltaT, time));
			performDuringTimeStep();
			if(this.isTerminated()) break;
		}
	}

	protected abstract void performDuringTimeStep();

	public abstract boolean isDead();
	protected abstract void accept(Collidable collidable);

	public void terminate(){
		if(getWorld() == null) return;
		this.getWorld().removeGameObject(this);
	}

	public boolean overlaps(Rectangle rectangle) {
		return visualizer.overlaps(rectangle);
	}

	public void updateImage() {
		visualizer.updateImage();
	}

	protected Set<Creature> getCollidingCreatures(Rectangle border) {
		if(isStillNotInAGameWorld() || isTerminated()) return new HashSet<>();
		HashSet<Creature> collidingCreatures = new HashSet<>();
		for(GameObject object: getWorld().getOthers()) {
			try {
				if(object.overlaps(border) && object != this) {
					collidingCreatures.add((Creature) object);
				}
			}catch (ClassCastException ignored){
			}
		}
		return collidingCreatures;
	}

	public Set<Creature> getCollidingCreatures(int index) {
		return getCollidingCreatures(visualizer.getRectangleOfImage(index));
	}

	protected Set<Plant> getOverlappingPlants(){
		if(isStillNotInAGameWorld() || isTerminated()) return new HashSet<>();
		Set<Plant> overlappingPlants = new HashSet<>();
		Rectangle rectangle = new Rectangle(getXCoordinate(),getYCoordinate() , getImageWidth(), getImageHeight());
		for(Plant plant: this.getWorld().getPlants()) {
			if(plant.overlaps(rectangle) && plant != this) {
				overlappingPlants.add(plant);
			}
		}
		return overlappingPlants;
	}

	public Set<GameObject> getOverlappingCreatures(World world){
		if(world == null) return new HashSet<>();
		HashSet<GameObject> collidingCreatures = new HashSet<>();
		Rectangle rectangle = new Rectangle(getXCoordinate(),getYCoordinate() , getImageWidth(), getImageHeight());
		for(GameObject object: world.getOthers()) {
			if(object.overlaps(rectangle) && object != this) {
				collidingCreatures.add(object);
			}
		}
		return collidingCreatures;
	}

	public abstract int getOrientation();

	public abstract void addToStorage(GameObjectStorage worldStorage);

	public abstract void removeFromStorage(GameObjectStorage worldStorage);

	protected GameObjectVisualizer getVisualizer() {
		return visualizer;
	}

	protected Collidable getCollider() {
		return collider;
	}

	protected FeatureHandler getFeatureHandler() {
		return featureHandler;
	}
}
