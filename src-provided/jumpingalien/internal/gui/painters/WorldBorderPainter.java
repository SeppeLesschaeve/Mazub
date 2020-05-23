package jumpingalien.internal.gui.painters;

import java.awt.Color;
import java.awt.Graphics2D;

import jumpingalien.internal.gui.AlienGameScreen;

public class WorldBorderPainter extends
		AbstractAlienPainter {

	public WorldBorderPainter(AlienGameScreen screen) {
		super(screen);
	}

	@Override
	public void paintInWorld(Graphics2D g) {
		g.setColor(Color.YELLOW);
		getGame().getWorldInfoProvider().getWorldSize()
				.ifPresent(wh -> g.drawRect(0, 0, wh[0], wh[1]));
	}
}
