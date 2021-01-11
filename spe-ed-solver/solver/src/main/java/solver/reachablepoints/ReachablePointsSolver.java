package solver.reachablepoints;

import java.util.function.Consumer;

import solver.ISpeedSolver;
import solver.SolverType;
import solver.analysis.ActionsRating;
import solver.analysis.enemyprobability.EnemyProbabilityCalculator;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * Player implementing {@link ISpeedSolver} used for multiple {@link SolverType}
 * values. For each possible {@link PlayerAction} the reachable points are
 * determined. For each reachable point is rated for its success and for the cut
 * off potential. The {@link ActionsRating} is used to give each analysis its
 * weight. The overall highest rated {@link PlayerAction} gets chosen for each
 * {@link GameStep}.
 */
public class ReachablePointsSolver implements ISpeedSolver {

	private final EnemyProbabilityCalculator enemyProbabilityCalculator;
	private final IReachablePoints reachablePointsCalculator;
	private final float aggressiveWeight;
	private final float defensiveWeight;

	/**
	 * Creates a new {@link ReachablePointsSolver} with the given configuration
	 * values.
	 * 
	 * @param enemySearchDepth recursive search depth to search for enemy actions
	 * @param aggressiveWeight relative weight for aggressive {@link ActionsRating}
	 * @param defensiveWeight  relative weight for the defensive
	 *                         {@link ActionsRating}
	 * @param type             the {@link ReachablePointsType} of the calculation
	 */
	public ReachablePointsSolver(final int enemySearchDepth, float aggressiveWeight, float defensiveWeight,
			final ReachablePointsType type) {
		this.enemyProbabilityCalculator = new EnemyProbabilityCalculator(enemySearchDepth);
		this.reachablePointsCalculator = type.newInstance();
		this.aggressiveWeight = aggressiveWeight;
		this.defensiveWeight = defensiveWeight;
	}

	@Override
	public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {
		if (!gameStep.getSelf().isActive())
			return PlayerAction.CHANGE_NOTHING;

		// Calculate enemyProbability
		enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard());

		// Calculate the Action
		reachablePointsCalculator.performCalculation(gameStep, enemyProbabilityCalculator.getProbabilitiesMatrix(),
				enemyProbabilityCalculator.getMinStepsMatrix());

		// Combine the results
		final ActionsRating combinedActionsRating = reachablePointsCalculator.combineActionsRating(aggressiveWeight,
				defensiveWeight);

		// Log the results
		reachablePointsCalculator.logGameInformation(combinedActionsRating);

		// get the best action
		final PlayerAction actionToTake = combinedActionsRating.maxAction();

		// send the Data to the viewer
		for (final ContextualFloatMatrix matrix : reachablePointsCalculator.getContextualFloatMatrices(actionToTake)) {
			boardRatingConsumer.accept(matrix);
		}

		// send the Calculated Action
		return actionToTake;
	}
}