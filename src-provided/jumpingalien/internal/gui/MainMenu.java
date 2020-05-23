package jumpingalien.internal.gui;

import jumpingalien.internal.game.JumpingAlienGame;
import jumpingalien.internal.gui.AlienScreenPanel;
import ogp.framework.gui.Screen;
import ogp.framework.gui.menu.MenuOption;
import ogp.framework.gui.menu.MenuScreen;

public class MainMenu
		extends MenuScreen<JumpingAlienGame, JumpingAlienGUI> {

	protected MainMenu(AlienScreenPanel panel, JumpingAlienGUI gui,
			Screen<JumpingAlienGame, JumpingAlienGUI> previous) {
		super(panel, gui, previous);
	}

	@Override
	protected AlienScreenPanel getPanel() {
		return (AlienScreenPanel) super.getPanel();
	}

	@Override
	protected void registerMenuOptions() {
		addOption(new MenuOption("Start game", this::startGame));
		addOption(new MenuOption(
				"Start game with helpful debug visualisations",
				this::startGameWithDebug));

		addOption(new MenuOption(() -> "Change world: " + getCurrentMap(),
				this::selectMap));

		addOption(new MenuOption("Set debug options", this::setDebugOptions));

		addOption(new MenuOption("Quit (Esc)", this::quit));
	}
	
	protected void startGame() {
		if (getGame().setMapFile(getCurrentMap())) {
			getPanel().switchToScreen(
					new AlienGameScreen(getPanel(), getGUI(), this));
		}
	}
	
	protected void setDebugOptions() {
		if (getGame().setMapFile(getCurrentMap())) {
			getPanel().switchToScreen(
					new DebugMenu(getPanel(), getGUI(), this));
		}
	}

	protected void quit() {
		getGUI().exit();
	}

	@Override
	public void screenStarted() {
		super.screenStarted();
		String[] maps = getMaps();
		for (int i = 0; i < maps.length; i++) {
			if (maps[i].equals(getGame().getMapFile())) {
				currentIndex = i;
				break;
			}
		}
	}

	private int currentIndex = 0;
	private String[] mapFilenames;

	private String[] getMaps() {
		if (mapFilenames == null) {
			mapFilenames = getGame().getAvailableMaps();
			if (mapFilenames == null || mapFilenames.length == 0) {
				throw new IllegalStateException("No maps found.");
			}
		}
		return mapFilenames;
	}

	private String getCurrentMap() {
		return getMaps()[currentIndex];
	}

	private void selectMap() {
		currentIndex = (currentIndex + 1) % getMaps().length;
	}

	private void startGameWithDebug() {
		if (getGame().setMapFile(getCurrentMap())) {
			getGUI().getGUIOptions().setDebugShowInfo(true);
			getGUI().getGUIOptions().setDebugShowObjectLocationAndSize(true);
			getGUI().getGUIOptions().setDebugShowAlienOverlappingTiles(true);
			getGUI().getGUIOptions().setDebugShowObjectString(true);
			getGUI().getGUIOptions().setDebugShowTileGridlines(true);

			getPanel().switchToScreen(
					new AlienGameScreen(getPanel(), getGUI(), this));
		}
	}
}
