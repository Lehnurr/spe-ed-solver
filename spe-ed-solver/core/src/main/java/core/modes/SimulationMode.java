package core.modes;

import java.util.ArrayList;
import java.util.List;

import core.controller.GameController;
import solver.ISpeedSolver;
import solver.SolverType;
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

	private final List<SolverType> solverTypes;
	private final boolean viewerEnabled;
	private final int maxThreadCount;

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed offline
	 * in an simulation. The constructor is used to set starting parameters.
	 * 
	 * @param height         height of the {@link Board}
	 * @param width          width of the {@link Board}
	 * @param solverTypes    {@link List} of {@link SolverType} of the
	 *                       {@link ISpeedSolver solvers} participating
	 * @param viewerEnabled  true if the viewer should be enabled for the
	 *                       {@link ISpeedSolver solvers}
	 * @param maxThreadCount specifies the maximum number of concurrent threads to
	 *                       use
	 * 
	 */
	public SimulationMode(final int height, final int width, final List<SolverType> solverTypes,
			final boolean viewerEnabled, int maxThreadCount) {
		this.height = height;
		this.width = width;
		this.solverTypes = solverTypes;
		this.viewerEnabled = viewerEnabled;
		this.maxThreadCount = maxThreadCount;
	}

	@Override
	public void run() {

		final Game game = new Game(height, width, solverTypes.size());
		final GameController gameController = new GameController(viewerEnabled, new ArrayList<>(solverTypes),
				maxThreadCount);

		ApplicationLogger.logInformation("RUNNING SIMULATED");

		final List<GameStep> gameSteps = game.startSimulation();

		for (int i = 0; i < gameSteps.size(); i++) {
			final GameStep gameStep = gameSteps.get(i);

			final PlayerAction action = gameController.handleGameStep(gameStep);

			if (gameStep.isRunning()) {
				final List<GameStep> nextGameSteps = game.setAction(gameStep.getSelf().getPlayerId(), action);
				gameSteps.addAll(nextGameSteps);
			}
		}

		ApplicationLogger.logInformation("FINISHED SIMULATED");
	}

}
