package jumpingalien.facade;

import java.util.Collection;
import java.util.Set;
import jumpingalien.model.*;
import jumpingalien.internal.gui.sprites.JumpingAlienSprites;
import jumpingalien.util.ModelException;
import jumpingalien.util.ShouldNotImplementException;
import jumpingalien.util.Sprite;

/**
 * Implement this interface to connect your code to the graphical user interface
 * (GUI).
 * 
 * <ul>
 * <li>For separating the code that you wrote from the code that was provided to
 * you, put <b>ALL your code in the <code>src</code> folder.</b> The code that
 * is provided to you stays in the <code>src-provided</code> folder.
 * Note: classes that belong to some package can be spread over multiple source
 * folders. For example, two classes, one in <code>src</code> and one in
 * <code>src-provided</code>, can be declared to belong to the same package,
 * even though they are not located in the same source folder.</li>
 * 
 * <li>You should at least <b>create the following classes</b> in the package
 * <code>jumpingalien.model</code>:
 * <ul>
 * <li>a class <code>Mazub</code> for representing Mazub the alien</li>
 * <li>a class <code>World</code> for representing the game world</li>
 * <li>a class <code>Sneezewort</code> for representing Sneezewort</li>
 * <li>a class <code>Skullcab</code> for representing Skullcab</li>
 * <li>a class <code>Shark</code> for representing a shark enemy</li>
 * <li>a class <code>Slime</code> for representing a slime enemy</li>
 * <li>a class <code>School</code> for representing a slime school</li>
 * </ul>
 * You may, of course, add additional classes as you see fit.
 * 
 * <li>You should <b>create a class <code>Facade</code></b> that implements this
 * interface. This class should be placed <b>in the package
 * <code>jumpingalien.facade</code></b>.</li>
 * 
 * <li>
 * The <b>header of that Facade class</b> should look as follows:<br>
 * <code>class Facade implements IFacade { ... }</code><br>
 * Consult the <a href=
 * "http://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html">
 * Java tutorial</a> for more information on interfaces, if necessary.</li>
 *
 * <li>Your <code>Facade</code> class should offer a <b>default constructor</b>.
 * </li>
 * 
 * <li><b>Each method</b> defined in the interface <code>IFacade</code> must be
 * implemented by the class <code>Facade</code>. For example, the implementation
 * of <code>getActualPosition</code> should call one or more methods on the given
 * <code>alien</code> to retrieve its current location.</li>
 * 
 * <li>Methods in this interface are <b>only allowed to throw exceptions of type
 * <code>jumpingalien.util.ModelException</code></b> (this class is provided).
 * No other exception types are allowed. This exception can be thrown only if
 * calling a method of your classes with the given parameters would (1) violate
 * a precondition or (2) if the method of your throws an exception (if so,
 * wrap the exception in a <code>ModelException</code>).</li>
 * 
 * <li><b>ModelException should not be used anywhere outside of your Facade
 * implementation.</b></li>
 * 
 * <li>Your Facade implementation should <b>only contain trivial code</b> (for
 * example, calling a method, combining multiple return values into an array,
 * creating @Value instances, catching exceptions and wrapping it in a
 * ModelException). All non-trivial code should be placed in the other classes
 * that you create.</li>
 * 
 * <li>The rules described above and the documentation described below for each
 * method apply only to the class implementing IFacade. <b>Your class for
 * representing aliens should follow the rules described in the assignment.</b></li>
 * 
 * <li><b>Do not modify the signatures</b> of the methods defined in this
 * interface.</li>
 * 
 * </ul>
 *
 */
public interface IFacade {
	
	/**
	 * Return a boolean indicating whether the code at stake is the effort of a
	 * team of two students or the effort of an individual student. 
	 */
	boolean isTeamSolution();
	
	/**
	 * Return a boolean indicating whether the code at stake is the effort of an
	 * late team split (split after May 5). In that case, the code must include sharks.
	 */
	boolean isLateTeamSplit();
	
	/**
	 * Return a boolean indicating whether the code at stake has implemented the
	 * positioning of the world's window.
	 * This is especially meant for students that started in a team,
	 * but ended up working individually.
	 */
	boolean hasImplementedWorldWindow();

	/**
	 * Create an instance of Mazub with given pixel position and given sprites.
	 *   The actual position of the new Mazub will correspond with the coordinates
	 *   of the left-bottom corner of the left-bottom pixel occupied by the new Mazub.
	 *   The new Mazub will be stationary.
	 */
	Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException;

	/**
	 * Return the actual position of the given alien.
	 *   Returns an array of 2 doubles {x, y} that represents the coordinates of the
	 *   bottom-left corner of the given alien in the world.
	 *   Coordinates are expressed in meters.
	 */
	double[] getActualPosition(Mazub alien) throws ModelException;

	/**
	 * Change the actual position of the given alien to the given position.
	 */
	void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException;

	/**
	 * Return the pixel position of the given alien.
	 *   Returns an array of 2 integers {x, y} that represents the coordinates of the
	 *   pixel in the game world occupied by the bottom-left pixel of the given alien.
	 */
	int[] getPixelPosition(Mazub alien) throws ModelException;
	
	/**
	 * Return the orientation of the given alien.
	 *   The resulting value is negative if the given alien is oriented to the left,
	 *   0 if the given alien is oriented to the front and positive if the given
	 *   alien is oriented to the right.
	 */
	int getOrientation(Mazub alien) throws ModelException;
	
	/**
	 * Return the current velocity of the given alien.
	 *   Returns an array consisting of 2 doubles {vx, vy} that represents the
	 *   horizontal and vertical components of the given alien's current
	 *   velocity in m/s.
	 */
	double[] getVelocity(Mazub alien) throws ModelException;
	
	/**
	 * Return the current acceleration of the given alien.
	 *   Returns an array consisting of 2 doubles {ax, ay} that represents the
	 *   horizontal and vertical components of the given alien's current
	 *   acceleration in m/s^2.
	 */
	double[] getAcceleration(Mazub alien) throws ModelException;

	/**
	 * Return the sprites to be used to animate the given alien.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may ignore this method. 
	 */
	default Sprite[] getSprites(Mazub alien) throws ModelException, ShouldNotImplementException {
		if (!isTeamSolution()) {
			throw new ShouldNotImplementException("Not to be implemented by individual students");
		}
		throw new NoSuchMethodError("Teams of 2 students should implement this method.");
	}

	/**
	 * Return the current sprite image for the given alien.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may stick to the default implementation. 
	 */
	default Sprite getCurrentSprite(Mazub alien) throws ModelException {
		if (!isTeamSolution()) {
			return JumpingAlienSprites.DEFAULT_MAZUB_SPRITE;
		}
		throw new NoSuchMethodError("Teams of 2 students should implement this method.");
	}
	
	/**
	 * Return whether the given alien is moving.
	 */
	boolean isMoving(Mazub alien) throws ModelException;
	
	/**
	 * Make the given alien move to the left.
	 */
	void startMoveLeft(Mazub alien) throws ModelException;

	/**
	 * Make the given alien move to the right.
	 */
	void startMoveRight(Mazub alien) throws ModelException;

	/**
	 * End the given alien's move.
	 */
	void endMove(Mazub alien) throws ModelException;
	
	/**
	 * Return whether the given alien is jumping.
	 */
	boolean isJumping(Mazub alien) throws ModelException;

	/**
	 * Make the given alien jump.
	 */
	void startJump(Mazub alien) throws ModelException;

	/**
	 * End the given alien's jump.
	 */
	void endJump(Mazub alien) throws ModelException;
	
	/**
	 * Return whether the given alien is ducking.
	 */
	boolean isDucking(Mazub alien) throws ModelException;

	/**
	 * Make the given alien duck.
	 */
	void startDuck(Mazub alien) throws ModelException;

	/**
	 * End the given alien's ducking.
	 */
	void endDuck(Mazub alien) throws ModelException;

	/*****************
	 * World methods *
	 *****************/
	
	/**
	 * Create a new game world with given tile size, given number of tiles in both
	 * directions, with given coordinate for the target tile, with given width and height
	 * for the visible window, with its tiles filled with the given geological features, 
	 * and with no game objects yet.
	 *   The geological features are given in the order from left to right and (then) from
	 *   bottom to top. Tiles for which no geological feature is given, are filled with AIR. 
	 */
	World createWorld(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate,
				int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures)
		throws ModelException;
	
	/**
	 * Terminate the given world.
	 */
	void terminateWorld(World world) throws ModelException;
	
	/**
	 * Return the size of the given game world in number of pixels.
	 *   The resulting array contains the width of the given world in pixels followed
	 *   by the height of the given world in pixels.
	 */
	int[] getSizeInPixels(World world) throws ModelException;

	/**
	 * Return the length of the side of a tile in the given world.
	 *   The length is expressed as a number of pixels.
	 */
	int getTileLength(World world) throws ModelException;

	/**
	 * Return the geological feature of the tile in the given world at the given pixel position.
	 *   The function returns 0 for AIR, 1 for SOLID GROUND, 2 for WATER and 3 for MAGMA,
	 *   4 for ICE and 5 for GAS.
	 *   Throws ModelException if there is no tile at the given position.
	 *   This method must return its result in (nearly) constant time.
	 */
	int getGeologicalFeature(World world, int pixelX, int pixelY) throws ModelException;

	/**
	 * Set the geological feature of the tile in the given world at the given pixel position
	 * to the given geological feature.
	 */
	void setGeologicalFeature(World world, int pixelX, int pixelY, int geologicalFeature) throws ModelException;
	
	/**
	 * Return the dimension of the visible window of the given world.
	 *   The resulting array contains the width of the visible window in pixels followed
	 *   by the height of the visible window in pixels.
	 */
	int[] getVisibleWindowDimension(World world) throws ModelException;
	
	/**
	 * Return the position of the bottom left corner of the visible window of the given world.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may stick to the default implementation. 
	 */
	default int[] getVisibleWindowPosition(World world) throws ModelException {
		if (!isTeamSolution()) {
			try {
				int[] mazubPixelPosition = getPixelPosition(getMazub(world));
				return new int[]
					{ (int)Math.max(mazubPixelPosition[0]-100, 0),
					  (int)Math.max(mazubPixelPosition[1]-50, 0.0)};
			} catch (Exception exc) {
				throw new ModelException(exc);
			}
		}
		throw new NoSuchMethodError("Teams of 2 students should implement this method.");
	}

	/**
	 * Check whether the given world has has the given object as one of its game objects.
	 */
	boolean hasAsGameObject(Object object, World world) throws ModelException;
	
	/**
	 * Return a collection of all the game objects in the given world.
	 */
	Set<? extends Object> getAllGameObjects(World world) throws ModelException;
	
	/**
	 * Return the mazub under control of the end user in the given world.
	 */
	Mazub getMazub(World world) throws ModelException;
	
	/**
	 * Add the given object to the given world.
	 */
	void addGameObject(Object object, World world) throws ModelException;
	
	/**
	 * Remove the given object to the given world.
	 */
	void removeGameObject(Object object, World world) throws ModelException;
	
	/**
	 * Return the coordinate of the target tile in the given world.
	 *   Returns an array of 2 integers {x, y} that represents the tile coordinate
	 *   of the target tile.
	 */
	int[] getTargetTileCoordinate(World world) throws ModelException;
	
	/**
	 * Set the coordinate of the target tile in the given world to the given
	 * tile coordinate.
	 */
	void setTargetTileCoordinate(World world, int[] tileCoordinate) throws ModelException;
	
	/**
	 * Start a game in the given world.
	 */
	void startGame(World world) throws ModelException;

	/**
	 * Returns whether the game, played in the given game world, is over.
	 * The game is over when either Mazub is terminated and removed from its world, 
	 * or when mazub has reached the target tile.
	 */
	boolean isGameOver(World world) throws ModelException;

	/**
	 * Returns whether the game played in the given world has finished and the
	 * player has won. The player wins when Mazub has reached the target tile.
	 */
	boolean didPlayerWin(World world) throws ModelException;

	/**
	 * Advance the time for the world and all its objects by the given amount.
	 */
	void advanceWorldTime(World world, double dt);
	
	/**
	 * Return a set of all schools in the given world.
	 */
	Set<School> getAllSchools(World world) throws ModelException;

	/***********************
	 * Game Object methods *
	 ***********************/
	
	/**
	 * Terminate the given game object.
	 */
	void terminateGameObject(Object gameObject) throws ModelException;
	
	/**
	 * Check whether the given game object is terminated.
	 *    Terminated game objects can not be located in a world.
	 */
	boolean isTerminatedGameObject(Object gameObject) throws ModelException;
	
	/**
	 * Check whether the given game object is dead.
	 *    Dead game objects can still be located in a world. After some period of time,
	 *    dead game objects are terminated.
	 */
	boolean isDeadGameObject(Object gameObject) throws ModelException;

	/**
	 * Return the actual position of the given game object.
	 *   Returns an array of 2 doubles {x, y} that represents the coordinates of the
	 *   bottom-left corner of the given game object.
	 *   Coordinates are expressed in meters.
	 */
	double[] getActualPosition(Object gameObject) throws ModelException;

	/**
	 * Change the actual position of the given game object to the given position.
	 */
	void changeActualPosition(Object gameObject, double[] newPosition) throws ModelException;

	/**
	 * Return the pixel position of the given game object.
	 *   Returns an array of 2 integers {x, y} that represents the coordinates of the
	 *   pixel occupied by the bottom-left pixel of the given game object.
	 */
	int[] getPixelPosition(Object gameObject) throws ModelException;

	/**
	 * Return the orientation of the given game object.
	 *   The resulting value is
	 *   - negative if the given game object is oriented to the left or to the bottom,
	 *   - 0 if the given game object is oriented to the front, and
	 *   - positive if the given game object is oriented to the right or to the top.
	 */
	int getOrientation(Object gameObject) throws ModelException;
	
	/**
	 * Return the current velocity of the given game object.
	 *   Returns an array consisting of 2 doubles {vx, vy} that represents the
	 *   horizontal and vertical components of the given game object's current
	 *   velocity in m/s.
	 */
	double[] getVelocity(Object gameObject) throws ModelException;

	/**
	 * Return the current acceleration of the given game object.
	 */
	double[] getAcceleration(Object gameObject) throws ModelException;
	
	/**
	 * Return the world in which the given game object is positioned.
	 *   Returns null if the object is not positioned in a world.
	 */
	World getWorld(Object object) throws ModelException;
	
	/**
	 * Return the hit points of the given game object.
	 */
	int getHitPoints(Object object) throws ModelException;
	
	/**
	 * Return the sprites to be used to animate the given game object.
	 */
	Sprite[] getSprites(Object gameObject) throws ModelException;

	/**
	 * Return the current sprite image for the given game object.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may stick to the default implementation. 
	 */
	default Sprite getCurrentSprite(Object gameObject) throws ModelException {
		if (!isTeamSolution()) {
			if (gameObject instanceof Mazub) {
				return JumpingAlienSprites.DEFAULT_MAZUB_SPRITE;
			} else if (gameObject instanceof Sneezewort || gameObject instanceof Skullcab) {
				return JumpingAlienSprites.DEFAULT_PLANT_SPRITE; 
			} else if (gameObject instanceof Slime) {
				return JumpingAlienSprites.DEFAULT_SLIME_SPRITE;
			} else if (gameObject instanceof Shark) {
				return JumpingAlienSprites.DEFAULT_SHARK_SPRITE;
			}
		}
		throw new NoSuchMethodError("Teams of 2 students should implement this method.");
	}

	/**
	 * Advance the state of the given game object by the given time period.
	 */
	void advanceTime(Object gameObject, double dt) throws ModelException;
	
	

	/**
	 * Create an instance of Sneezewort with given pixel position and given sprites.
	 */
	Sneezewort createSneezewort(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException;

	/**
	 * Create an instance of Skullcab with given pixel position and given sprites.
	 */
	Skullcab createSkullcab(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException;

	/**
	 * Create an instance of Slime with given identification, givenpixel position,
	 * belonging to the given school and with given sprites.
	 */
	Slime createSlime(long id, int pixelLeftX, int pixelBottomY, School school, Sprite... sprites) throws ModelException;

	/**
	 * Return the identification of the given slime.
	 */
	long getIdentification(Slime slime) throws ModelException;
	
	/**
	 * Clean all identification used for slimes created so far.
	 */
	void cleanAllSlimeIds();

	/**
	 * Create an instance of School belonging to the given world and with no slimes yet.
	 */
	School createSchool(World world) throws ModelException;
	
	/**
	 * Terminate the given school
	 */
	void terminateSchool(School school) throws ModelException;
	
	/**
	 * Return a boolean indicating whether the given slime belongs to the given school.
	 */
	boolean hasAsSlime(School school, Slime slime) throws ModelException;
	
	/**
	 * Return all the slimes belonging to the given school.
	 */
	Collection<? extends Slime> getAllSlimes(School school);
		
	/**
	 * Add the given slime to the given school.
	 */
	void addAsSlime(School school, Slime slime) throws ModelException;
	
	/**
	 * Remove the given slime from the given school.
	 */
	void removeAsSlime(School school, Slime slime) throws ModelException;
	
	/**
	 * Have the given slime switch from its old school to the given school.
	 */
	void switchSchool(School newSchool, Slime slime) throws ModelException;
	
	/**
	 * Return the school to which the given slime belongs.
	 */
	School getSchool(Slime slime) throws ModelException;
	
	/**
	 * Create an instance of Shark with given pixel position and given sprites.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may stick to the default implementation. 
	 */
	default Shark createShark(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		if (!isTeamSolution()) {
			return null;
		}
		throw new NoSuchMethodError("Teams of 2 students should implement this method.");
	}
	

	/**
	 * Create an instance of Spider with given number of legs, given pixel position,
	 * and with given sprites.
	 */
	Spider createSpider(int nbLegs, int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException;


	/**
	 * Return the number of legs of the given spider.
	 */
	int getNbLegs(Spider spider) throws ModelException;

}
