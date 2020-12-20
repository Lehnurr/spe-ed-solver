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
		System.out.printf(" %d Players on a %d*%d Board%n", 1, 1, 1);

		// Init Simulation and send the Initial GameStep to each PlayerController
		game.startSimulation().forEach(gameController::sendGameStep);
		// TODO: process the returned player actions...
	}

}
