package jumpingalien.internal.gui.painters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Optional;

import jumpingalien.internal.gui.AlienGUIUtils;
import jumpingalien.internal.gui.AlienGameScreen;
import jumpingalien.internal.gui.painters.AbstractAlienPainter;
import jumpingalien.internal.tmxfile.data.ImageTile;
import jumpingalien.internal.tmxfile.data.Layer;
import jumpingalien.internal.tmxfile.data.Map;
import jumpingalien.internal.tmxfile.data.ImageTile.TileType;
import jumpingalien.internal.game.WorldInfoProvider;

public class TilePainter extends AbstractAlienPainter {

	private static final Color OVERLAPPING_TILE_COLOR = new Color(255, 64, 64,
			64);
	private final WorldInfoProvider worldInfoProvider;
	private final int tileSize;
	private final Map map;

	public TilePainter(AlienGameScreen screen, Map map,
		      WorldInfoProvider worldInfoProvider) {
		super(screen);
		this.map = map;
		this.worldInfoProvider = worldInfoProvider;
		this.tileSize = worldInfoProvider.getTileLength();
	}

	@Override
	public void paintInWorld(Graphics2D g) {
		int[] worldSize = worldInfoProvider.getWorldSize().get();

		g.setColor(getBackgroundColor());
		g.fillRect(0, 0, worldSize[0], worldSize[1]);

		Optional<int[][]> tileCoordinatesToDrawOpt;
		if (getOptions().getDebugShowEntireWorld()) {
			tileCoordinatesToDrawOpt = worldInfoProvider.getTilesIn(0, 0,
					worldSize[0] - 1, worldSize[1] - 1);
		} else {
			Optional<int[]> visibleWindowOpt = worldInfoProvider
					.getVisibleWindow();

			if (!visibleWindowOpt.isPresent()) {
				return;
			}
			int[] visibleWindow = visibleWindowOpt.get();

			tileCoordinatesToDrawOpt = worldInfoProvider.getTilesIn(
					visibleWindow[0], visibleWindow[1], visibleWindow[2],
					visibleWindow[3]);
		}
		if (!tileCoordinatesToDrawOpt.isPresent()) {
			return;
		}
		int[][] tileCoordinatesToDraw = tileCoordinatesToDrawOpt.get();

		for (int[] tileXY : tileCoordinatesToDraw) {
			int tileX = tileXY[0];
			int tileY = tileXY[1];

			paintTile(g, tileX, tileY);
		}

		if (getOptions().getDebugShowAlienOverlappingTiles()) {

			Optional<int[]> alienXYOpt = getGame().getAlienInfoProvider()
					.getAlienXYPixel();
			Optional<int[]> alienSizeOpt = getGame().getAlienInfoProvider()
					.getAlienSize();
			if (alienXYOpt.isPresent() && alienSizeOpt.isPresent()) {
				int[] alienXY = alienXYOpt.get();
				int[] alienSize = alienSizeOpt.get();

				g.setColor(OVERLAPPING_TILE_COLOR);
				worldInfoProvider.getTilesIn(alienXY[0], alienXY[1],
						alienXY[0] + alienSize[0] - 1,
						alienXY[1] + alienSize[1] - 1).ifPresent(
						overlappingTileCoords -> {
							for (int[] tileCoord : overlappingTileCoords) {
								worldInfoProvider.getBottomLeftPixelOfTile(
										tileCoord[0], tileCoord[1]).ifPresent(
										overlappingXY ->

										g.fillRect(overlappingXY[0],
												overlappingXY[1], tileSize,
												tileSize));
							}
						});
			}
		}
	}

	private Color getBackgroundColor() {
		String bg = map.getBackground();
		if (bg != null) {
			return Color.decode(bg);
		} else {
			return Color.black;
		}
	}

	private void paintTile(Graphics2D g, int tileX, int tileY) {
		Optional<int[]> bottomLeftOpt = worldInfoProvider
				.getBottomLeftPixelOfTile(tileX, tileY);
		if (!bottomLeftOpt.isPresent()) {
			// TODO: do something else?
			return;
		}
		int[] bottomLeftXY = bottomLeftOpt.get();

		int tileSize = worldInfoProvider.getTileLength();

		for (Layer layer : map.getLayers()) {
			if (map.isValidTileCoordinate(tileX, tileY)) {
				ImageTile tile = layer.getTile(tileX, tileY);
				if (tile != null) {
					AlienGUIUtils.drawImageInWorld(g, tile.getImage(),
							bottomLeftXY[0], bottomLeftXY[1], false);
				}
			}
		}

		if (getOptions().getDebugShowTileTypes()) {
			Optional<TileType> optType = worldInfoProvider
					.getGeologicalFeature(bottomLeftXY[0], bottomLeftXY[1]);
			if (optType.isPresent()) {
				TileType type = optType.get();
				int alpha = 200;
				switch (type) {
				case AIR:
					g.setColor(new Color(0, 255, 255, alpha));
					break;
				case SOLID:
					g.setColor(new Color(255, 128, 128, alpha));
					break;
				case MAGMA:
					g.setColor(new Color(255, 255, 128, alpha));
					break;
				case WATER:
					g.setColor(new Color(0, 128, 255, alpha));
					break;
				default:
					g.setColor(new Color(128, 128, 128, alpha));
					break;
				}
				g.fillRect(bottomLeftXY[0], bottomLeftXY[1], tileSize, tileSize);
			}
		}

		if (getOptions().getDebugShowTileGridlines()) {
			g.setColor(Color.WHITE);
			g.setXORMode(Color.BLACK);
			int offset = 5;
			g.drawLine(bottomLeftXY[0], bottomLeftXY[1], bottomLeftXY[0],
					bottomLeftXY[1] + tileSize);
			g.drawLine(bottomLeftXY[0], bottomLeftXY[1], bottomLeftXY[0]
					+ tileSize, bottomLeftXY[1]);

			g.setXORMode(Color.LIGHT_GRAY);
			g.drawLine(bottomLeftXY[0] + 2, bottomLeftXY[1] + 2,
					bottomLeftXY[0] + 2, bottomLeftXY[1] + tileSize - offset);
			g.drawLine(bottomLeftXY[0] + 2, bottomLeftXY[1] + 2,
					bottomLeftXY[0] + tileSize - offset, bottomLeftXY[1] + 2);
			g.setPaintMode();
		}
	}
}
