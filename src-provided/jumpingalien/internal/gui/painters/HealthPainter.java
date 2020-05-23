package jumpingalien.internal.gui.painters;

import java.awt.Graphics2D;

import jumpingalien.internal.gui.AlienGameScreen;
import jumpingalien.internal.gui.painters.AbstractAlienPainter;
import jumpingalien.internal.gui.sprites.ImageSprite;
import jumpingalien.internal.gui.sprites.JumpingAlienSprites;
import jumpingalien.internal.game.AlienInfoProvider;

public class HealthPainter extends AbstractAlienPainter {

	private static final int H_MARGIN = 30;
	private static final int WIDTH = 30;
	private static final int V_MARGIN = 30;

	private final AlienInfoProvider<?> alienInfoProvider;

	public HealthPainter(AlienGameScreen screen,
			AlienInfoProvider<?> alienInfoProvider) {
		super(screen);
		this.alienInfoProvider = alienInfoProvider;
	}

	@Override
	public void paintScreenPost(Graphics2D g) {
		alienInfoProvider.getAlienHealth().ifPresent(
				health -> paintHealth(g, health));
	}

	private void paintHealth(Graphics2D g, Integer health) {
		int count = 0;
		int origHealth = health;
		if (origHealth > 0) {
			while (health > 0) {
				count += 1;
				int digit = health % 10;
				health = health / 10;
				ImageSprite image = JumpingAlienSprites.NUMBER_SPRITES[digit];
				g.drawImage(image.getImage(), getScreenWidth() - H_MARGIN
						- count * WIDTH, V_MARGIN, null);
			}
		} else {
			count += 1;
			ImageSprite image = JumpingAlienSprites.NUMBER_SPRITES[0];
			g.drawImage(image.getImage(), getScreenWidth() - H_MARGIN - count
					* WIDTH, V_MARGIN, null);
		}

		if (origHealth >= 66) {
			g.drawImage(JumpingAlienSprites.HEALTH_FULL.getImage(), getScreenWidth()
					- H_MARGIN - count * WIDTH
					- JumpingAlienSprites.HEALTH_FULL.getImage().getWidth(), V_MARGIN,
					null);
		} else if (origHealth >= 33) {
			g.drawImage(JumpingAlienSprites.HEALTH_HALF.getImage(), getScreenWidth()
					- H_MARGIN - count * WIDTH
					- JumpingAlienSprites.HEALTH_HALF.getImage().getWidth(), V_MARGIN,
					null);
		} else {
			g.drawImage(JumpingAlienSprites.HEALTH_EMPTY.getImage(), getScreenWidth()
					- H_MARGIN - count * WIDTH
					- JumpingAlienSprites.HEALTH_EMPTY.getImage().getWidth(), V_MARGIN,
					null);
		}
	};
}