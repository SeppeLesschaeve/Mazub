package jumpingalien.internal.gui;

import ogp.framework.gui.GUIOptions;

public interface JumpingAlienGUIOptions extends GUIOptions {

	@Override
	public default boolean isFullScreenEnabled() {
		return false;
	}
	
	public abstract boolean getDebugShowHistory();
	public abstract void setDebugShowHistory(boolean value);

	public abstract boolean getDebugShowPixels();
	public abstract void setDebugShowPixels(boolean value);

	public abstract boolean getDebugShowObjectLocationAndSize();
	public abstract void setDebugShowObjectLocationAndSize(boolean value);

	public abstract boolean getDebugShowAxes();
	public abstract void setDebugShowAxes(boolean value);

	public abstract boolean getDebugShowInfo();
	public abstract void setDebugShowInfo(boolean value);
	
	public abstract boolean getDebugShowEntireWorld();
	public abstract void setDebugShowEntireWorld(boolean value);

	public abstract boolean getDebugShowTileTypes();
	public abstract void setDebugShowTileTypes(boolean debugShowTileTypes);

	public abstract boolean getDebugShowTileGridlines();
	public abstract void setDebugShowTileGridlines(boolean debugShowTileGridlines);

	public abstract boolean getDebugShowAlienOverlappingTiles();
	public abstract void setDebugShowAlienOverlappingTiles(
			boolean debugShowAlienOverlappingTiles);

	public abstract boolean getDebugShowObjectString();
	public abstract void setDebugShowObjectString(boolean value);
}