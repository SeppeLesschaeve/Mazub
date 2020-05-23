package jumpingalien.internal.gui.sprites;

import static jumpingalien.internal.gui.sprites.ImageSprite.*;

import java.util.stream.IntStream;

import jumpingalien.util.Sprite;

public class JumpingAlienSprites {

	protected static final int WIDTH = 70;
	protected static final int FULL_HEIGHT = 97;
	protected static final int DUCK_HEIGHT = 70;
	
	public static final Sprite[] ALIEN_SPRITESET = new Sprite[30];

	static {
		/* 0: stand, not ducking, front */
		ALIEN_SPRITESET[0] = createSprite("levels/player/p1_front.png")
				.resizeTo(WIDTH, FULL_HEIGHT);
		/* 1: stand, ducking, front */
		ALIEN_SPRITESET[1] = createSprite("levels/player/p1_duck_front.png")
				.resizeTo(WIDTH, DUCK_HEIGHT);
		/* 2: stand, not ducking, right */
		ALIEN_SPRITESET[2] = createSprite("levels/player/p1_stand.png")
				.resizeTo(WIDTH, FULL_HEIGHT);
		/* 3: stand, not ducking, left */
		ALIEN_SPRITESET[3] = createHFlippedSprite("levels/player/p1_stand.png")
				.resizeTo(WIDTH, FULL_HEIGHT);
		/* 4: jump, not ducking, right */
		ALIEN_SPRITESET[4] = createSprite("levels/player/p1_jump.png")
				.resizeTo(WIDTH, FULL_HEIGHT);
		/* 5: jump, not ducking, left */
		ALIEN_SPRITESET[5] = createHFlippedSprite("levels/player/p1_jump.png")
				.resizeTo(WIDTH, FULL_HEIGHT);
		/* 6: ducking, right */
		ALIEN_SPRITESET[6] = createSprite("levels/player/p1_duck.png")
				.resizeTo(WIDTH, DUCK_HEIGHT);
		/* 7: ducking, left */
		ALIEN_SPRITESET[7] = createHFlippedSprite("levels/player/p1_duck.png")
				.resizeTo(WIDTH, DUCK_HEIGHT);
		/* 8..18: moving, not ducking, right */
		for (int i = 0; i < 11; i++) {
			/* walk right */
			ALIEN_SPRITESET[8 + i] = createSprite(
					String.format("levels/player/p1_walk/PNG/p1_walk%02d.png",
							i + 1)).resizeTo(WIDTH, FULL_HEIGHT);
		}
		/* 19..29: moving, not ducking, right */
		for (int i = 0; i < 11; i++) {
			/* walk left */
			ALIEN_SPRITESET[19 + i] = createHFlippedSprite(
					String.format("levels/player/p1_walk/PNG/p1_walk%02d.png",
							i + 1)).resizeTo(WIDTH, FULL_HEIGHT);
		}

	}
	
	public static final String PLANT_LEFT_FILENAME = "levels/items/plantPurple.png";
	public static final ImageSprite PLANT_SPRITE_LEFT = ImageSprite
			.createSprite(PLANT_LEFT_FILENAME);
	public static final ImageSprite PLANT_SPRITE_RIGHT = ImageSprite
			.createHFlippedSprite(PLANT_LEFT_FILENAME);
	public static final ImageSprite PLANT_SPRITE_UP = ImageSprite
			.createSprite(PLANT_LEFT_FILENAME);
	public static final ImageSprite PLANT_SPRITE_DOWN = ImageSprite
			.createVFlippedSprite(PLANT_LEFT_FILENAME);
	
	public static final String SHARK_LEFT_FILENAME = "levels/enemies/fishSwim1.png";
	public static final String SHARK_LEFT2_FILENAME = "levels/enemies/fishSwim2.png";
	public static final String SHARK_DEAD_FILENAME = "levels/enemies/fishDead.png";

	public static final ImageSprite SHARK_SPRITE_REST = ImageSprite
			.createSprite(SHARK_DEAD_FILENAME);
	public static final ImageSprite SHARK_SPRITE_LEFT = ImageSprite
			.createSprite(SHARK_LEFT_FILENAME);
	public static final ImageSprite SHARK_SPRITE_RIGHT = ImageSprite
			.createHFlippedSprite(SHARK_LEFT_FILENAME);

	public static final String SLIME_LEFT_FILENAME = "levels/enemies/slimeWalk1.png";
	public static final String SLIME_LEFT2_FILENAME = "levels/enemies/slimeWalk2.png";
	public static final String SLIME_DEAD_FILENAME = "levels/enemies/slimeDead.png";

	public static final ImageSprite SLIME_SPRITE_LEFT = ImageSprite
			.createSprite(SLIME_LEFT_FILENAME);
	public static final ImageSprite SLIME_SPRITE_RIGHT = ImageSprite
			.createHFlippedSprite(SLIME_LEFT_FILENAME);

	public static final ImageSprite[] NUMBER_SPRITES = IntStream
			.rangeClosed(0, 9)
			.mapToObj(n -> String.format("levels/hud/hud_%d.png", n))
			.map(ImageSprite::createSprite)
			.toArray(size -> new ImageSprite[size]);
	public static final ImageSprite HEALTH_FULL = ImageSprite
			.createSprite("levels/hud/hud_heartFull.png");
	public static final ImageSprite HEALTH_HALF = ImageSprite
			.createSprite("levels/hud/hud_heartHalf.png");
	public static final ImageSprite HEALTH_EMPTY = ImageSprite
			.createSprite("levels/hud/hud_heartEmpty.png");

	
	public static void setDefaultMazubSprite(Sprite sprite) {
		DEFAULT_MAZUB_SPRITE = sprite;
	}
	
	public static Sprite DEFAULT_MAZUB_SPRITE = createSprite("levels/player/p1_front.png")
			.resizeTo(50, 100);
	
	public static void setDefaultPlantSprite(Sprite sprite) {
		DEFAULT_PLANT_SPRITE = sprite;
	}
	
	public static Sprite DEFAULT_PLANT_SPRITE = PLANT_SPRITE_LEFT.resizeTo(40, 30);
	
	public static void setDefaultSharkSprite(Sprite sprite) {
		DEFAULT_SHARK_SPRITE = sprite;
	}
	

	public static Sprite DEFAULT_SHARK_SPRITE = SHARK_SPRITE_REST.resizeTo(66, 42);
	
	public static void setDefaultSlimeSprite(Sprite sprite) {
		DEFAULT_SLIME_SPRITE = sprite;
	}
	
	public static Sprite DEFAULT_SLIME_SPRITE = SLIME_SPRITE_LEFT.resizeTo(50, 28);


}
