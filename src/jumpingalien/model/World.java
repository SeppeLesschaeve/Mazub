package jumpingalien.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import annotate.Basic;
import annotate.Raw;

/**
 * This class holds a rectangular Game World that is composed of a fixed number of non-overlapping pixels with length 0.01 m;
 * a Visible Window as the part of the Game World that is visible during the game,
 * a table of tiles that groups pixels with a length that indicates the number of pixel in height and width 
 * and indicates the geological features of the Game World evaluated by the bottom-left position of that tile,
 * a Target Tile that is used as the tile that must be reached to win the game, 
 * a set of Game Objects whereby one Mazub that is used as the playable character, a set of Schools
 * and a set of Handlers that handle the collision between Game Objects  
 * 
 * @invar ...
 * 		| hasProperGameObjects()
 * @invar ...
 * 		| hasProperSchools()
 * @invar ...
 * 		| getGameWorldWidth() >= getVisibleWindowWidth() && getGameWorldHeight() >= getVisibleWindowHeight()
 * @invar ...
 * 		| getGameObjects().size() <= MAX_OBJECTS + 1
 * @invar ...
 * 		| getSchools().size() <= MAX_SCHOOLS
 * @invar ...
 * 		| getGameWorldWidth() % getTileLength() == 0 && getGameWorldHeight() % getTileLength() == 0               
 *              
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 * 
 */
public class World{

	private final int tileLength;
	private Rectangle gameWorld;
	private Rectangle visibleWindow;
	private Tile[][] tiles;
	private Tile targetTile;
	
	private static final int MAX_SCHOOLS = 10;
	private final int maxObjects;
	
	private PriorityQueue<GameObject> gameObjects = new PriorityQueue<>();
	private Set<School> schools = new HashSet<>();
	private Mazub player;

	private boolean isTerminated;
	private boolean started;

	/**
	 * This constructor will set the Game World, Visible Window, Handlers, Tiles and the Target Tile
	 * 
	 * @param tileSize
	 * 				This parameter is used to set the Tile Size 
	 * @param nbTilesX
	 * 				This parameter is used to indicate how many Tiles in the horizontal direction
	 * @param nbTilesY
	 * 				This parameter is used to indicate how many Tiles in the vertical direction
	 * @param target
	 * 				This parameter is used to set the Target Tile
	 * @param visibleWidth
	 * 				This parameter is used to set the horizontal dimension of the Visible Window
	 * @param visibleHeight
	 * 				This parameter is used to set the vertical dimension of the Visible Window
	 * @param geologicalFeatures
	 * 				This parameter is used to set the features in the Tiles
	 * 
	 * @throws IllegalArgumentException
	 * 			...
	 * 			|(geoFeatures == null || target == null || target.length != 2 || 
	 * 			|			tileSize*nbTilesX < visibleWidth || tileSize*nbTilesY < visibleHeight)
	 * @post ...
	 * 		| (new this).tiles = new Tile[nbTilesY][nbTilesX]
	 * 		| setFeatureOfTiles(tileSize, nbTilesX, nbTilesY, geologicalFeatures)
	 * 		| (new this).tileLength = tileSize
	 * @post ...
	 *		| (new this).gameWorld = new Rectangle(0,0, tileSize*nbTilesX, tileSize*nbTilesY)
	 *		| setWindow(visibleWindowWidth, visibleWindowHeight)
	 *		| setTargetTile(tileSize, targetTileCoordinate)
	 * @post ...
	 * 		| handlers.addAll(Arrays.asList(new SharkMazubHandler(), new SlimeMazubHandler(), new SlimeSharkHandler(),
	 *		|	new SpiderMazubHandler(), new SpiderSharkHandler(), new SpiderSlimeHandler(), new SneezewortMazubHandler(), new SkullcabMazubHandler()))
	 *
	 */
	public World(int tileSize, int nbTilesX, int nbTilesY, int[] target, int visibleWidth, int visibleHeight, int maxObjects, int... geologicalFeatures){
		if(tileSize <= 0) tileSize = Math.abs(tileSize);
		if(nbTilesX < 0 ) nbTilesX = Math.abs(nbTilesX);
		if(nbTilesY < 0 ) nbTilesY = Math.abs(nbTilesY);
		if(geologicalFeatures == null) throw new IllegalArgumentException("The features must be specified");
		if(target == null || target.length != 2) throw new IllegalArgumentException("The target tile must be specified and two-dimensional");
		if (tileSize*nbTilesX < visibleWidth || tileSize*nbTilesY < visibleHeight) throw new IllegalArgumentException("The visible window can not be bigger than the game world");
		this.tiles = new Tile[nbTilesY][nbTilesX];
		setFeatureOfTiles(tileSize, nbTilesX, nbTilesY, geologicalFeatures);
		this.tileLength = tileSize;
		this.gameWorld = new Rectangle(0,0, tileSize*nbTilesX, tileSize*nbTilesY);
		setWindow(visibleWidth, visibleHeight);
		setTargetTile(target);
		this.maxObjects = maxObjects;
	}

	/**
	 * This method is used to initialize the tiles
	 * 
	 * @param tileSize
	 * 			This parameter is used to indicate the size of the tiles
	 * @param nbTilesX
	 * 			This parameter is used to indicate the number of tiles in the x- direction
	 * @param nbTilesY
	 * 			This parameter is used to indicate the number of tiles in the y- direction
	 * @param geologicalFeatures
	 * 			This parameter is used as list of features that must be used as features of the tiles from left to right, from bottom to top
	 * 
	 * @post ...
	 * 		| for(int index = 0; index < geologicalFeatures.length; index++) 
	 *		| 	if(geologicalFeatures[index] < 0 || geologicalFeatures[index] > 5) then geologicalFeatures[index] = 0
	 * @post ...
	 * 		| int index = 0
	 * 		| for(int i = 0; i < nbTilesY; i++) 
	 *		| 	 for(int j = 0; j < nbTilesX; j++, index++) {
	 *		|		Position<Integer> position = new Position<>(j*tileSize, i*tileSize);
	 *		|		if(index < geologicalFeatures.length) then 
	 *		|			tiles[i][j] = new Tile(position, tileSize, Feature.getFeature(geologicalFeatures[index]));
	 *		|		else tiles[i][j]= new Tile(position, tileSize, Feature.AIR);		
	 *
	 */
	private void setFeatureOfTiles(int tileSize, int nbTilesX, int nbTilesY, int... geologicalFeatures) {
		for(int index = 0; index < geologicalFeatures.length; index++) {
			if(geologicalFeatures[index] < 0 || geologicalFeatures[index] > 5) geologicalFeatures[index] = 0;
		}
		int index = 0;
		for(int i = 0; i < nbTilesY; i++) {
			for(int j = 0; j < nbTilesX; j++, index++) {
				Position<Integer> position = new Position<>(j*tileSize, i*tileSize);
				if(index < geologicalFeatures.length) {
					tiles[i][j] = new Tile(position, tileSize, Feature.getFeature(geologicalFeatures[index]));
				}else tiles[i][j]= new Tile(position, tileSize, Feature.AIR);
			}
		}
	}
	
	/**
	 * This method is used to set the Target Tile
	 * @param target
	 * 			This parameter is used as the x- and y- coordinate of the Target Tile 
	 * 			(as Tile Coordinates that are the coordinates of the origin of that Tile divided by tileSize)
	 * 
	 * @post ...
	 * 		| Position<Integer> goal = new Position<>(target[0]*tileSize, target[1]*tileSize) 
	 * 		| if(!getGameWorld().contains(goal) then (new this).targetTile = new Tile(goal, tileSize, Feature.AIR);
	 * 		| else (new this).targetTile = this.tiles.clone()[target[1]][target[0]];
	 * 
	 */
	public void setTargetTile(int[] target) {
		Position<Integer> goal = new Position<>(target[0]*tileLength, target[1]*tileLength);
		if(!this.getGameWorld().contains(goal))
			this.targetTile = new Tile(goal, tileLength, Feature.AIR);
		else
			this.targetTile = this.tiles.clone()[target[1]][target[0]];
	}

	/**
	 * This method returns a representation of the Game World without features
	 * 
	 * @return ...
	 * 		| result == gameWorld
	 */
	public Rectangle getGameWorld() {
		return this.gameWorld;
	}
	
	/**
	 * This method returns the width of the Game World
	 * 
	 * @return ...
	 * 		| result == gameWorld.getDimension().getWidth()
	 */
	@Basic
	public int getGameWorldWidth() {
		return this.gameWorld.getWidth();
	}
	
	/**
	 * This method returns the height of the Game World
	 * 
	 * @return ...
	 * 		| result == gameWorld.getDimension().getHeight()
	 */
	@Basic
	public int getGameWorldHeight() {
		return this.gameWorld.getHeight();
	}
	
	/**
	 * This method returns the width of the Visible Window
	 * 
	 * @return ...
	 * 		| result == visibleWindow.getDimension().getWidth()
	 */
	@Basic
	public int getVisibleWindowWidth() {
		return this.visibleWindow.getWidth();
	}
	
	/**
	 * This method returns the height of the Visible Window
	 * 
	 * @return ...
	 * 		| result == visibleWindow.getDimension().getHeight()
	 */
	@Basic
	public int getVisibleWindowHeight() {
		return this.visibleWindow.getHeight();
	}
	
	/**
	 * This method returns the x-coordinate of the origin of the Visible Window
	 * 
	 * @return ...
	 * 		| result == visibleWindow.getOrigin().getX()
	 */
	@Basic
	public int getVisibleWindowXCoordinate() {
		return this.visibleWindow.getX();
	}
	
	/**
	 * This method returns the y-coordinate of the origin of the Visible Window
	 * 
	 * @return ...
	 * 		| result == visibleWindow.getOrigin().getY()
	 */
	@Basic
	public int getVisibleWindowYCoordinate() {
		return this.visibleWindow.getY();
	}
	
	/**
	 * This method returns the feature of the Tile given a coordinate of the Tile
	 * 
	 * @param x
	 * 		This parameter is used as x-coordinate
	 * @param y
	 * 		This parameter is used as y-coordinate
	 * @return ...
	 * 		| Position<Integer> point = new Position<>(x,y)
	 * 		| if(!this.gameWorld.contains(point)) then result == Feature.AIR
	 * 		| else
	 * 		| 	x = (x- x%tileLength)/tileLength && y = (y- y%tileLength)/tileLength
	 *		| 	return tiles[y][x].getFeature()
	 *
	 */
	@Basic
	public Feature getTileFeature(int x, int y) {
		Position<Integer> point = new Position<>(x,y);
		if(!this.gameWorld.contains(point)) return Feature.AIR;
		x = (x- x%tileLength)/tileLength;  y = (y-y%tileLength)/tileLength;
		return tiles[y][x].getFeature();
	}
	
	/**
	 * This method returns if the rectangle will not be partially overlapping impassable terrain
	 * 
	 * @param rect
	 * 			This parameter is used as rectangle
	 * 
	 * @return ...
	 * 		|if(rect == null) then result == true
	 *		|for(int x = rect.getOrigin().getX(); x < rect.getOrigin().getX() + rect.getDimension().getWidth(); x++) 
	 *		|	for(int y = rect.getOrigin().getY(); y < rect.getOrigin().getY() + rect.getDimension().getHeight(); y++)
	 *		|		if(!getTileFeature(x, y).isPassable()) then result == false 
	 *		|
	 *		| result == true
	 *
	 */
	public boolean shallBePassable(Rectangle rect) {
		if(rect == null) return true;
		int newX = 0;
		int newY = 0;
		for(int pixelX = rect.getX(); pixelX <= rect.getX()+rect.getWidth()-1; pixelX+=newX) {
			for(int pixelY = rect.getY(); pixelY <= rect.getY()+rect.getHeight()-1; pixelY+=newY) {
				if(!getTileFeature(pixelX, pixelY).isPassable()) {
					return false; 
				}
				newY = Math.min(tileLength, rect.getY() + rect.getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, rect.getX() + rect.getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return true;
	}

	/**
	 * This method returns a representation of the Target Tile
	 * 
	 * @return ...
	 * 		| result == targetTile
	 */
	public Tile getTargetTile() {
		return this.targetTile;
	}
	
	/**
	 * This method returns the x-coordinate of the origin of the Target Tile in Tile Coordinates
	 * 
	 * @return ...
	 * 		| result == targetTile.getRectangle().getOrigin().getX()/getTileLength()
	 */
	@Basic
	public int getTargetTileXCoordinate() {
		return this.targetTile.getRectangle().getX()/getTileLength();
	}
	
	/**
	 * This method returns the y-coordinate of the origin of the Target Tile in Tile Coordinates
	 * 
	 * @return ...
	 * 		| result == targetTile.getRectangle().getOrigin().getY()/getTileLength()
	 */
	@Basic
	public int getTargetTileYCoordinate() {
		return this.targetTile.getRectangle().getY()/getTileLength();
	}
	
	/**
	 * This method will set the feature of a Tile
	 * 
	 * @param x
	 * 			This parameter is used as the x-coordinate
	 * @param y
	 * 			This parameter is used as the y-coordinate
	 * @param geologicalFeature
	 * 			This parameter is used as feature
	 * @post...
	 * 		| if(geologicalFeature < 0 || geologicalFeature > 5) then geologicalFeature = 0
	 *		| if(x < 0) then x = Math.abs(x) && if(y < 0) then y = Math.abs(y)
	 *		| x = (x - x%getTileLength())/getTileLength() && y = (y - y%getTileLength())/getTileLength()
	 *		| if(x >= tiles[1].length) then x = tiles[1].length - 1 && if(y >= tiles.length) then y = tiles.length - 1
	 *		| Tile tileToSet = tiles[y][x]
	 *		| Feature feature = Feature.getFeature(geologicalFeature)
	 *		| tileToSet.setFeature(feature)
	 *
	 */
	public void setGeoFeature(int x, int y, int geologicalFeature) {
		if(geologicalFeature < 0 || geologicalFeature > 5) geologicalFeature = 0;
		if(x < 0) x = Math.abs(x);
		if(y < 0) y = Math.abs(y);
		x = (x - x%getTileLength())/getTileLength(); 
		y = (y - y%getTileLength())/getTileLength(); 
		if(x >= tiles[1].length) x = tiles[1].length - 1;
		if(y >= tiles.length) y = tiles.length - 1;
		Tile tileToSet = tiles[y][x];
		Feature feature = Feature.getFeature(geologicalFeature);
		tileToSet.setFeature(feature);
	}
	
	/**
	 * This methods returns the player 
	 * 
	 * @return ...
	 * 		|result.equals(player)
	 */
	@Basic
	public Mazub getPlayer() {
		return player;
	}
	
	/**
	 * This method will set the player 
	 * 
	 * @param player
	 * 		This parameter is used as player
	 * @post ...
	 * 		|(new this).player = player
	 * 
	 */
	public void setPlayer(Mazub player)  {
		this.player = player;
	}
	
	/**
	 * This method will unset the player
	 * 
	 * @effect ...
	 * 		|setPlayer(null)
	 * 
	 */
	public void unsetPlayer() {
		setPlayer(null);
	}
	
	/**
	 * This method returns whether a GameObject overlaps with any other GameObject in gameObjects
	 * 
	 * @param object
	 * 			This parameter is used as the GameObject
	 * @return ...
	 * 			| for(Object other: getGameObjects()) 
	 *			| 	 if(object.getRectangle().overlaps(((GameObject) other).getRectangle()) && !object.equals(other) && other instanceof Creature && object instanceof Creature)
	 *			|		if(other instanceof Slime && object instanceof Slime) then result == ( ((Slime) other).getSchool() == ((Slime) object).getSchool() )
	 *			|		
	 *			|	 else result == true
	 *			|
	 *			| result ==  false
	 *
	 */
	private boolean shallOverlap(GameObject object) {
		for(Object other: getGameObjects()) {
			if(object.getRectangle().overlaps(((Organism) other).getRectangle()) && !object.equals(other) && other instanceof Creature && object instanceof Creature) {
				if(other instanceof Slime && object instanceof Slime) {
					return((Slime) other).getSchool() == ((Slime) object).getSchool();
				}else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method returns if the Game Object will be considered placed in impassable terrain
	 * 
	 * @param object
	 * 			This parameter is used as Game Object
	 * @return ...
	 * 		| if(!(object instanceof Plant))
	 *		|	int newX = object.getOrigin().getX()  && int newY = object.getOrigin().getY()+1
	 *		|	for(int pixelX = newX; pixelX < newX+object.getDimension().getWidth()-1; pixelX++)
	 *		|		for(int pixelY = newY; pixelY < newY+object.getDimension().getHeight()-1; pixelY++) {
	 *		|			if(!getTileFeature(pixelX, pixelY).isPassable()) then result == true
	 *		| result == false
	 * 
	 */
	private boolean shallBeImpassable(GameObject object) {
		Rectangle rect = object.getRectangle();
		if(object instanceof Plant) return false;
		int newX = 0; 
		int newY = 0;
		for(int pixelX = rect.getX(); pixelX < rect.getX()+rect.getWidth()-1; pixelX+=newX) {
			for(int pixelY = rect.getY()+1; pixelY < rect.getY()+rect.getHeight()-1; pixelY+=newY) {
				if(!getTileFeature(pixelX, pixelY).isPassable()) return true;
				newY = Math.min(tileLength, rect.getY() + rect.getHeight()-1 - pixelY);
				if(newY < tileLength) newY=1;
			}
			newX = Math.min(tileLength, rect.getX() + rect.getWidth()-1 - pixelX);
			if(newX < tileLength) newX=1;
		}
		return false;
	}

	/**
	 * This method returns whether this World has proper GameObjects attached to it.
	 * 
	 * @return ...
	 * 		| result == for each gameObject in gameObjects:
	 * 		|	( canHaveAsGameObject((GameObject) gameObject) && ((GameObject)gameObject).getWorld().equals(this))
	 */
	public boolean hasProperGameObjects() {
		for(Object gameObject: getGameObjects()) {
			if(!canHaveAsGameObject((GameObject) gameObject)) return false;
			if(!((GameObject)gameObject).getWorld().equals(this)) return false;
		}
		return true;
	}
	
	/**
	 * This method returns whether an GameObject object can belong to this world
	 * 
	 * @param object
	 * 			This parameter is used as GameObject
	 * @return ...
	 * 		| if( gameObjects.size() == MAX_OBJECTS && !(object instanceof Mazub) ) then result == false
	 * 		| result == object != null && !object.isTerminated() && object.getWorld() == null &&
	 *		|	!shallBeImpassable(object) && !shallOverlap(object) && object.getRectangle().overlaps(gameWorld)
	 *		
	 */
	public boolean canHaveAsGameObject(GameObject object) {
		if(gameObjects.size() == maxObjects && !(object instanceof Mazub)) return false;
		if(object.getWorld() != null) return false; 
		return !object.isTerminated() && !shallBeImpassable(object) && !shallOverlap(object) && object.getRectangle().overlaps(gameWorld);
	}
	
	/**
	 * This method returns whether a GameObject still belongs to this world
	 * 
	 * @param object
	 * 			This parameter is used as GameObject
	 * @return ...
	 * 		| result == this.gameObjects.contains(object) && object.getWorld().equals(this)
	 */
	public boolean hasProperGameObject(GameObject object) {
		return this.gameObjects.contains(object) && object.getWorld().equals(this);
	}
	
	/**
	 * This method is used to add an Game Object to this Game World and will store him in a set
	 * 
	 * @param object
	 * 		This parameter is used as the Game Object
	 * @throws IllegalArgumentException 
	 * 			...
	 * 			| (!canHaveAsGameObject(object) || isTerminated() || isStarted() || ( player != null && object instanceof Mazub))
	 * @post ...
	 * 		|gameObjects.add(object)
	 *		|if(object instanceof Mazub)  then 
	 *		|	player = (Mazub) object
	 *		|	setWindow(getVisibleWindowWidth, getVisibleWindowHeight())
	 *		|if(object instanceof Slime && ((Slime) object).getSchool() != null ) then ((Slime) object).getSchool().setWorld(this)
	 *		|object.setWorld(this);
	 */
	@Raw
	public void addGameObject(GameObject object){
		if(!canHaveAsGameObject(object) || isTerminated() || isStarted()) 
			throw new IllegalArgumentException("You can not use this object in this situation");
		if(player != null && object instanceof Mazub) throw new IllegalArgumentException("You can not have multiple aliens");
		gameObjects.add(object);
		if(object instanceof Mazub) {
			player = (Mazub) object;
			setWindow(getVisibleWindowWidth(), getVisibleWindowHeight());
		}
		if(object instanceof Slime && ((Slime) object).getSchool() != null )
			((Slime) object).getSchool().setWorld(this);
		object.setWorld(this);
	}
	
	/**
	 * This method removes a Game Object from this World
	 * 
	 * @param object
	 * 			This parameter is used as Game Object
	 * @throws IllegalArgumentException
	 * 			...
	 * 			|(!hasProperGameObject(object))
	 * @post  ...
	 * 		| if(object instanceof Mazub) then unsetPlayer()
	 *		| gameObjects.remove(object)
	 *		| if(object instanceof Slime && ((Slime) object).getSchool() != null && ((Slime) object).getSchool().getSlimes().size() == 1)
	 *		|	then ((Slime) object).getSchool().setWorld(null)
	 *		| object.setWorld(null)
	 *
	 */
	public void removeGameObject(GameObject object) {
		if (!hasProperGameObject(object)) throw new IllegalArgumentException("You can not use this object");
		if(object instanceof Mazub) unsetPlayer();
		gameObjects.remove(object);
		if(object instanceof Slime && ((Slime) object).getSchool() != null && ((Slime) object).getSchool().getSlimes().size() == 1)
			((Slime) object).getSchool().setWorld(null);
		object.clearWorld();
	}

	/**
	 * This method returns the game objects in the Game World
	 * 
	 * @return ...
	 * 		|result == gameObjects.toArray().clone()
	 */
	@Basic
	public Object[] getGameObjects() {
		return gameObjects.toArray().clone();
	}

	/**
	 * This method returns whether this World has proper Schools attached to it.
	 *
	 * @return ...
	 * 		| result == for each school in schools:
	 * 		|	( canHaveAsSchool(school) && school.getWorld().equals(this))
	 */
	public boolean hasProperSchools() {
		for(Object school: getSchools()) {
			if(!canHaveAsSchool((School) school)) return false;
			if(!((School) school).getWorld().equals(this)) return false;
		}
		return true;
	}

	/**
	 * This method returns whether the World can take this school
	 *
	 * @param school
	 * 			This parameter is used as school
	 * @return ...
	 * 		| result == getSchools().length  <  MAX_SCHOOLS
	 */
	public boolean canHaveAsSchool(School school){
		return getSchools().length <  MAX_SCHOOLS;
	}

	/**
	 * This method is used to indicate whether this World has this school attached to it
	 *
	 * @param school
	 * 			This parameter is used as school
	 * @return ...
	 * 		| result == canHaveAsSchool(school) && this.schools.contains(school)  && !school.isTerminated()
	 */
	public boolean hasProperSchool(School school) {
		return canHaveAsSchool(school) && this.schools.contains(school)  && !school.isTerminated();
	}

	/**
	 * This method returns the school in the Game World
	 * 
	 * @return ...
	 * 		|result == schools.toArray().clone() 
	 */
	@Basic
	public Object[] getSchools() {
		return schools.toArray().clone();
	}
	
	/**
	 * This method is used to add a school to this world and to add every slime in it to  gameObjects
	 * 
	 * @param school
	 * 			This parameter is used as the school to add
	 * @post ...
	 * 		| this.schools.add(school)
	 *		| for(Slime slime: school.getSlimes())
	 *		|	this.gamObjects.add(slime)
	 *
	 */
	public void setSchool(School school) {
		this.schools.add(school);
		for(Slime slime: school.getSlimes()) {
			this.gameObjects.add(slime);
		}
	}

	/**
	 * This method returns the Tile Size
	 * 
	 * @return ...
	 * 		| result == tileLength
	 */
	@Basic
	public int getTileLength() {
		return tileLength;
	}

	/**
	 * This method is used to set the Visible Window at the construction time
	 *  
	 * @param xVisible
	 * 			This parameter is used as the horizontal dimension of the Visible Window 
	 * @param yVisible
	 * 			This parameter is used as the vertical dimension of the Visible Window
	 * @throws IllegalArgumentException
	 * 		...
	 * 		| if(xVisible < 0 || yVisible < 0)
	 * @post ...
	 * 		| if(player.getOrigin().getX() - 200 >= 0 && player.getOrigin().getY() - 200 >= 0 &&
	 *		| 	player.getOrigin().getX()+player.getDimension().getWidth()-1 +200 <= gameWorld.getDimension().getWidth() &&
	 *		|		player.getOrigin().getY()+player.getDimension().getHeight()-1 +200 <= gameWorld.getDimension().getHeight() &&
	 *		|			xVisible >= 400 + player.getDimension().getWidth() && yVisible >= 400 + player.getDimension().getWidth()) then
	 *		|				this.visibleWindow = new Rectangle(player.getOrigin().getX() -200, player.getOrigin().getY()- 200, xVisible, yVisible)
	 *		| else if(!visibleWindow.contains(player.getOrigin())) then 
	 *		|	this.visibleWindow = new Rectangle(gameWorld.getDimension().getWidth()-visibleWindow.getDimension().getWidth(), 
	 *		|		gameWorld.getDimension().getHeight()-visibleWindow.getDimension().getHeight(), xVisible, yVisible)
	 *
	 */
	private void setWindow(int xVisible, int yVisible) {
		if(xVisible < 0 || yVisible < 0) { throw new IllegalArgumentException("The dimensions can not be negative");}
		if(player == null) {
			this.visibleWindow = new Rectangle(0, 0, xVisible, yVisible); return;
		}
		if(player.getRectangle().getX() - 200 >= 0 && player.getRectangle().getY() - 200 >= 0 &&
			player.getRectangle().getX()+player.getRectangle().getWidth()-1 +200 <= gameWorld.getWidth() &&
				player.getRectangle().getY()+player.getRectangle().getHeight()-1 +200 <= gameWorld.getHeight() &&
					xVisible >= 400 + player.getRectangle().getWidth() && yVisible >= 400 + player.getRectangle().getWidth()) {
						this.visibleWindow = new Rectangle(player.getRectangle().getX() -200, player.getRectangle().getY()- 200, xVisible, yVisible);
		}else if(!visibleWindow.contains(player.getRectangle().getOrigin())){
			this.visibleWindow = new Rectangle(gameWorld.getWidth()-visibleWindow.getWidth(), 
					gameWorld.getHeight()-visibleWindow.getHeight(), xVisible, yVisible);
		}
	}
	
	public void advanceTime(double deltaT) {
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT > 0.2 || Double.isInfinite(deltaT)) {
			throw new IllegalArgumentException("You can not invoke advanceTime with a NaN as time are not between 0 and 0.2");}
		Iterator<GameObject> gameObjectIterator = gameObjects.iterator();
		while(gameObjectIterator.hasNext()) {
			gameObjectIterator.next().advanceTime(deltaT);
		}
	}
	
	/**
	 * This method is used to update the position of the Visible Window using the original position of the player
	 * 
	 * @param position
	 * 			This parameter is used as the original position
	 * @post ...
	 * 		|	int x = player.getOrigin().getX() - position.getX()
	 * 		|if((x > 0 && visibleWindow.getOrigin().getX() + x + visibleWindow.getDimension().getWidth() - 1 < gameWorld.getDimension().getWidth()) || 
	 *		|	(x < 0 && visibleWindow.getOrigin().getX() + x >= 0)) then visibleWindow.getOrigin().setX(visibleWindow.getOrigin().getX() + x)
	 * @post ...
	 * 		|	int y = player.getOrigin().getY() - position.getY()
	 *		|if((y > 0 && visibleWindow.getOrigin().getY() + y + visibleWindow.getDimension().getHeight() -1 < gameWorld.getDimension().getHeight()) ||
	 *		|	(y < 0 && visibleWindow.getOrigin().getY() + y >= 0)) then visibleWindow.getOrigin().setY(visibleWindow.getOrigin().getY() + y)
	 *
	 */
	public void updateWindow(Position<Integer> position) {
		int x = player.getRectangle().getX() - position.getX(); 
		int y = player.getRectangle().getY() - position.getY();
		if((x > 0 && visibleWindow.getOrigin().getX() + x + visibleWindow.getWidth() - 1 < gameWorld.getWidth()) || 
				(x < 0 && visibleWindow.getOrigin().getX() + x >= 0)) 
			visibleWindow.getOrigin().setX(visibleWindow.getOrigin().getX() + x);
		if((y > 0 && visibleWindow.getOrigin().getY() + y + visibleWindow.getHeight() -1 < gameWorld.getHeight()) ||
				(y < 0 && visibleWindow.getOrigin().getY() + y >= 0)) 
			visibleWindow.getOrigin().setY(visibleWindow.getOrigin().getY() + y);
	}
	
	/**
	 * This method returns whether the Game World is terminated or not terminated
	 * 
	 * @return ...
	 * 		|result == isTerminated
	 */
	@Basic
	public boolean isTerminated() {
		return isTerminated;
	}
	
	/**
	 * This method will set the indication that the world is terminated
	 * 
	 * @post ...
	 * 		| (new this).isTerminated = true
	 */
	public void setTerminated() {
		this.isTerminated = true;
	}

	/**
	 * This method will terminate the Game World
	 * 
	 * @post ...
	 * 		|gameObjects.stream().forEach(object -> object.setWorld(null))
	 *		|gameObjects.clear();
	 *		|handlers.clear();
	 * @effect ...
	 * 		|this.setTerminated()
	 */
	public void terminate() {
		gameObjects.stream().forEach(object -> object.clearWorld());
		gameObjects.clear();
		this.setTerminated();
	}
	
	/**
	 * This method returns whether the game is started or not
	 * 
	 * @return ...
	 * 		| result == started
	 * 
	 */
	@Basic
	public boolean isStarted() {
		return started;
	}

	/**
	 * This method is used to mark the game started
	 * 
	 * @throws IllegalStateException
	 * 		...
	 * 		| getPlayer() == null
	 * @post ...
	 * 		|(new this).started = true
	 * 
	 */
	public void setStarted() {
		if(getPlayer() == null) throw new IllegalStateException();
		this.started = true;
	}

	/**
	 * This method returns if the Game is considered to be over
	 * 
	 * @return ...
	 * 		| result == getPlayer() == null || getPlayer().getRectangle().overlaps(getTargetTile().getRectangle())
	 */
	public boolean isGameOver() {
		return getPlayer() == null || getPlayer().getRectangle().overlaps(getTargetTile().getRectangle());
	}

}
