package jumpingalien.internal.gui;

import java.awt.Dimension;

import jumpingalien.internal.game.JumpingAlienGame;
import ogp.framework.gui.GUI;
import ogp.framework.gui.Screen;
import ogp.framework.gui.ScreenPanel;

public class JumpingAlienGUI extends
		GUI<JumpingAlienGame> {

	public JumpingAlienGUI(JumpingAlienGame game, JumpingAlienGUIOptions options) {
		super(game, options);
	}

	@Override
	public JumpingAlienGUIOptions getGUIOptions() {
		return (JumpingAlienGUIOptions) super.getGUIOptions();
	}

	@Override
	protected String getTitle() {
		return "Jumping Alien (Part 3)";
	}

	@Override
	protected Dimension getDefaultSize() {
		return new Dimension(1024, 768);
	}

	@SuppressWarnings("serial")
	@Override
	protected ScreenPanel<JumpingAlienGame> createScreenPanel() {
		return new AlienScreenPanel() {
			@Override
			protected Screen<JumpingAlienGame, ? extends GUI<JumpingAlienGame>> createInitialScreen() {
				return new MainMenu(this, JumpingAlienGUI.this, null);
			}
		};
	}

}
