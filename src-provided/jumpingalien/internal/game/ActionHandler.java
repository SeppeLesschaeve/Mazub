package jumpingalien.internal.game;

public class ActionHandler extends AbstractActionHandler {

	public ActionHandler(JumpingAlienGame game) {
		super(game);
	}

	@Override
	public void startJump() {
		addAlienCommand("startJump", getFacade()::startJump);
	}

	@Override
	public void endJump() {
		addAlienCommand("endJump", (alien) -> {
			if (getFacade().isJumping(alien)) {
				getFacade().endJump(alien);
			}
		});
	}

	@Override
	public void startMoveLeft() {
		addAlienCommand("startMoveLeft", getFacade()::startMoveLeft);
	}

	@Override
	public void startMoveRight() {
		addAlienCommand("startMoveRight", getFacade()::startMoveRight);
	}

	@Override
	public void endMoveLeft() {
		addAlienCommand("endMoveLeft", (alien) -> {
			if (getFacade().isMoving(alien)) {
				getFacade().endMove(alien);
			}
		});
	}

	@Override
	public void endMoveRight() {
		addAlienCommand("endMoveRight", (alien) -> {
			if (getFacade().isMoving(alien)) {
				getFacade().endMove(alien);
			}
		});
	}

	@Override
	public void startDuck() {
		addAlienCommand("startDuck", getFacade()::startDuck);
	}

	@Override
	public void endDuck() {
		addAlienCommand("endDuck", (alien) -> {
			if (getFacade().isDucking(alien)) {
				getFacade().endDuck(alien);
			}
		});
	}
}