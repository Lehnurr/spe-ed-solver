package player.solver.reachablepoints.multithreaded;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import player.analysis.ActionsRating;
import player.analysis.slowdown.SlowDown;
import player.solver.reachablepoints.IReachablePoints;
import player.solver.reachablepoints.RatedPredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerAction;
import utility.game.step.IDeadline;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.FloatMatrix;
import utility.logging.ApplicationLogger;
import utility.logging.GameLogger;
import utility.logging.LoggingLevel;

/**
 * Calculator class calculating success and cut off ratings as
 * {@link ActionsRating} objects and storing the last calculated results.
 */
public class ReachablePointsMultithreaded implements IReachablePoints {

	private static final int DEADLINE_MILLISECOND_INTERRUPT = 250;

	private ActionsRating successRatingsResult;
	private ActionsRating cutOffRatingsResult;
	private ActionsRating slowDownRatingsResult;

	private Map<PlayerAction, FloatMatrix> successMatrixResult;
	private Map<PlayerAction, FloatMatrix> cutOffMatrixResult;

	private FloatMatrix enemyProbabilitiesMatrix;
	private FloatMatrix enemyMinStepsMatrix;

	@Override
	public void performCalculation(final GameStep gameStep, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {

		this.enemyProbabilitiesMatrix = probabilities;
		this.enemyMinStepsMatrix = minSteps;
		slowDownRatingsResult = SlowDown.getActionsRating(gameStep.getSelf(), gameStep.getBoard());

		final RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(gameStep.getSelf());

		final Map<PlayerAction, DeadlineReachablePointsCalculation> calculations = getCalculations(startPlayer,
				gameStep.getBoard(), probabilities, minSteps, gameStep.getDeadline());

		calculateMultithreaded(calculations.values(), gameStep.getDeadline());

		updateResults(calculations);
	}

	/**
	 * Generates {@link DeadlineReachablePointsCalculation} objects for each
	 * possible {@link PlayerAction} a given {@link RatedPredictivePlayer} can make.
	 * The objects are mapped to the performed {@link PlayerAction} and returned.
	 * 
	 * @param startPlayer   {@link RatedPredictivePlayer} to start the calculations
	 *                      with
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 * @param deadline      {@link IDeadline} which must not be exceeded
	 * @return {@link DeadlineReachablePointsCalculation} objects mapped to the
	 *         taken child {@link PlayerAction}
	 */
	private Map<PlayerAction, DeadlineReachablePointsCalculation> getCalculations(
			final RatedPredictivePlayer startPlayer, final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final IDeadline deadline) {

		final Map<PlayerAction, DeadlineReachablePointsCalculation> result = new EnumMap<>(PlayerAction.class);

		for (final PlayerAction action : PlayerAction.values()) {
			final RatedPredictivePlayer child = new RatedPredictivePlayer(startPlayer, action, board, probabilities,
					minSteps);
			final DeadlineReachablePointsCalculation calculation = new DeadlineReachablePointsCalculation(board,
					probabilities, minSteps, child, deadline);
			result.put(action, calculation);
		}

		return result;
	}

	/**
	 * Calculates each given {@link DeadlineReachablePointsCalculation} in a
	 * separate threads and joins all of them. Additionally a timer is set to
	 * interrupt running threads, which are not finished in time.
	 * 
	 * @param calculations {@link DeadlineReachablePointsCalculation} objects to
	 *                     execute the calculation for
	 * @param deadline     {@link IDeadline} for the calculations
	 */
	private void calculateMultithreaded(final Collection<DeadlineReachablePointsCalculation> calculations,
			final IDeadline deadline) {

		final List<Thread> threads = new ArrayList<>();

		for (final DeadlineReachablePointsCalculation calculation : calculations) {
			final Thread thread = new Thread(calculation::execute);
			threads.add(thread);
			thread.start();
		}

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (final Thread thread : threads) {
					if (thread.isAlive()) {
						thread.interrupt();
					}
				}
				timer.cancel();
			}
		}, Math.max(1, deadline.getRemainingMilliseconds() - DEADLINE_MILLISECOND_INTERRUPT));

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				ApplicationLogger.logWarning("The reachable points calculation was interrupted!");
				ApplicationLogger.logException(e, LoggingLevel.WARNING);
			}
		}
	}

	/**
	 * Updates all the locally stored results by collecting all result of the
	 * {@link DeadlineReachablePointsCalculation} objects.
	 * 
	 * @param calculations {@link DeadlineReachablePointsCalculation} objects mapped
	 *                     to the taken {@link PlayerAction}
	 */
	private void updateResults(final Map<PlayerAction, DeadlineReachablePointsCalculation> calculations) {

		clearResults();

		int calculatedPaths = 0;

		for (final PlayerAction action : PlayerAction.values()) {
			final DeadlineReachablePointsCalculation calculation = calculations.get(action);
			final FloatMatrix successMatrix = calculation.getSuccessMatrixResult();
			final FloatMatrix cutOffMatrix = calculation.getCutOffMatrixResult();

			successMatrixResult.put(action, successMatrix);
			cutOffMatrixResult.put(action, cutOffMatrix);

			successRatingsResult.setRating(action, successMatrix.sum());
			cutOffRatingsResult.setRating(action, cutOffMatrix.max());

			calculatedPaths += calculation.getCalculatedPathsCount();
		}

		GameLogger.logGameInformation(String.format("Calculated %d reachable points paths!", calculatedPaths));

		successRatingsResult.normalize();
	}

	/**
	 * Clears all locally stored results.
	 */
	private void clearResults() {
		successMatrixResult = new EnumMap<>(PlayerAction.class);
		cutOffMatrixResult = new EnumMap<>(PlayerAction.class);
		successRatingsResult = new ActionsRating();
		cutOffRatingsResult = new ActionsRating();
	}

	@Override
	public ActionsRating combineActionsRating(float aggressiveWeight, float defensiveWeight) {
		return successRatingsResult.combine(cutOffRatingsResult, aggressiveWeight).combine(slowDownRatingsResult,
				defensiveWeight);
	}

	@Override
	public void logGameInformation(ActionsRating combinedActionsRating) {
		GameLogger.logGameInformation(String.format("success-rating:\t%s", successRatingsResult));
		GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffRatingsResult));
		GameLogger.logGameInformation(String.format("slow-down-rating:\t%s", slowDownRatingsResult));
		GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));

	}

	@Override
	public Collection<ContextualFloatMatrix> getContextualFloatMatrices(PlayerAction action) {
		final ArrayList<ContextualFloatMatrix> matrices = new ArrayList<>();

		matrices.add(new ContextualFloatMatrix("probability", enemyProbabilitiesMatrix, 0, 1));
		matrices.add(new ContextualFloatMatrix("min steps", enemyMinStepsMatrix));
		matrices.add(new ContextualFloatMatrix("success", successMatrixResult.get(action), 0, 1));
		matrices.add(new ContextualFloatMatrix("cut off", cutOffMatrixResult.get(action), 0, 1));

		return matrices;
	}
}
