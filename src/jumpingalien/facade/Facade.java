package jumpingalien.facade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jumpingalien.model.*;
import jumpingalien.util.*;

public class Facade implements IFacade {
	
	Set<Long> ids = new HashSet<>();
	
	public Facade() {
	}
	
	@Override
	public boolean isTeamSolution() {
		return true;
	}
	
	@Override
	public boolean isLateTeamSplit() {
		return false;
	}
	
	@Override
	public boolean hasImplementedWorldWindow() {
		return true;
	}	
	
	@Override
	public Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		try{ 
			return new Mazub(pixelLeftX, pixelBottomY, sprites);
		}catch(NullPointerException nul) {
			throw new ModelException("You need to specify the images the right way.");
		}catch (IllegalArgumentException ill){
			throw new ModelException("You can not create a Mazub with given parameters");
		}
	}
	
	@Override
	public double[] getActualPosition(Mazub alien) throws ModelException {
		return alien.getActualPosition();
	}
	
	@Override
	public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
		try{
			alien.changeActualPosition(newPosition);
		}catch (IllegalArgumentException ill) {
			throw new ModelException("You can not change the position with given parameters");
		}
	}
	
	@Override
	public int[] getPixelPosition(Mazub alien) throws ModelException {
		return new int[] {alien.getRectangle().getX(), alien.getRectangle().getY()};
	}
	
	@Override
	public int getOrientation(Mazub alien) throws ModelException {
		return alien.getOrientation();
	}
	
	@Override
	public double[] getVelocity(Mazub alien) throws ModelException {
		return alien.getVelocity();
	}
	
	@Override
	public double[] getAcceleration(Mazub alien) throws ModelException {
		return alien.getAcceleration();
	}
	
	@Override
	public boolean isMoving(Mazub alien) throws ModelException {
		return alien.isMoving();
	}
	
	@Override
	public void startMoveLeft(Mazub alien) throws ModelException {
		try{
			alien.startRunLeft();
		}catch(AssertionError ass) {
			throw new ModelException("You can not make the alien make run left");
		}
	}
	
	@Override
	public void startMoveRight(Mazub alien) throws ModelException {
		try{
			alien.startRunRight();
		}catch(AssertionError ass) {
			throw new ModelException("You can not make the alien make run right");
		}
	}
	
	@Override
	public void endMove(Mazub alien) throws ModelException {
		try{
			alien.endRun();
		}catch(AssertionError ass) {
			throw new ModelException("You can not make the alien make end running");
		}
	}
	
	@Override
	public boolean isJumping(Mazub alien) throws ModelException {
		return alien.isJumping();
	}
	
	@Override
	public void startJump(Mazub alien) throws ModelException {
		try {
			alien.startJump();
		} catch (Exception exc) {
			throw new ModelException("Mazub can not start jump if he did jump before");
		}
	}
	
	@Override
	public void endJump(Mazub alien) throws ModelException {
		try {
			alien.endJump();
		} catch (Exception exc) {
			throw new ModelException("Mazub can not stop jumping if he didn' t jump before it.");
		}
	}
	
	@Override
	public boolean isDucking(Mazub alien) throws ModelException {
		return alien.isDucking();
	}
	
	@Override
	public void startDuck(Mazub alien) throws ModelException {
		alien.startDuck();
	}
	
	@Override
	public void endDuck(Mazub alien) throws ModelException {
		alien.endDuck();
	}
	
	public int getHeight(Mazub alien) throws ModelException {
		return alien.getImageHeight();
	}
	
	public int getWidth(Mazub alien) throws ModelException {
		return alien.getImageWidth();
	}
	
	@Override
	public Sprite getCurrentSprite(Mazub alien) throws ModelException {
		return alien.getSprite();
	}
	
	@Override
	public Sprite[] getSprites(Mazub alien) throws ModelException {
		return alien.getSprites();
	}
	
	@Override
	public Sprite getCurrentSprite(Object gameObject) throws ModelException {
		return ((Organism) gameObject).getSprite();
	}

	@Override
	public World createWorld(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate,
			int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws ModelException {
		try {
			return new World(tileSize, nbTilesX, nbTilesY, targetTileCoordinate, visibleWindowWidth,
					visibleWindowHeight, 100, geologicalFeatures);
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not create a world with given parameters");
		}
	}
	
	@Override
	public void terminateWorld(World world) throws ModelException {
		world.terminate();
	}
	
	@Override
	public int[] getSizeInPixels(World world) throws ModelException {
		return new int[] {world.getGameWorldWidth(), world.getGameWorldHeight()};
	}
	
	@Override
	public int getTileLength(World world) throws ModelException {
		return world.getTileLength();
	}
	
	@Override
	public int getGeologicalFeature(World world, int pixelX, int pixelY) throws ModelException {
		return world.getTileFeature(pixelX, pixelY).getFeatureId();
	}
	
	@Override
	public void setGeologicalFeature(World world, int pixelX, int pixelY, int geologicalFeature) throws ModelException {
		world.setGeoFeature(pixelX, pixelY, geologicalFeature);
	}
	
	@Override
	public int[] getVisibleWindowDimension(World world) throws ModelException {
		return new int[] {world.getVisibleWindowWidth(), world.getVisibleWindowHeight()};
	}
	
	@Override
	public int[] getVisibleWindowPosition(World world) throws ModelException {
		return new int[] {world.getVisibleWindowXCoordinate(), world.getVisibleWindowYCoordinate()};
	}
	
	@Override
	public boolean hasAsGameObject(Object object, World world) throws ModelException {
		return world.hasProperGameObject((Organism)object);
	}
	
	@Override
	public Set<? extends Object> getAllGameObjects(World world) throws ModelException {
		Set<Object> result = new HashSet<>();
		for(Object object : world.getGameObjects()) result.add(object);
		return result;
	}
	
	@Override
	public Mazub getMazub(World world) throws ModelException {
		return world.getPlayer();
	}
	
	@Override
	public void addGameObject(Object object, World world) throws ModelException {
		try{
			world.addGameObject((Organism)object);
		}catch(NullPointerException nul) {
			throw new ModelException("You can not use a null as object or world");
		}catch(IllegalArgumentException ill){
			throw new ModelException("You can not add this object to this world");
		}
	}
	
	@Override
	public void removeGameObject(Object object, World world) throws ModelException {
		try{
			world.removeGameObject((Organism) object);
		}catch(NullPointerException nul) {
			throw new ModelException("You can not use a null as object or world");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not remove this object off this world");
		}
	}
	
	@Override
	public int[] getTargetTileCoordinate(World world) throws ModelException {
		return new int[] {world.getTargetTileXCoordinate(), world.getTargetTileYCoordinate()};
	}
	
	@Override
	public void setTargetTileCoordinate(World world, int[] tileCoordinate) throws ModelException {
		world.setTargetTile(tileCoordinate);
	}
	
	@Override
	public void startGame(World world) throws ModelException {
		try{
			world.setStarted();
		}catch(IllegalStateException ise) {
			throw new ModelException("You can not start the game without a playable charachter.");
		}
	}
	
	@Override
	public boolean isGameOver(World world) throws ModelException {
		return world.isGameOver();
	}
	
	@Override
	public boolean didPlayerWin(World world) throws ModelException {
		return world.getPlayer() != null && world.getPlayer().getRectangle().overlaps(world.getTargetTile().getRectangle());
	
	}
	
	@Override
	public void advanceWorldTime(World world, double dt) {
		try {
			world.advanceTime(dt);
		} catch (IllegalArgumentException ill) { 
			throw new ModelException("You can not advance the world time with that amount of time");
		}
	}
	
	@Override
	public Set<School> getAllSchools(World world) throws ModelException {
		Set<School> result = new HashSet<>();
		for(Object object : world.getSchools()) result.add((School) object);
		return result;
	}
	
	@Override
	public void terminateGameObject(Object object) throws ModelException {
		((Organism) object).terminate();
	}
	
	@Override
	public boolean isTerminatedGameObject(Object object) throws ModelException {
		return ((GameObject) object).isTerminated();
	}
	
	@Override
	public boolean isDeadGameObject(Object object) throws ModelException {
		return ((Organism) object).isDead();
	}
	
	@Override
	public double[] getActualPosition(Object object) throws ModelException {
		return new double[] {((Organism) object).getPosition().getX(), ((Organism) object).getPosition().getY()};
	}
	
	@Override
	public void changeActualPosition(Object object, double[] newPosition) throws ModelException {
		((Organism) object).changeActualPosition(newPosition);
	}
	
	@Override
	public int[] getPixelPosition(Object object) throws ModelException {
		return new int[] {((Organism)object).getRectangle().getX(), ((Organism)object).getRectangle().getY()};
	}
	
	@Override
	public int getOrientation(Object object) throws ModelException {
		return ((Organism) object).getOrientation();
	}
	
	@Override
	public double[] getVelocity(Object object) throws ModelException {
		return ((Organism) object).getVelocity();
	}
	
	@Override
	public double[] getAcceleration(Object object) throws ModelException {
		return ((Organism) object).getAcceleration();
	}
	
	@Override
	public World getWorld(Object object) throws ModelException {
		if(object instanceof Organism) {
			return ((Organism) object).getWorld();
		}
		return ((School) object).getWorld();
	}
	
	@Override
	public int getHitPoints(Object object) throws ModelException {
		return ((Organism) object).getPoints();
	}
	
	@Override
	public Sprite[] getSprites(Object gameObject) throws ModelException {
		return ((Organism) gameObject).getSprites();
	}
	
	@Override
	public void advanceTime(Object gameObject, double dt) throws ModelException {
		try {
			((Organism) gameObject).advanceTime(dt);
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not advance the time with given parameter");
		}
	}
	
	@Override
	public Sneezewort createSneezewort(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		try {
			return new Sneezewort(pixelLeftX, pixelBottomY, sprites);
		}catch(NullPointerException nul) {
			throw new ModelException("The given sprites are null and so can not be used");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not have more or less than two defined images for a sneezewort");
		}
	}
	
	@Override
	public Skullcab createSkullcab(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		try {
			return new Skullcab(pixelLeftX, pixelBottomY, sprites);
		}catch(NullPointerException nul) {
			throw new ModelException("The given sprites are null and so can not be used");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not have more or less than two defined images for a skullcab");
		}
	}
	
	@Override
	public Slime createSlime(long id, int pixelLeftX, int pixelBottomY, School school, Sprite... sprites)
			throws ModelException {
		try {
			Set<Long> iDs = new HashSet<>(); this.ids.stream().forEach(iD -> iDs.add(iD));
			Slime slime = new Slime(iDs, pixelLeftX, pixelBottomY, id, school, sprites);
			this.ids.add(id);
			return slime;
		}catch(NullPointerException nul) {
			throw new ModelException("The given sprites are null and so can not be used");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not have more or less than two defined images for a slime");
		}
	}
	
	@Override
	public long getIdentification(Slime slime) throws ModelException {
		return slime.getId();
	}
	
	@Override
	public void cleanAllSlimeIds() {
		this.ids.clear();
	}
	
	@Override
	public School createSchool(World world) throws ModelException {
		try {
			return new School(world);
		}catch(IllegalStateException ise) {
			throw new ModelException("You can not have more than 10 schools in a game world");
		}
	}
	
	@Override
	public void terminateSchool(School school) throws ModelException {
		school.terminate();
	}
	
	@Override
	public boolean hasAsSlime(School school, Slime slime) throws ModelException {
		return school.getSlimes().contains(slime);
	}
	
	@Override
	public Collection<? extends Slime> getAllSlimes(School school) {
		return school.getSlimes();
	}
	
	@Override
	public void addAsSlime(School school, Slime slime) throws ModelException {
		try {
			school.addSlime(slime);
		}catch(NullPointerException nul){
			throw new ModelException("You can not use not existing object");
		}catch(IllegalArgumentException ill){
			throw new ModelException("You can not use this parameters");
		}
	}
	
	@Override
	public void removeAsSlime(School school, Slime slime) throws ModelException {
		try {
			school.removeSlime(slime);
		}catch(NullPointerException nul){
			throw new ModelException("You can not use not existing object");
		}catch(IllegalArgumentException ill){
			throw new ModelException("You can not use this parameters");
		}
	}
	
	@Override
	public void switchSchool(School newSchool, Slime slime) throws ModelException {
		try {
			slime.setNewSchool(newSchool);
		}catch(NullPointerException nul) {
			throw new ModelException("You can not use not existing objects");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not use terminated objects or that your"
					+ "slime has no school yet");
		}
	}
	
	@Override
	public School getSchool(Slime slime) throws ModelException {
		return slime.getSchool();
	}
	
	@Override
	public Shark createShark(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		try {
			return new Shark(pixelLeftX, pixelBottomY, sprites);
		}catch(NullPointerException nul) {
			throw new ModelException("The given sprites are not specified");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not create a Shark with given parameters");
		}
	}

	@Override
	public Spider createSpider(int nbLegs, int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		try{
			return new Spider(nbLegs, pixelLeftX, pixelBottomY, sprites);
		}catch(NullPointerException nul) {
			throw new ModelException("The given sprites are not specified");
		}catch(IllegalArgumentException ill) {
			throw new ModelException("You can not create a Spider with given parameters");
		}
	}

	@Override
	public int getNbLegs(Spider spider) throws ModelException {
		return spider.getLegs();
	}
}
