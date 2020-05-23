package jumpingalien;

import jumpingalien.facade.Facade;
import jumpingalien.internal.gui.JumpingAlienGUI;
import jumpingalien.internal.game.JumpingAlienGame;
import jumpingalien.internal.JumpingAlienOptions;

public class JumpingAlien {

	public static void main(String[] args) {
		JumpingAlienOptions options = JumpingAlienOptions.parse(args);

		JumpingAlienGame game = new JumpingAlienGame(options, new Facade());

		new JumpingAlienGUI(game, options).start();
	}

}
