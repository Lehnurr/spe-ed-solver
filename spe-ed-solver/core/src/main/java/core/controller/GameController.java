package core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import solver.SolverType;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.logging.ApplicationLogger;

/**
 * {@link GameController} to control multiple {@link SolverController}.
 */
public class GameController {

	private final List<SolverType> solverTypes;

	private final Map<Integer, SolverController> solverController;

	private final boolean viewerEnabled;

	/**
	 * A Controller to Control multiple {@link SolverController}.
	 * 
	 * @param viewerEnabled true if the viewer should be enabled for the
	 *                      {@link SolverController solvers}
	 * @param solverTypes   {@link List} of {@link SolverType} of the
	 *                      {@link SolverController solvers} participating
	 */
	public GameController(final boolean viewerEnabled, final List<SolverType> solverTypes) {
		this.solverTypes = solverTypes;
		this.solverController = new HashMap<>();
		this.viewerEnabled = viewerEnabled;
	}

	/**
	 * Sends the new {@link GameStep} to the {@link IPlayer} self of the
	 * {@link GameStep}.
	 * 
	 * @param gameStep the new {@link GameStep}
	 * @return {@link PlayerAction} to send back to the server
	 */
	public PlayerAction handleGameStep(final GameStep gameStep) {
		final int playerId = gameStep.getSelf().getPlayerId();

		solverController.computeIfAbsent(playerId, key -> {
			final SolverType solverType = solverTypes.remove(0);
			ApplicationLogger
					.logInformation(String.format("Registered solver of type %s and id %d.", solverType.name(), key));
			return new SolverController(viewerEnabled, solverType);
		});

		return this.solverController.get(playerId).calculateAction(gameStep);
	}
}
