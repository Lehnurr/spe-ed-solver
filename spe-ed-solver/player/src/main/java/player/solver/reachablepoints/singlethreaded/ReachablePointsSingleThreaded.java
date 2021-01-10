package player.solver.reachablepoints.singlethreaded;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import player.analysis.ActionsRating;
import player.analysis.slowdown.SlowDown;
import player.solver.reachablepoints.IReachablePoints;
import player.solver.reachablepoints.RatedPredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.FloatMatrix;
import utility.logging.GameLogger;

/**
 * Class implementing the {@link IReachablePoints} interface. The reachable
 * points calculation is performed on a single thread (main thread). The
 * calculation is distributed to multiple
 * {@link GradualReachablePointsCalculation} objects which are repeatedly
 * alternated to distribute the resources equally.
 */
public class ReachablePointsSingleThreaded implements IReachablePoints {

	private static final int DEADLINE_MILLISECOND_BUFFER = 500;

	private Map<PlayerAction, GradualReachablePointsCalculation> calculations;

	private ActionsRating successRating;
	private ActionsRating cutOffRating;
	private ActionsRating slowDownRating;

	private FloatMatrix enemyProbabilitiesMatrix;
	private FloatMatrix enemyMinStepsMatrix;

	@Override
	public void performCalculation(final GameStep gameStep, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {
		reset();

		this.enemyProbabilitiesMatrix = probabilities;
		this.enemyMinStepsMatrix = minSteps;
		slowDownRating = SlowDown.getActionsRating(gameStep.getSelf(), gameStep.getBoard());

		initCalculations(gameStep.getSelf(), gameStep.getBoard(), probabilities, minSteps);
		executeCalculationLoop(gameStep.getDeadline());
		updateActionsRatings();
	}

	/**
	 * Resets the {@link GradualReachablePointsCalculation calculations} and
	 * {@link ActionsRating ratings}.
	 */
	private void reset() {
		calculations = new EnumMap<>(PlayerAction.class);
		successRating = new ActionsRating();
		cutOffRating = new ActionsRating();
	}

	/**
	 * Initializes the {@link GradualReachablePointsCalculation calculations} with
	 * the given start information.
	 * 
	 * @param self          {@link IPlayer} to test for further movement
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities probabilities of enemies as {@link FloatMatrix}
	 * @param minSteps      minimum steps of enemies as {@link FloatMatrix}
	 */
	private void initCalculations(final IPlayer self, final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {

		final RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(self);
		for (final PlayerAction action : PlayerAction.values()) {
			final RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, action, board,
					probabilities, minSteps);
			final GradualReachablePointsCalculation calculation = new GradualReachablePointsCalculation(board,
					probabilities, minSteps, nextPlayer);
			calculations.put(action, calculation);
		}
	}

	/**
	 * Executes the main calculation loop. Thereby multiple
	 * {@link GradualReachablePointsCalculation calculations} are repeatedly
	 * alternated until a {@link Deadline} is reached.
	 * 
	 * @param deadline {@link Deadline} for the
	 *                 {@link GradualReachablePointsCalculation calculations}
	 */
	private void executeCalculationLoop(final Deadline deadline) {

		boolean finished = false;

		while (deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER && !finished) {
			finished = true;
			for (final PlayerAction action : PlayerAction.values()) {
				final GradualReachablePointsCalculation calculation = calculations.get(action);
				if (!calculation.isFinished()) {
					calculation.performSingleStep();
					finished = false;
				}
			}
		}

		int calculatedPaths = 0;
		for (final GradualReachablePointsCalculation calculation : calculations.values())
			calculatedPaths += calculation.getCalculatedPathsCount();
		GameLogger.logGameInformation(String.format("Calculated %d reachable points paths!", calculatedPaths));
	}

	/**
	 * Updates the success and cut off {@link ActionsRating ratings} with the
	 * calculated success and cut off {@link FloatMatrix matrices}.
	 */
	private void updateActionsRatings() {
		for (final PlayerAction action : PlayerAction.values()) {
			final float successValue = calculations.get(action).getSuccessMatrixResult().sum();
			successRating.setRating(action, successValue);
			final float cutOffValue = calculations.get(action).getCutOffMatrixResult().max();
			cutOffRating.setRating(action, cutOffValue);
		}
		successRating.normalize();
	}

	@Override
	public ActionsRating combineActionsRating(float aggressiveWeight, float defensiveWeight) {
		return successRating.combine(cutOffRating, aggressiveWeight).combine(slowDownRating, defensiveWeight);
	}

	@Override
	public void logGameInformation(ActionsRating combinedActionsRating) {
		GameLogger.logGameInformation(String.format("success-rating:\t%s", successRating));
		GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffRating));
		GameLogger.logGameInformation(String.format("slow-down-rating:\t%s", slowDownRating));
		GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));

	}

	@Override
	public Collection<ContextualFloatMatrix> getContextualFloatMatrices(PlayerAction action) {
		final ArrayList<ContextualFloatMatrix> matrices = new ArrayList<>();

		matrices.add(new ContextualFloatMatrix("probability", enemyProbabilitiesMatrix, 0, 1));
		matrices.add(new ContextualFloatMatrix("min steps", enemyMinStepsMatrix));
		matrices.add(new ContextualFloatMatrix("success", calculations.get(action).getSuccessMatrixResult(), 0, 1));
		matrices.add(new ContextualFloatMatrix("cut off", calculations.get(action).getCutOffMatrixResult(), 0, 1));

		return matrices;
	}
}
