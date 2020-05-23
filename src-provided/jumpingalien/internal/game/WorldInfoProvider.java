package jumpingalien.internal.game;

import java.util.Optional;

import jumpingalien.internal.tmxfile.data.ImageTile.TileType;

public interface WorldInfoProvider {

	public Optional<int[]> getWorldSize();
	
	Optional<int[]> getVisibleWindow();

	int getTileLength();

	Optional<int[][]> getTilesIn(int left, int bottom, int right, int top);

	Optional<TileType> getGeologicalFeature(int bottomLeftX, int bottomLeftY);

	Optional<int[]> getBottomLeftPixelOfTile(int tileX, int tileY);

	Optional<Boolean> isGameOver();

	Optional<Boolean> didPlayerWin();

}
