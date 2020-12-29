package core.modes;

import java.util.ArrayList;
import java.util.List;

import core.player.GameController;
import player.PlayerType;
import simulation.Game;
import utility.game.board.Board;
import utility.logging.ApplicationLogger;
import utility.logging.GameLogger;

/**
 * {@link Runnable} for the live play mode to play spe_ed in an offline
 * simulation of the game.
 */
public class SimulationMode implements Runnable {

	private final int height;
	private final int width;

	private final List<PlayerType> playerTypes;
	private final boolean viewerEnabled;

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed offline
	 * in an simulation. The constructor is used to set starting parameters.
	 * 
	 * @param height        height of the {@link Board}
	 * @param width         width of the {@link Board}
	 * @param playerTypes   {@link List} of {@link PlayerType} of the players
	 *                      participating
	 * @param viewerEnabled true if the viewer should be enabled for the players
	 * 
	 */
	public SimulationMode(final int height, final int width, final List<PlayerType> playerTypes,
			final boolean viewerEnabled) {
		this.height = height;
		this.width = width;
		this.playerTypes = playerTypes;
		this.viewerEnabled = viewerEnabled;
	}

	@Override
	public void run() {

		var game = new Game(height, width, playerTypes.size());
		var gameController = new GameController(viewerEnabled, new ArrayList<>(playerTypes));

		ApplicationLogger.logInformation("RUNNING SIMULATED");

		// Start the Simulation and get the initial GameSteps for each Player
		var gameSteps = game.startSimulation();

		// Log the initial GameStep
		GameLogger.logGameStep(gameSteps.get(0));

		// Iterate through all GameSteps
		for (int i = 0; i < gameSteps.size(); i++) {
			// Determine the current GameStep for a specific Player
			var gameStep = gameSteps.get(i);

			// Send the GameStep and receive the Players chosen Action
			var action = gameController.handleGameStep(gameStep);

			// Log the Action
			GameLogger.logPlayerAction(gameStep.getSelf(), action);

			if (gameStep.isRunning()) {
				// Send the Action to the Simulation and get the new GameSteps (if every
				// alive Player has already sent an Action)
				var nextGameSteps = game.setAction(gameStep.getSelf().getPlayerId(), action);

				if (!nextGameSteps.isEmpty()) {
					// Log the new Gamestep
					GameLogger.logGameStep(nextGameSteps.get(0));
				}

				// Add all new GameSteps to the gameSteps-List
				gameSteps.addAll(nextGameSteps);
			}
		}

		ApplicationLogger.logInformation("FINISHED SIMULATED");
	}

}
