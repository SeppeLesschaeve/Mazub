package jumpingalien.internal.gui;

import java.util.function.BiConsumer;
import java.util.function.Function;

import jumpingalien.internal.game.JumpingAlienGame;
import ogp.framework.gui.Screen;
import ogp.framework.gui.menu.MenuOption;
import ogp.framework.gui.menu.MenuScreen;

public class DebugMenu extends
		MenuScreen<JumpingAlienGame, JumpingAlienGUI> {

	public DebugMenu(AlienScreenPanel panel,
			JumpingAlienGUI gui,
			Screen<JumpingAlienGame, JumpingAlienGUI> previous) {
		super(panel, gui, previous);
	}

	@Override
	protected AlienScreenPanel getPanel() {
		return (AlienScreenPanel) super.getPanel();
	}

	@Override
	protected void registerMenuOptions() {

		addDebugOption("Show info", JumpingAlienGUIOptions::getDebugShowInfo,
				JumpingAlienGUIOptions::setDebugShowInfo);
		addDebugOption("Show game object location and size",
				JumpingAlienGUIOptions::getDebugShowObjectLocationAndSize,
				JumpingAlienGUIOptions::setDebugShowObjectLocationAndSize);
		addDebugOption("Show game object toString()",
				JumpingAlienGUIOptions::getDebugShowObjectString,
				JumpingAlienGUIOptions::setDebugShowObjectString);
		addDebugOption("Show axes", JumpingAlienGUIOptions::getDebugShowAxes,
				JumpingAlienGUIOptions::setDebugShowAxes);
		addDebugOption("Show location history",
				JumpingAlienGUIOptions::getDebugShowHistory,
				JumpingAlienGUIOptions::setDebugShowHistory);
		addDebugOption("Color overlapping tiles",
				JumpingAlienGUIOptions::getDebugShowAlienOverlappingTiles,
				JumpingAlienGUIOptions::setDebugShowAlienOverlappingTiles);
		addDebugOption("Color-code tile types",
				JumpingAlienGUIOptions::getDebugShowTileTypes,
				JumpingAlienGUIOptions::setDebugShowTileTypes);
		addDebugOption("Show tile gridlines",
				JumpingAlienGUIOptions::getDebugShowTileGridlines,
				JumpingAlienGUIOptions::setDebugShowTileGridlines);
		addDebugOption("Show entire world on screen",
				JumpingAlienGUIOptions::getDebugShowEntireWorld,
				JumpingAlienGUIOptions::setDebugShowEntireWorld);

		addOption(new MenuOption("Start game", this::startGame));

		addOption(new MenuOption("Return (Esc)", this::close));

	}

	private void addDebugOption(String name,
			Function<JumpingAlienGUIOptions, Boolean> optionGetter,
			BiConsumer<JumpingAlienGUIOptions, Boolean> optionSetter) {
		addDebugOption(name, optionGetter, optionSetter, null);
	}

	private void addDebugOption(String name,
			Function<JumpingAlienGUIOptions, Boolean> optionGetter,
			BiConsumer<JumpingAlienGUIOptions, Boolean> optionSetter, String description) {
		JumpingAlienGUIOptions options = getGUI().getGUIOptions();
		MenuOption debugOption = new MenuOption(() -> {
			String state = "Off";
			if (optionGetter.apply(options)) {
				state = "On";
			}
			return String.format("%s: %s", name, state);
		}, () -> {
			optionSetter.accept(options, !optionGetter.apply(options));
		}, description);
		debugOption.setScale(0.7f);
		addOption(debugOption);
	}

	private void startGame() {
		getPanel().switchToScreen(
				new AlienGameScreen(getPanel(), getGUI(), this));
	}
}