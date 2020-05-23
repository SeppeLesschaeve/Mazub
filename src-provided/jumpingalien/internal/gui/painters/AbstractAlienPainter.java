package jumpingalien.internal.gui.painters;

import jumpingalien.internal.game.JumpingAlienGame;
import jumpingalien.internal.gui.AlienGameScreen;
import jumpingalien.internal.gui.JumpingAlienGUIOptions;
import ogp.framework.gui.Painter;
import ogp.framework.gui.camera.Camera;

public abstract class AbstractAlienPainter
		extends Painter<AlienGameScreen> {

	private final Camera mainCamera;
	private final JumpingAlienGame game;

	public AbstractAlienPainter(AlienGameScreen screen) {
		super(screen);
		this.mainCamera = screen.getMainCamera();
		this.game = screen.getGame();
	}

	protected JumpingAlienGUIOptions getOptions() {
		return getScreen().getOptions();
	}

	protected Camera getMainCamera() {
		return mainCamera;
	}

	protected JumpingAlienGame getGame() {
		return game;
	}

	protected int getScreenWidth() {
		return getScreen().getScreenWidth();
	}

	protected int getScreenHeight() {
		return getScreen().getScreenHeight();
	}

}