package core.modes;

import java.util.ArrayList;
import java.util.List;

import core.controller.GameController;
import player.PlayerType;
import simulation.Game;
import utility.game.board.Board;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.logging.ApplicationLogger;

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

		final Game game = new Game(height, width, playerTypes.size());
		final GameController gameController = new GameController(viewerEnabled, new ArrayList<>(playerTypes));

		ApplicationLogger.logInformation("RUNNING SIMULATED");

		// Start the Simulation and get the initial GameSteps for each Player
		final List<GameStep> gameSteps = game.startSimulation();

		// Iterate through all GameSteps
		for (int i = 0; i < gameSteps.size(); i++) {
			// Determine the current GameStep for a specific Player
			final GameStep gameStep = gameSteps.get(i);

			// Send the GameStep and receive the Players chosen Action
			final PlayerAction action = gameController.handleGameStep(gameStep);

			if (gameStep.isRunning()) {
				// Send the Action to the Simulation and get the new GameSteps (if every
				// alive Player has already sent an Action)
				final List<GameStep> nextGameSteps = game.setAction(gameStep.getSelf().getPlayerId(), action);

				// Add all new GameSteps to the gameSteps-List
				gameSteps.addAll(nextGameSteps);
			}
		}

		ApplicationLogger.logInformation("FINISHED SIMULATED");
	}

}
