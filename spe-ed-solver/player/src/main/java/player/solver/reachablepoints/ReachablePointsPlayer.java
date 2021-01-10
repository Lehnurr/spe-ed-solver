package player.solver.reachablepoints;

import java.util.function.Consumer;

import player.ISpeedSolverPlayer;
import player.analysis.ActionsRating;
import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * Player implementing {@link ISpeedSolverPlayer} used for multiple
 * {@link PlayerType} values. For each possible {@link PlayerAction} the
 * reachable points are determined. For each reachable point is rated for its
 * success and for the cut off potential. The {@link ActionsRating} is used to
 * give each analysis its weight. The overall highest rated {@link PlayerAction}
 * gets chosen for each {@link GameStep}.
 */
public class ReachablePointsPlayer implements ISpeedSolverPlayer {

	private final EnemyProbabilityCalculator enemyProbabilityCalculator;
	private final IReachablePoints reachablePointsCalculator;
	private final float aggressiveWeight;
	private final float defensiveWeight;

	/**
	 * Creates a new {@link ReachablePointsPlayer} with the given configuration
	 * values.
	 * 
	 * @param enemySearchDepth recursive search depth to search for enemy actions
	 * @param cutOffWeight     relative weight for the cut off {@link ActionsRating}
	 * @param slowDownWeight   relative weight for the slow down
	 *                         {@link ActionsRating}
	 * @param type             the {@link ReachablePointsType} of the calculation
	 */
	public ReachablePointsPlayer(final int enemySearchDepth, float aggressiveWeight, float defensiveWeight,
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