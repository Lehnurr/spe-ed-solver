package core.controller;

import java.util.ArrayList;
import java.util.List;

import solver.ISpeedSolver;
import solver.SolverType;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.logging.GameLogger;
import visualisation.IViewer;
import visualisation.InactiveViewer;
import visualisation.Viewer;

/**
 * {@link SolverController} to control one {@link ISpeedSolver}
 */
public class SolverController {

	private final ISpeedSolver solver;

	private final IViewer viewer;

	/**
	 * A Controller to control one {@link ISpeedSolver}.
	 * 
	 * @param viewerEnabled  true if the viewer should be enabled for the
	 *                       {@link ISpeedSolver solver}
	 * @param solverType     the type of the controlled {@link ISpeedSolver solver}
	 * @param maxThreadCount specifies the maximum number of concurrent threads to
	 *                       use
	 */
	public SolverController(final boolean viewerEnabled, final SolverType solverType, final int maxThreadCount) {
		this.solver = solverType.newInstance(maxThreadCount);

		if (viewerEnabled) {
			this.viewer = new Viewer(solverType.name());
		} else {
			this.viewer = new InactiveViewer();
		}

	}

	/**
	 * Sends the new {@link GameStep} to the {@link ISpeedSolver} and returns the
	 * chosen {@link PlayerAction}.
	 * 
	 * @param gameStep the current {@link GameStep}
	 * @return the {@link PlayerAction} chosen by the {@link ISpeedSolver solver}
	 */
	public PlayerAction calculateAction(GameStep gameStep) {

		GameLogger.logGameStep(gameStep);

		final long availableMilliseconds = gameStep.getDeadline().getRemainingMilliseconds();
		final float availableSeconds = availableMilliseconds / 1000f;

		final List<ContextualFloatMatrix> boardRatings = new ArrayList<>();

		final PlayerAction action = solver.calculateAction(gameStep, boardRatings::add);

		final long requiredMilliseconds = availableMilliseconds - gameStep.getDeadline().getRemainingMilliseconds();
		final float requiredSeconds = requiredMilliseconds / 1000f;

		viewer.commitRound(gameStep.getSelf().getPlayerId(), availableSeconds, action, requiredSeconds,
				gameStep.getBoard(), boardRatings);

		GameLogger.logPlayerAction(gameStep.getSelf(), action, requiredSeconds, availableSeconds);

		return action;
	}
}
