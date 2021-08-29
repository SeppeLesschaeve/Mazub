package jumpingalien.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.*;

import jumpingalien.facade.Facade;
import jumpingalien.facade.IFacade;
import jumpingalien.internal.gui.sprites.JumpingAlienSprites;
import jumpingalien.model.*;
import jumpingalien.util.Sprite;
import jumpingalien.util.ModelException;

class FullFacadeTest {

	// Precision constants for checks involving floating point numbers.
	public final static double HIGH_PRECISION = 0.1E-10;
	public final static double LOW_PRECISION = 0.01;

	// Some constants as defined in the assignment.
	public final static double MINIMUM_HORIZONTAL_VELOCITY = 1.0;
	public static final double V_VELOCITY_JUMPING = 8.0;
	public final static double HORIZONTAL_ACCELERATION = 0.9;
	public static final double V_ACCELERATION_JUMPING = -10.0;

	// Variable referencing the facade.
	IFacade facade = new Facade();

	// Variables referencing some predefined mazub's
	private static Mazub mazub_0_0, mazub_0_1000, mazub_100_0, mazub_100_1000,
			mazub_20_45;

	// Variables referencing some predefined sneezeworts.
	private static Sneezewort sneezewort_120_10;

	// Variables referencing some predefined schools.
	private static School someSchool;

	// Variables referencing a proper array of sprites for mazubs, for sneezeworts,
	// for Skullcabs, for slimes and for sharks.
	private static Sprite[] mazubSprites;
	private static Sprite[] sneezewortSprites;
	private static Sprite[] SkullcabSprites;
	private static Sprite[] slimeSprites;
	private static Sprite[] sharkSprites;
	private static Sprite[] spiderSprites;
	
	// Variable referencing a predefined world.
	private static World world_100_200, world_250_400;

	// Variables storing the actual score and the maximum score.
	private static int actualScore = 0;
	private static int maximumScore = 0;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Set up the array of sprites with several pictures to
		// move to the left and to the right.
		mazubSprites = new Sprite[] { new Sprite("Stationary Idle", 100, 50),
				new Sprite("Stationary Ducking", 75, 30),
				new Sprite("Ending Right Move", 90, 45),
				new Sprite("Ending Left Move", 90, 45),
				new Sprite("Moving Right and Jumping", 90, 45),
				new Sprite("Moving Left and Jumping", 90, 45),
				new Sprite("Moving Right and Ducking", 90, 30),
				new Sprite("Moving Left and Ducking", 90, 30),
				new Sprite("Moving Right 1", 90, 45),
				new Sprite("Moving Right 2", 90, 45),
				new Sprite("Moving Right 3", 90, 45),
				new Sprite("Moving Right 4", 90, 45),
				new Sprite("Moving Right 5", 90, 45), new Sprite("Moving Left 1", 90, 45),
				new Sprite("Moving Left 2", 90, 45), new Sprite("Moving Left 3", 90, 45),
				new Sprite("Moving Left 4", 90, 45),
				new Sprite("Moving Left 5", 90, 45), };
		sneezewortSprites = new Sprite[] { new Sprite("Sneezewort Moving Left", 40, 30),
				new Sprite("Sneezewort Moving Right", 40, 30) };
		SkullcabSprites = new Sprite[] { new Sprite("Scullcab Moving Up", 50, 40),
				new Sprite("Scullcab Moving Down", 50, 40) };
		slimeSprites = new Sprite[] { new Sprite("Slime Moving Right", 60, 60),
				new Sprite("Slime Moving Left", 60, 60) };
		sharkSprites = new Sprite[] { new Sprite("Shark Resting", 70, 30),
				new Sprite("Shark Moving Left", 70, 30),
				new Sprite("Shark Moving Right", 70, 30) };
		spiderSprites = new Sprite[] { new Sprite("Spider Moving Horizontally", 70, 30),
				new Sprite("Spider Moving Vertically", 70, 30),
				new Sprite("Spider Falling", 70, 30) };
//		// Instruction to see to it that the default sprite for all game objects has the
//		// proper size for the test suite.
		JumpingAlienSprites.setDefaultMazubSprite(mazubSprites[0]);
		JumpingAlienSprites.setDefaultPlantSprite(sneezewortSprites[0]);
		JumpingAlienSprites.setDefaultSlimeSprite(slimeSprites[0]);
		JumpingAlienSprites.setDefaultSharkSprite(sharkSprites[0]);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println();
		System.out.println("Score: " + actualScore + "/" + maximumScore);
	}

	@BeforeEach
	void setUp() throws Exception {
		facade.cleanAllSlimeIds();
		mazub_0_0 = facade.createMazub(0, 0, mazubSprites);
		mazub_0_1000 = facade.createMazub(0, 1000, mazubSprites);
		mazub_100_0 = facade.createMazub(100, 0, mazubSprites);
		mazub_100_1000 = facade.createMazub(100, 1000, mazubSprites);
		mazub_20_45 = facade.createMazub(20, 45, mazubSprites);
		sneezewort_120_10 = facade.createSneezewort(120, 10, sneezewortSprites);
		world_100_200 = facade.createWorld(10, 100, 200, new int[] { 10, 20 }, 20, 10);
		world_250_400 = facade.createWorld(5, 250, 400, new int[] { 33, 222 }, 25, 40);
		someSchool = facade.createSchool(world_100_200);
	}

	/*******************
	 * Tests for Mazub *
	 *******************/

	@Test
	void createMazub_LegalCase() throws Exception {
		maximumScore += 18;
		Mazub newMazub = facade.createMazub(10, 6, mazubSprites);
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newMazub));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newMazub),
				LOW_PRECISION);
		assertTrue(facade.getOrientation(newMazub) == 0);
		assertFalse(facade.isMoving(newMazub));
		assertFalse(facade.isJumping(newMazub));
		assertFalse(facade.isDucking(newMazub));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(newMazub));
		assertEquals(0.0, facade.getAcceleration(newMazub)[0], HIGH_PRECISION);
		assertTrue((Math.abs(facade.getAcceleration(newMazub)[1]) <= HIGH_PRECISION) ||
				((Math.abs(facade.getAcceleration(newMazub)[1]) >= 10.0 -
						LOW_PRECISION) &&
						(Math.abs(facade.getAcceleration(newMazub)[1]) <= 10.0 +
								LOW_PRECISION)));
		assertEquals(100, facade.getHitPoints(newMazub));
		if (facade.isTeamSolution()) {
			assertArrayEquals(mazubSprites, facade.getSprites(newMazub));
			assertSame(mazubSprites[0], facade.getCurrentSprite(newMazub));
		}
		actualScore += 18;
	}

	@Test
	void createMazub_OutsideUniverse() throws Exception {
		maximumScore += 4;
		assertThrows(ModelException.class,
				() -> facade.createMazub(-2000, 10, mazubSprites));
		assertThrows(ModelException.class,
				() -> facade.createMazub(200, Integer.MIN_VALUE, mazubSprites));
		actualScore += 4;
	}

	@Test
	void createMazub_IllegalSprites() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 8;
			// Non-effective array of sprites
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, (Sprite[]) null));
			// Not enough sprites
			assertThrows(ModelException.class, () -> facade.createMazub(5, 10,
					new Sprite[] { new Sprite("a", 1, 2) }));
			// Odd number of sprites
			Sprite[] oddSprites = new Sprite[13];
			for (int i = 0; i < oddSprites.length; i++)
				oddSprites[i] = new Sprite("z", i, i * 2);
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, oddSprites));
			// Non-effective sprites
			assertThrows(ModelException.class,
					() -> facade.createMazub(5, 10, new Sprite[] { null, null, null, null,
							null, null, null, null, null, null }));
			actualScore += 8;
		}
	}

	@Test
	void createMazub_LeakTest() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 10;
			Sprite[] clonedSprites = Arrays.copyOf(mazubSprites, mazubSprites.length);
			Mazub theMazub = facade.createMazub(10, 20, clonedSprites);
			clonedSprites[1] = null;
			Sprite[] currentSprites = facade.getSprites(theMazub);
			assertArrayEquals(mazubSprites, currentSprites);
			actualScore += 10;
		}
	}

	@Test
	void getSprites_LeakTest() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 10;
			Sprite[] clonedSprites = Arrays.copyOf(mazubSprites, mazubSprites.length);
			Mazub theMazub = facade.createMazub(10, 20, clonedSprites);
			Sprite[] mazubSprites = facade.getSprites(theMazub);
			mazubSprites[3] = null;
			assertArrayEquals(clonedSprites, facade.getSprites(theMazub));
			actualScore += 10;
		}
	}

	@Test
	void terminate_ObjectInWorld() throws Exception {
		maximumScore += 15;
		// Mazub
		facade.addGameObject(mazub_0_0, world_100_200);
		assertFalse(facade.isTerminatedGameObject(mazub_0_0));
		assertEquals(world_100_200, facade.getWorld(mazub_0_0));
		assertTrue(facade.getAllGameObjects(world_100_200).contains(mazub_0_0));
		facade.terminateGameObject(mazub_0_0);
		assertTrue(facade.isTerminatedGameObject(mazub_0_0));
		assertNull(facade.getWorld(mazub_0_0));
		assertFalse(facade.getAllGameObjects(world_100_200).contains(mazub_0_0));
		// Sneezewort
		Sneezewort someSneezewort = facade.createSneezewort(10, 20, sneezewortSprites);
		facade.terminateGameObject(someSneezewort);
		assertTrue(facade.isTerminatedGameObject(someSneezewort));
		assertNull(facade.getWorld(someSneezewort));
		// Skullcab
		Skullcab someSkullcab = facade.createSkullcab(10, 20, SkullcabSprites);
		facade.terminateGameObject(someSkullcab);
		assertTrue(facade.isTerminatedGameObject(someSkullcab));
		assertNull(facade.getWorld(someSkullcab));
		// Slime
		Slime someSlime = facade.createSlime(22, 10, 20, someSchool, slimeSprites);
		facade.terminateGameObject(someSlime);
		assertTrue(facade.isTerminatedGameObject(someSlime));
		assertNull(facade.getWorld(someSlime));
		assertFalse(facade.hasAsSlime(someSchool, someSlime));
		// Shark
		if ( shouldImplementSharks() ) {
			Shark someShark = facade.createShark(10, 20, sharkSprites);
			facade.terminateGameObject(someShark);
			assertTrue(facade.isTerminatedGameObject(someShark));
			assertNull(facade.getWorld(someShark));
		}
		actualScore += 15;
	}

	@Test
	void terminate_ObjectNotInWorld() throws Exception {
		maximumScore += 3;
		facade.terminateGameObject(mazub_0_0);
		assertTrue(facade.isTerminatedGameObject(mazub_0_0));
		assertNull(facade.getWorld(mazub_0_0));
		actualScore += 3;
	}

	@Test
	void changeActualPosition_LegalCases() throws Exception {
		maximumScore += 4;
		// Changing position of Mazub located in a game world.
		facade.addGameObject(mazub_0_0, world_100_200);
		facade.changeActualPosition(mazub_0_0, new double[] { 2.4, 3.6 });
		assertArrayEquals(new double[] { 2.4, 3.6 }, facade.getActualPosition(mazub_0_0),
				LOW_PRECISION);
		// Changing position of Mazub in the universe (not in a world).
		facade.changeActualPosition(mazub_100_0,
				new double[] { 33.6, Double.POSITIVE_INFINITY });
		assertArrayEquals(new double[] { 33.6, Double.POSITIVE_INFINITY },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		actualScore += 4;
	}

	@Test
	void changeActualPosition_IllegalCases() throws Exception {
		maximumScore += 4;
		// Null position
		assertThrows(ModelException.class,
				() -> facade.changeActualPosition(mazub_0_0, null));
		// Position with number of displacements different from 2.
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { 1.0, 2.0, 3.0 }));
		// Position with NaN as displacement
		assertThrows(ModelException.class, () -> facade.changeActualPosition(mazub_0_0,
				new double[] { Double.NaN, 33.6 }));
		actualScore += 4;
	}

	@Test
	void changeActualPosition_OutsideWorldBoundaries() throws Exception {
		maximumScore += 12;
		// Position to the left of the world
		Mazub theMazub = facade.createMazub(0, 0, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		facade.changeActualPosition(theMazub, new double[] { -0.1, 33.6 });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		// Position below the world
		theMazub = facade.createMazub(12, 7, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		facade.changeActualPosition(theMazub, new double[] { 12.3, -0.1 });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		// Position to the right of the world
		theMazub = facade.createMazub(12, 7, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		double newX = (facade.getSizeInPixels(world_250_400)[0] + 1) * 0.01;
		facade.changeActualPosition(theMazub, new double[] { newX, 14.7 });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		// Position above top of the world
		theMazub = facade.createMazub(12, 7, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		double newY = (facade.getSizeInPixels(world_250_400)[1] + 1) * 0.01;
		facade.changeActualPosition(theMazub, new double[] { 3.5, newY });
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertFalse(facade.hasAsGameObject(theMazub, world_250_400));
		actualScore += 12;
	}

	@Test
	void changeActualPosition_OnImpassableTerrain() throws Exception {
		maximumScore += 10;
		Mazub theMazub = facade.createMazub(0, 0, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		facade.setGeologicalFeature(world_250_400, 550, 300, SOLID_GROUND);
		assertThrows(ModelException.class,
				() -> facade.changeActualPosition(theMazub, new double[] { 5.2, 2.7 }));
		Sneezewort theSneezewort = facade.createSneezewort(10, 20, sneezewortSprites);
		facade.addGameObject(theSneezewort, world_250_400);
		facade.setGeologicalFeature(world_250_400, 220, 110, ICE);
		double[] newPosition = { 2.1, 1.0 };
		facade.changeActualPosition(theSneezewort, newPosition);
		assertArrayEquals(newPosition, facade.getActualPosition(theSneezewort),
				LOW_PRECISION);
		actualScore += 10;
	}

	@Test
	void changeActualPosition_DeadObject() throws Exception {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 10, 1010, MAGMA);
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		Mazub theMazub = facade.createMazub(0, 1000, mazubSprites);
		facade.addGameObject(theMazub, world_250_400);
		// After 0.3 seconds, mazub will be dead because of its contact with water.
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(0, facade.getHitPoints(theMazub));
		assertEquals(0.0, facade.getActualPosition(theMazub)[0], HIGH_PRECISION);
		assertTrue((facade.getActualPosition(theMazub)[1] >= 9.99 - LOW_PRECISION) &&
				facade.getActualPosition(theMazub)[1] <= 10.01);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(theMazub));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(theMazub));
		// 0.6 seconds after its dead, mazub must be removed from its game world.
		for (int i = 1; i <= 4; i++)
			facade.advanceWorldTime(world_250_400, 0.16);
		assertTrue(facade.isTerminatedGameObject(theMazub));
		assertNull(facade.getWorld(theMazub));
		assertFalse(facade.getAllGameObjects(world_250_400).contains(theMazub));
		actualScore += 15;
	}

	@Test
	public void startMoveLeft_LegalCase() throws Exception {
		maximumScore += 8;
		facade.startMoveLeft(mazub_100_0);
		assertTrue(facade.isMoving(mazub_100_0));
		assertTrue(facade.getOrientation(mazub_100_0) < 0);
		assertArrayEquals(new double[] { -MINIMUM_HORIZONTAL_VELOCITY, 0.0 },
				facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { -HORIZONTAL_ACCELERATION, 0.0 },
				facade.getAcceleration(mazub_100_0), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazub_100_0));
		actualScore += 8;
	}

	@Test
	public void startMoveRight_LegalCase() throws Exception {
		maximumScore += 4;
		facade.startMoveRight(mazub_100_0);
		assertTrue(facade.isMoving(mazub_100_0));
		assertTrue(facade.getOrientation(mazub_100_0) > 0);
		assertArrayEquals(new double[] { MINIMUM_HORIZONTAL_VELOCITY, 0.0 },
				facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { HORIZONTAL_ACCELERATION, 0.0 },
				facade.getAcceleration(mazub_100_0), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[8], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void startMove_AlreadyMoving() throws Exception {
		maximumScore += 4;
		facade.startMoveLeft(mazub_100_0);
		assertThrows(ModelException.class, () -> facade.startMoveLeft(mazub_100_0));
		assertThrows(ModelException.class, () -> facade.startMoveRight(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void startMove_DeadMazub() throws Exception {
		maximumScore += 4;
		facade.setGeologicalFeature(world_250_400, 80, 40, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 30, 50, MAGMA);
		facade.addGameObject(mazub_20_45, world_250_400);
		// Mazub will be dead after more than 0.2 seconds in contact with MAGMA.
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isDeadGameObject(mazub_20_45));
		assertThrows(ModelException.class, () -> facade.startMoveLeft(mazub_20_45));
		assertThrows(ModelException.class, () -> facade.startMoveRight(mazub_20_45));
		actualScore += 4;
	}

	@Test
	public void endMove_LegalCase() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 80, 40, SOLID_GROUND);
		facade.addGameObject(mazub_20_45, world_250_400);
		facade.startMoveLeft(mazub_20_45);
		facade.advanceWorldTime(world_250_400, 0.1);
		facade.endMove(mazub_20_45);
		assertFalse(facade.isMoving(mazub_20_45));
		assertTrue(facade.getOrientation(mazub_20_45) < 0);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_20_45),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_20_45),
				HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[3], facade.getCurrentSprite(mazub_20_45));
		actualScore += 6;
	}

	@Test
	public void endMove_NotMoving() throws Exception {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.endMove(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startJump_LegalCase() throws Exception {
		maximumScore += 6;
		facade.startJump(mazub_100_0);
		assertTrue(facade.isJumping(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, V_VELOCITY_JUMPING },
				facade.getVelocity(mazub_100_0), HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, V_ACCELERATION_JUMPING },
				facade.getAcceleration(mazub_100_0), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void startJump_AlreadyJumping() throws Exception {
		maximumScore += 2;
		facade.startJump(mazub_100_0);
		assertThrows(ModelException.class, () -> facade.startJump(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startJump_DeadMazub() throws Exception {
		maximumScore += 2;
		facade.setGeologicalFeature(world_250_400, 80, 40, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 30, 50, MAGMA);
		facade.addGameObject(mazub_20_45, world_250_400);
		// Mazub will be dead after more than 0.2 seconds in contact with MAGMA.
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isDeadGameObject(mazub_20_45));
		assertThrows(ModelException.class, () -> facade.startJump(mazub_20_45));
		actualScore += 2;
	}

	@Test
	public void endJump_PositiveVelocity() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub still has a velocity of 6.5 m/s after 0.15 seconds of jumping.
		facade.advanceTime(mazub_100_0, 0.15);
		facade.endJump(mazub_100_0);
		assertFalse(facade.isJumping(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void endJump_NegativeVelocity() throws Exception {
		maximumScore += 4;
		facade.startJump(mazub_100_0);
		// Mazub has a velocity of -1.0 m/s after 0.9 seconds of jumping.
		for (int i = 1; i <= 6; i++)
			facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.getVelocity(mazub_100_0)[1] < 0.0);
		double[] oldVelocity = facade.getVelocity(mazub_100_0);
		facade.endJump(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		assertFalse(facade.isJumping(mazub_100_0));
		assertArrayEquals(oldVelocity, facade.getVelocity(mazub_100_0), LOW_PRECISION);
		actualScore += 4;
	}

	@Test
	public void endJump_NotJumping() throws Exception {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.endJump(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startDuck_StationaryMazub() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[1], facade.getCurrentSprite(mazub_100_0));
		actualScore += 4;
	}

	@Test
	public void startDuck_AlreadyDucking() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startDuck(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[1], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void startDuck_MazubMovingAndJumping() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		facade.startJump(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertTrue(facade.isDucking(mazub_100_0));
		assertArrayEquals(new double[] { -1.0, 8.0 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		assertArrayEquals(new double[] { 0.0, -10.0 },
				facade.getAcceleration(mazub_100_0), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_MazubDucking() throws Exception {
		maximumScore += 2;
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.startMoveLeft(mazub_100_1000);
		facade.advanceTime(mazub_100_1000, 0.15);
		facade.startDuck(mazub_100_1000);
		facade.endDuck(mazub_100_1000);
		assertFalse(facade.isDucking(mazub_100_1000));
		assertArrayEquals(new double[] { -1.0, 0.0 }, facade.getVelocity(mazub_100_1000),
				LOW_PRECISION);
		assertArrayEquals(new double[] { -0.9, 0.0 },
				facade.getAcceleration(mazub_100_1000), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 2;
	}

	@Test
	public void endDuck_NotDucking() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.endDuck(mazub_100_0);
		assertFalse(facade.isDucking(mazub_100_0));
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 2;
	}

	@Test
	public void endDuck_OverlappingImpassableTerrain() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 8;
			facade.setGeologicalFeature(world_250_400, 220, 1035, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 200, 995, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 250, 995, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 300, 995, SOLID_GROUND);
			facade.addGameObject(mazub_100_1000, world_250_400);
			// Mazub will move over a distance of 100.0 cm in 1 second.
			facade.startMoveRight(mazub_100_1000);
			facade.startDuck(mazub_100_1000);
			for (int i = 0; i < 10; i++)
				facade.advanceTime(mazub_100_1000, 0.1);
			// Mazub will not be able to stand up, because of the solid ground at
			// (220,35).
			facade.endDuck(mazub_100_1000);
			assertTrue(facade.isDucking(mazub_100_1000));
			// Mazub will move over 50.0 cm in 0.5 seconds.
			for (int i = 0; i < 5; i++)
				facade.advanceTime(mazub_100_1000, 0.1);
			if (facade.isTeamSolution())
				assertTrue(facade.getCurrentSprite(mazub_100_1000)
						.equals(mazubSprites[6]) ||
						facade.getCurrentSprite(mazub_100_1000).equals(mazubSprites[11]));
			// Mazub will now be able to stand up, and might already stand up at this
			// point.
			facade.endDuck(mazub_100_1000);
			assertFalse(facade.isDucking(mazub_100_1000));
			if (facade.isTeamSolution())
				assertEquals(mazubSprites[8], facade.getCurrentSprite(mazub_100_1000));
			actualScore += 8;
		}
	}

	@Test
	public void endDuck_OverlappingOtherGameObject() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 6;
			facade.addGameObject(mazub_100_0, world_250_400);
			facade.startDuck(mazub_100_0);
			Slime slimeToBumpAgainst = facade.createSlime(10, 120, 35, someSchool,
					slimeSprites);
			facade.addGameObject(slimeToBumpAgainst, world_250_400);
			// Mazub will not be able to stand up, because of the newly added game object.
			facade.endDuck(mazub_100_0);
			if (facade.isTeamSolution())
				assertEquals(mazubSprites[1], facade.getCurrentSprite(mazub_100_0));
			assertTrue(facade.isDucking(mazub_100_0));
			// After removing the newly added game object, mazub can stand up.
			facade.removeGameObject(slimeToBumpAgainst, world_250_400);
			facade.endDuck(mazub_100_0);
			assertFalse(facade.isDucking(mazub_100_0));
			if (facade.isTeamSolution())
				assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
			actualScore += 6;
		}
	}

	@Test
	public void advanceTime_MazubStationary() throws Exception {
		maximumScore += 4;
		facade.setGeologicalFeature(world_100_200, 120, 990, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_100_200);
		facade.advanceWorldTime(world_100_200, 0.15);
		assertEquals(1.0, facade.getActualPosition(mazub_100_1000)[0], HIGH_PRECISION);
		assertTrue(
				(facade.getActualPosition(mazub_100_1000)[1] >= 9.99 - LOW_PRECISION) &&
						facade.getActualPosition(mazub_100_1000)[1] <= 10.01);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(mazub_100_1000),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 },
				facade.getAcceleration(mazub_100_1000), HIGH_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 4;
	}

	@Test
	public void advanceTime_MazubMovingLeftWithinBoundaries() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.startMoveLeft(mazub_100_1000);
		// Mazub will move 14.882 cm over 0.14 seconds; its velocity increases with 0.126
		// m/s
		facade.advanceTime(mazub_100_1000, 0.14);
		assertEquals(0.85118, facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		assertEquals(-1.126, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[14], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubMovingRightWithinBoundaries() throws Exception {
		maximumScore += 3;
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.startMoveRight(mazub_100_1000);
		// Mazub will move 18.3 cm over 0.17 seconds; its velocity increases with 0.153
		// m/s
		facade.advanceWorldTime(world_250_400, 0.17);
		assertEquals(1.183, facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		assertEquals(1.153, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[10], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 3;
	}

	@Test
	public void advanceTime_MazubReachingLeftBorder() throws Exception {
		maximumScore += 5;
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.changeActualPosition(mazub_100_1000, new double[] { 0.05, 10.0 });
		facade.startMoveLeft(mazub_100_1000);
		// Mazub reaches left border after 0.1024 seconds.
		facade.advanceWorldTime(world_250_400, 0.19);
		assertTrue(facade.isTerminatedGameObject(mazub_100_1000));
		assertNull(facade.getWorld(mazub_100_1000));
		actualScore += 5;
	}

	@Test
	public void advanceTime_MazubReachingRightBorder() throws Exception {
		maximumScore += 3;
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.changeActualPosition(mazub_100_1000, new double[] {
				facade.getSizeInPixels(world_250_400)[0] / 100.0 - 0.05, 10.0 });
		facade.startMoveRight(mazub_100_1000);
		// Mazub reaches right border after 0.1447 seconds.
		facade.advanceWorldTime(world_250_400, 0.19);
		assertTrue(facade.isTerminatedGameObject(mazub_100_1000));
		assertNull(facade.getWorld(mazub_100_1000));
		actualScore += 3;
	}

	@Test
	public void advanceTime_MazubCollidingWithSlimeAtLeftSide() throws Exception {
		maximumScore += 10;
		Slime slimeToCollideWith = facade.createSlime(10, 0, 1000, someSchool,
				slimeSprites);
		facade.addGameObject(slimeToCollideWith, world_250_400);
		int movingMazubXposition = 20 +
				facade.getCurrentSprite(slimeToCollideWith).getWidth();
		Mazub moving_mazub = facade.createMazub(movingMazubXposition, 1000, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveLeft(moving_mazub);
		// The moving mazub is at a distance of 20 cm from the slime and will
		// collide with it after 0.184656 seconds.
		facade.advanceTime(moving_mazub, 0.19);
		assertEquals((movingMazubXposition - 20) / 100.0,
				facade.getActualPosition(moving_mazub)[0], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(100-20,facade.getHitPoints(moving_mazub));
		actualScore += 10;
	}

	@Test
	public void advanceTime_MazubCollidingWithObjectAtRightSide() throws Exception {
		maximumScore += 10;
		Object sharkToCollideWith = null;
		Slime slimeToCollideWith = null;
		if (shouldImplementSharks()) {
			sharkToCollideWith= facade.createShark(200, 990,sharkSprites);
			facade.addGameObject(sharkToCollideWith, world_250_400);
		}
		else {
			slimeToCollideWith = facade.createSlime(10, 200, 990, null, slimeSprites);
			facade.addGameObject(slimeToCollideWith, world_250_400);
		}
		int movingMazubXPos = facade.isTeamSolution() ? 95 : 85;
		Mazub moving_mazub = facade.createMazub(movingMazubXPos, 1000, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveRight(moving_mazub);
		// The moving mazub is at a distance of 15 cm from the object to collide with and will
		// collide with it after 0.141 seconds.
		facade.advanceTime(moving_mazub, 0.15);
		double expectedMazubXPos = facade.isTeamSolution() ? 1.1 : 1.0;
		assertEquals(expectedMazubXPos, facade.getActualPosition(moving_mazub)[0],
				LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		if (shouldImplementSharks())
			assertEquals(100-50,facade.getHitPoints(moving_mazub));
		else
			assertEquals(100-20,facade.getHitPoints(moving_mazub));
		actualScore += 10;
	}

	@Test
	public void advanceTime_MazubReachingImpassbleTerrainAtLeftSide() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 100, 1010, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 55, 995, SOLID_GROUND);
		Mazub moving_mazub = facade.createMazub(110, 999, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveLeft(moving_mazub);
		// The moving mazub will collide with the impassable terrain after 0.0489 seconds.
		facade.advanceTime(moving_mazub, 0.1);
		assertEquals(1.05, facade.getActualPosition(moving_mazub)[0], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingImpassbleTerrainAtRightSide() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 205, 1015, SOLID_GROUND);
		int movingMazubXPos = facade.isTeamSolution() ? 100 : 90;
		Mazub moving_mazub = facade.createMazub(movingMazubXPos, 1000, mazubSprites);
		facade.addGameObject(moving_mazub, world_250_400);
		facade.startMoveRight(moving_mazub);
		// The moving mazub will collide with the impassable terrain after 0.141 seconds.
		facade.advanceTime(moving_mazub, 0.15);
		double expectedPosition = facade.isTeamSolution() ? 1.15 : 1.05;
		assertEquals(expectedPosition, facade.getActualPosition(moving_mazub)[0],
				LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(moving_mazub)[0], HIGH_PRECISION);
		assertEquals(0.0, facade.getAcceleration(moving_mazub)[0], HIGH_PRECISION);
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubReachingTopSpeed() throws Exception {
		maximumScore += 8;
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 200, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 250, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 300, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 350, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 400, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 450, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 500, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 550, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		// Mazub needs (3.0-1.0)/0.9 = 2.22 seconds to reach top speed.
		// Mazub will move over 452.778 cm in a period of 2.25 seconds
		// (444.438 cm in 2.2222 seconds at increasing speed,
		// and 8.34 cm in a period of 2.25-0.2222 seconds at top speed)
		facade.startMoveRight(mazub_100_1000);
		for (int i = 1; i <= 15; i++)
			facade.advanceTime(mazub_100_1000, 0.15);
		assertEquals(3.0, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		assertEquals(5.52778, facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		// After having reached its top speed, Mazub no longer accelerates.
		// It will move over 30.0 cm at top speed in a period of 0.1 seconds.
		facade.advanceTime(mazub_100_1000, 0.1);
		assertEquals(5.52778 + 0.3, facade.getActualPosition(mazub_100_1000)[0],
				LOW_PRECISION);
		assertEquals(3.0, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[9], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 8;
	}

	@Test
	public void advanceTime_MazubJumpingUpwardsOnly() throws Exception {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 70, 40, SOLID_GROUND);
		facade.addGameObject(mazub_20_45, world_250_400);
		facade.startJump(mazub_20_45);
		// Mazub will jump over 108.75 cm in 0.15 seconds; it will have a velocity of
		// 6.5m/s
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(0.45 + 1.0875, facade.getActualPosition(mazub_20_45)[1],
				LOW_PRECISION);
		assertEquals(6.5, facade.getVelocity(mazub_20_45)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_20_45));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingOutOfWorld() throws Exception {
		maximumScore += 6;
		int worldHeight = facade.getSizeInPixels(world_250_400)[1] / 100;
		facade.changeActualPosition(mazub_100_0, new double[] { 1.0, worldHeight - 0.3 });
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub reaches top of the world after 0.0384 seconds.
		facade.advanceTime(mazub_100_0, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAndFallingDown() throws Exception {
		maximumScore += 6;
		// Mazub starts falling after 0.8 seconds; it will then have jumped over 320 cm;
		// its velocity is reduced to 0 m/s.
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(3.2, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(0.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		// After 0.18 seconds, mazub will have fallen over 16.2 cm; its velocity is
		// changed to -1.8 m/s.
		facade.advanceTime(mazub_100_0, 0.18);
		assertEquals(3.2 - 0.162, facade.getActualPosition(mazub_100_0)[1],
				LOW_PRECISION);
		assertEquals(-1.8, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAndFallingOutOfWorld() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_100_0, world_250_400);
		// Mazub reaches ground after 1.6 seconds; its velocity is changed to -8.0 m/s
		facade.startJump(mazub_100_0);
		for (int i = 1; i <= 10; i++)
			facade.advanceTime(mazub_100_0, 0.16);
		assertEquals(0.0, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(-8.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		// If time advances, mazub will fall out of its world.
		facade.advanceTime(mazub_100_0, 0.18);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubJumpingAgainstImpassableTerrain() throws Exception {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 150, 200, SOLID_GROUND);
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		// Mazub reaches solid ground after 0.2169 seconds, and then starts falling down.
		facade.advanceTime(mazub_100_0, 0.1);
		facade.advanceTime(mazub_100_0, 0.1169);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.5, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// Mazub falls down 5 cm over a period of 0.1 seconds
		facade.advanceTime(mazub_100_0, 0.1);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(1.45, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// Mazub reaches bottom of world after falling 0.5385 seconds.
		for (int i = 0; i < 3; i++)
			facade.advanceTime(mazub_100_0, 0.18);
		assertTrue(facade.isTerminatedGameObject(mazub_100_0));
		assertNull(facade.getWorld(mazub_100_0));
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubLandingOnImpassableTerrain() throws Exception {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 100, 45, SOLID_GROUND);
		Mazub jumpingMazub = facade.createMazub(100, 150, mazubSprites);
		facade.addGameObject(jumpingMazub, world_250_400);
		facade.startJump(jumpingMazub);
		// Mazub reaches top after 0.8 seconds; the distance of the jump is 320 cm.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(jumpingMazub, 0.16);
		assertEquals(1.0, facade.getActualPosition(jumpingMazub)[0], LOW_PRECISION);
		assertEquals(4.70, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub is still falling after 0.9 seconds.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(jumpingMazub, 0.18);
		assertEquals(0.65, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub lands on solid ground after another 0.0165 seconds.
		facade.advanceTime(jumpingMazub, 0.04);
		assertTrue((0.49 <= facade.getActualPosition(jumpingMazub)[1]) &&
				facade.getActualPosition(jumpingMazub)[1] <= 0.51);
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubBumpingAgainstOtherGameObject() throws Exception {
		maximumScore += 15;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startJump(mazub_100_0);
		int mazubHeight = facade.getCurrentSprite(mazub_100_0).getHeight();
		Slime slimeToBumpAgainst = facade.createSlime(10, 150, mazubHeight + 50,
				someSchool, slimeSprites);
		facade.addGameObject(slimeToBumpAgainst, world_250_400);
		// Mazub reaches slime after 0.06515 seconds, and then starts falling down.
		// The slime will have moved 0.0139 cm to the right.
		facade.advanceWorldTime(world_250_400, 0.06515);
		assertEquals(1.0, facade.getActualPosition(mazub_100_0)[0], LOW_PRECISION);
		assertEquals(0.50, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(1.5 + 0.00139, facade.getActualPosition(slimeToBumpAgainst)[0],
				LOW_PRECISION);
		// The mazub now starts falling down. It will have fallen over 5cm in 0.1 seconds.
		// The hit points of one of the objects will have changed because of the
		// collision.
		facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(0.5 - 0.05, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		assertEquals(1.5 + 0.009546, facade.getActualPosition(slimeToBumpAgainst)[0],
				LOW_PRECISION);
		assertTrue(facade.getHitPoints(mazub_100_0) +
				facade.getHitPoints(slimeToBumpAgainst) < 200);
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubLandingOnTopOfGameObject() throws Exception {
		maximumScore += 15;
		Mazub jumpingMazub = facade.createMazub(100, 150, mazubSprites);
		facade.addGameObject(jumpingMazub, world_250_400);
		Slime slimeToLandOn = facade.createSlime(10, 50, 0, null, slimeSprites);
		facade.addGameObject(slimeToLandOn, world_250_400);
		facade.startJump(jumpingMazub);
		// Mazub reaches top after 0.8 seconds; the distance of the jump is 320 cm.
		for (int i = 0; i < 5; i++)
			facade.advanceWorldTime(world_250_400, 0.16);
		assertEquals(1.0, facade.getActualPosition(jumpingMazub)[0], LOW_PRECISION);
		assertEquals(4.70, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// The total time to land depends on the height of both objects.
		int slimeHeight = facade.getCurrentSprite(slimeToLandOn).getHeight();
		double timeToLand = Math.sqrt((470 - slimeHeight) / 500.0);
		// Mazub is still falling after 0.9 seconds.
		for (int i = 0; i < 5; i++)
			facade.advanceWorldTime(world_250_400, 0.18);
		timeToLand -= 0.9;
		assertEquals(0.65, facade.getActualPosition(jumpingMazub)[1], LOW_PRECISION);
		// Mazub reaches the top of the slime after the remaining time to land.
		// The hit points of one of both objects will have been decreased
		facade.advanceWorldTime(world_250_400, timeToLand + 0.03);
		assertEquals(slimeHeight / 100.0, facade.getActualPosition(jumpingMazub)[1],
				LOW_PRECISION);
		assertTrue(facade.getHitPoints(jumpingMazub) +
				facade.getHitPoints(slimeToLandOn) < 200);
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubFalling() throws Exception {
		maximumScore += 6;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.changeActualPosition(mazub_100_0, new double[] { 10.0, 2.5 });
		// After 0.1 seconds, Mazub will have fallen over 5 cm; its velocity has changed
		// to -1 m/s.
		facade.advanceTime(mazub_100_0, 0.1);
		assertArrayEquals(new double[] { 10.0, 2.5 - 0.05 },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		assertEquals(-1.0, facade.getVelocity(mazub_100_0)[1], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_0));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubMovingRightAndDucking() throws Exception {
		maximumScore += 12;
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 200, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 250, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 300, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.startMoveRight(mazub_100_1000);
		// Mazub will have moved over 14.822 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.126 m/s.
		facade.advanceTime(mazub_100_1000, 0.14);
		assertEquals(1.14822, facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		assertEquals(1.126, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		// Starting to duck
		facade.startDuck(mazub_100_1000);
		assertTrue(facade.isDucking(mazub_100_1000));
		assertEquals(MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_1000)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[6], facade.getCurrentSprite(mazub_100_1000));
		// Mazub will have moved over 12 cm in a period of 0.12 seconds. Its
		// velocity stays constant at 1.0 m/s.
		facade.advanceTime(mazub_100_1000, 0.12);
		assertEquals(1.14822 + 0.12, facade.getActualPosition(mazub_100_1000)[0],
				LOW_PRECISION);
		assertEquals(MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_1000)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[6], facade.getCurrentSprite(mazub_100_1000));
		// Ending the duck
		facade.endDuck(mazub_100_1000);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[8], facade.getCurrentSprite(mazub_100_1000));
		assertFalse(facade.isDucking(mazub_100_1000));
		assertEquals(HORIZONTAL_ACCELERATION, facade.getAcceleration(mazub_100_1000)[0],
				LOW_PRECISION);
		// Mazub will have moved over 10.45 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.09 m/s.
		facade.advanceTime(mazub_100_1000, 0.1);
		assertEquals(1.14822 + 0.12 + 0.1045, facade.getActualPosition(mazub_100_1000)[0],
				0.1);
		assertEquals(1.09, facade.getVelocity(mazub_100_1000)[0], 0.1E-10);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[9], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 12;
	}

	@Test
	public void advanceTime_MazubMovingLeftAndDucking() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_100_1000, world_250_400);
		facade.startMoveLeft(mazub_100_1000);
		// Mazub will have moved over 14.882 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.126 m/s.
		facade.advanceTime(mazub_100_1000, 0.14);
		assertEquals(1.0 - 0.14882, facade.getActualPosition(mazub_100_1000)[0],
				LOW_PRECISION);
		assertEquals(-1.126, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		// Starting to duck
		facade.startDuck(mazub_100_1000);
		assertTrue(facade.isDucking(mazub_100_1000));
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_1000)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazub_100_1000));
		// Mazub will have moved over 12 cm in a period of 0.12 seconds. Its
		// velocity stays constant at 1.0 m/s.
		facade.advanceTime(mazub_100_1000, 0.12);
		assertEquals(1.0 - 0.14882 - 0.12, facade.getActualPosition(mazub_100_1000)[0],
				LOW_PRECISION);
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_1000)[0],
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazub_100_1000));
		// Ending the duck
		facade.endDuck(mazub_100_1000);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazub_100_1000));
		assertFalse(facade.isDucking(mazub_100_1000));
		assertEquals(-HORIZONTAL_ACCELERATION, facade.getAcceleration(mazub_100_1000)[0],
				LOW_PRECISION);
		assertEquals(-MINIMUM_HORIZONTAL_VELOCITY, facade.getVelocity(mazub_100_1000)[0],
				LOW_PRECISION);
		// Mazub will have moved over 10.45 cm in a period of 0.14 seconds. Its
		// velocity will have raised to 1.09 m/s.
		facade.advanceTime(mazub_100_1000, 0.1);
		assertEquals(1.0 - 0.14882 - 0.12 - 0.1045,
				facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		assertEquals(-1.09, facade.getVelocity(mazub_100_1000)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[14], facade.getCurrentSprite(mazub_100_1000));
		actualScore += 4;
	}

	@Test
	public void advanceTime_MazubMovingAndJumping() throws Exception {
		maximumScore += 10;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveRight(mazub_100_0);
		facade.startJump(mazub_100_0);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[4], facade.getCurrentSprite(mazub_100_0));
		// Mazub will have moved over 14.822 cm in 0.14 seconds, and will have a
		// horizontal velocity of 1.126 m/s.
		// Mazub will also have jumped over 102.2 cm in 0.14 seconds, and will have a
		// vertical velocity of 6.6 m/s.
		facade.advanceTime(mazub_100_0, 0.14);
		assertArrayEquals(new double[] { 1.14822, 1.022 },
				facade.getActualPosition(mazub_100_0), LOW_PRECISION);
		assertArrayEquals(new double[] { 1.126, 6.6 }, facade.getVelocity(mazub_100_0),
				LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(mazubSprites[4], facade.getCurrentSprite(mazub_100_0));
		actualScore += 10;
	}

	@Test
	public void advanceTime_MazubInWater() {
		maximumScore += 12;
		facade.setGeologicalFeature(world_250_400, 120, 1010, WATER);
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		// No loss of hit points as long as the time in water is below 0.2 seconds.
		facade.advanceTime(mazub_100_1000, 0.15);
		assertEquals(100, facade.getHitPoints(mazub_100_1000));
		// After 0.30 seconds in water, mazub looses 2 hit points.
		facade.advanceTime(mazub_100_1000, 0.15);
		assertEquals(98, facade.getHitPoints(mazub_100_1000));
		// After 0.98 seconds in water, mazub has lost 8 hit points in total.
		for (int i = 1; i <= 4; i++)
			facade.advanceTime(mazub_100_1000, 0.15);
		facade.advanceTime(mazub_100_1000, 0.08);
		assertEquals(92, facade.getHitPoints(mazub_100_1000));
		actualScore += 12;
	}

	@Test
	public void advanceTime_MazubInAndOutWater() {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 120, 1010, WATER);
		facade.setGeologicalFeature(world_250_400, 050, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 100, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 200, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 250, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		// After 0.45 seconds in water, mazub looses 4 hit points.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(mazub_100_1000, 0.15);
		assertEquals(96, facade.getHitPoints(mazub_100_1000));
		// After moving to the right for at least 0.2268 seconds, mazub is out of the
		// water.
		// The hit points after moving to the right for 0.9 seconds may therefore only
		// diminish with 2.
		facade.startMoveRight(mazub_100_1000);
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_1000, 0.18);
		assertEquals(94, facade.getHitPoints(mazub_100_1000));
		assertEquals(2.2645, facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		// After moving back to the left for 0.9 seconds, mazub will only have contact
		// with water for < 0.2 seconds (its velocity increases)
		facade.endMove(mazub_100_1000);
		facade.startMoveLeft(mazub_100_1000);
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_1000, 0.18);
		assertEquals(94, facade.getHitPoints(mazub_100_1000));
		assertEquals(1.0, facade.getActualPosition(mazub_100_1000)[0], LOW_PRECISION);
		// After moving for another 0.15 seconds, the hit points must be diminished with
		// 2.
		facade.advanceTime(mazub_100_1000, 0.15);
		assertEquals(92, facade.getHitPoints(mazub_100_1000));
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubDyingInWater() {
		maximumScore += 6;
		facade.setGeologicalFeature(world_250_400, 120, 1010, WATER);
		facade.setGeologicalFeature(world_250_400, 120, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		// After 9.90 seconds in water, mazub has lost 98 hit points.
		for (int i = 1; i <= 99; i++)
			facade.advanceTime(mazub_100_1000, 0.1);
		assertEquals(2, facade.getHitPoints(mazub_100_1000));
		// After another 0.15 seconds in water, mazub is dead.
		facade.advanceTime(mazub_100_1000, 0.15);
		assertEquals(0, facade.getHitPoints(mazub_100_1000));
		// After another 0.60 seconds, mazub is terminated and removed from its world
		for (int i = 1; i <= 5; i++)
			facade.advanceTime(mazub_100_1000, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_100_1000));
		assertNull(facade.getWorld(mazub_100_1000));
		actualScore += 6;
	}

	@Test
	public void advanceTime_MazubInMagma() {
		maximumScore += 16;
		facade.setGeologicalFeature(world_250_400, 120, 1010, MAGMA);
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		// Mazub immediately looses 50 hit points when in contact with water.
		facade.advanceTime(mazub_100_1000, 0.10);
		assertEquals(50, facade.getHitPoints(mazub_100_1000));
		// After 0.15 seconds in magma, the loss of hit points is still equal to 50.
		facade.advanceTime(mazub_100_1000, 0.05);
		assertEquals(50, facade.getHitPoints(mazub_100_1000));
		// After 0.22 seconds in magma, mazug has lost all its hit points
		facade.advanceTime(mazub_100_1000, 0.07);
		assertEquals(0, facade.getHitPoints(mazub_100_1000));
		assertTrue(facade.isDeadGameObject(mazub_100_1000));
		// After 0.45 seconds, mazub is still dead.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(mazub_100_1000, 0.15);
		assertTrue(facade.isDeadGameObject(mazub_100_1000));
		// After another 0.30 seconds, mazub is terminated and is no longer part of its
		// world.
		for (int i = 1; i <= 2; i++)
			facade.advanceTime(mazub_100_1000, 0.15);
		assertTrue(facade.isTerminatedGameObject(mazub_100_1000));
		assertNull(facade.getWorld(mazub_100_1000));
		actualScore += 16;
	}

	@Test
	public void advanceTime_MazubInAndOutMagma() {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 120, 0, MAGMA);
		facade.addGameObject(mazub_100_0, world_250_400);
		// After jumping for 0.0063 seconds, mazub is out of the magma. The hit points
		// are diminished with 50.
		facade.startJump(mazub_100_0);
		facade.advanceTime(mazub_100_0, 0.01);
		assertEquals(50, facade.getHitPoints(mazub_100_0));
		// After another 0.19 seconds, mazub is still out of magma.
		facade.advanceTime(mazub_100_0, 0.19);
		assertEquals(1.40, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// After falling down for 0.5196 seconds, mazub is just above the magma
		facade.endJump(mazub_100_0);
		facade.advanceTime(mazub_100_0, 0.17);
		facade.advanceTime(mazub_100_0, 0.17);
		facade.advanceTime(mazub_100_0, 0.1796);
		assertEquals(50, facade.getHitPoints(mazub_100_0));
		assertEquals(0.05, facade.getActualPosition(mazub_100_0)[1], LOW_PRECISION);
		// After falling down a bit more, mazub is back in magma and its hitpoints
		// are reduced to 0.
		facade.advanceTime(mazub_100_0, 0.005);
		assertEquals(0, facade.getHitPoints(mazub_100_0));
		actualScore += 15;
	}

	@Test
	public void advanceTime_MazubInGas() {
		maximumScore += 16;
		facade.setGeologicalFeature(world_250_400, 220, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 175, 1010, GAS);
		facade.setGeologicalFeature(world_250_400, 125, 1020, WATER);
		facade.addGameObject(mazub_100_1000, world_250_400);
		// Mazub reaches gas after 0.1846 seconds. Mazub immediately looses 4 hit points.
		facade.startMoveLeft(mazub_100_1000);
		int mazubWidth = facade.getCurrentSprite(mazub_100_1000).getWidth();
		double timeToLeaveGas = (-100.0 +
				Math.sqrt(100.0 * 100.0 + 4.0 * 45.0 * (mazubWidth + 30))) / (2 * 45.0);
		facade.changeActualPosition(mazub_100_1000, new double[] { 2.0, 10.0 });
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.05);
		timeToLeaveGas -= 0.2;
		assertEquals(96, facade.getHitPoints(mazub_100_1000));
		// 0.2 seconds after the first contact with gas, mazub looses another 4 hit
		// points.
		facade.advanceWorldTime(world_250_400, 0.12);
		facade.advanceWorldTime(world_250_400, 0.12);
		timeToLeaveGas -= 0.24;
		assertEquals(92, facade.getHitPoints(mazub_100_1000));
		// 0.5593 seconds after starting its first move, mazub reaches water.
		// It has no impact as long as mazub is also in contact with gas.
		facade.advanceWorldTime(world_250_400, 0.11);
		facade.advanceWorldTime(world_250_400, 0.1);
		timeToLeaveGas -= 0.21;
		assertEquals(88, facade.getHitPoints(mazub_100_1000));
		// When the time to leave gas has passed,mazub is no longer in contact
		// with gas, but still in contact with water.
		facade.advanceWorldTime(world_250_400, 0.1);
		facade.advanceWorldTime(world_250_400, timeToLeaveGas - 0.1);
		assertEquals(84, facade.getHitPoints(mazub_100_1000));
		// after 0.2 seconds in contact with water only, mazub loses 2 hit points.
		facade.advanceWorldTime(world_250_400, 0.1);
		facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(82, facade.getHitPoints(mazub_100_1000));
		actualScore += 16;
	}

	@Test
	public void advanceTime_EatingSingleSneezewort() {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 50, 995, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 120, 995, SOLID_GROUND);
		Mazub theEatingMazub = facade.createMazub(100, 1000, mazubSprites);
		facade.addGameObject(theEatingMazub, world_250_400);
		Sneezewort theSneezewortToEat = facade.createSneezewort(220, 1010,
				sneezewortSprites);
		facade.addGameObject(theSneezewortToEat, world_250_400);
		// Mazub strats moving towards the sneezewort.
		facade.startMoveRight(theEatingMazub);
		int mazubWidth = facade.getCurrentSprite(theEatingMazub).getWidth();
		double timeToReachSneezewort = (-100.0 +
				Math.sqrt(100.0 * 100.0 + 4.0 * 45.0 * (120 - mazubWidth))) / (2 * 45.0);
		facade.advanceTime(theEatingMazub, 0.12);
		timeToReachSneezewort -= 0.12;
		assertEquals(100, facade.getHitPoints(theEatingMazub));
		assertEquals(1.0 + 0.12648, facade.getActualPosition(theEatingMazub)[0],
				LOW_PRECISION);
		// Mazub now reaches sneezewort and eats it.
		facade.advanceTime(theEatingMazub, timeToReachSneezewort + 0.01);
		assertEquals(150, facade.getHitPoints(theEatingMazub));
		actualScore += 15;
	}

	@Test
	public void advanceTime_EatingDeadSneezewort() {
		maximumScore += 15;
		facade.addGameObject(mazub_0_1000, world_250_400);
		Sneezewort theSneezewort = facade.createSneezewort(50, 900, sneezewortSprites);
		facade.addGameObject(theSneezewort, world_250_400);
		int sneezeWortHeight = facade.getCurrentSprite(theSneezewort).getHeight();
		// The plant dies after 10 seconds of game time, but is still in the world
		// for 0.6 seconds.
		for (int i = 1; i <= 100; i++)
			facade.advanceTime(theSneezewort, 0.1);
		facade.advanceTime(theSneezewort, 0.05);
		assertTrue(facade.isDeadGameObject(theSneezewort));
		assertEquals(world_250_400, facade.getWorld(theSneezewort));
		// Mazub will start to fall towards the sneezewort.
		double timetoReachSneezewort = Math.sqrt((100 - sneezeWortHeight) / 500.0);
		while (timetoReachSneezewort > 0.15) {
			facade.advanceTime(mazub_0_1000, 0.16);
			timetoReachSneezewort -= 0.15;
		}
		assertEquals(100, facade.getHitPoints(mazub_0_1000));
		assertTrue(facade.getActualPosition(mazub_0_1000)[1] > 9.0 +
				(sneezeWortHeight / 100.0));
		// Mazub now eats the dead sneezewort.
		facade.advanceTime(mazub_0_1000, timetoReachSneezewort + 0.02);
		assertTrue(facade.getActualPosition(mazub_0_1000)[1] < 9.0 +
				(sneezeWortHeight / 100.0));
		assertEquals(80, facade.getHitPoints(mazub_0_1000));
		actualScore += 15;
	}

	@Test
	public void advanceTime_EatingSeveralSneezeworts() {
		maximumScore += 10;
		facade.addGameObject(mazub_100_1000, world_250_400);
		Sneezewort sneeze1 = facade.createSneezewort(120, 1010, sneezewortSprites);
		facade.addGameObject(sneeze1, world_250_400);
		Sneezewort sneeze2 = facade.createSneezewort(150, 1030, sneezewortSprites);
		facade.addGameObject(sneeze2, world_250_400);
		Sneezewort sneeze3 = facade.createSneezewort(190, 1040, sneezewortSprites);
		facade.addGameObject(sneeze3, world_250_400);
		// Time must be advanced for mazub to eat all the plants.
		assertEquals(100, facade.getHitPoints(mazub_100_1000));
		facade.advanceTime(mazub_100_1000, 0.01);
		assertEquals(250, facade.getHitPoints(mazub_100_1000));
		actualScore += 10;
	}

	@Test
	public void advanceTime_EatingSingleScullCab() {
		maximumScore += 15;
		facade.setGeologicalFeature(world_250_400, 50, 875, SOLID_GROUND);
		Mazub theEatingMazub = facade.createMazub(0, 1000, mazubSprites);
		facade.addGameObject(theEatingMazub, world_250_400);
		Skullcab theScullcab = facade.createSkullcab(40, 900, SkullcabSprites);
		facade.addGameObject(theScullcab, world_250_400);
		int SkullcabHeight = facade.getCurrentSprite(theScullcab).getHeight();
		double timeToReachScullcab = Math.sqrt(2 * (100 - SkullcabHeight) / 1000.0);
		// Mazub starts to fall.
		while (timeToReachScullcab > 0.15) {
			facade.advanceTime(theEatingMazub, 0.15);
			timeToReachScullcab -= 0.15;
		}
		assertEquals(100, facade.getHitPoints(theEatingMazub));
		assertEquals(3, facade.getHitPoints(theScullcab));
		// Mazub will now eat the scullcab for the first time.
		facade.advanceTime(theEatingMazub, timeToReachScullcab + 0.02);
		assertEquals(150, facade.getHitPoints(theEatingMazub));
		assertEquals(2, facade.getHitPoints(theScullcab));
		// Mazub will land on the solid ground and eat the scullCab for a second time
		// 0.6 seconds after it has eaten it for the first time.
		for (int i = 0; i < 4; i++)
			facade.advanceTime(theEatingMazub, 0.15);
		assertEquals(200, facade.getHitPoints(theEatingMazub));
		assertEquals(1, facade.getHitPoints(theScullcab));
		// Mazub will eat the scullCab for a third time
		// 0.6 seconds after it has eaten it for the second time.
		for (int i = 0; i < 4; i++)
			facade.advanceTime(theEatingMazub, 0.15);
		assertEquals(250, facade.getHitPoints(theEatingMazub));
		assertEquals(0, facade.getHitPoints(theScullcab));
		// After at least another 0.6 seconds, the scullcab will have been removed
		// from the world.
		for (int i = 1; i <= 8; i++)
			facade.advanceTime(theScullcab, 0.15);
		assertTrue(facade.isTerminatedGameObject(theScullcab));
		assertNull(facade.getWorld(theScullcab));
		actualScore += 15;
	}

	@Test
	public void advanceTime_MaximumHitpointsReached() {
		maximumScore += 10;
		facade.setGeologicalFeature(world_250_400, 140, 995, SOLID_GROUND);
		facade.addGameObject(mazub_100_1000, world_250_400);
		for (int i = 1; i <= 10; i++)
			facade.addGameObject(
					facade.createSneezewort(100 + i * 8, 1000 + i * 4, sneezewortSprites),
					world_250_400);
		// Time must be advanced for mazub to eat. Mazub will eat 8 sneezeworts to
		// reach the maximum number of hit points.
		facade.advanceTime(mazub_100_1000, 0.01);
		assertEquals(500, facade.getHitPoints(mazub_100_1000));
		actualScore += 10;
	}

	@Test
	public void getCurrentSprite_Scenario() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			World broadWorld = facade.createWorld(10, 1000, 200, new int[] { 10, 20 },
					200, 100);
			facade.setGeologicalFeature(broadWorld, 900, 90, SOLID_GROUND);
			facade.setGeologicalFeature(broadWorld, 820, 90, SOLID_GROUND);
			facade.setGeologicalFeature(broadWorld, 750, 90, SOLID_GROUND);
			facade.setGeologicalFeature(broadWorld, 680, 90, SOLID_GROUND);
			facade.setGeologicalFeature(broadWorld, 600, 90, SOLID_GROUND);
			Mazub mazubToPlayWith = facade.createMazub(850, 100, mazubSprites);
			facade.addGameObject(mazubToPlayWith, broadWorld);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazubToPlayWith));
			facade.startMoveLeft(mazubToPlayWith);
			for (int i = 1; i < 12; i++) {
				// Mazub changes sprite every 0.075 seconds.
				facade.advanceTime(mazubToPlayWith, 0.08);
				assertEquals(mazubSprites[13 + i % 5],
						facade.getCurrentSprite(mazubToPlayWith));
			}
			facade.startDuck(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.15);
			assertEquals(mazubSprites[7], facade.getCurrentSprite(mazubToPlayWith));
			facade.endDuck(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.095);
			assertEquals(mazubSprites[14], facade.getCurrentSprite(mazubToPlayWith));
			facade.endMove(mazubToPlayWith);
			assertEquals(mazubSprites[3], facade.getCurrentSprite(mazubToPlayWith));
			for (int i = 1; i <= 6; i++) {
				facade.advanceTime(mazubToPlayWith, 0.15);
				assertEquals(mazubSprites[3], facade.getCurrentSprite(mazubToPlayWith));
			}
			facade.advanceTime(mazubToPlayWith, 0.12);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazubToPlayWith));
			facade.startMoveLeft(mazubToPlayWith);
			facade.startJump(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.15);
			assertEquals(mazubSprites[5], facade.getCurrentSprite(mazubToPlayWith));
			facade.endJump(mazubToPlayWith);
			facade.advanceTime(mazubToPlayWith, 0.07);
			assertEquals(mazubSprites[13], facade.getCurrentSprite(mazubToPlayWith));
			actualScore += 15;
		}
	}

	@Test
	public void getCurrentSprite_NewSpriteTooLarge() {
		if (facade.isTeamSolution()) {
			maximumScore += 15;
			facade.setGeologicalFeature(world_250_400, 205, 1010, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 75, 995, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 150, 995, SOLID_GROUND);
			facade.addGameObject(mazub_100_1000, world_250_400);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_1000));
			facade.startMoveRight(mazub_100_1000);
			// Mazub needs 0.141 seconds to reach the impassable terrain.
			// We invoke advanceTime twice to force a change of sprite half-way the move.
			// Because the move has ended, the sprite reflects the end of the move.
			facade.advanceTime(mazub_100_1000, 0.08);
			facade.advanceTime(mazub_100_1000, 0.08);
			assertEquals(mazubSprites[2], facade.getCurrentSprite(mazub_100_1000));
			// After 1 second mazub should switch to stationary sprite, but that would
			// cause an overlap with stationary terrain.
			for (int i = 1; i <= 7; i++)
				facade.advanceTime(mazub_100_1000, 0.15);
			assertEquals(mazubSprites[2], facade.getCurrentSprite(mazub_100_1000));
			// After moving to the left for 0.05 seconds, mazub is still not able to
			// switch to the stationary sprite.
			facade.startMoveLeft(mazub_100_1000);
			facade.advanceTime(mazub_100_1000, 0.05);
			facade.endMove(mazub_100_1000);
			for (int i = 1; i <= 7; i++)
				facade.advanceTime(mazub_100_1000, 0.15);
			assertEquals(mazubSprites[3], facade.getCurrentSprite(mazub_100_1000));
			facade.advanceTime(mazub_100_1000, 0.01);
			// After moving to the left for another 0.05 seconds, mazub is able to
			// switch to the stationary sprite.
			facade.startMoveLeft(mazub_100_1000);
			facade.advanceTime(mazub_100_1000, 0.05);
			facade.endMove(mazub_100_1000);
			for (int i = 1; i <= 7; i++)
				facade.advanceTime(mazub_100_1000, 0.15);
			assertEquals(mazubSprites[0], facade.getCurrentSprite(mazub_100_1000));
			actualScore += 15;
		}
	}

	/*************************************
	 * Tests for Sneezewort and Skullcab *
	 *************************************/

	@Test
	void createSneezewort_LegalCase() throws Exception {
		maximumScore += 8;
		Sneezewort newSneeze = facade.createSneezewort(10, 6, sneezewortSprites);
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newSneeze));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newSneeze),
				HIGH_PRECISION);
		assertTrue(facade.getOrientation(newSneeze) < 0);
		assertArrayEquals(new double[] { -0.5, 0.0 }, facade.getVelocity(newSneeze),
				LOW_PRECISION);
		assertEquals(1, facade.getHitPoints(newSneeze));
		if (facade.isTeamSolution()) {
			assertArrayEquals(sneezewortSprites, facade.getSprites(newSneeze));
			assertEquals(sneezewortSprites[0], facade.getCurrentSprite(newSneeze));
		}
		actualScore += 8;
	}

	@Test
	void createSneezewort_IllegalSprites() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 4;
			// Array of sprites must be effective.
			assertThrows(ModelException.class,
					() -> facade.createSneezewort(5, 10, (Sprite[]) null));
			// Array of sprites must have exactly 2 elements.
			assertThrows(ModelException.class, () -> facade.createSneezewort(5, 10,
					new Sprite[] { new Sprite("a", 1, 2) }));
			assertThrows(ModelException.class,
					() -> facade.createSneezewort(5, 10,
							new Sprite[] { new Sprite("a", 1, 2), new Sprite("b", 2, 3),
									new Sprite("c", 3, 4) }));
			// Array of sprites must have effective elements.
			assertThrows(ModelException.class,
					() -> facade.createSneezewort(5, 10, new Sprite[] { null, null }));
			actualScore += 4;
		}
	}

	@Test
	void advanceTimeSneezewort_NoOrientationSwitch() throws Exception {
		maximumScore += 8;
		facade.addGameObject(sneezewort_120_10, world_250_400);
		facade.advanceWorldTime(world_250_400, 0.1);
		// The sneezewort will have moved 5 cm to the left after 0.1 seconds
		assertArrayEquals(new double[] { 1.15, 0.1 },
				facade.getActualPosition(sneezewort_120_10), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sneezewortSprites[0],
					facade.getCurrentSprite(sneezewort_120_10));
		actualScore += 8;
	}

	@Test
	void advanceTimeSneezewort_OrientationSwitch() throws Exception {
		maximumScore += 8;
		facade.addGameObject(sneezewort_120_10, world_250_400);
		for (int i = 1; i <= 4; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		// The sneezewort will have moved 25 cm to the left after 0.5 seconds and 5 cm to
		// the right after and additional 0.1 seconds.
		assertArrayEquals(new double[] { 1.0, 0.1 },
				facade.getActualPosition(sneezewort_120_10), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(sneezewortSprites[1],
					facade.getCurrentSprite(sneezewort_120_10));
		actualScore += 8;
	}

	@Test
	void advanceTimeSneezewort_OverlapHungryMazub() throws Exception {
		maximumScore += 12;
		facade.addGameObject(mazub_0_0, world_250_400);
		facade.addGameObject(sneezewort_120_10, world_250_400);
		for (int i = 1; i <= 2; i++)
			facade.advanceTime(sneezewort_120_10, 0.15);
		assertArrayEquals(new double[] { 1.05, 0.1 },
				facade.getActualPosition(sneezewort_120_10), LOW_PRECISION);
		facade.advanceTime(sneezewort_120_10, 0.15);
		assertTrue(facade.isTerminatedGameObject(sneezewort_120_10));
		assertNull(facade.getWorld(sneezewort_120_10));
		actualScore += 12;
	}

	@Test
	void advanceTimeSneezewort_SneezewortDying() throws Exception {
		maximumScore += 12;
		facade.addGameObject(sneezewort_120_10, world_250_400);
		// The sneezewort is still alive after 9.9 seconds of game time.
		for (int i = 1; i <= 66; i++)
			facade.advanceTime(sneezewort_120_10, 0.15);
		assertFalse(facade.isDeadGameObject(sneezewort_120_10));
		// The sneezewort dies after 10 seconds of game time.
		facade.advanceTime(sneezewort_120_10, 0.15);
		assertTrue(facade.isDeadGameObject(sneezewort_120_10));
		assertFalse(facade.isTerminatedGameObject(sneezewort_120_10));
		assertEquals(world_250_400, facade.getWorld(sneezewort_120_10));
		// The sneezewort no longer moves once it has died.
		double[] oldPosition = facade.getActualPosition(sneezewort_120_10);
		facade.advanceTime(sneezewort_120_10, 0.15);
		assertArrayEquals(oldPosition, facade.getActualPosition(sneezewort_120_10));
		// The sneezewort is destroyed 0.6 seconds after its dead.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(sneezewort_120_10, 0.15);
		assertTrue(facade.isTerminatedGameObject(sneezewort_120_10));
		assertNull(facade.getWorld(sneezewort_120_10));
		actualScore += 12;
	}

	@Test
	void advanceTimeSneezewort_SneezewortLeavingWorld() throws Exception {
		maximumScore += 8;
		Sneezewort theSneezewort = facade.createSneezewort(15, 20, sneezewortSprites);
		facade.addGameObject(theSneezewort, world_250_400);
		// The sneezewort will leave its world after 0.3 seconds
		facade.advanceTime(theSneezewort, 0.15);
		facade.advanceTime(theSneezewort, 0.14);
		assertFalse(facade.isTerminatedGameObject(theSneezewort));
		assertEquals(world_250_400, facade.getWorld(theSneezewort));
		facade.advanceTime(theSneezewort, 0.03);
		assertTrue(facade.isTerminatedGameObject(theSneezewort));
		assertNull(facade.getWorld(theSneezewort));
		assertFalse(facade.getAllGameObjects(world_250_400).contains(theSneezewort));
		actualScore += 8;
	}

	@Test
	void createSkullcab_LegalCase() throws Exception {
		maximumScore += 8;
		Skullcab newSkullcab = facade.createSkullcab(40, 50, SkullcabSprites);
		assertArrayEquals(new int[] { 40, 50 }, facade.getPixelPosition(newSkullcab));
		assertArrayEquals(new double[] { 0.4, 0.5 },
				facade.getActualPosition(newSkullcab), HIGH_PRECISION);
		assertTrue(facade.getOrientation(newSkullcab) > 0);
		assertArrayEquals(new double[] { 0.0, 0.5 }, facade.getVelocity(newSkullcab),
				LOW_PRECISION);
		assertEquals(3, facade.getHitPoints(newSkullcab));
		if (facade.isTeamSolution()) {
			assertArrayEquals(SkullcabSprites, facade.getSprites(newSkullcab));
			assertEquals(SkullcabSprites[0], facade.getCurrentSprite(newSkullcab));
		}
		actualScore += 8;
	}

	@Test
	void advanceTimeSkullcab_NoOrientationSwitch() throws Exception {
		maximumScore += 4;
		Skullcab theSkullcab = facade.createSkullcab(300, 500, SkullcabSprites);
		facade.addGameObject(theSkullcab, world_250_400);
		facade.advanceWorldTime(world_250_400, 0.1);
		// The s will have moved up over 5 cm after 0.1 seconds
		assertArrayEquals(new double[] { 3.0, 5.05 },
				facade.getActualPosition(theSkullcab), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(SkullcabSprites[0], facade.getCurrentSprite(theSkullcab));
		actualScore += 4;
	}

	@Test
	void advanceTimeSkullcab_OrientationSwitch() throws Exception {
		maximumScore += 4;
		Skullcab theSkullcab = facade.createSkullcab(300, 500, SkullcabSprites);
		facade.addGameObject(theSkullcab, world_250_400);
		for (int i = 1; i <= 4; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		// The Skullcab will have moved up 25 cm after 0.5 seconds and
		// 5 cm down after and additional 0.1 seconds.
		assertArrayEquals(new double[] { 3.0, 5.2 },
				facade.getActualPosition(theSkullcab), LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(SkullcabSprites[1], facade.getCurrentSprite(theSkullcab));
		actualScore += 4;
	}

	@Test
	void advanceTimeSkullcab_OverlapHungryMazub() throws Exception {
		maximumScore += 15;
		facade.addGameObject(mazub_0_0, world_250_400);
		int mazubHeight = facade.getCurrentSprite(mazub_0_0).getHeight();
		Skullcab theSkullcab = facade.createSkullcab(20, mazubHeight - 10,
				SkullcabSprites);
		facade.addGameObject(theSkullcab, world_250_400);
		// Mazub immediately eats the skulcab for the first time.
		facade.advanceTime(theSkullcab, 0.01);
		assertEquals(2, facade.getHitPoints(theSkullcab));
		// The Skullcab leaves the mazub and reenters after 0.79 seconds.
		// It is then eaten for the second time.
		for (int i = 0; i < 5; i++)
			facade.advanceTime(theSkullcab, 0.15);
		facade.advanceTime(theSkullcab, 0.09);
		assertEquals((mazubHeight / 100.0 - 0.025),
				facade.getActualPosition(theSkullcab)[1], LOW_PRECISION);
		assertEquals(1, facade.getHitPoints(theSkullcab));
		// After another second, the Skullcab will have left and re-entered the mazub
		// once more.
		// It is then eaten for the third time, and dies.
		for (int i = 0; i < 10; i++)
			facade.advanceTime(theSkullcab, 0.1);
		assertTrue(facade.isDeadGameObject(theSkullcab));
		// 0.6 seconds after it died, the Skullcab is terminated.
		double oldYPosition = facade.getActualPosition(theSkullcab)[1];
		facade.advanceTime(theSkullcab, 0.1);
		assertEquals(oldYPosition, facade.getActualPosition(theSkullcab)[1]);
		for (int i = 0; i < 6; i++)
			facade.advanceTime(theSkullcab, 0.1);
		assertTrue(facade.isTerminatedGameObject(theSkullcab));
		assertNull(facade.getWorld(theSkullcab));
		actualScore += 15;
	}

	@Test
	void advanceTimeSkullcab_SkullcabDying() throws Exception {
		maximumScore += 12;
		Skullcab theSkullcab = facade.createSkullcab(20, 70, SkullcabSprites);
		facade.addGameObject(theSkullcab, world_250_400);
		// The Skullcab is still alive after 11.9 seconds of game time.
		for (int i = 0; i < 119; i++)
			facade.advanceTime(theSkullcab, 0.1);
		assertFalse(facade.isDeadGameObject(theSkullcab));
		// The Skullcab dies after 12 seconds of game time.
		facade.advanceTime(theSkullcab, 0.15);
		assertTrue(facade.isDeadGameObject(theSkullcab));
		assertFalse(facade.isTerminatedGameObject(theSkullcab));
		assertEquals(world_250_400, facade.getWorld(theSkullcab));
		// The Skullcab no longer moves once it has died.
		double[] oldPosition = facade.getActualPosition(theSkullcab);
		facade.advanceTime(theSkullcab, 0.15);
		assertArrayEquals(oldPosition, facade.getActualPosition(theSkullcab));
		// The Skullcab is destroyed 0.6 seconds after its dead.
		for (int i = 1; i <= 3; i++)
			facade.advanceTime(theSkullcab, 0.15);
		assertTrue(facade.isTerminatedGameObject(theSkullcab));
		assertNull(facade.getWorld(theSkullcab));
		actualScore += 12;
	}

	@Test
	void advanceTimeSkullcab_SkullcabLeavingWorld() throws Exception {
		maximumScore += 6;
		Skullcab theSkullcab = facade.createSkullcab(15, 1990, SkullcabSprites);
		facade.addGameObject(theSkullcab, world_250_400);
		// The Skullcab will leave its world after 0.2 seconds
		facade.advanceTime(theSkullcab, 0.18);
		assertFalse(facade.isTerminatedGameObject(theSkullcab));
		assertEquals(world_250_400, facade.getWorld(theSkullcab));
		facade.advanceTime(theSkullcab, 0.1);
		assertTrue(facade.isTerminatedGameObject(theSkullcab));
		assertNull(facade.getWorld(theSkullcab));
		assertFalse(facade.getAllGameObjects(world_250_400).contains(theSkullcab));
		actualScore += 6;
	}

	/*******************
	 * Tests for Slime *
	 *******************/

	@Test
	void createSlime_LegalCase() throws Exception {
		maximumScore += 10;
		Slime newSlime = facade.createSlime(10, 10, 6, null, slimeSprites);
		assertEquals(10, facade.getIdentification(newSlime));
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newSlime));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newSlime),
				HIGH_PRECISION);
		assertTrue(facade.getOrientation(newSlime) > 0);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(newSlime),
				LOW_PRECISION);
		assertArrayEquals(new double[] { 0.7, 0.0 }, facade.getAcceleration(newSlime),
				LOW_PRECISION);
		assertEquals(100, facade.getHitPoints(newSlime));
		if (facade.isTeamSolution()) {
			assertArrayEquals(slimeSprites, facade.getSprites(newSlime));
			assertEquals(slimeSprites[0], facade.getCurrentSprite(newSlime));
		}
		actualScore += 10;
	}

	@Test
	void createSlime_IllegalId() throws Exception {
		maximumScore += 2;
		// Identification must be positive.
		assertThrows(ModelException.class,
				() -> facade.createSlime(-66, 5, 10, null, slimeSprites));
		actualScore += 2;
	}

	@Test
	void createSlime_NonUniqueId() throws Exception {
		maximumScore += 10;
		facade.createSlime(10, 10, 6, null, slimeSprites);
		assertThrows(ModelException.class,
				() -> facade.createSlime(10, 5, 10, null, slimeSprites));
		actualScore += 10;
	}

	@Test
	void createSlime_IllegalSprites() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 4;
			// Array of sprites must be effective.
			assertThrows(ModelException.class,
					() -> facade.createSlime(6, 5, 10, null, (Sprite[]) null));
			// Array of sprites must have exactly 2 elements.
			assertThrows(ModelException.class, () -> facade.createSlime(12, 5, 10, null,
					new Sprite[] { new Sprite("a", 1, 2) }));
			assertThrows(ModelException.class,
					() -> facade.createSlime(7, 5, 10, null,
							new Sprite[] { new Sprite("a", 1, 2), new Sprite("b", 2, 3),
									new Sprite("c", 3, 4) }));
			// Array of sprites must have effective elements.
			assertThrows(ModelException.class, () -> facade.createSlime(8, 5, 10, null,
					new Sprite[] { null, null }));
			actualScore += 4;
		}
	}

	@Test
	public void advanceTime_SlimeMovingWithinBoundaries() throws Exception {
		maximumScore += 10;
		Slime movingSlime = facade.createSlime(10, 20, 30, null, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		// Slime will move 0.7875 cm over 0.15 seconds; its velocity increases with 0.105
		// m/s
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(0.2 + 0.007875, facade.getActualPosition(movingSlime)[0],
				LOW_PRECISION);
		assertEquals(0.105, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(slimeSprites[0], facade.getCurrentSprite(movingSlime));
		// After another 0.1 seconds Slime will have moved 2.1875 cm in total;
		// its velocity is increased to 0.175 m/s
		facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(0.2 + 0.021875, facade.getActualPosition(movingSlime)[0],
				LOW_PRECISION);
		assertEquals(0.175, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(slimeSprites[0], facade.getCurrentSprite(movingSlime));
		actualScore += 10;
	}

	@Test
	public void advanceTime_SlimeReachingTopSpeed() throws Exception {
		maximumScore += 10;
		Slime movingSlime = facade.createSlime(10, 20, 30, null, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		// Slime will reach its maximum velocity (2.5 m/s) after (250/70 = 3,5714 s).
		// It will have moved over 446,43 cm in that time span
		for (int i = 0; i < 35; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		facade.advanceWorldTime(world_250_400, 0.0714);
		assertEquals(0.2 + 4.4643, facade.getActualPosition(movingSlime)[0],
				LOW_PRECISION);
		assertEquals(2.50, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(slimeSprites[0], facade.getCurrentSprite(movingSlime));
		// From this point on, the slime moves at maximum speed.
		// After another second, it will have moved over another 250 cm.
		for (int i = 0; i < 10; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(0.2 + 4.4643 + 2.5, facade.getActualPosition(movingSlime)[0],
				LOW_PRECISION);
		assertEquals(2.50, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		if (facade.isTeamSolution())
			assertEquals(slimeSprites[0], facade.getCurrentSprite(movingSlime));
		actualScore += 10;
	}

	@Test
	public void advanceTime_SlimeMovingOutOfWorld() throws Exception {
		maximumScore += 6;
		Slime movingSlime = facade.createSlime(10, 1200, 30, null, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		// Slime reaches the right side of the world after 1,195 seconds.
		for (int i = 0; i < 11; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		facade.advanceWorldTime(world_250_400, 0.09);
		assertEquals(12.0 + 0.4956, facade.getActualPosition(movingSlime)[0],
				LOW_PRECISION);
		assertEquals(0.833, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		// Slime moves out of world after another 0.005 seconds.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isTerminatedGameObject(movingSlime));
		assertNull(facade.getWorld(movingSlime));
		actualScore += 6;
	}

	@Test
	public void advanceTime_SlimeInWater() {
		maximumScore += 12;
		facade.setGeologicalFeature(world_250_400, 120, 1010, WATER);
		Slime slimeInWater = facade.createSlime(10, 100, 1000, null, slimeSprites);
		facade.addGameObject(slimeInWater, world_250_400);
		// No loss of hit points as long as the time in water is below 0.4 seconds.
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(100, facade.getHitPoints(slimeInWater));
		// After another 0.15 seconds in water, the slime looses 4 hit points.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(96, facade.getHitPoints(slimeInWater));
		// After a total time of 0.8452 seconds, the slime moves out of the water.
		for (int i = 1; i <= 3; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(92, facade.getHitPoints(slimeInWater));
		assertTrue(facade.getActualPosition(slimeInWater)[0] > 1.25);
		actualScore += 12;
	}

	@Test
	public void advanceTime_SlimeOfSchoolInWater() {
		maximumScore += 8;
		facade.setGeologicalFeature(world_250_400, 120, 1010, WATER);
		Slime mate1 = facade.createSlime(10, 20, 20, someSchool, slimeSprites);
		Slime mate2 = facade.createSlime(20, 100, 100, someSchool, slimeSprites);
		Slime slimeInWater = facade.createSlime(30, 100, 1000, someSchool, slimeSprites);
		facade.addGameObject(slimeInWater, world_250_400);
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		facade.advanceWorldTime(world_250_400, 0.15);
		// The slimes in the same school as the slime in water each loose 1 hit-point.
		assertEquals(96, facade.getHitPoints(slimeInWater));
		assertEquals(100 - 1, facade.getHitPoints(mate1));
		assertEquals(100 - 1, facade.getHitPoints(mate2));
		actualScore += 8;
	}

	@Test
	public void advanceTime_SlimeInGas() {
		maximumScore += 12;
		facade.setGeologicalFeature(world_250_400, 120, 1010, GAS);
		Slime slimeInGas = facade.createSlime(10, 100, 1000, null, slimeSprites);
		facade.addGameObject(slimeInGas, world_250_400);
		// No gain of hit points as long as the time in gas is below 0.3 seconds.
		facade.advanceWorldTime(world_250_400, 0.12);
		facade.advanceWorldTime(world_250_400, 0.12);
		assertEquals(100, facade.getHitPoints(slimeInGas));
		// After another 0.12 seconds in gas, the slime gains 2 hit points.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(102, facade.getHitPoints(slimeInGas));
		// After a total time of 0.8452 seconds, the slime moves out of the gas.
		for (int i = 1; i <= 5; i++)
			facade.advanceWorldTime(world_250_400, 0.12);
		assertEquals(104, facade.getHitPoints(slimeInGas));
		assertTrue(facade.getActualPosition(slimeInGas)[0] > 1.25);
		actualScore += 12;
	}

	@Test
	public void advanceTime_SlimeInMagma() {
		maximumScore += 12;
		Slime slimeInMagma = facade.createSlime(10, 100, 1000, null, slimeSprites);
		int slimeWidth = facade.getCurrentSprite(slimeInMagma).getWidth();
		facade.setGeologicalFeature(world_250_400, 100 + slimeWidth + 10, 1010, MAGMA);
		facade.addGameObject(slimeInMagma, world_250_400);
		// The slime reaches the magma after 0.5345 seconds.
		for (int i = 0; i < 3; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		assertFalse(facade.isDeadGameObject(slimeInMagma));
		// After another 0.15 seconds the slime moves into the magma and immediately dies.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isDeadGameObject(slimeInMagma));
		actualScore += 12;
	}

	@Test
	public void advanceTime_SlimeReachingImpassableTerrain() throws Exception {
		maximumScore += 8;
		Slime movingSlime = facade.createSlime(10, 100, 30, null, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		int slimeWidth = facade.getCurrentSprite(movingSlime).getWidth();
		facade.setGeologicalFeature(world_250_400, 100 + slimeWidth + 30, 50, ICE);
		// Slime reaches the impassable terrain after 0,9258 seconds.
		for (int i = 0; i < 9; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(1.0 + 0.2835, facade.getActualPosition(movingSlime)[0],
				LOW_PRECISION);
		assertEquals(0.63, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		// After another 0.1, the slime is blocked by the impassable terrain, and its
		// velocity drops to 0.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(130, facade.getPixelPosition(movingSlime)[0]);
		assertEquals(0.0, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		// Position and velocity of the slime do not change as long as it is blocked by
		// impassable terrain.
		facade.advanceWorldTime(world_250_400, 0.15);
		assertEquals(130, facade.getPixelPosition(movingSlime)[0]);
		assertEquals(0.0, facade.getVelocity(movingSlime)[0], LOW_PRECISION);
		actualScore += 8;
	}

	@Test
	public void advanceTime_SlimeBouncingAgainstOtherSlime() throws Exception {
		maximumScore += 18;
		// The slime to bounce with is blocked by impassable terrain.
		Slime slimeToBounceWith = facade.createSlime(10, 200, 30, null, slimeSprites);
		facade.addGameObject(slimeToBounceWith, world_250_400);
		int slimeWidth = facade.getCurrentSprite(slimeToBounceWith).getWidth();
		facade.setGeologicalFeature(world_250_400, 200 + slimeWidth, 50, SOLID_GROUND);
		Slime movingSlime = facade.createSlime(20, 100, 30, null, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		// The time needed to reach the slime to bounce with depends on the width of
		// the slime's sprite.
		double timeToReachSlime = Math.sqrt((100.0 - slimeWidth) / 35.0);
		while (timeToReachSlime > 0.15) {
			facade.advanceWorldTime(world_250_400, 0.15);
			timeToReachSlime -= 0.15;
		}
		facade.advanceWorldTime(world_250_400, timeToReachSlime+0.02);
		assertTrue((facade.getPixelPosition(movingSlime)[0] == 100 + (100 - slimeWidth)-1) ||
				(facade.getPixelPosition(movingSlime)[0] == 100 + (100 - slimeWidth)) );
		// The slime to bounce against will not change its position.
		assertEquals(200, facade.getPixelPosition(slimeToBounceWith)[0]);
		// The moving slime must now reverse its direction and reset its velocity.
		// It might take a small amount of time to see that such a switch is needed.
		// After 1.0 seconds, it will have moved around 35 cm to the left.
		// It will then have a velocity around -70 cm/s.
		double bouncingXPos = facade.getActualPosition(movingSlime)[0];
		for (int i = 0; i < 10; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		if (facade.isTeamSolution())
			assertEquals(slimeSprites[1], facade.getCurrentSprite(movingSlime));
		assertTrue(bouncingXPos - 0.3 > facade.getActualPosition(movingSlime)[0]);
		assertTrue(facade.getVelocity(movingSlime)[0] < 0);
		// The slime to bounce against may have changed its position. This may happen in
		// particular if that slime is the first slime whose time is advanced.
		assertTrue(200 >= facade.getPixelPosition(slimeToBounceWith)[0]);
		actualScore += 18;
	}

	@Test
	public void advanceTime_SlimeBouncingAgainstMazub() throws Exception {
		maximumScore += 24;
		// The mazub to bounce with.
		Mazub mazoubToBounceWith = facade.createMazub(200, 30, mazubSprites);
		facade.setGeologicalFeature(world_250_400, 240, 25, SOLID_GROUND);
		facade.addGameObject(mazoubToBounceWith, world_250_400);
		Slime movingSlime = facade.createSlime(10, 100, 30, null, slimeSprites);
		int slimeWidth = facade.getCurrentSprite(movingSlime).getWidth();
		facade.addGameObject(movingSlime, world_250_400);
		// The time needed to reach the mazub to bounce with depends on the width of
		// the slime's sprite. The sprite looses 30 hit points because of the collision.
		// Because mazub is stationary, the collision has no impact on it.
		double timeToReachSlime = Math.sqrt((100.0 - slimeWidth) / 35.0);
		while (timeToReachSlime > 0.15) {
			facade.advanceWorldTime(world_250_400, 0.15);
			timeToReachSlime -= 0.15;
		}
		facade.advanceWorldTime(world_250_400, timeToReachSlime + 0.03);
		assertEquals(100 + (100 - slimeWidth), facade.getPixelPosition(movingSlime)[0]);
		assertEquals(100 - 30, facade.getHitPoints(movingSlime));
		assertEquals(100, facade.getHitPoints(mazoubToBounceWith));
		assertEquals(0.0, facade.getVelocity(movingSlime)[0],LOW_PRECISION);
		// Both objects are now stationary.
		facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(100 + (100 - slimeWidth), facade.getPixelPosition(movingSlime)[0]);
		assertEquals(100 - 30, facade.getHitPoints(movingSlime));
		// Mazub now moves away from the slime. The slime also starts to move again.
		facade.startMoveRight(mazoubToBounceWith);
		facade.advanceWorldTime(world_250_400, 0.02);
		facade.endMove(mazoubToBounceWith);
		assertEquals(2.0 + 0.02018, facade.getActualPosition(mazoubToBounceWith)[0],
				LOW_PRECISION);
		assertTrue(facade.getVelocity(movingSlime)[0] > 0.0);
		// The slime needs 0.2401 seconds to collide with mazub again.
		// The collision has no effect, because no 0.6 seconds have passed since the
		// previous collision
		for (int i = 0; i < 3; i++)
			facade.advanceWorldTime(world_250_400, 0.1);
		assertEquals(2.0 - (slimeWidth / 100.0) + 0.02018,
				facade.getActualPosition(movingSlime)[0], LOW_PRECISION);
		assertEquals(100 - 30, facade.getHitPoints(movingSlime));
		assertEquals(0.0, facade.getVelocity(movingSlime)[0]);
		actualScore += 24;
	}

	@Test
	public void advanceTime_SlimeInSchoolBouncingAgainstMazub() throws Exception {
		maximumScore += 10;
		// The mazub to bounce with.
		Mazub mazoubToBounceWith = facade.createMazub(200, 30, mazubSprites);
		facade.setGeologicalFeature(world_250_400, 240, 25, SOLID_GROUND);
		facade.addGameObject(mazoubToBounceWith, world_250_400);
		Slime mate1 = facade.createSlime(10, 20, 20, someSchool, slimeSprites);
		Slime mate2 = facade.createSlime(20, 100, 100, someSchool, slimeSprites);
		Slime movingSlime = facade.createSlime(30, 100, 30, someSchool, slimeSprites);
		int slimeWidth = facade.getCurrentSprite(movingSlime).getWidth();
		facade.addGameObject(movingSlime, world_250_400);
		// The time needed to reach the mazub to bounce with depends on the width of
		// the slime's sprite. The sprite looses 30 hit points because of the collision.
		// Because mazub is stationary, the collision has no impact on it.
		double timeToReachSlime = Math.sqrt((100.0 - slimeWidth) / 35.0);
		while (timeToReachSlime > 0.15) {
			facade.advanceWorldTime(world_250_400, 0.15);
			timeToReachSlime -= 0.15;
		}
		facade.advanceWorldTime(world_250_400, timeToReachSlime + 0.03);
		// The slimes in the same school as the moving slime each loose 1 hit-point.
		assertEquals(100 - 30, facade.getHitPoints(movingSlime));
		assertEquals(100 - 1, facade.getHitPoints(mate1));
		assertEquals(100 - 1, facade.getHitPoints(mate2));
		actualScore += 10;
	}

	@Test
	public void advanceTime_SlimeBouncingAgainstSlimeOfLargerSchool() throws Exception {
		maximumScore += 12;
		// The slime to bounce with is blocked by impassable terrain.
		School largerSchool = facade.createSchool(null);
		facade.createSlime(10, 20, 30, largerSchool, slimeSprites);
		facade.createSlime(20, 120, 30, largerSchool, slimeSprites);
		facade.createSlime(30, 20, 120, largerSchool, slimeSprites);
		facade.createSlime(40, 120, 120, largerSchool, slimeSprites);
		Slime slimeToBounceWith = facade.createSlime(50, 200, 30, largerSchool,
				slimeSprites);
		facade.addGameObject(slimeToBounceWith, world_250_400);
		int slimeWidth = facade.getCurrentSprite(slimeToBounceWith).getWidth();
		facade.setGeologicalFeature(world_250_400, 200 + slimeWidth, 50, SOLID_GROUND);
		Slime movingSlime = facade.createSlime(60, 100, 30, someSchool, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		Slime oldMate = facade.createSlime(70, 20, 30, someSchool, slimeSprites);
		// The time needed to reach the slime to bounce with depends on the width of
		// the slime's sprite.
		double timeToReachSlime = Math.sqrt((100.0 - slimeWidth) / 35.0);
		while (timeToReachSlime > 0.15) {
			facade.advanceWorldTime(world_250_400, 0.15);
			timeToReachSlime -= 0.15;
		}
		facade.advanceWorldTime(world_250_400, timeToReachSlime + 0.03);
		// The bouncing slime must now have switched to the larger school.
		assertTrue(facade.getAllSlimes(largerSchool).contains(movingSlime));
		assertFalse(facade.getAllSlimes(someSchool).contains(movingSlime));
		assertEquals(largerSchool, facade.getSchool(movingSlime));
		assertEquals(100 + 1, facade.getHitPoints(oldMate));
		assertEquals(100 - 1 + 5, facade.getHitPoints(movingSlime));
		assertEquals(100 - 1, facade.getHitPoints(slimeToBounceWith));
		actualScore += 12;
	}

	@Test
	public void advanceTime_SlimeBouncingAgainstSlimeOfSmallerSchool() throws Exception {
		maximumScore += 4;
		// The slime to bounce with is blocked by impassable terrain.
		School smallerSchool = facade.createSchool(null);
		Slime slimeToBounceWith = facade.createSlime(10, 200, 30, smallerSchool,
				slimeSprites);
		facade.addGameObject(slimeToBounceWith, world_250_400);
		int slimeWidth = facade.getCurrentSprite(slimeToBounceWith).getWidth();
		facade.setGeologicalFeature(world_250_400, 200 + slimeWidth, 50, SOLID_GROUND);
		Slime movingSlime = facade.createSlime(20, 100, 30, someSchool, slimeSprites);
		facade.addGameObject(movingSlime, world_250_400);
		facade.createSlime(30, 20, 30, someSchool, slimeSprites);
		facade.createSlime(40, 20, 30, someSchool, slimeSprites);
		// The time needed to reach the slime to bounce with depends on the width of
		// the slime's sprite.
		double timeToReachSlime = Math.sqrt((100.0 - slimeWidth) / 35.0);
		while (timeToReachSlime > 0.15) {
			facade.advanceWorldTime(world_250_400, 0.15);
			timeToReachSlime -= 0.15;
		}
		facade.advanceWorldTime(world_250_400, timeToReachSlime + 0.03);
		// The moving slime must still be part of the same school.
		// The other slime may have moved to the larger school.
		assertTrue(facade.getAllSlimes(someSchool).contains(movingSlime));
		actualScore += 4;
	}

	/**********
	 * Sharks *
	 **********/

	boolean shouldImplementSharks() {
		return facade.isTeamSolution() || facade.isLateTeamSplit();
	}

	@Test
	void createShark_LegalCase() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 6;
			Shark newShark = facade.createShark(10, 6, sharkSprites);
			assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newShark));
			assertArrayEquals(new double[] { 0.1, 0.06 },
					facade.getActualPosition(newShark), HIGH_PRECISION);
			assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(newShark),
					LOW_PRECISION);
			assertTrue((Math.abs(facade.getAcceleration(newShark)[0]) <= HIGH_PRECISION) ||
					((Math.abs(facade.getAcceleration(newShark)[0]) >= 1.5 -
							LOW_PRECISION) &&
							(Math.abs(facade.getAcceleration(newShark)[0]) <= 1.5 +
									LOW_PRECISION)));
			assertTrue((Math.abs(facade.getAcceleration(newShark)[1]) <= HIGH_PRECISION) ||
					((Math.abs(facade.getAcceleration(newShark)[1]) >= 10.0 -
							LOW_PRECISION) &&
							(Math.abs(facade.getAcceleration(newShark)[1]) <= 10.0 +
									LOW_PRECISION)));
			assertEquals(100, facade.getHitPoints(newShark));
			if (facade.isTeamSolution()) {
				assertArrayEquals(sharkSprites, facade.getSprites(newShark));
				assertEquals(sharkSprites[0], facade.getCurrentSprite(newShark));
			}
			actualScore += 6;
		}
	}

	@Test
	void createShark_IllegalSprites() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 4;
			// Array of sprites must be effective.
			assertThrows(ModelException.class,
					() -> facade.createShark(5, 10, (Sprite[]) null));
			// Array of sprites must have exactly 3 elements.
			assertThrows(ModelException.class, () -> facade.createShark(5, 10,
					new Sprite[] { new Sprite("a", 1, 2), new Sprite("b",3,4) }));
			assertThrows(ModelException.class,
					() -> facade.createShark(5, 10,
							new Sprite[] { new Sprite("a", 1, 2), new Sprite("b", 2, 3),
									new Sprite("c", 3, 4), new Sprite("d",5,6) }));
			// Array of sprites must have effective elements.
			assertThrows(ModelException.class, () -> facade.createShark(5, 10,
					new Sprite[] { null, null,null }));
			actualScore += 4;
		}
	}
	
	@Test
	void advanceTime_SharkMovingLeftAndJumping() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 12;
			Shark theShark = facade.createShark(50, 100, sharkSprites);
			facade.addGameObject(theShark, world_100_200);
			int sharkWidth = facade.getCurrentSprite(theShark).getWidth();
			facade.setGeologicalFeature(world_100_200, 50+sharkWidth-10, 90, SOLID_GROUND);
			double sharkInitXpos = facade.getActualPosition(theShark)[0];
			double sharkInitYpos = facade.getActualPosition(theShark)[1];
			// After 0.1 seconds, the shark shall have moved to the left over 0.75 cm,
			// and shall have jumped up over 15 cm.
			facade.advanceWorldTime(world_100_200, 0.1);
			assertArrayEquals(new double[] {sharkInitXpos-0.0075,sharkInitYpos+0.15},
					facade.getActualPosition(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {-0.15,1.0}, facade.getVelocity(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {-1.5,-10.0},facade.getAcceleration(theShark),LOW_PRECISION);
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[1],facade.getCurrentSprite(theShark));				
			// After another 0.3 seconds, the shark shall have moved to the left over 12.0 cm,
			// and is falling down.
			facade.advanceWorldTime(world_100_200, 0.15);
			facade.advanceWorldTime(world_100_200, 0.15);
			assertArrayEquals(new double[] {sharkInitXpos-0.12,sharkInitYpos},
					facade.getActualPosition(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {-0.6,-2.0}, facade.getVelocity(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {-1.5,-10.0},facade.getAcceleration(theShark),LOW_PRECISION);			
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[1],facade.getCurrentSprite(theShark));				
			// After another 0.1 seconds, the shark stops moving to the left, but keeps on falling.
			facade.advanceWorldTime(world_100_200, 0.11);
			assertArrayEquals(new double[] {sharkInitXpos-0.1875,sharkInitYpos-0.2805},
					facade.getActualPosition(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {0.0,-3.1}, facade.getVelocity(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {0.0,-10.0},facade.getAcceleration(theShark),LOW_PRECISION);			
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[0],facade.getCurrentSprite(theShark));				
			actualScore += 12;
		}
	}
	
	@Test
	void advanceTime_SharkMovingRightAndJumping() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 12;
			Shark theShark = facade.createShark(50, 100, sharkSprites);
			facade.addGameObject(theShark, world_100_200);
			facade.setGeologicalFeature(world_100_200, 50, 100, WATER);
			facade.setGeologicalFeature(world_100_200, 65, 70, SOLID_GROUND);
			double sharkInitXpos = facade.getActualPosition(theShark)[0];
			double sharkInitYpos = facade.getActualPosition(theShark)[1];
			// After 1.5 seconds, the shark has moved to the left during 0.5 seconds,
			// and has rested during 1.0 seconds during which it has fallen down to
			// land on solid ground.
			for (int i=0; i<15; i++)
				facade.advanceWorldTime(world_100_200, 0.1);
			assertEquals(sharkInitXpos-0.1875,facade.getActualPosition(theShark)[0],LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] >= 0.79-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 0.8+LOW_PRECISION);
			// After 0.1 seconds, the shark has moved to the right over 0.75 cm
			// and has jumped up over 15 cm.
			sharkInitXpos = facade.getActualPosition(theShark)[0];
			sharkInitYpos = facade.getActualPosition(theShark)[1];
			facade.advanceWorldTime(world_100_200, 0.1);
			assertArrayEquals(new double[] {sharkInitXpos+0.0075,sharkInitYpos+0.15},
					facade.getActualPosition(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {0.15,1.0}, facade.getVelocity(theShark),LOW_PRECISION);
			assertArrayEquals(new double[] {1.5,-10.0},facade.getAcceleration(theShark),LOW_PRECISION);			
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[2],facade.getCurrentSprite(theShark));				
			// After another 0.3 seconds, the shark shall have moved to the right over 0.75 cm,
			// and re-enters the water tile.
			facade.advanceWorldTime(world_100_200, 0.15);
			facade.advanceWorldTime(world_100_200, 0.15);
			assertArrayEquals(new double[] {sharkInitXpos+0.12,sharkInitYpos},
					facade.getActualPosition(theShark),LOW_PRECISION);
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[2],facade.getCurrentSprite(theShark));				
			// After another 0.1 seconds, the shark stops moving to the right.
			facade.advanceWorldTime(world_100_200, 0.11);
			assertEquals(0.0, facade.getVelocity(theShark)[0],LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] >= 0.79-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 0.8+LOW_PRECISION);
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[0],facade.getCurrentSprite(theShark));				
			actualScore += 12;
		}
	}

	@Test
	void advanceTime_SharkLandingOnImpassableTerrain() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 10;
			Shark theShark = facade.createShark(50, 100, sharkSprites);
			facade.addGameObject(theShark, world_250_400);
			facade.setGeologicalFeature(world_250_400, 80, 95, SOLID_GROUND);
			facade.setGeologicalFeature(world_250_400, 50, 95, ICE);
			facade.setGeologicalFeature(world_250_400, 70, 120, WATER);
			double sharkInitXpos = facade.getActualPosition(theShark)[0];
			double sharkInitYpos = facade.getActualPosition(theShark)[1];
			// After 0.2 seconds, the shark has reached its top.
			facade.advanceWorldTime(world_250_400, 0.1);
			facade.advanceWorldTime(world_250_400, 0.1);
			assertArrayEquals(new double[] {sharkInitXpos-0.03,sharkInitYpos+0.2},
					facade.getActualPosition(theShark),LOW_PRECISION);
			// After another 0.2 seconds, the shark lands on impassable terrain.
			facade.advanceWorldTime(world_250_400, 0.1);
			facade.advanceWorldTime(world_250_400, 0.11);
			assertEquals(sharkInitXpos-0.1261,facade.getActualPosition(theShark)[0],LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] >= 0.99-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 1.0+LOW_PRECISION);
			// After another 0.09 seconds, the shark stops moving to the left
			facade.advanceWorldTime(world_250_400, 0.11);
			assertTrue(facade.getActualPosition(theShark)[0] <= sharkInitXpos-0.1261+LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[0] >= sharkInitXpos-0.1875-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] >= 0.99-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 1.0+LOW_PRECISION);
			actualScore += 10;
		}
	}

	@Test
	void advanceTime_SharkFalling() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 14;
			World theTallWorld = facade.createWorld(10, 100, 400, new int[] {10, 10}, 30, 40);
			Shark theShark = facade.createShark(50, 3990, sharkSprites);
			facade.addGameObject(theShark, theTallWorld);
			double sharkInitXpos = facade.getActualPosition(theShark)[0];
			double sharkInitYpos = facade.getActualPosition(theShark)[1];
			// After 0.2 seconds, the shark has fallen down 20 cm.
			facade.advanceWorldTime(theTallWorld, 0.1);
			facade.advanceWorldTime(theTallWorld, 0.1);
			assertArrayEquals(new double[] {sharkInitXpos-0.03,sharkInitYpos-0.2},
					facade.getActualPosition(theShark),LOW_PRECISION);
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[1],facade.getCurrentSprite(theShark));				
			// After another 0.3 seconds, the shark stops moving to the left.
			// It has fallen down 125 cm in total.
			facade.advanceWorldTime(theTallWorld, 0.15);
			facade.advanceWorldTime(theTallWorld, 0.15);
			assertArrayEquals(new double[] {sharkInitXpos-0.1875,sharkInitYpos-1.25},
					facade.getActualPosition(theShark),LOW_PRECISION);
			// After another 1.2 seconds, the shark is moving to the right.
			// It has moved over 3 cm and has fallen 14.45 cm in total.
			for (int i=0; i<12; i++)
				facade.advanceWorldTime(theTallWorld, 0.1);
			assertArrayEquals(new double[] {sharkInitXpos-0.1875+0.03,sharkInitYpos-14.45},
					facade.getActualPosition(theShark),LOW_PRECISION);
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[2],facade.getCurrentSprite(theShark));				
			// After another 0.3 seconds, the shark stops moving to the right.
			// It has fallen over 2000 cm in total
			facade.advanceWorldTime(theTallWorld, 0.15);
			facade.advanceWorldTime(theTallWorld, 0.15);
			assertArrayEquals(new double[] {sharkInitXpos,sharkInitYpos-20.00},
					facade.getActualPosition(theShark),LOW_PRECISION);
			// After a total time of 2.8249 seconds, the shark falls out of the world.
			facade.advanceWorldTime(theTallWorld, 0.15);
			if (facade.isTeamSolution())
				assertEquals(sharkSprites[0],facade.getCurrentSprite(theShark));				
			for (int i=0; i<5; i++)
				facade.advanceWorldTime(theTallWorld, 0.15);
			assertTrue(facade.isTerminatedGameObject(theShark));
			assertNull(facade.getWorld(theShark));
			assertFalse(facade.getAllGameObjects(theTallWorld).contains(theShark));
			actualScore += 14;
		}
	}
	
	@Test
	void advanceTime_SharkFallingInWater() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 12;
			World theTallWorld = facade.createWorld(10, 100, 400, new int[] {10, 10}, 30, 40);
			facade.setGeologicalFeature(theTallWorld, 70, 990, WATER);
			Shark theShark = facade.createShark(50, 2750, sharkSprites);
			facade.addGameObject(theShark, theTallWorld);
			// The top of the shark must reach the water in order to stop the fall.
			int sharkHeight = facade.getCurrentSprite(theShark).getHeight();
			double timeToHaveTopInWater = Math.sqrt(2*(1750.0+sharkHeight/100.0)/1000.0);
			while (timeToHaveTopInWater > 0.1) {
				facade.advanceWorldTime(theTallWorld, 0.1);
				timeToHaveTopInWater -= 0.1;
			}
			facade.advanceWorldTime(theTallWorld, 0.15);
			double expectedYpos = 10.0 - (sharkHeight/100.0);
			assertTrue(facade.getActualPosition(theShark)[1] >= expectedYpos-0.01-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= expectedYpos+LOW_PRECISION);
			// After some time, the shark will start to move horizontally.
			// Because it is positioned in water, it will also start jumping.
			while (facade.getVelocity(theShark)[0] == 0.0)
				facade.advanceWorldTime(theTallWorld, 0.1);
			double oldXPos = facade.getActualPosition(theShark)[0];
			facade.advanceWorldTime(theTallWorld, 0.15);
			assertTrue(facade.getActualPosition(theShark)[0] != oldXPos);
			actualScore += 12;
		}
	}
	
	@Test
	void advanceTime_SharkOutOfWater() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 12;
			Shark theShark = facade.createShark(50, 1000, sharkSprites);
			facade.addGameObject(theShark, world_250_400);
			int sharkWidth = facade.getCurrentSprite(theShark).getWidth();
			facade.setGeologicalFeature(world_250_400, 50+sharkWidth-5, 1010, WATER);			
			facade.setGeologicalFeature(world_250_400, 70, 910, WATER);			
			// The shark first jumps up over 20 cm for 0.2 seconds, leaving the water for
			// a short while. It then starts falling down until it re-enters the water.
			// After moving to the left during 0.2582 seconds over 5 cm, the shark is out
			// of the water, and starts falling again.
			facade.advanceWorldTime(world_250_400, 0.12);
			facade.advanceWorldTime(world_250_400, 0.14);
			assertEquals(100,facade.getHitPoints(theShark));
			// The shark looses 6 hit points after 0.2 seconds out of the water.
			facade.advanceWorldTime(world_250_400, 0.1);
			facade.advanceWorldTime(world_250_400, 0.1);
			assertEquals(100-6,facade.getHitPoints(theShark));
			// The shark looses 6 hit points after another 0.2 seconds out of the water.
			facade.advanceWorldTime(world_250_400, 0.1);
			facade.advanceWorldTime(world_250_400, 0.1);
			assertEquals(100-2*6,facade.getHitPoints(theShark));
			// The shark will now dive into the water, and will no longer loose hit points
			facade.advanceWorldTime(world_250_400, 0.15);
			facade.advanceWorldTime(world_250_400, 0.15);			
			assertEquals(100-2*6,facade.getHitPoints(theShark));
			actualScore += 12;
		}
	}
	
	@Test
	void advanceTime_SharkOverlappingWithPlant() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 4;
			facade.setGeologicalFeature(world_250_400, 70, 95, SOLID_GROUND);
			Shark theShark = facade.createShark(50, 100, sharkSprites);
			int sharkWidth = facade.getCurrentSprite(theShark).getWidth();
			facade.addGameObject(theShark, world_250_400);
			Sneezewort theSneezeWort = facade.createSneezewort(50+sharkWidth+5, 110, sneezewortSprites);
			facade.addGameObject(theSneezeWort, world_250_400);
			// After 0.2 seconds, the shark shall have moved to the left over 0.75 cm,
			// and shall have jumped up over 3 cm.
			// The plant will have moved to the left over 10 cm, meaning that it overlaps
			// with the shark.
			// Sharks do not eat plants.
			facade.advanceWorldTime(world_250_400, 0.1);
			facade.advanceWorldTime(world_250_400, 0.1);
			assertTrue(facade.getActualPosition(theShark)[0]+(sharkWidth/100.0) > 
					facade.getActualPosition(theSneezeWort)[0]);
			assertTrue(facade.getHitPoints(theShark) >= 94);
			assertFalse(facade.isDeadGameObject(theSneezeWort));
			assertEquals(world_250_400,facade.getWorld(theSneezeWort));
			actualScore += 4;
		}
	}

	@Test
	void advanceTime_SharkBouncingAgainstOtherShark() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 8;
			facade.setGeologicalFeature(world_100_200, 65, 120, SOLID_GROUND);
			facade.setGeologicalFeature(world_100_200, 130, 120, SOLID_GROUND);
			Shark sharkOrientedToTheRight = facade.createShark(50, 300, sharkSprites);
			facade.addGameObject(sharkOrientedToTheRight, world_100_200);
			double sharkInitXpos = facade.getActualPosition(sharkOrientedToTheRight)[0];
			int sharkWidth = facade.getCurrentSprite(sharkOrientedToTheRight).getWidth();
			Shark sharkOrientedToTheLeft = facade.createShark
					(facade.getPixelPosition(sharkOrientedToTheRight)[0]+sharkWidth+1, 130, sharkSprites);
			facade.addGameObject(sharkOrientedToTheLeft, world_100_200);
			// After 1.5 seconds, the shark has moved to the left during 0.5 seconds,
			// and has rested during 1.0 seconds during which it has fallen down to
			// land on solid ground.
			for (int i=0; i<15; i++)
				facade.advanceTime(sharkOrientedToTheRight, 0.1);
			assertEquals(sharkInitXpos-0.1875,facade.getActualPosition(sharkOrientedToTheRight)[0],LOW_PRECISION);
			assertTrue(facade.getActualPosition(sharkOrientedToTheRight)[1] >= 1.29-LOW_PRECISION);
			assertTrue(facade.getActualPosition(sharkOrientedToTheRight)[1] <= 1.3+LOW_PRECISION);
			// After less than 0.45 seconds, one shark will bounce against the other shark.
			facade.advanceWorldTime(world_100_200, 0.15);
			facade.advanceWorldTime(world_100_200, 0.15);
			facade.advanceWorldTime(world_100_200, 0.15);
			assertTrue(facade.getPixelPosition(sharkOrientedToTheRight)[0]+sharkWidth ==
					facade.getPixelPosition(sharkOrientedToTheLeft)[0]);
			// After 1.05 seconds, the sharks start moving away from each other.
			for (int i=0; i<13; i++)
				facade.advanceWorldTime(world_100_200, 0.1);
			assertTrue(facade.getPixelPosition(sharkOrientedToTheRight)[0]+sharkWidth <
					facade.getPixelPosition(sharkOrientedToTheLeft)[0]);
			actualScore += 8;
		}
	}

	@Test
	void advanceTime_SharkBouncingAgainstSlime() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 14;
			facade.setGeologicalFeature(world_100_200, 65, 290, SOLID_GROUND);
			Shark theShark = facade.createShark(50, 300, sharkSprites);
			int sharkHeight = facade.getCurrentSprite(theShark).getHeight();
			facade.addGameObject(theShark, world_100_200);
			Slime theSlime = facade.createSlime(10, 60, 310+sharkHeight, null, slimeSprites);
			double slimeInitXPos = facade.getActualPosition(theSlime)[0];
			facade.addGameObject(theSlime, world_100_200);
			// After 0.0586 seconds, the shark bounces against the slime. 
			// As a result, the shark gains 10 hit points and starts falling down.
			facade.advanceTime(theShark, 0.07);
			assertEquals(100+10,facade.getHitPoints(theShark));
			assertTrue(facade.getActualPosition(theShark)[1] <= 3.1+LOW_PRECISION);
			// After another 0.15 seconds, the shark has landed on the solid ground,
			// and the slime has moved to the left.
			facade.advanceWorldTime(world_100_200, 0.18);
			assertTrue(facade.getActualPosition(theShark)[1] >= 2.9-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 3.0+LOW_PRECISION);
			assertEquals(slimeInitXPos+0.01134,facade.getActualPosition(theSlime)[0],LOW_PRECISION);
			actualScore += 14;
		}
	}

	@Test
	void advanceTime_SharkBouncingAgainstMazoub() throws Exception {
		if (shouldImplementSharks()) {
			maximumScore += 14;
			facade.setGeologicalFeature(world_100_200, 60, 230, SOLID_GROUND);
			Shark theShark = facade.createShark(50, 300, sharkSprites);
			facade.addGameObject(theShark, world_100_200);
			Mazub theMazub = facade.createMazub(30, 240, mazubSprites);
			int mazubHeight = facade.getCurrentSprite(theMazub).getHeight();
			facade.addGameObject(theMazub, world_100_200);
			// When the shark lands on the mazub, it stops falling and looses 50 hit points
			// because of the contact with the mazub, and 6 because the shark is not
			// in contacgt with water.
			double timeToLandOnMazub = Math.sqrt(2.0*(60.0-mazubHeight)/1000.0);
			System.out.println(timeToLandOnMazub);
			while (timeToLandOnMazub > 0.1) {
				facade.advanceTime(theShark, 0.1);
				System.out.println("Time advanced with 0.1 s");
				timeToLandOnMazub -= 0.1;
			}
			facade.advanceTime(theShark,timeToLandOnMazub+0.1);
			System.out.println("Time advanced with " + (timeToLandOnMazub+0.1));
			assertEquals(100-50-6,facade.getHitPoints(theShark));
			assertTrue(facade.getActualPosition(theShark)[1] >= 2.4+(mazubHeight/100.0)-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 2.4+(mazubHeight/100.0)+LOW_PRECISION);
			// After another 0.3 seconds, the shark is still in contact with the mazub,
			// but has not lost any additional hit points.
			System.out.println("Further advancing time");
			facade.advanceTime(theShark, 0.15);
			facade.advanceTime(theShark, 0.15);
			assertFalse(facade.isDeadGameObject(theShark));
			assertTrue(facade.getActualPosition(theShark)[1] >= 2.4+(mazubHeight/100.0)-LOW_PRECISION);
			assertTrue(facade.getActualPosition(theShark)[1] <= 2.4+(mazubHeight/100.0)+LOW_PRECISION);
			actualScore += 14;
		}
	}

	
	/*******************
	 * Tests for World *
	 *******************/

	public final static int AIR = 0;
	public final static int SOLID_GROUND = 1;
	public final static int WATER = 2;
	public final static int MAGMA = 3;
	public final static int ICE = 4;
	public final static int GAS = 5;

	@Test
	void createWorld_AllDefaultGeologicalFeatures() throws Exception {
		maximumScore += 12;
		int[] targetTile = { -3, 999 };
		World newWorld = facade.createWorld(5, 10, 20, targetTile, 10, 20);
		assertEquals(5, facade.getTileLength(newWorld));
		assertArrayEquals(new int[] { 10 * 5, 20 * 5 }, facade.getSizeInPixels(newWorld));
		assertTrue(facade.getAllGameObjects(newWorld).isEmpty());
		assertNull(facade.getMazub(newWorld));
		assertArrayEquals(targetTile, facade.getTargetTileCoordinate(newWorld));
		assertArrayEquals(new int[] { 10, 20 },
				facade.getVisibleWindowDimension(newWorld));
		if (facade.isTeamSolution())
			assertArrayEquals(new int[] { 0, 0 },
					facade.getVisibleWindowPosition(newWorld));
		for (int tileX = 0; tileX < 10; tileX++)
			for (int tileY = 0; tileY < 20; tileY++)
				assertEquals(AIR,
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 12;
	}

	@Test
	void createWorld_AllExplicitGeologicalFeatures() throws Exception {
		maximumScore += 8;
		int[] geologicalFeatures = new int[] { AIR, SOLID_GROUND, WATER, WATER, MAGMA,
				AIR, ICE, GAS };
		World newWorld = facade.createWorld(5, 2, 4, new int[] { 0, 1 }, 4, 5,
				geologicalFeatures);
		for (int tileY = 0; tileY < 4; tileY++)
			for (int tileX = 0; tileX < 2; tileX++)
				assertEquals(geologicalFeatures[tileY * 2 + tileX],
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 8;
	}

	@Test
	void createWorld_SomeExplicitGeologicalFeatures() throws Exception {
		maximumScore += 4;
		int[] geologicalFeatures = new int[] { AIR, SOLID_GROUND, WATER, MAGMA, ICE,
				GAS };
		World newWorld = facade.createWorld(5, 2, 4, new int[] { 1, 2 }, 3, 3,
				geologicalFeatures);
		for (int tileY = 0; tileY < 4; tileY++)
			for (int tileX = 0; tileX < 2; tileX++)
				assertEquals(
						tileY * 2 + tileX < geologicalFeatures.length
								? geologicalFeatures[tileY * 2 + tileX]
								: AIR,
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 4;
	}

	@Test
	void createWorld_IllegalTargetTile() throws Exception {
		maximumScore += 4;
		// Non-effective target tile
		assertThrows(ModelException.class,
				() -> facade.createWorld(5, 20, 30, null, 20, 30));
		// Target tile with illegal number of displacements
		assertThrows(ModelException.class,
				() -> facade.createWorld(5, 20, 30, new int[] { 1, 1, 2 }, 10, 30));
		actualScore += 4;
	}

	@Test
	void createWorld_IllegalWindow() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 4;
			assertThrows(ModelException.class,
					() -> facade.createWorld(5, 2, 3, new int[] { 1, 1 }, 20, 30));
			assertThrows(ModelException.class,
					() -> facade.createWorld(5, 2, 3, new int[] { 1, 1 }, -10, 30));
			actualScore += 4;
		}
	}

	@Test
	void createWorld_IllegalDimensions() throws Exception {
		maximumScore += 3;
		// Illegal tile size
		World newWorld = facade.createWorld(-10, 100, 200, new int[] { 10, 20 }, 10, 15);
		assertTrue(facade.getTileLength(newWorld) >= 0);
		// Illegal number of horizontal pixels
		newWorld = facade.createWorld(10, -100, 200, new int[] { 10, 20 }, 20, 30);
		assertTrue(facade.getSizeInPixels(newWorld)[0] >= 0);
		// Illegal number of vertical pixels
		newWorld = facade.createWorld(10, 100, -200, new int[] { 10, 20 }, 10, 20);
		assertTrue(facade.getSizeInPixels(newWorld)[1] >= 0);
		actualScore += 3;
	}

	@Test
	void createWorld_IllegalGeologicalFeatures() throws Exception {
		maximumScore += 4;
		assertThrows(ModelException.class,
				() -> facade.createWorld(5, 2, 3, new int[] { 0, 1 }, 4, 5, null));
		try {
			World newWorld = facade.createWorld(5, 2, 3, new int[] { 0, 1 }, 4, 5, null);
			for (int tileY = 0; tileY < 3; tileY++)
				for (int tileX = 0; tileX < 2; tileX++)
					assertEquals(AIR,
							facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		} catch (ModelException exc) {
			
		}
		int[] geologicalFeatures = new int[] { AIR, -4, WATER, 12 };
		World newWorld = facade.createWorld(5, 2, 3, new int[] { 4, 7 }, 2, 2,
				geologicalFeatures);
		for (int tileY = 0; tileY < 3; tileY++)
			for (int tileX = 0; tileX < 2; tileX++)
				assertEquals(tileY * 2 + tileX == 2 ? WATER : AIR,
						facade.getGeologicalFeature(newWorld, tileX * 5, tileY * 5));
		actualScore += 4;
	}

	@Test
	void terminateWorld() throws Exception {
		maximumScore += 12;
		Mazub someMazub = facade.createMazub(500, 150, mazubSprites);
		facade.addGameObject(someMazub, world_100_200);
		Sneezewort someSneezeWort = facade.createSneezewort(10, 10, sneezewortSprites);
		facade.addGameObject(someSneezeWort, world_100_200);
		Skullcab someSkullcab = facade.createSkullcab(200, 10, SkullcabSprites);
		facade.addGameObject(someSkullcab, world_100_200);
		Slime someSlime = facade.createSlime(10, 10, 100, someSchool, slimeSprites);
		facade.addGameObject(someSlime, world_100_200);
		Object someShark = null;
		if (shouldImplementSharks()) {
			someShark = facade.createShark(200, 10, sharkSprites);
			facade.addGameObject(someShark, world_100_200);
		}
		facade.terminateWorld(world_100_200);
		assertTrue(facade.getAllGameObjects(world_100_200).isEmpty());
		assertNull(facade.getWorld(someMazub));
		assertNull(facade.getWorld(someSneezeWort));
		assertNull(facade.getWorld(someSkullcab));
		assertNull(facade.getWorld(someSlime));
		if (shouldImplementSharks())
			assertNull(facade.getWorld(someShark));
		actualScore += 12;
	}

	@Test
	void getGeologicalFeatureAt_IllegalCases() throws Exception {
		maximumScore += 2;
		int tileSize = facade.getTileLength(world_100_200);
		// Any value can be returned outside the boundaries.
		facade.getGeologicalFeature(world_100_200, -tileSize - 1, 10);
		facade.getGeologicalFeature(world_100_200, 12, 200 * tileSize);
		actualScore += 2;
	}

	@Test
	void setGeologicalFeatureAt_LegalCase() throws Exception {
		maximumScore += 2;
		facade.setGeologicalFeature(world_100_200, 50, 100, MAGMA);
		assertEquals(MAGMA, facade.getGeologicalFeature(world_100_200, 50, 100));
		actualScore += 2;
	}

	@Test
	void setGeologicalFeatureAt_IllegalCases() throws Exception {
		maximumScore += 2;
		int tileSize = facade.getTileLength(world_100_200);
		facade.setGeologicalFeature(world_100_200, 50, 100, -20);
		assertTrue(facade.getGeologicalFeature(world_100_200, 50, 100) >= AIR);
		assertTrue(facade.getGeologicalFeature(world_100_200, 50, 100) <= GAS);
		facade.setGeologicalFeature(world_100_200, -tileSize - 1, 10, WATER);
		facade.setGeologicalFeature(world_100_200, 12, 200 * tileSize, SOLID_GROUND);
		actualScore += 2;
	}

	@Test
	void addGameObject_LegalCasePassableTerrain() throws Exception {
		maximumScore += 25;
		Mazub someMazub = facade.createMazub(500, 150, mazubSprites);
		facade.addGameObject(someMazub, world_100_200);
		assertTrue(facade.hasAsGameObject(someMazub, world_100_200));
		assertEquals(1, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(someMazub));
		assertEquals(world_100_200, facade.getWorld(someMazub));
		assertEquals(someMazub, facade.getMazub(world_100_200));
		Sneezewort someSneezeWort = facade.createSneezewort(10, 10, sneezewortSprites);
		facade.addGameObject(someSneezeWort, world_100_200);
		assertTrue(facade.hasAsGameObject(someSneezeWort, world_100_200));
		assertEquals(2, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(someSneezeWort));
		assertEquals(world_100_200, facade.getWorld(someSneezeWort));
		Skullcab someSkullcab = facade.createSkullcab(200, 10, SkullcabSprites);
		facade.addGameObject(someSkullcab, world_100_200);
		assertTrue(facade.hasAsGameObject(someSkullcab, world_100_200));
		assertEquals(3, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(someSkullcab));
		assertEquals(world_100_200, facade.getWorld(someSkullcab));
		Slime someSlime = facade.createSlime(10, 10, 100, someSchool, slimeSprites);
		facade.addGameObject(someSlime, world_100_200);
		assertTrue(facade.hasAsGameObject(someSlime, world_100_200));
		assertEquals(4, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(someSlime));
		assertEquals(world_100_200, facade.getWorld(someSlime));
		if (shouldImplementSharks()) {
			Shark someShark = facade.createShark(200, 10, sharkSprites);
			facade.addGameObject(someShark, world_100_200);
			assertTrue(facade.hasAsGameObject(someShark, world_100_200));
			assertEquals(5, facade.getAllGameObjects(world_100_200).size());
			assertTrue(facade.getAllGameObjects(world_100_200).contains(someShark));
			assertEquals(world_100_200, facade.getWorld(someShark));
		}
		actualScore += 25;
	}

	@Test
	void addGameObject_SimpleIllegalCases() throws Exception {
		maximumScore += 3;
		// Non effective game object
		assertThrows(ModelException.class,
				() -> facade.addGameObject(null, world_100_200));
		// Terminated game object
		facade.terminateGameObject(mazub_0_0);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_0_0, world_100_200));
		// Terminated world.
		facade.terminateWorld(world_100_200);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, world_100_200));
		actualScore += 3;
	}

	@Test
	void addGameObject_ObjectInOtherWorld() throws Exception {
		maximumScore += 3;
		facade.addGameObject(mazub_20_45, world_250_400);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, world_100_200));
		actualScore += 3;
	}

	@Test
	void addGameObject_AtWorldBoundaries() throws Exception {
		maximumScore += 10;
		World small_world = facade.createWorld(2, 5, 8, new int[] { 3, 3 }, 4, 7);
		// Just inside boundaries
		facade.addGameObject(facade.createMazub(9, 6, mazubSprites), small_world);
		World other_small_world = facade.createWorld(2, 5, 8, new int[] { 10, 20 }, 4, 6);
		facade.addGameObject(facade.createMazub(3, 15, mazubSprites), other_small_world);
		// Just outside the boundaries
		World yet_another_small_world = facade.createWorld(2, 5, 8, new int[] { 12, 33 },
				3, 9);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, yet_another_small_world));
		World small_horizontal_world = facade.createWorld(10, 10, 50,
				new int[] { 10, 20 }, 4, 8);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_100_0, small_horizontal_world));
		World small_vertical_world = facade.createWorld(5, 100, 9, new int[] { 10, 20 },
				20, 4);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_20_45, small_vertical_world));
		actualScore += 10;
	}

	@Test
	void addGameObject_AdjacentToImpassableTerrain() throws Exception {
		maximumScore += 10;
		int mazubWidth = mazubSprites[0].getWidth();
		int slimeHeight = slimeSprites[0].getHeight();
		// Adjacent left and right
		facade.setGeologicalFeature(world_100_200, 220, 330, SOLID_GROUND);
		facade.addGameObject(facade.createMazub(220 - mazubWidth, 335, mazubSprites),
				world_100_200);
		facade.addGameObject(facade.createSlime(10, 230, 335, someSchool, slimeSprites),
				world_100_200);
		// Adjacent bottom and top
		facade.setGeologicalFeature(world_100_200, 220, 530, SOLID_GROUND);
		facade.addGameObject(
				facade.createSlime(20, 225, 530 - slimeHeight, someSchool, slimeSprites),
				world_100_200);
		if (shouldImplementSharks())
			facade.addGameObject(facade.createShark(225, 540, sharkSprites), world_100_200);
		// Overlapping with top row of impassable terrain.
		facade.setGeologicalFeature(world_100_200, 220, 630, SOLID_GROUND);
		if (shouldImplementSharks())
			facade.addGameObject(facade.createShark(225, 639, sharkSprites), world_100_200);
		actualScore += 10;
	}

	@Test
	void addGameObject_OnImpassableTerrain() throws Exception {
		maximumScore += 8;
		facade.setGeologicalFeature(world_100_200, 220, 330, SOLID_GROUND);
		facade.setGeologicalFeature(world_100_200, 500, 500, ICE);
		int width = mazubSprites[0].getWidth();
		int height = mazubSprites[0].getHeight();
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(220 - width + 2, 335, mazubSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createSlime(10, 229, 335, someSchool, slimeSprites),
						world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(
						facade.createMazub(225, 330 - height + 2, mazubSprites),
						world_100_200));
		if (shouldImplementSharks())
			assertThrows(ModelException.class,
					() -> facade.addGameObject(facade.createShark(225, 338, mazubSprites),
							world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(facade.createMazub(505, 503, mazubSprites),
						world_100_200));
		actualScore += 8;
	}

	@Test
	void addGameObject_SneezewortOnImpassableTerrain() throws Exception {
		maximumScore += 10;
		facade.setGeologicalFeature(world_100_200, 220, 330, SOLID_GROUND);
		facade.setGeologicalFeature(world_100_200, 10, 10, ICE);
		Sneezewort newSneezewort = facade.createSneezewort(210, 320, sneezewortSprites);
		facade.addGameObject(newSneezewort, world_100_200);
		assertTrue(facade.hasAsGameObject(newSneezewort, world_100_200));
		assertEquals(world_100_200, facade.getWorld(newSneezewort));
		Skullcab newSkullcab = facade.createSkullcab(210, 320, SkullcabSprites);
		facade.addGameObject(newSkullcab, world_100_200);
		assertTrue(facade.hasAsGameObject(newSkullcab, world_100_200));
		assertEquals(world_100_200, facade.getWorld(newSkullcab));
		actualScore += 10;
	}

	@Test
	void addGameObject_AdjacentObjects() throws Exception {
		maximumScore += 10;
		Mazub mazubInWorld = facade.createMazub(140, 250, mazubSprites);
		facade.addGameObject(mazubInWorld, world_100_200);
		int mazubWidth = mazubSprites[0].getWidth();
		int mazubHeight = mazubSprites[0].getHeight();
		int slimeWidth = slimeSprites[0].getWidth();
		int slimeHeight = slimeSprites[0].getHeight();
		// Adjacent left and right
		facade.addGameObject(
				facade.createSlime(10, 140 - slimeWidth, 270, someSchool, slimeSprites),
				world_100_200);
		if (shouldImplementSharks())
			facade.addGameObject(facade.createShark(140 + mazubWidth, 270, sharkSprites),
				world_100_200);
		// Adjacent top and bottom
		facade.addGameObject(
				facade.createSlime(20, 180, 450 - slimeHeight, someSchool, slimeSprites),
				world_100_200);
		if (shouldImplementSharks())
			facade.addGameObject(facade.createShark(60, 450 + mazubHeight, sharkSprites),
					world_100_200);
		actualScore += 10;
	}

	@Test
	void addGameObject_OverlappingObjects() throws Exception {
		maximumScore += 8;
		Mazub mazubInWorld = facade.createMazub(140, 250, mazubSprites);
		facade.addGameObject(mazubInWorld, world_100_200);
		int mazubWidth = mazubSprites[0].getWidth();
		int mazubHeight = mazubSprites[0].getHeight();
		int slimeWidth = slimeSprites[0].getWidth();
		int slimeHeight = slimeSprites[0].getHeight();
		assertThrows(ModelException.class,
				() -> facade.addGameObject(facade.createSlime(10, 140 - slimeWidth + 2,
						270, someSchool, slimeSprites), world_100_200));
		if (shouldImplementSharks())
			assertThrows(ModelException.class,
					() -> facade.addGameObject(
							facade.createShark(140 + mazubWidth - 2, 270, sharkSprites),
							world_100_200));
		assertThrows(ModelException.class,
				() -> facade.addGameObject(facade.createSlime(20, 170,
						250 - slimeHeight + 2, someSchool, slimeSprites), world_100_200));
		if (shouldImplementSharks())
			assertThrows(ModelException.class,
					() -> facade.addGameObject(
							facade.createShark(170, 250 + mazubHeight - 2, mazubSprites),
							world_100_200));
		actualScore += 8;
	}

	@Test
	void addGameObject_ActiveGameInWorld() throws Exception {
		maximumScore += 2;
		facade.addGameObject(mazub_0_0, world_100_200);
		facade.startGame(world_100_200);
		Sneezewort newSneezewort = facade.createSneezewort(210, 320, sneezewortSprites);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(newSneezewort, world_100_200));
		actualScore += 2;
	}

	@Test
	void addGameObject_TooManyObjects() throws Exception {
		maximumScore += 12;
		World bigWorld = facade.createWorld(5, 200, 200, new int[] { 10, 20 }, 50, 40);
		for (int i = 0; i <= 99; i++)
			if (shouldImplementSharks())
				facade.addGameObject(
						facade.createShark((i % 10) * 100, (i / 10) * 50, sharkSprites),
						bigWorld);
			else
				facade.addGameObject(facade.createSneezewort(10, 20, sneezewortSprites), bigWorld);
		// No more than 100 other game objects in a world.
		if (shouldImplementSharks())
			assertThrows(ModelException.class, () -> facade
				.addGameObject(facade.createShark(0, 800, sharkSprites), bigWorld));
		else
			assertThrows(ModelException.class, () -> facade
				.addGameObject(facade.createSkullcab(100, 200, SkullcabSprites), bigWorld));
		// Adding a mazub must still be possible.
		facade.addGameObject(facade.createMazub(100, 800, mazubSprites), bigWorld);
		actualScore += 12;
	}

	@Test
	void addGameObject_SeveralMazubs() throws Exception {
		maximumScore += 4;
		facade.addGameObject(mazub_0_0, world_250_400);
		assertThrows(ModelException.class,
				() -> facade.addGameObject(mazub_100_0, world_250_400));
		actualScore += 4;
	}

	@Test
	void removeGameObject_LegalCase() throws Exception {
		maximumScore += 12;
		Mazub some_mazub = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(some_mazub, world_100_200);
		facade.removeGameObject(some_mazub, world_100_200);
		assertFalse(facade.hasAsGameObject(some_mazub, world_100_200));
		assertNull(facade.getWorld(some_mazub));
		assertNull(facade.getMazub(world_100_200));
		// Adding another mazub should not be a problem.
		facade.addGameObject(mazub_0_0, world_100_200);
		actualScore += 12;
	}

	@Test
	void removeGameObject_ObjectNotInWorld() throws Exception {
		maximumScore += 6;
		// Mazub not in a world
		assertThrows(ModelException.class,
				() -> facade.removeGameObject(mazub_100_0, world_100_200));
		// Non-effective mazub
		assertThrows(ModelException.class,
				() -> facade.removeGameObject(null, world_100_200));
		// Mazub in other world
		facade.addGameObject(mazub_0_0, world_100_200);
		assertThrows(ModelException.class,
				() -> facade.removeGameObject(mazub_0_0, world_250_400));
		actualScore += 6;
	}

	@Test
	void getAllGameObjects_SeveralObjects() {
		maximumScore += 5;
		Mazub mazub = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(mazub, world_100_200);
		Object shark = null;
		if (shouldImplementSharks()) {
			shark = facade.createShark(600, 150, sharkSprites);
			facade.addGameObject(shark, world_100_200);
		}
		Slime slime = facade.createSlime(10, 500, 550, someSchool, slimeSprites);
		facade.addGameObject(slime, world_100_200);
		int expectedNbGameObjects = shouldImplementSharks() ? 3 : 2;
		assertEquals(expectedNbGameObjects, facade.getAllGameObjects(world_100_200).size());
		assertTrue(facade.getAllGameObjects(world_100_200).contains(mazub));
		if (shouldImplementSharks())
			assertTrue(facade.getAllGameObjects(world_100_200).contains(shark));
		assertTrue(facade.getAllGameObjects(world_100_200).contains(slime));
		actualScore += 5;
	}

	@Test
	void getAllGameObjects_LeakTest() {
		maximumScore += 15;
		Mazub mazub = facade.createMazub(300, 150, mazubSprites);
		facade.addGameObject(mazub, world_100_200);
		if (shouldImplementSharks()) {
			Shark shark = facade.createShark(600, 150, sharkSprites);
			facade.addGameObject(shark, world_100_200);
		}
		else {
			Slime slime = facade.createSlime(10,600, 150, null, slimeSprites);
			facade.addGameObject(slime, world_100_200);
			
		}
		Set<? extends Object> allGameObjects = facade.getAllGameObjects(world_100_200);
		allGameObjects.add(null);
		assertEquals(2, facade.getAllGameObjects(world_100_200).size());
		allGameObjects.remove(mazub);
		assertTrue(facade.hasAsGameObject(mazub, world_100_200));
		actualScore += 15;
	}

	@Test
	void getTargetTile_OnPassableTerrain() {
		maximumScore += 2;
		int[] targetTileCoords = new int[] { 10, 20 };
		World theWorld = facade.createWorld(10, 100, 200, targetTileCoords, 20, 10);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void getTargetTile_OnImpassableTerrain() {
		maximumScore += 2;
		int[] targetTileCoords = new int[] { 1, 2 };
		World theWorld = facade.createWorld(10, 10, 20, targetTileCoords, 20, 10,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND,
				SOLID_GROUND, SOLID_GROUND, SOLID_GROUND, SOLID_GROUND);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void getTargetTile_OutsideWorld() {
		maximumScore += 2;
		int[] targetTileCoords = new int[] { -3, 2499 };
		World theWorld = facade.createWorld(10, 100, 200, targetTileCoords, 20, 10);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	void setTargetTile_GameNotActive() {
		maximumScore += 5;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 1, 2 }, 20, 10);
		int[] targetTileCoords = new int[] { 4000, -100 };
		facade.setTargetTileCoordinate(theWorld, targetTileCoords);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		targetTileCoords[0] = -2000;
		assertEquals(4000, facade.getTargetTileCoordinate(theWorld)[0]);
		actualScore += 5;
	}

	@Test
	void setTargetTile_GameActive() {
		maximumScore += 2;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 1, 2 }, 20, 10);
		facade.addGameObject(mazub_0_0, theWorld);
		facade.startGame(theWorld);
		int[] targetTileCoords = new int[] { 0, 0 };
		facade.setTargetTileCoordinate(theWorld, targetTileCoords);
		assertArrayEquals(targetTileCoords, facade.getTargetTileCoordinate(theWorld));
		actualScore += 2;
	}

	@Test
	public void getVisibleWindowPosition_WindowUpdateFarFromBoundaries() {
		maximumScore += 15;
		int windowWidth = 550;
		int windowHeight = 500;
		World theWorld = facade.createWorld(10, 1000, 500, new int[] { 9900, 4900 },
				windowWidth, windowHeight);
		Mazub theMazub = facade.createMazub(400, 300, mazubSprites);
		facade.addGameObject(theMazub, theWorld);
		facade.startGame(theWorld);
		facade.startMoveRight(theMazub);
		int mazubWidth = facade.getCurrentSprite(theMazub).getWidth();
		int mazubHeight = facade.getCurrentSprite(theMazub).getHeight();
		for (int nbSteps = 0; nbSteps < 5; nbSteps++) {
			int[] windowPixelPosition = facade.getVisibleWindowPosition(theWorld);
			int mazubPixelPositionX = facade.getPixelPosition(theMazub)[0];
			int mazubPixelPositionY = facade.getPixelPosition(theMazub)[1];
			if (facade.isTeamSolution() || facade.hasImplementedWorldWindow()) {
				assertTrue(windowPixelPosition[0] + 200 <= mazubPixelPositionX);
				assertTrue(windowPixelPosition[0] + windowWidth +
						200 >= mazubPixelPositionX + mazubWidth);
				assertTrue(windowPixelPosition[1] + 200 <= mazubPixelPositionY);
				assertTrue(windowPixelPosition[1] + windowHeight +
						200 >= mazubPixelPositionY + mazubHeight);
			} else {
				assertEquals(mazubPixelPositionX, windowPixelPosition[0] + 100);
				assertEquals(mazubPixelPositionY, windowPixelPosition[1] + 50);
			}
			// Mazub advances to the right and falls down.
			facade.advanceWorldTime(theWorld, 0.1);
		}
		actualScore += 15;
	}

	@Test
	public void getVisibleWindowPosition_WindowUpdateNearBoundaries() {
		maximumScore += 12;
		int windowWidth = 150;
		int windowHeight = 120;
		World theWorld = facade.createWorld(10, 28, 17, new int[] { 100, 200 },
				windowWidth, windowHeight);
		Mazub theMazub = facade.createMazub(80, 30, mazubSprites);
		facade.addGameObject(theMazub, theWorld);
		facade.startGame(theWorld);
		facade.startMoveLeft(theMazub);
		for (int nbSteps = 0; nbSteps < 2; nbSteps++) {
			int windowLeft = facade.getVisibleWindowPosition(theWorld)[0];
			int windowRight = facade.getVisibleWindowPosition(theWorld)[0] + windowWidth;
			int windowBottom = facade.getVisibleWindowPosition(theWorld)[1];
			int windowTop = facade.getVisibleWindowPosition(theWorld)[1] + windowHeight;
			int mazubPixelPositionX = facade.getPixelPosition(theMazub)[0];
			int mazubPixelPositionY = facade.getPixelPosition(theMazub)[1];
			int mazubWidth = facade.getCurrentSprite(theMazub).getWidth();
			int mazubHeight = facade.getCurrentSprite(theMazub).getHeight();
			if (facade.isTeamSolution() || facade.hasImplementedWorldWindow()) {
				assertTrue(((windowLeft >= mazubPixelPositionX) &&
						(windowLeft < mazubPixelPositionX + mazubWidth)) ||
						((windowRight > mazubPixelPositionX) &&
								(windowRight <= mazubPixelPositionX + mazubWidth)) ||
						((windowLeft <= mazubPixelPositionX) &&
								(windowRight >= mazubPixelPositionX + mazubWidth)));
				assertTrue(((windowBottom >= mazubPixelPositionY) &&
						(windowBottom < mazubPixelPositionY + mazubHeight)) ||
						((windowTop > mazubPixelPositionY) &&
								(windowTop <= mazubPixelPositionY + mazubHeight)) ||
						((windowBottom <= mazubPixelPositionY) &&
								(windowTop >= mazubPixelPositionY + mazubHeight)));
			} else {
				assertEquals(0, windowLeft);
				assertEquals(0, windowBottom);
			}
			// After 0.1 seconds, mazub will have moved to the left and will have fallen
			// down.
			facade.advanceWorldTime(theWorld, 0.1);
		}
		actualScore += 12;
	}

	@Test
	public void getVisibleWindowPosition_MazubPartiallyOutsideBoundaries() {
		maximumScore += 10;
		int windowWidth = 150;
		int windowHeight = 120;
		int worldWidth = 280;
		int worldHeight = 170;
		World theWorld = facade.createWorld(10, worldWidth / 10, worldHeight / 10,
				new int[] { 100, 200 }, windowWidth, windowHeight);
		Mazub theMazub = facade.createMazub(250, 150, mazubSprites);
		facade.addGameObject(theMazub, theWorld);
		facade.startGame(theWorld);
		facade.startMoveRight(theMazub);
		facade.startJump(theMazub);
		for (int nbSteps = 0; nbSteps < 2; nbSteps++) {
			int windowLeft = facade.getVisibleWindowPosition(theWorld)[0];
			int windowRight = facade.getVisibleWindowPosition(theWorld)[0] + windowWidth;
			int windowBottom = facade.getVisibleWindowPosition(theWorld)[1];
			int windowTop = facade.getVisibleWindowPosition(theWorld)[1] + windowHeight;
			int mazubPixelPositionX = facade.getPixelPosition(theMazub)[0];
			int mazubPixelPositionY = facade.getPixelPosition(theMazub)[1];
			if (facade.isTeamSolution() || facade.hasImplementedWorldWindow()) {
				assertEquals(worldWidth - windowWidth, windowLeft);
				assertEquals(worldWidth, windowRight);
				assertEquals(worldHeight - windowHeight, windowBottom);
				assertEquals(worldHeight, windowTop);
			} else {
				assertEquals(mazubPixelPositionX - 100, windowLeft);
				assertEquals(mazubPixelPositionY - 50, windowBottom);
			}
			// After 0.01 seconds, mazub will have moved to the right and up.
			facade.advanceWorldTime(theWorld, 0.01);
		}
		actualScore += 10;
	}

	@Test
	public void getVisibleWindowPosition_VerySmallWorld() {
		maximumScore += 5;
		int windowWidth = 115;
		int windowHeight = 60;
		World theWorld = facade.createWorld(10, 13, 7, new int[] { 100, 200 },
				windowWidth, windowHeight);
		Mazub theMazub = facade.createMazub(10, 10, mazubSprites);
		facade.addGameObject(theMazub, theWorld);
		facade.startGame(theWorld);
		int windowLeft = facade.getVisibleWindowPosition(theWorld)[0];
		int windowRight = facade.getVisibleWindowPosition(theWorld)[0] + windowWidth;
		int windowBottom = facade.getVisibleWindowPosition(theWorld)[1];
		int windowTop = facade.getVisibleWindowPosition(theWorld)[1] + windowHeight;
		int mazubPixelPositionX = facade.getPixelPosition(theMazub)[0];
		int mazubPixelPositionY = facade.getPixelPosition(theMazub)[1];
		int mazubWidth = facade.getCurrentSprite(theMazub).getWidth();
		int mazubHeight = facade.getCurrentSprite(theMazub).getHeight();
		if (facade.isTeamSolution() || facade.hasImplementedWorldWindow()) {
			assertTrue(((windowLeft >= mazubPixelPositionX) &&
					(windowLeft < mazubPixelPositionX + mazubWidth)) ||
					((windowRight > mazubPixelPositionX) &&
							(windowRight <= mazubPixelPositionX + mazubWidth)) ||
					((windowLeft <= mazubPixelPositionX) &&
							(windowRight >= mazubPixelPositionX + mazubWidth)));
			assertTrue(((windowBottom >= mazubPixelPositionY) &&
					(windowBottom < mazubPixelPositionY + mazubHeight)) ||
					((windowTop > mazubPixelPositionY) &&
							(windowTop <= mazubPixelPositionY + mazubHeight)) ||
					((windowBottom <= mazubPixelPositionY) &&
							(windowTop >= mazubPixelPositionY + mazubHeight)));
		} else {
			assertEquals(0, windowLeft);
			assertEquals(0, windowBottom);
		}
		actualScore += 5;
	}

	@Test
	void startGame_IllegalCase() {
		maximumScore += 2;
		assertThrows(ModelException.class, () -> facade.startGame(world_100_200));
		actualScore += 2;
	}

	@Test
	void isGameOver_WinningScenario() {
		maximumScore += 6;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 11, 101 }, 20, 10);
		facade.setGeologicalFeature(theWorld, 50, 990, SOLID_GROUND);
		facade.addGameObject(mazub_0_1000, theWorld);
		assertFalse(facade.isGameOver(theWorld));
		assertFalse(facade.didPlayerWin(theWorld));
		facade.startGame(theWorld);
		assertFalse(facade.isGameOver(theWorld));
		assertFalse(facade.didPlayerWin(theWorld));
		// Mazub will reach the target tile after 0.1847 seconds.
		facade.startMoveRight(mazub_0_1000);
		facade.advanceTime(mazub_0_1000, 0.19);
		facade.advanceTime(mazub_0_1000, 0.10);
		assertTrue(facade.isGameOver(theWorld));
		assertTrue(facade.didPlayerWin(theWorld));
		actualScore += 6;
	}

	@Test
	void isGameOver_LoosingScenario() {
		maximumScore += 6;
		World theWorld = facade.createWorld(10, 100, 200, new int[] { 30, 1 }, 20, 10);
		facade.addGameObject(mazub_0_0, theWorld);
		facade.startGame(theWorld);
		// Mazub will fall out of the world after 1.6 seconds.
		facade.startJump(mazub_0_0);
		for (int i = 1; i <= 10; i++)
			facade.advanceWorldTime(theWorld, 0.17);
		assertTrue(facade.isGameOver(theWorld));
		assertFalse(facade.didPlayerWin(theWorld));
		actualScore += 6;
	}

	@Test
	public void advanceTime_IllegalTime() throws Exception {
		maximumScore += 3;
		facade.addGameObject(mazub_100_0, world_250_400);
		facade.startMoveLeft(mazub_100_0);
		facade.startJump(mazub_100_0);
		facade.startDuck(mazub_100_0);
		assertThrows(ModelException.class,
				() -> facade.advanceWorldTime(world_250_400, Double.NaN));
		assertThrows(ModelException.class,
				() -> facade.advanceWorldTime(world_250_400, -3.0));
		assertThrows(ModelException.class,
				() -> facade.advanceWorldTime(world_250_400, Double.POSITIVE_INFINITY));
		actualScore += 3;
	}

	@Test
	void advanceWorldTime_SimpleScenario() {
		maximumScore += 40;
		Mazub playerMazub = facade.createMazub(200, 1075, mazubSprites);
		facade.startMoveRight(playerMazub);
		facade.startJump(playerMazub);
		facade.addGameObject(playerMazub, world_250_400);
		int mazubWidth = facade.getCurrentSprite(playerMazub).getWidth();
		double mazubOldXPos = facade.getActualPosition(playerMazub)[0];
		double mazubOldYPos = facade.getActualPosition(playerMazub)[1];
		int mazubOldHitPoints = facade.getHitPoints(playerMazub);
		Sneezewort sneeze = facade.createSneezewort(200 + mazubWidth + 5, 1080,
				sneezewortSprites);
		facade.addGameObject(sneeze, world_250_400);
		double sneezeOldXPos = facade.getActualPosition(sneeze)[0];
		Skullcab skull = facade.createSkullcab(210, 1180, SkullcabSprites);
		facade.addGameObject(skull, world_250_400);
		double skullOldYpos = facade.getActualPosition(skull)[1];
		int skullOldHitPoints = facade.getHitPoints(skull);
		Slime slime = facade.createSlime(10, 210, 1350, someSchool, slimeSprites);
		int slimeOldHitPoints = facade.getHitPoints(slime);
		int slimeWidth = facade.getCurrentSprite(slime).getWidth();
		facade.changeActualPosition(slime,
				new double[] { 2.16 - (slimeWidth / 100.0) - 0.1, 13.5 });
		double slimeOldXPos = facade.getActualPosition(slime)[0];
		facade.addGameObject(slime, world_250_400);
		assertEquals(4, facade.getAllGameObjects(world_250_400).size());
		facade.startGame(world_250_400);
		facade.advanceWorldTime(world_250_400, 0.15);
		// Because Mazub is advanced first, it will eat the Skullcab for the first time,
		// it will not overlap with the sneezewort nor with the slime.
		assertArrayEquals(new double[] { mazubOldXPos + 0.16, mazubOldYPos + 1.0875 },
				facade.getActualPosition(playerMazub), LOW_PRECISION);
		assertEquals(mazubOldHitPoints + 50, facade.getHitPoints(playerMazub));
		assertEquals(skullOldYpos + 0.075, facade.getActualPosition(skull)[1],LOW_PRECISION);
		assertEquals(skullOldHitPoints - 1, facade.getHitPoints(skull));
		assertEquals(sneezeOldXPos - 0.075, facade.getActualPosition(sneeze)[0],
				LOW_PRECISION);
		assertFalse(facade.isDeadGameObject(sneeze));
		assertEquals(slimeOldXPos + 0.007875, facade.getActualPosition(slime)[0],
				LOW_PRECISION);
		// Mazub stops moving right now. The slime will bounce against the mazub after
		// 0.6 seconds.
		facade.endMove(playerMazub);
		for (int i = 0; i < 3; i++)
			facade.advanceWorldTime(world_250_400, 0.15);
		assertArrayEquals(new double[] { mazubOldXPos + 0.16, mazubOldYPos + 3.0 },
				facade.getActualPosition(playerMazub), LOW_PRECISION);
		double mazubNewXPos = facade.getActualPosition(playerMazub)[0];
		assertEquals(mazubOldHitPoints + 50, facade.getHitPoints(playerMazub));
		assertEquals(mazubNewXPos - (slimeWidth / 100.0),
				facade.getActualPosition(slime)[0], LOW_PRECISION);
		assertEquals(slimeOldHitPoints - 30, facade.getHitPoints(slime));
		assertEquals(skullOldYpos + 0.2, facade.getActualPosition(skull)[1],LOW_PRECISION);
		assertEquals(skullOldHitPoints - 1, facade.getHitPoints(skull));
		assertEquals(sneezeOldXPos - 0.2, facade.getActualPosition(sneeze)[0],
				LOW_PRECISION);
		actualScore += 40;
	}

	/***********
	 * SCHOOL  *
	 ***********/

	@Test
	void createSchool_LegalCases() throws ModelException {
		maximumScore += 8;
		// School not in a world
		School newSchool = facade.createSchool(null);
		assertTrue(facade.getAllSlimes(newSchool).isEmpty());
		assertNull(facade.getWorld(newSchool));
		// School in world
		newSchool = facade.createSchool(world_250_400);
		assertTrue(facade.getAllSlimes(newSchool).isEmpty());
		assertEquals(world_250_400, facade.getWorld(newSchool));
		assertEquals(1, facade.getAllSchools(world_250_400).size());
		assertTrue(facade.getAllSchools(world_250_400).contains(newSchool));
		actualScore += 8;
	}

	@Test
	void createSchool_TooManySchoolsInWorld() throws ModelException {
		maximumScore += 8;
		for (int i = 0; i < 10; i++)
			facade.createSchool(world_250_400);
		assertThrows(ModelException.class, () -> facade.createSchool(world_250_400));
		actualScore += 8;
	}

	@Test
	void addAsSlime_LegalCase() throws ModelException {
		maximumScore += 6;
		School someSchool = facade.createSchool(world_250_400);
		Slime slime1 = facade.createSlime(10, 20, 30, someSchool, slimeSprites);
		facade.addGameObject(slime1, world_250_400);
		Slime slime2 = facade.createSlime(20, 120, 30, someSchool, slimeSprites);
		facade.addGameObject(slime2, world_250_400);
		Slime slime3 = facade.createSlime(30, 20, 120, someSchool, slimeSprites);
		facade.addGameObject(slime3, world_250_400);
		Slime slimeToBeAdded = facade.createSlime(40, 120, 120, null, slimeSprites);
		facade.addGameObject(slimeToBeAdded, world_250_400);
		facade.addAsSlime(someSchool, slimeToBeAdded);
		assertEquals(4, facade.getAllSlimes(someSchool).size());
		assertTrue(facade.getAllSlimes(someSchool).contains(slimeToBeAdded));
		assertEquals(someSchool, facade.getSchool(slimeToBeAdded));
		actualScore += 6;
	}

	@Test
	void addAsSlime_IllegalCases() throws ModelException {
		maximumScore += 6;
		// Slime already in school.
		School someSchool = facade.createSchool(world_250_400);
		Slime someSlime = facade.createSlime(10, 20, 30, someSchool, slimeSprites);
		facade.addGameObject(someSlime, world_250_400);
		School otherSchool = facade.createSchool(world_250_400);
		assertThrows(ModelException.class,
				() -> facade.addAsSlime(otherSchool, someSlime));
		// Terminated slime
		Slime terminatedSlime = facade.createSlime(20, 20, 30, null, slimeSprites);
		facade.terminateGameObject(terminatedSlime);
		assertThrows(ModelException.class,
				() -> facade.addAsSlime(otherSchool, terminatedSlime));
		// Terminated school
		facade.terminateSchool(someSchool);
		Slime freeSlime = facade.createSlime(30, 20, 30, null, slimeSprites);
		assertThrows(ModelException.class,
				() -> facade.addAsSlime(someSchool, freeSlime));
		actualScore += 6;
	}

	@Test
	void removeAsSlime_LegalCase() throws ModelException {
		maximumScore += 6;
		School someSchool = facade.createSchool(world_250_400);
		Slime slime1 = facade.createSlime(10, 20, 30, someSchool, slimeSprites);
		facade.addGameObject(slime1, world_250_400);
		Slime slime2 = facade.createSlime(20, 120, 30, someSchool, slimeSprites);
		facade.addGameObject(slime2, world_250_400);
		Slime slime3 = facade.createSlime(30, 20, 120, someSchool, slimeSprites);
		facade.addGameObject(slime3, world_250_400);
		facade.removeAsSlime(someSchool, slime2);
		assertEquals(2, facade.getAllSlimes(someSchool).size());
		assertFalse(facade.getAllSlimes(someSchool).contains(slime2));
		assertNull(facade.getSchool(slime2));
		actualScore += 6;
	}

	@Test
	void removeAsSlime_IllegalCases() throws ModelException {
		maximumScore += 4;
		// Slime in other school.
		School someSchool = facade.createSchool(world_250_400);
		Slime someSlime = facade.createSlime(10, 20, 30, someSchool, slimeSprites);
		facade.addGameObject(someSlime, world_250_400);
		School otherSchool = facade.createSchool(world_250_400);
		assertThrows(ModelException.class,
				() -> facade.removeAsSlime(otherSchool, someSlime));
		// Slime not in a school.
		Slime freeSlime = facade.createSlime(30, 20, 30, null, slimeSprites);
		assertThrows(ModelException.class,
				() -> facade.removeAsSlime(someSchool, freeSlime));
		actualScore += 4;

	}

	@Test
	void getAllSlimes_LeakTest() throws ModelException {
		maximumScore += 8;
		School someSchool = facade.createSchool(world_250_400);
		Slime slime1 = facade.createSlime(10, 20, 30, someSchool, slimeSprites);
		facade.addGameObject(slime1, world_250_400);
		Slime slime2 = facade.createSlime(20, 120, 30, someSchool, slimeSprites);
		facade.addGameObject(slime2, world_250_400);
		Slime slime3 = facade.createSlime(30, 20, 120, someSchool, slimeSprites);
		facade.addGameObject(slime3, world_250_400);
		Collection<? extends Slime> someSchoolSlimes = facade.getAllSlimes(someSchool);
		assert someSchoolSlimes != facade.getAllSlimes(someSchool);
		someSchoolSlimes.add(null);
		assertFalse(facade.getAllSlimes(someSchool).contains(null));
		someSchoolSlimes.remove(slime1);
		assertEquals(3, someSchoolSlimes.size());
		assertEquals(3, facade.getAllSlimes(someSchool).size());
		assertTrue(facade.getAllSlimes(someSchool).contains(slime1));
		actualScore += 8;
	}

	@Test
	void switchSchool_LegalCase() throws ModelException {
		maximumScore += 12;
		School oldSchool = facade.createSchool(world_250_400);
		Slime switchingSlime = facade.createSlime(10, 20, 30, oldSchool, slimeSprites);
		facade.addGameObject(switchingSlime, world_250_400);
		Slime oldMate1 = facade.createSlime(20, 120, 30, oldSchool, slimeSprites);
		facade.addGameObject(oldMate1, world_250_400);
		Slime oldMate2 = facade.createSlime(30, 20, 120, oldSchool, slimeSprites);
		facade.addGameObject(oldMate2, world_250_400);
		School newSchool = facade.createSchool(world_100_200);
		Slime newMate1 = facade.createSlime(40, 20, 30, newSchool, slimeSprites);
		facade.addGameObject(newMate1, world_100_200);
		Slime newMate2 = facade.createSlime(50, 120, 30, newSchool, slimeSprites);
		facade.addGameObject(newMate2, world_100_200);
		Slime newMate3 = facade.createSlime(60, 20, 120, newSchool, slimeSprites);
		facade.addGameObject(newMate3, world_100_200);
		Slime newMate4 = facade.createSlime(70, 120, 120, newSchool, slimeSprites);
		facade.addGameObject(newMate4, world_100_200);
		facade.switchSchool(newSchool, switchingSlime);
		assertEquals(2, facade.getAllSlimes(oldSchool).size());
		assertFalse(facade.getAllSlimes(oldSchool).contains(switchingSlime));
		assertEquals(5, facade.getAllSlimes(newSchool).size());
		assertTrue(facade.getAllSlimes(newSchool).contains(switchingSlime));
		assertEquals(100 - 2 + 4, facade.getHitPoints(switchingSlime));
		assertEquals(100 + 1, facade.getHitPoints(oldMate1));
		assertEquals(100 - 1, facade.getHitPoints(newMate2));
		actualScore += 12;
	}

	@Test
	void switchSchool_IllegalCases() throws ModelException {
		maximumScore += 8;
		// Slime not in a school.
		School someSchool = facade.createSchool(world_250_400);
		School otherSchool = facade.createSchool(world_250_400);
		Slime freeSlime = facade.createSlime(30, 20, 30, null, slimeSprites);
		assertThrows(ModelException.class,
				() -> facade.switchSchool(someSchool, freeSlime));
		// No school to switch to.
		Slime someSlime = facade.createSlime(10, 20, 30, someSchool, slimeSprites);
		facade.addGameObject(someSlime, world_250_400);
		assertThrows(ModelException.class, () -> facade.switchSchool(null, someSlime));
		// Terminated slime
		Slime terminatedSlime = facade.createSlime(20, 20, 30, someSchool, slimeSprites);
		facade.terminateGameObject(terminatedSlime);
		assertThrows(ModelException.class,
				() -> facade.switchSchool(otherSchool, terminatedSlime));
		// Terminated school.
		facade.terminateSchool(otherSchool);
		assertThrows(ModelException.class,
				() -> facade.switchSchool(otherSchool, someSlime));
		actualScore += 8;
	}

	/********************
	 * Tests for Spider *
	 ********************/

	@Test
	void createSpider_LegalCase() throws Exception {
		maximumScore += 10;
		Spider newSpider = facade.createSpider(22, 10, 6, spiderSprites);
		assertArrayEquals(new int[] { 10, 6 }, facade.getPixelPosition(newSpider));
		assertArrayEquals(new double[] { 0.1, 0.06 }, facade.getActualPosition(newSpider),
				HIGH_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getVelocity(newSpider),
				LOW_PRECISION);
		assertArrayEquals(new double[] { 0.0, 0.0 }, facade.getAcceleration(newSpider),
				LOW_PRECISION);
		assertEquals(22, facade.getNbLegs(newSpider));
		if (facade.isTeamSolution()) {
			assertArrayEquals(spiderSprites, facade.getSprites(newSpider));
			assertEquals(spiderSprites[0], facade.getCurrentSprite(newSpider));
		}
		actualScore += 10;
	}

	@Test
	void createSpider_IllegalNbLegs() throws Exception {
		maximumScore += 4;
		// Spiders die as soon as they do not have at least 2 legs.
		// The construction of a spider with not enough legs may be refused or
		// the new spider will be terminated.
		assertThrows(ModelException.class,
				() -> facade.createSpider(-3, 5, 10, spiderSprites));
		actualScore += 2;
		try {
			Spider newSpider = facade.createSpider(1, 5, 10, spiderSprites);
			if (facade.isDeadGameObject(newSpider))
				actualScore += 2;
			else fail();
		} catch (ModelException exc) {
			actualScore += 2;
		}
	}

	@Test
	void createSpider_IllegalSprites() throws Exception {
		if (facade.isTeamSolution()) {
			maximumScore += 4;
			// Array of sprites must be effective.
			assertThrows(ModelException.class,
					() -> facade.createSpider(6, 5, 10, (Sprite[]) null));
			// Array of sprites must have exactly 3 elements.
			assertThrows(ModelException.class, () -> facade.createSpider(12, 5, 10,
					new Sprite[] { new Sprite("a", 1, 2), new Sprite("b", 1, 2) }));
			assertThrows(ModelException.class,
					() -> facade.createSpider(7, 5, 10,
							new Sprite[] { new Sprite("a", 1, 2), new Sprite("b", 2, 3),
									new Sprite("c", 3, 4), new Sprite("d", 1, 2) }));
			// Array of sprites must have effective elements.
			assertThrows(ModelException.class, () -> facade.createSpider(8, 5, 10,
					new Sprite[] { null, null }));
			actualScore += 4;
		}
	}
	
	@Test
	public void advanceTime_SpiderLeftAdjacentToSolidTerrain() throws Exception {
		maximumScore += 10;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 95, 10+k, SOLID_GROUND);
		Spider movingSpider = facade.createSpider(4, 100, 20,spiderSprites);
		facade.addGameObject(movingSpider, world_250_400);
		//The spider starts moving vertically with an initial velocity of 1.0m/s,
		// accelerating with 0.5m/s2 until it reaches a top speed of 2.0 m/s. 
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.getOrientation(movingSpider) > 0);
		assertEquals(1.0,facade.getActualPosition(movingSpider)[0],LOW_PRECISION);
		double expected_Y_Position = 0.2 + 1.0*0.15 + 0.5*0.15*0.15/2;
		assertEquals(expected_Y_Position,facade.getActualPosition(movingSpider)[1],LOW_PRECISION);
		assertEquals(0.0,facade.getVelocity(movingSpider)[0],LOW_PRECISION);
		double expected_Y_Velocity = 1.0 + 0.5*0.15;
		assertEquals(expected_Y_Velocity,facade.getVelocity(movingSpider)[1],LOW_PRECISION);
		assertEquals(0.0,facade.getAcceleration(movingSpider)[0],LOW_PRECISION);
		assertEquals(0.5,facade.getAcceleration(movingSpider)[1],LOW_PRECISION);
		// After 12*.015 seconds, the spider almost reaches its maximum speed.
		for (int k=1; k<=12; k++)
			facade.advanceWorldTime(world_250_400, 0.15);
		expected_Y_Position = 0.2 + 1.0*(13*0.15) + 0.5*(13*0.15)*(13*0.15)/2;
		assertEquals(expected_Y_Position,facade.getActualPosition(movingSpider)[1],LOW_PRECISION);
		expected_Y_Velocity = 1.0 + 0.5*(13*0.15);
		assertEquals(expected_Y_Velocity,facade.getVelocity(movingSpider)[1],LOW_PRECISION);
		// After another 0.15 seconds, the spider has reached its top speed.
		facade.advanceWorldTime(world_250_400, 0.15);
		expected_Y_Position = 0.2 + 1.0*(2.0) + 0.5*(2.0)*(2.0)/2 + 2.0*0.1;
		assertEquals(expected_Y_Position,facade.getActualPosition(movingSpider)[1],LOW_PRECISION);
		assertEquals(0.0,facade.getVelocity(movingSpider)[0],LOW_PRECISION);
		expected_Y_Velocity = 1.0 + 4*0.25;
		assertEquals(expected_Y_Velocity,facade.getVelocity(movingSpider)[1],LOW_PRECISION);
		actualScore += 10;
	}
	
	@Test
	public void advanceTime_SpiderRightAdjacentToSolidTerrain() throws Exception {
		maximumScore += 10;
		for (int k=0;k<2000;k+=5)
			facade.setGeologicalFeature(world_250_400, 200, 10+k, SOLID_GROUND);
		Spider spiderToMoveDown = facade.createSpider(4, 200-70, 1000,spiderSprites);
		facade.addGameObject(spiderToMoveDown, world_250_400);
		Spider spiderMovingUp = facade.createSpider(6, 200-70, 1010+30,spiderSprites);
		facade.addGameObject(spiderMovingUp, world_250_400);
		// The spider to move down will bounce against the spider moving up, and change its
		// orientation. It will have moved about 0.1m upwards and thereafter abuot 0.06125m downwards
		facade.advanceTime(spiderToMoveDown, 0.15);
		assertTrue(facade.getOrientation(spiderToMoveDown) < 0);
		double expected_Y_Position = 10.0 + 0.1 - 0.06125;
		assertEquals(expected_Y_Position,facade.getActualPosition(spiderToMoveDown)[1],LOW_PRECISION*2);
		assertEquals(0.0,facade.getVelocity(spiderToMoveDown)[0],LOW_PRECISION);
		double expected_Y_Velocity = -(1.0 + 0.5*0.05); //0.15 <-> 0.05
		assertEquals(expected_Y_Velocity,facade.getVelocity(spiderToMoveDown)[1],LOW_PRECISION);
		facade.advanceTime(spiderMovingUp, 0.15);
		expected_Y_Position = 10.4 + 1.0*0.15 + 0.5*0.15*0.15/2; //op het eind /2
		assertEquals(expected_Y_Position,facade.getActualPosition(spiderMovingUp)[1],LOW_PRECISION);			
		// After 12*.015 seconds, the spider moving down almost reaches its maximum speed.
		double YPositionAfterFirstMove = facade.getActualPosition(spiderToMoveDown)[1];
		double YVelocityAfterFirstMove = facade.getVelocity(spiderToMoveDown)[1];
		for (int k=1; k<=12; k++)
			facade.advanceWorldTime(world_250_400, 0.15);
		expected_Y_Position = YPositionAfterFirstMove +
				YVelocityAfterFirstMove*(12*0.15) - 0.5*(12*0.15)*(12*0.15)/2;
		assertEquals(expected_Y_Position,facade.getActualPosition(spiderToMoveDown)[1],LOW_PRECISION);
		expected_Y_Velocity = YVelocityAfterFirstMove - 0.5*(12*0.15);
		assertEquals(expected_Y_Velocity,facade.getVelocity(spiderToMoveDown)[1],LOW_PRECISION);
		// After another 0.15 seconds, the spider moving down has reached its top speed.
		facade.advanceWorldTime(world_250_400, 0.15);
		expected_Y_Velocity = -(1.0 + 4*0.25);
		assertEquals(expected_Y_Velocity,facade.getVelocity(spiderToMoveDown)[1],LOW_PRECISION);
		// After another 5*0.16 seconds, the spider moving up almost reaches its maximum speed.
		for (int k=1; k<=5; k++)
			facade.advanceWorldTime(world_250_400, 0.16);
		expected_Y_Position = 10.4 + 1.0*(14*0.15 + 5*0.16) + 0.5*2.9*2.9/2;
		assertEquals(expected_Y_Position,facade.getActualPosition(spiderMovingUp)[1],LOW_PRECISION);
		expected_Y_Velocity = 1.0 + 0.5*(14*0.15 + 5*0.16);
		assertEquals(expected_Y_Velocity,facade.getVelocity(spiderMovingUp)[1],LOW_PRECISION);
		// After another 0.16 seconds, the spider moving up has reached its top speed.
		facade.advanceWorldTime(world_250_400, 0.16);
		expected_Y_Velocity = 1.0 + 6*0.25;
		assertEquals(expected_Y_Velocity,facade.getVelocity(spiderMovingUp)[1],LOW_PRECISION);
		actualScore += 10;
	}

	@Test
	public void advanceTime_SpiderTopAdjacentToSolidTerrain() throws Exception {
		maximumScore += 10;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 100+k, 150, SOLID_GROUND);
		Spider movingSpider = facade.createSpider(8, 1000, 150-30,spiderSprites);
		facade.addGameObject(movingSpider, world_250_400);
		//The spider starts moving vertically with an initial velocity of 1.0m/s,
		// accelerating with 0.5m/s2 until it reaches a top speed of 2.0 m/s. 
		facade.advanceWorldTime(world_250_400, 0.12);
		assertTrue(facade.getOrientation(movingSpider) < 0);
		assertEquals(1.2,facade.getActualPosition(movingSpider)[1],LOW_PRECISION);
		double expected_X_Position = 10.0 - (0.15*8)*0.12;
		assertEquals(expected_X_Position,facade.getActualPosition(movingSpider)[0],LOW_PRECISION);
		assertEquals(0.0,facade.getVelocity(movingSpider)[1],LOW_PRECISION);
		double expected_X_Velocity = -0.15*8;
		assertEquals(expected_X_Velocity,facade.getVelocity(movingSpider)[0],LOW_PRECISION);
		assertEquals(0.0,facade.getAcceleration(movingSpider)[0],LOW_PRECISION);
		assertEquals(0.0,facade.getAcceleration(movingSpider)[1],LOW_PRECISION);
//		// The spider keeps moving on at constant speed.
		for (int k=1; k<=9; k++)
			facade.advanceWorldTime(world_250_400, 0.12);
		expected_X_Position = 10.0 - (0.15*8)*(10*0.12);
		assertEquals(expected_X_Position,facade.getActualPosition(movingSpider)[0],LOW_PRECISION);
		assertEquals(expected_X_Velocity,facade.getVelocity(movingSpider)[0],LOW_PRECISION);
		actualScore += 10;
	}
	
	@Test
	public void advanceTime_SpiderBottomAdjacentToSolidTerrain() throws Exception {
		maximumScore += 10;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 100+k, 150-5, SOLID_GROUND);
		Spider spiderToMoveRight = facade.createSpider(8, 500, 150,spiderSprites);
		facade.addGameObject(spiderToMoveRight, world_250_400);
		Spider spiderMovingLeft = facade.createSpider(6, 500-70-5, 150,spiderSprites);
		facade.addGameObject(spiderMovingLeft, world_250_400);
		// The spider to move right will bounce against the spider moving left, and change its
		// orientation. It will have moved about 0.05m to the left and thereafter about 0.0944m
		// to the right
		facade.advanceTime(spiderToMoveRight, 0.12);
		assertTrue(facade.getOrientation(spiderToMoveRight) > 0);
		double expected_X_Position = 5.0 -0.05 + 0.0944;
		assertEquals(expected_X_Position,facade.getActualPosition(spiderToMoveRight)[0],LOW_PRECISION*2);
		assertEquals(0.15*8,facade.getVelocity(spiderToMoveRight)[0],LOW_PRECISION);
		facade.advanceTime(spiderMovingLeft, 0.12);
		expected_X_Position = 4.25 - 6*0.15*0.12;
		assertEquals(expected_X_Position,facade.getActualPosition(spiderMovingLeft)[0],LOW_PRECISION);
		// Both spiders keep on moving in opposite directions
		double X_positionSpiderToMoveRight = facade.getActualPosition(spiderToMoveRight)[0];
		double X_positionSpiderMovingLeft = facade.getActualPosition(spiderMovingLeft)[0];
		facade.advanceWorldTime(world_250_400, 0.12);
		assertEquals(X_positionSpiderToMoveRight+0.12*0.15*8,
				facade.getActualPosition(spiderToMoveRight)[0],LOW_PRECISION*2);
		assertEquals(X_positionSpiderMovingLeft-0.12*0.15*6,
				facade.getActualPosition(spiderMovingLeft)[0],LOW_PRECISION*2);
		actualScore += 10;
	}
	
	@Test
	public void advanceTime_SpiderInContactWithWater() throws Exception {
		maximumScore += 6;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 95, 10+k, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 110, 55, WATER);
		Spider movingSpider = facade.createSpider(4, 100, 20,spiderSprites);
		facade.addGameObject(movingSpider, world_250_400);
		//The spider starts moving vertically and dies as soon as it makes contact with
		// the water. 
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isDeadGameObject(movingSpider));
		actualScore += 6;
	}
	
	@Test
	public void advanceTime_SpiderInContactWithIce() throws Exception {
		maximumScore += 6;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 95, 10+k, SOLID_GROUND);
		facade.setGeologicalFeature(world_250_400, 110, 60, ICE);
		System.out.println(facade.getGeologicalFeature(world_250_400, 110, 55));
		Spider movingSpider = facade.createSpider(8, 100, 20,spiderSprites);
		facade.addGameObject(movingSpider, world_250_400);
		//The spider starts moving vertically and dies as soon as it makes contact with
		// the ice. 
		facade.advanceWorldTime(world_250_400, 0.15);
		assertTrue(facade.isDeadGameObject(movingSpider));
		actualScore += 6;
	}

	@Test
	public void advanceTime_SpiderInContactWithMagma() throws Exception {
		maximumScore += 10;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 95, 10+k, SOLID_GROUND);
		for (int k=0; k<70; k+=5)
			facade.setGeologicalFeature(world_250_400, 110, 55+k, MAGMA);
		Spider movingSpider = facade.createSpider(8, 100, 20,spiderSprites);
		facade.addGameObject(movingSpider, world_250_400);
		//The spider starts moving vertically and loses 2 legs as soon as it makes contact
		// with magma. 
		facade.advanceWorldTime(world_250_400, 0.18);
		assertEquals(6,facade.getNbLegs(movingSpider));
		// Because the spider is still in contact with magma 0.6 seconds after the first
		// contact, it looses another 2 legs.
		facade.advanceWorldTime(world_250_400, 0.18);
		facade.advanceWorldTime(world_250_400, 0.18);
		facade.advanceWorldTime(world_250_400, 0.18);
		assertEquals(4,facade.getNbLegs(movingSpider));
		// Another 0.6 seconds later, the spider is no longer in contact with magma
		facade.advanceWorldTime(world_250_400, 0.18);
		facade.advanceWorldTime(world_250_400, 0.18);
		facade.advanceWorldTime(world_250_400, 0.18);
		assertEquals(4,facade.getNbLegs(movingSpider));
		actualScore += 10;
	}
	
	@Test
	public void advanceTime_SpiderInContactWithMazub() throws Exception {
		maximumScore += 15;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 100+k, 150-5, SOLID_GROUND);
		Mazub theMazubToBounceAgainst = facade.createMazub(80, 150, mazubSprites);
		facade.startMoveRight(theMazubToBounceAgainst);
		int mazubWidth = facade.getCurrentSprite(theMazubToBounceAgainst).getWidth();
		facade.addGameObject(theMazubToBounceAgainst, world_250_400);
		Spider theBouncingSpider = facade.createSpider(3, 100+mazubWidth, 150,spiderSprites);
		facade.addGameObject(theBouncingSpider, world_250_400);
		// The spider will bounce against the mazub when moving to the left.
		facade.advanceTime(theMazubToBounceAgainst, 0.15);
		facade.advanceTime(theBouncingSpider, 0.15);
		assertEquals(3-1,facade.getNbLegs(theBouncingSpider));
		assertEquals(facade.getActualPosition(theMazubToBounceAgainst)[0]+mazubWidth/100.0,
				facade.getActualPosition(theBouncingSpider)[0],LOW_PRECISION);
		// The mazub now turns to the left and will move away from the spider.
		facade.endMove(theMazubToBounceAgainst);
		facade.startMoveLeft(theMazubToBounceAgainst);
		facade.advanceTime(theMazubToBounceAgainst, 0.15);
		facade.advanceTime(theBouncingSpider, 0.15);
		assertEquals(3-1,facade.getNbLegs(theBouncingSpider));
		assertTrue(facade.getActualPosition(theMazubToBounceAgainst)[0]+(mazubWidth/100.0) <
				facade.getActualPosition(theBouncingSpider)[0]);
		// The mazub now turns to the right again and will bounce against the spider.
		int mazubOldHitPoints = facade.getHitPoints(theMazubToBounceAgainst);
		facade.endMove(theMazubToBounceAgainst);
		facade.startMoveRight(theMazubToBounceAgainst);
		facade.advanceTime(theMazubToBounceAgainst, 0.15);
		assertEquals(mazubOldHitPoints-15,facade.getHitPoints(theMazubToBounceAgainst));
		actualScore += 15;
	}
	
	@Test
	public void advanceTime_SpiderInContactWithShark() throws Exception {
		maximumScore += 15;
		for (int k=0;k<1000;k+=5)
			facade.setGeologicalFeature(world_250_400, 100+k, 150-5, SOLID_GROUND); 
		Shark theSharkToBounceAgainst = facade.createShark(300, 150, sharkSprites);
		facade.addGameObject(theSharkToBounceAgainst, world_250_400);
		Spider theBouncingSpider = facade.createSpider(10, 380, 150, spiderSprites);
		facade.addGameObject(theBouncingSpider, world_250_400);
		// The spider will bounce against the shark and lose halve its legs.
		facade.advanceTime(theBouncingSpider, 0.18);
		assertEquals(5,facade.getNbLegs(theBouncingSpider));
		actualScore += 15;
	}

}