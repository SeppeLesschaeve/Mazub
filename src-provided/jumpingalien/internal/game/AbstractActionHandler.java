package jumpingalien.internal.game;

import java.util.function.Consumer;

import jumpingalien.facade.IFacade;
import jumpingalien.internal.game.IActionHandler;
import jumpingalien.model.Mazub;
import ogp.framework.command.Command;

public abstract class AbstractActionHandler implements
		IActionHandler {
	/**
	 * 
	 */
	private final JumpingAlienGame game;

	protected AbstractActionHandler(JumpingAlienGame game) {
		this.game = game;
	}

	protected JumpingAlienGame getGame() {
		return game;
	}
	
	protected Mazub getAlien() {
		return game.getAlien();
	}

	protected IFacade getFacade() {
		return game.getFacade();
	}

	protected void addAlienCommand(String name, Consumer<Mazub> action) {
		game.addCommand(new Command(name) {
			@Override
			public void execute() {
				game.catchErrorAction(() -> action.accept(getAlien()));
			}
		});
	}
}
