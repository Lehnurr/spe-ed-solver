package core.modes;

import core.player.GameController;
import simulation.Game;

/**
 * {@link Runnable} for the live play mode to play spe_ed in an offline
 * simulation of the game.
 */
public class SimulationMode implements Runnable {

	private final Game game;
	private final GameController gameController;

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed offline
	 * in an simulation. The constructor is used to set starting parameters.
	 * 
	 * @param height      Height of the Board
	 * @param width       Widht of the Board
	 * @param playerCount Number of Simulated Players
	 * 
	 */
	public SimulationMode(int height, int width, int playerCount) {
		game = new Game(height, width, playerCount);
		gameController = new GameController();
	}

	@Override
	public void run() {
		System.out.println("RUNNING SIMULATED");

		// Start the Simulation and get the initial GameSteps for each Player
		var gameSteps = game.startSimulation();

		// Iterate through all GameSteps
		for (int i = 0; i < gameSteps.size(); i++) {
			// Determine the current GameStep for a specific Player
			var gameStep = gameSteps.get(i);

			// Send the GameStep and receive the Players chosen Action
			var action = gameController.sendGameStep(gameStep);

			// Send the Action to the Simulation and get the new GameSteps (if every
			// alive Player has already sent an Action)
			var nextGameSteps = game.setAction(gameStep.getSelf().getPlayerId(), action);

			// Add all new GameSteps to the gameSteps-List
			gameSteps.addAll(nextGameSteps);
		}

		System.out.println("FINISHED SIMULATED");
	}

}
