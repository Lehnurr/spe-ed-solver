package player.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.List;

import player.analysis.PredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Does the enemy forward prediction for a single {@link IPlayer}.
 */
public class SingleEnemyPrediction {

	private final Board<Cell> board;

	private final IPlayer player;

	private FloatMatrix probabilities;
	private FloatMatrix minSteps;

	/**
	 * Creates a new {@link SingleEnemyPrediction} calculation object for the
	 * given {@link IPlayer}.
	 * 
	 * @param board  {@link Board} the player moves on
	 * @param player {@link IPlayer} the calculation is for
	 */
	public SingleEnemyPrediction(final Board<Cell> board, final IPlayer player) {
		this.board = board;
		this.player = player;
	}

	/**
	 * Performs the enemy prediction by recursively searching forward until a
	 * certain depth is reached.
	 * 
	 * @param maxDepth the depth to search with
	 */
	public void doCalculation(final int maxDepth) {
		clearResults(maxDepth + 1);
		final PredictivePlayer startPlayer = new PredictivePlayer(player);
		doRecursiveStep(startPlayer, 1f, 1, maxDepth);
	}

	/**
	 * Clears the result of the contained matrixes. The probabilities matrix is
	 * initialized with 0s. The minSteps matrix is initialized with the given value.
	 * 
	 * @param maxStepsValue value to initialize the minSteps matrix with
	 */
	private void clearResults(final float maxStepsValue) {
		this.probabilities = new FloatMatrix(board.getWidth(), board.getHeight(), 0);
		this.minSteps = new FloatMatrix(board.getWidth(), board.getHeight(), maxStepsValue);
	}

	/**
	 * Performs a single recursive step of the calculation and calls more recursive
	 * steps for the generated children if the max depth is not reached.
	 * 
	 * @param player           {@link PredictivePlayer} to generate children with
	 * @param startProbability float probability value of the given
	 *                         {@link PredictivePlayer}
	 * @param depth            current depth of the calculation
	 * @param maxDepth         max depth the calculation should reach
	 */
	private void doRecursiveStep(final PredictivePlayer player, final float startProbability, final int depth,
			final int maxDepth) {

		final List<PredictivePlayer> validChildren = getValidChildren(player);
		final float probabilityFactor = 1f / validChildren.size();
		final float childProbability = startProbability * probabilityFactor;

		for (final PredictivePlayer child : validChildren) {

			for (final Point2i point : child.getShortTail()) {
				probabilities.add(point, childProbability);
				minSteps.min(point, depth);
			}

			if (depth <= maxDepth)
				doRecursiveStep(child, childProbability, depth + 1, maxDepth);
		}
	}

	/**
	 * Returns all valid children of a given {@link PredictivePlayer}.
	 * 
	 * @param player {@link PredictivePlayer} to generate children with
	 * @return {@link List} of {@link PredictivePlayer} objects
	 */
	private List<PredictivePlayer> getValidChildren(final PredictivePlayer player) {
		final List<PredictivePlayer> validChildren = new ArrayList<>();
		for (final PlayerAction action : PlayerAction.values()) {
			final PredictivePlayer child = new PredictivePlayer(player, action, board);
			if (child.isActive())
				validChildren.add(child);
		}
		return validChildren;
	}

	/**
	 * Returns the probability result of the calculation as {@link FloatMatrix}.
	 * 
	 * @return probability result as {@link FloatMatrix}
	 */
	public FloatMatrix getProbabilitiesMatrix() {
		return probabilities;
	}

	/**
	 * Returns the minimum step amount for each position as {@link FloatMatrix}
	 * @return min steps {@link FloatMatrix}
	 */
	public FloatMatrix getMinStepsMatrix() {
		return minSteps;
	}

}
