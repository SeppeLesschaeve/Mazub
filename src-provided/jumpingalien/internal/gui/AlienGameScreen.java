package jumpingalien.internal.gui;

import java.awt.Color;

import jumpingalien.internal.game.IActionHandler;
import jumpingalien.internal.game.JumpingAlienGame;
import jumpingalien.internal.gui.painters.*;
import ogp.framework.gui.InputMode;
import ogp.framework.gui.MessagePainter;
import ogp.framework.gui.Screen;
import ogp.framework.gui.SolidBackgroundPainter;
import ogp.framework.gui.camera.Camera;
import ogp.framework.gui.camera.CameraScreen;
import ogp.framework.gui.camera.SimpleCamera;

public class AlienGameScreen
		extends CameraScreen<JumpingAlienGame, JumpingAlienGUI> {


	private Camera mainCamera, zoomCamera;

	public AlienGameScreen(AlienScreenPanel panel,
			JumpingAlienGUI gui,
			Screen<JumpingAlienGame, JumpingAlienGUI> previous) {
		super(panel, gui, previous);
	}

	public JumpingAlienGUIOptions getOptions() {
		return getGUI().getGUIOptions();
	}

	public IActionHandler getActionHandler() {
		return getGame().getActionHandler();
	}
	
	@Override
	public void screenStarted() {
		super.screenStarted();
		getGame().start();
	}

	@Override
	public void screenStopped() {
		super.screenStopped();
		getGUI().exit();
	}
	
	@Override
	protected void setupCameras() {
		if (!getOptions().getDebugShowEntireWorld()) {
			setupDefaultCamera();
		} else {
			setupOverviewCamera();
		}

		if (getOptions().getDebugShowPixels()) {
			int zoom = 20;
			int zoomCameraHeight = 250;
			int zoomCameraWidth = 500;
			zoomCamera = new Camera(new Camera.Rectangle(0, 0, zoomCameraWidth
					/ zoom, zoomCameraHeight / zoom), new Camera.Rectangle(
					getScreenWidth() - zoomCameraWidth, getScreenHeight()
							- zoomCameraHeight, zoomCameraWidth,
					zoomCameraHeight));
			zoomCamera.showBorder(true);
			addCamera(zoomCamera);
		}
	}

	private void setupOverviewCamera() {
		int[] size = getGame().getWorldSize();
		double ratioW = (double) getScreenWidth() / size[0];
		double ratioH = (double) getScreenHeight() / size[1];
		double ratio = Math.min(ratioW, ratioH);

		int scaledScreenWidth = (int) (ratio / ratioW * getScreenWidth());
		int scaledScreenHeight = (int) (ratio / ratioH * getScreenHeight());
		mainCamera = new Camera(new Camera.Rectangle(0, 0, size[0], size[1]),
				new Camera.Rectangle(
						(getScreenWidth() - scaledScreenWidth) / 2,
						(getScreenHeight() - scaledScreenHeight) / 2,
						scaledScreenWidth, scaledScreenHeight));
		addCamera(mainCamera);
	}

	private void setupDefaultCamera() {
		mainCamera = new SimpleCamera(0, 0, getScreenWidth(), getScreenHeight());
		addCamera(mainCamera);
	}

	@Override
	public Camera getMainCamera() {
		return mainCamera;
	}

	@Override
	protected void setupPainters() {
		addPainter(new SolidBackgroundPainter(Color.BLACK, this));

		addPainter(new TilePainter(this, getGame().getMap(), getGame()
				.getWorldInfoProvider()));

		if (getOptions().getDebugShowInfo()) {
			addPainter(new DebugInfoPainter(this));
		}

		if (getOptions().getDebugShowEntireWorld()) {
			addPainter(new VisibleWindowPainter(this));
		}

		if (getOptions().getDebugShowAxes()) {
			addPainter(new AxesPainter(this));
		}

		if (getOptions().getDebugShowPixels()) {
			addPainter(new PixelPainter(this));
		}

		if (getOptions().getDebugShowHistory()) {
			addPainter(new HistoryPainter(this));
		}

		addPainter(new GameObjectPainter(this,
				getGame().getAlienInfoProvider(), getGame()
						.getObjectInfoProvider()));

		addPainter(new PlayerPainter(this));

		addPainter(new WorldBorderPainter(this));

		addPainter(new HealthPainter(this, getGame().getAlienInfoProvider()));

		addPainter(new MessagePainter<>(this,
				getGame()::getCurrentMessage));

		addPainter(new GameOverPainter(this, getGame().getWorldInfoProvider()));
	}

	@Override
	public void updateState(double dt) {
		positionMainCamera();
		positionZoomCamera();

	}

	private void positionZoomCamera() {
		if (zoomCamera != null) {
			getGame()
					.getAlienInfoProvider()
					.getAlienXYPixel()
					.ifPresent(
							position -> zoomCamera.moveToWorldLocation(
									position[0] - 5, position[1] - 5));
		}
	}

	private void positionMainCamera() {
		if (!getOptions().getDebugShowEntireWorld()) {
			getGame()
					.getWorldInfoProvider()
					.getVisibleWindow()
					.ifPresent(
							activeRegion -> {
								if (mainCamera != null) {
									mainCamera.moveToWorldLocation(
											activeRegion[0], activeRegion[1]);
								}

							});
		}
	}

	@Override
	protected InputMode<JumpingAlienGame, JumpingAlienGUI> createDefaultInputMode() {
		return new AlienInputMode(this, null);
	}


}
