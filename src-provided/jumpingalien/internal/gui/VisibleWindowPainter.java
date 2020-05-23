package jumpingalien.internal.gui;

import java.awt.Color;

import jumpingalien.internal.gui.painters.AbstractAlienPainter;

public class VisibleWindowPainter extends AbstractAlienPainter {

	public VisibleWindowPainter(AlienGameScreen screen) {
		super(screen);
	}

	@Override
	public void paintInWorld(java.awt.Graphics2D g) {
		getGame().getWorldInfoProvider()
				.getVisibleWindow().ifPresent(
						activeRegion -> {
							g.setColor(Color.BLACK);
							g.setXORMode(Color.WHITE);
							g.drawRect(activeRegion[0],
									activeRegion[1],
									activeRegion[2]
											- activeRegion[0] + 1,
									activeRegion[3]
											- activeRegion[1] + 1);
							g.setPaintMode();
						});
	};
}
