package jumpingalien.internal.gui;

import jumpingalien.internal.game.JumpingAlienGame;
import ogp.framework.gui.ScreenPanel;

@SuppressWarnings("serial")
public abstract class AlienScreenPanel extends
		ScreenPanel<JumpingAlienGame> {

	public AlienScreenPanel() {
		super();
	}

	@Override
	public void initialize(JumpingAlienGame game) {
		game.setVisibleScreenSize(getWidth(), getHeight());
		super.initialize(game);
	}
}
