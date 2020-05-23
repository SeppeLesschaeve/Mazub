package jumpingalien.internal.game;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jumpingalien.facade.IFacade;
import jumpingalien.internal.gui.sprites.ImageSprite;
import jumpingalien.internal.gui.sprites.JumpingAlienSprites;
import jumpingalien.model.Mazub;
import jumpingalien.model.School;
import jumpingalien.model.Shark;
import jumpingalien.model.Skullcab;
import jumpingalien.model.Slime;
import jumpingalien.model.Sneezewort;
import jumpingalien.model.World;
import jumpingalien.internal.tmxfile.TMXFileReader;
import jumpingalien.internal.tmxfile.data.ImageTile;
import jumpingalien.internal.tmxfile.data.Layer;
import jumpingalien.internal.tmxfile.data.Map;
import jumpingalien.internal.tmxfile.data.MapObject;
import jumpingalien.internal.tmxfile.data.ImageTile.TileType;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;
import ogp.framework.command.Command;
import ogp.framework.game.Game;
import ogp.framework.messages.Message;
import ogp.framework.messages.MessageType;

public class JumpingAlienGame extends Game {

	private static final int MAX_MISSED_DEADLINES = 4;

	private static final double MAX_TIME_STEP = 0.200;

	private Mazub alien;
	private World world;

	private Map map;

	private int tileSize;
	private final ObjectInfoProvider objectInfoProvider;
	private String currentMap;
	
	private IActionHandler handler;

	private int visibleScreenWidth = -1;
	private int visibleScreenHeight = -1;

	private double elapsedTime = 0;
	private boolean running;

	private final IFacade facade;

	public JumpingAlienGame(JumpingAlienGameOptions options, IFacade facade) {
		super(options);
		this.handler = createActionHandler();
		this.worldInfoProvider = createWorldInfoProvider();
		this.alienInfoProvider = createAlienInfoProvider();
		this.objectInfoProvider = createObjectInfoProvider();		
		this.facade = facade;
	}

	public IFacade getFacade() {
		return facade;
	}

	@Override
	public JumpingAlienGameOptions getOptions() {
		return (JumpingAlienGameOptions) super.getOptions();
	}

	public void restart() {
		this.world = null;
		this.alien = null;
		start();
	}
	

	private boolean readLevelFile(String filename) {
		try {
			TMXFileReader reader = new TMXFileReader("levels/");
			map = reader.read(filename);

			if (map.getTileSizeY() != map.getTileSizeX()) {
				throw new IllegalArgumentException(
						"Can only work with square tile sizes");
			}

			if (map.getLayer("Terrain") == null) {
				throw new IllegalArgumentException(
						"The map must have a layer called 'Terrain'");
			}

			tileSize = map.getTileSizeY();
		} catch (Throwable e) {
			addMessage(new Message(MessageType.ERROR, "Error while reading "
					+ filename + ": " + e.getMessage()));
			return false;
		}

		return true;
	}

	@Override
	public void start() {
		if (visibleScreenWidth < 0 || visibleScreenHeight < 0) {
			throw new IllegalStateException("Visible screen size not set");
		}

		createModel();

		running = true;
	}

	public void setPause(boolean value) {
		running = !value;
	}

	public void stop() {
		running = false;
	}

	protected void createModel() {
		int visibleWidth = Math.min(getVisibleScreenWidth(), map.getPixelWidth());
		int visibleHeight = Math.min(getVisibleScreenHeight(), map.getPixelHeight());
		setWorld(getFacade().createWorld(tileSize, map.getNbTilesX(),
				map.getNbTilesY(),
				new int[] { map.getTargetTileX(), map.getTargetTileY() },
				visibleWidth, visibleHeight,
				getTileTypes()));

		setAlien(getFacade().createMazub(map.getInitialPositionX(),
				map.getInitialPositionY(), JumpingAlienSprites.ALIEN_SPRITESET));

		getFacade().addGameObject(getAlien(), getWorld());

		for (MapObject obj : map.getObjects()) {
			addObject(obj);
		}

		// no more object creations or tile changes after starting
		getFacade().startGame(getWorld());
	}
	
	private void setAlien(Mazub alien) {
		if (this.alien != null) {
			throw new IllegalStateException("Mazub already created!");
		}
		this.alien = alien;
	}

	Mazub getAlien() {
		return alien;
	}
	
	public int[] getWorldSize() {
		return new int[] { map.getPixelWidth(), map.getPixelHeight() };
	}
	
	private int[] getTileTypes() {
		Layer terrainLayer = map.getLayer("Terrain");

		int[] types = new int[map.getNbTilesX() * map.getNbTilesY()];
		int index = 0;
		
		for (int tileY = 0; tileY < map.getNbTilesY(); tileY++) {
			for (int tileX = 0; tileX < map.getNbTilesX(); tileX++) {
				ImageTile tile = terrainLayer.getTile(tileX, tileY);
				if (tile != null) {
					types[index++] = tile.getType().getValue();					
				} else {
					types[index++] = TileType.AIR.getValue();
				}
			}
		}
		return types;
	}
	
	private void addObject(MapObject obj) {
		switch (obj.getTile().getOSIndependentFilename()) {
		case JumpingAlienSprites.PLANT_LEFT_FILENAME:
			Object gameObject;
			if (obj.getBooleanAttribute("skullcab").orElse(false)) {
				gameObject = getFacade().createSkullcab(
						obj.getX(),
						obj.getY(),
						new Sprite[] { JumpingAlienSprites.PLANT_SPRITE_UP,
								JumpingAlienSprites.PLANT_SPRITE_DOWN });
			} else {
				gameObject = getFacade().createSneezewort(
							obj.getX(),
							obj.getY(),
							new Sprite[] { JumpingAlienSprites.PLANT_SPRITE_LEFT,
									JumpingAlienSprites.PLANT_SPRITE_RIGHT });
			}
			if (gameObject != null)
				getFacade().addGameObject(gameObject, getWorld());
			break;
		case JumpingAlienSprites.SHARK_LEFT_FILENAME:
		case JumpingAlienSprites.SHARK_LEFT2_FILENAME:
		case JumpingAlienSprites.SHARK_DEAD_FILENAME:
			Shark shark = getFacade().createShark(
					obj.getX(),
					obj.getY(),
					new Sprite[] { JumpingAlienSprites.SHARK_SPRITE_REST,
							JumpingAlienSprites.SHARK_SPRITE_LEFT,
							JumpingAlienSprites.SHARK_SPRITE_RIGHT });
			if (shark != null)
				getFacade().addGameObject(shark, getWorld());
			break;
		case JumpingAlienSprites.SLIME_LEFT_FILENAME:
		case JumpingAlienSprites.SLIME_LEFT2_FILENAME:
		case JumpingAlienSprites.SLIME_DEAD_FILENAME:
			int schoolNb = obj.getIntAttribute("school").orElse(0);
			School school = getSlimeSchool(schoolNb);
			Slime slime = getFacade().createSlime(
					nextSlimeId++,
					obj.getX(),
					obj.getY(),
					school,
					new Sprite[] { JumpingAlienSprites.SLIME_SPRITE_RIGHT,
							JumpingAlienSprites.SLIME_SPRITE_LEFT });
			if (slime != null)
				getFacade().addGameObject(slime, getWorld());
			break;
		default:
			System.out
					.println("ERROR while loading level: don't know how to deal with object "
							+ obj);
			break;
		}
	}
	
	private long nextSlimeId = 1;
	
	private java.util.Map<Integer, School> schools = new HashMap<Integer, School>();

	private School getSlimeSchool(int nb) {
		return schools.computeIfAbsent(nb, i -> getFacade().createSchool(getWorld()));
	}

	
	public Map getMap() {
		return map;
	}

	private void setWorld(World world) {
		if (this.world != null) {
			throw new IllegalStateException("World already created!");
		}
		this.world = world;
	}

	private World getWorld() {
		return world;
	}

	protected IActionHandler createActionHandler() {
		return new ActionHandler(this);
	}
	
	
	@Override
	public void load() {		
	}


	/**
	 * Hack to skip first call to update after game has started (time interval
	 * too large due to initial screen painting)
	 */
	private boolean firstUpdate = true;

	@Override
	protected void doUpdate(double dt) {
		if (isRunning()) {
			if (!firstUpdate) {
				dt = applyTimescale(dt);
				executePendingCommands();
				try {
					advanceTime(dt);
					elapsedTime += dt;
				} catch (ModelException e) {
					addMessage(new Message(MessageType.ERROR, e.getMessage()));
					System.out.println("Could not advance time by dt=" + dt
							+ ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			firstUpdate = false;
		}
	}

	/**
	 * Scale the given time interval based on the game options.
	 * 
	 * If the scaled time in MAX_MISSED_DEADLINES subsequent invocations exceeds
	 * MAX_TIME_STEP, the time scale is adapted.
	 * 
	 * The returned time interval is always guaranteed to be smaller than or
	 * equal to MAX_TIME_STEP.
	 */
	protected double applyTimescale(double dt) {
		double scaledDT = dt / getOptions().getTimescale();
		if (scaledDT > MAX_TIME_STEP) {
			scaledDT = MAX_TIME_STEP;

			deadlineMissed(dt);
		} else {
			deadlineMet(dt);
		}
		return scaledDT;
	}

	private int nbSubsequentDeadlinesMissed = 0;
	private double totalMissedTime;

	private void deadlineMissed(double dt) {
		nbSubsequentDeadlinesMissed++;
		totalMissedTime += dt;
		if (nbSubsequentDeadlinesMissed >= MAX_MISSED_DEADLINES) {
			adjustTimescale(totalMissedTime / nbSubsequentDeadlinesMissed);
			nbSubsequentDeadlinesMissed = 0;
			totalMissedTime = 0;
		}
	}

	private void deadlineMet(double dt) {
		nbSubsequentDeadlinesMissed = 0;
		totalMissedTime = 0;
	}

	private void adjustTimescale(double dt) {
		double newScale = 1.05 * dt / MAX_TIME_STEP; // 5% higher than
														// theoretically
														// necessary timescale
		getOptions().setTimescale(newScale);
		System.out
				.println(String
						.format("Warning: Your advanceTime code seems too slow to ensure dt <= %.3f with the current framerate.\n         In-game time will run slower than real time (1 in-game second = %.2f real-world seconds)",
								MAX_TIME_STEP, newScale));
	}

	public boolean isRunning() {
		return running;
	}

	protected void advanceTime(double dt) {
		getFacade().advanceWorldTime(getWorld(), dt);
		if (getFacade().isGameOver(getWorld())) {
			stop();
		}
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setVisibleScreenSize(int width, int height) {
		this.visibleScreenWidth = width;
		this.visibleScreenHeight = height;
	}

	public IActionHandler getActionHandler() {
		return handler;
	}

	public int getVisibleScreenWidth() {
		return visibleScreenWidth;
	}

	public int getVisibleScreenHeight() {
		return visibleScreenHeight;
	}

	private final AlienInfoProvider<Mazub> alienInfoProvider;

	protected AlienInfoProvider<Mazub> createAlienInfoProvider() {
		return new AlienInfoProvider<Mazub>() {
			
			@Override
			public Mazub getAlien() {
				return alien;
			}

			@Override
			public Optional<int[]> getAlienXYPixel() {
				return catchErrorGet(() -> getFacade().getPixelPosition(getAlien()));
			}
			
			@Override
			public Optional<double[]> getAlienXYPrecise() {
				return catchErrorGet(() -> getFacade().getActualPosition(getAlien()));
			}

			@Override
			public Optional<double[]> getAlienVelocity() {
				return catchErrorGet(() -> getFacade().getVelocity(getAlien()));
			}

			@Override
			public Optional<double[]> getAlienAcceleration() {
				return catchErrorGet(() -> getFacade().getAcceleration(
						getAlien()));
			}

			@Override
			public Optional<int[]> getAlienSize() {
				return getPlayerSprite().map(s -> new int[] { s.getWidth(), s.getHeight() });
			}

			@Override
			public Optional<Sprite> getPlayerSprite() {
				return catchErrorGet(() -> getFacade().getCurrentSprite(
						getAlien()));
			}

			@Override
			public Optional<Integer> getAlienHealth() {
				return catchErrorGet(() -> getFacade().getHitPoints(
						getAlien()));
			}
		};
	}

	private final WorldInfoProvider worldInfoProvider;

	protected WorldInfoProvider createWorldInfoProvider() {
		return new WorldInfoProvider() {


			@Override
			public Optional<int[]> getVisibleWindow() {
				return catchErrorGet(() -> {
					int[] pos = getFacade().getVisibleWindowPosition(getWorld());
					int[] size = getFacade().getVisibleWindowDimension(getWorld());
					// left, bottom, right, top
					return new int[] { pos[0], pos[1], pos[0] + size[0] - 1, pos[1] + size[1] - 1 };
				});
			}

			@Override
			public Optional<int[][]> getTilesIn(int pixelLeft, int pixelBottom,
					int pixelRight, int pixelTop) {
				int startTileX = pixelLeft / tileSize;
				int endTileX = pixelRight / tileSize;
				int startTileY = pixelBottom / tileSize;
				int endTileY = pixelTop / tileSize;
				
				int ntiles = (endTileX - startTileX + 1) * (endTileY - startTileY + 1);
				int[][] result = new int[ntiles][];
				int index = 0;
				for (int x = startTileX; x <= endTileX; x++) {
					for (int y = startTileY; y <= endTileY; y++) {
						result[index++] = new int[] { x , y };
					}
				}
				return Optional.of(result);
			}

			@Override
			public Optional<TileType> getGeologicalFeature(int bottomLeftX,
					int bottomLeftY) {
				return catchErrorGet(() -> TileType.fromValue(getFacade()
						.getGeologicalFeature(getWorld(), bottomLeftX,
								bottomLeftY)));
			}

			@Override
			public Optional<int[]> getBottomLeftPixelOfTile(int tileX, int tileY) {
				return Optional.of(new int[] { tileX * tileSize, tileY * tileSize });
			}

			@Override
			public Optional<int[]> getWorldSize() {
				return Optional.of(JumpingAlienGame.this.getWorldSize());
			}

			@Override
			public int getTileLength() {
				return catchErrorGet(
						() -> getFacade().getTileLength(getWorld())).orElse(
						tileSize);
			}

			@Override
			public Optional<Boolean> isGameOver() {
				return catchErrorGet(() -> getFacade().isGameOver(getWorld()));
			}

			@Override
			public Optional<Boolean> didPlayerWin() {
				return catchErrorGet(() -> getFacade().didPlayerWin(getWorld()));
			}

		};
	}
	

	protected ObjectInfoProvider createObjectInfoProvider() {
		return new ObjectInfoProvider() {

			@Override
			public Collection<Sneezewort> getSneezeworts() {
				return getFacade().getAllGameObjects(getWorld()).stream().filter(Sneezewort.class::isInstance).map(Sneezewort.class::cast).collect(Collectors.toSet());
			}

			@Override
			public Optional<int[]> getLocation(Sneezewort plant) {
				return Optional.of(getFacade().getPixelPosition(plant));
			}

			@Override
			public Optional<ImageSprite> getCurrentSprite(Sneezewort plant) {
				return Optional.of((ImageSprite) getFacade().getCurrentSprite(
						plant));
			}
			
			@Override
			public Collection<Skullcab> getSkullcabs() {
				return getFacade().getAllGameObjects(getWorld()).stream().filter(Skullcab.class::isInstance).map(Skullcab.class::cast).collect(Collectors.toSet());
			}
			
			@Override
			public Optional<int[]> getLocation(Skullcab plant) {
				return Optional.of(getFacade().getPixelPosition(plant));
			}
			
			@Override
			public Optional<ImageSprite> getCurrentSprite(Skullcab plant) {
				return Optional.of((ImageSprite) getFacade().getCurrentSprite(
						plant));
			}
			

			@Override
			public Collection<Slime> getSlimes() {
				return getFacade().getAllGameObjects(getWorld()).stream().filter(Slime.class::isInstance).map(Slime.class::cast).collect(Collectors.toSet());
			}

			@Override
			public Optional<int[]> getLocation(Slime slime) {
				return Optional.of(getFacade().getPixelPosition(slime));
			}

			@Override
			public Collection<Shark> getSharks() {
				return getFacade().getAllGameObjects(getWorld()).stream().filter(Shark.class::isInstance).map(Shark.class::cast).collect(Collectors.toSet());
			}

			@Override
			public Optional<int[]> getLocation(Shark shark) {
				return Optional.of(getFacade().getPixelPosition(shark));
			}

			@Override
			public Optional<ImageSprite> getCurrentSprite(Shark shark) {
				return Optional.of((ImageSprite) getFacade().getCurrentSprite(
						shark));
			}

			@Override
			public Optional<ImageSprite> getCurrentSprite(Slime slime) {
				return Optional.of((ImageSprite) getFacade().getCurrentSprite(
						slime));
			}

			@Override
			public Optional<School> getSchool(Slime slime) {
				return catchErrorGet(() -> getFacade().getSchool(slime));
			}


		};
	}
	
	public String[] getAvailableMaps() {
		return new File("levels").list((file, name) -> name.endsWith(".tmx"));
	}
	

	public boolean setMapFile(String currentMap) {
		this.currentMap = currentMap;
		return readLevelFile(currentMap);
	}


	public String getMapFile() {
		return currentMap;
	}

	public AlienInfoProvider<Mazub> getAlienInfoProvider() {
		return alienInfoProvider;
	}

	public WorldInfoProvider getWorldInfoProvider() {
		return worldInfoProvider;
	}
	

	public ObjectInfoProvider getObjectInfoProvider() {
		return objectInfoProvider;
	}


	@Override
	public void addCommand(Command command) {
		super.addCommand(command);
	}

	private final Consumer<ModelException> errorHandler = new Consumer<ModelException>() {

		@Override
		public void accept(ModelException error) {
			addMessage(new Message(MessageType.ERROR, error.getMessage()));
			error.printStackTrace();
		}
	};

	protected <T> Optional<T> catchErrorGet(Supplier<T> action) {
		try {
			return Optional.ofNullable(action.get());
		} catch (ModelException e) {
			errorHandler.accept(e);
			return Optional.empty();
		}
	}

	public <T> void catchErrorAction(Runnable action) {
		try {
			action.run();
		} catch (ModelException e) {
			errorHandler.accept(e);
		}
	}

}