package solver.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.List;

import solver.analysis.PredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Calculates the enemy forward prediction for a single {@link IPlayer}. Done by
 * calculating the probabilities of the enemy location based on his valid
 * {@link PlayerAction actions} with a certain recursive search depth. Then
 * flood filling is performed for the minimum steps.
 */
public class SingleEnemyPrediction {

	private final Board<Cell> board;

	private final IPlayer player;

	private final List<FloodFillPoint> floodFillPoints = new ArrayList<>();

	private FloatMatrix probabilities;
	private FloatMatrix minSteps;

	/**
	 * Creates a new {@link SingleEnemyPrediction} calculation object for the given
	 * {@link IPlayer}.
	 * 
	 * @param board  the {@link Board board} the {@link IPlayer player} moves on
	 * @param player the {@link IPlayer} the calculation is for
	 */
	public SingleEnemyPrediction(final Board<Cell> board, final IPlayer player) {
		this.board = board;
		this.player = player;
	}

	/**
	 * Performs the enemy prediction by recursively searching forward until a
	 * certain depth is reached. Further minimum steps are set with flood fill.
	 * 
	 * @param maxDepth the depth to search with
	 */
	public void doCalculation(final int maxDepth) {
		clearResults(maxDepth);
		final PredictivePlayer startPlayer = new PredictivePlayer(player);
		doRecursiveStep(startPlayer, 1, 1, maxDepth);
		floodFill();
	}

	/**
	 * Clears the result of the contained matrixes.
	 * 
	 * @param maxSteps the maximum amount of predicted rounds
	 */
	private void clearResults(final int maxSteps) {
		this.probabilities = new FloatMatrix(board.getWidth(), board.getHeight(),
				1 / Math.pow(PlayerAction.values().length, maxSteps + 1));
		this.minSteps = new FloatMatrix(board.getWidth(), board.getHeight(), Integer.MAX_VALUE);
	}

	/**
	 * Performs a single recursive step of the calculation and calls more recursive
	 * steps for the generated children if the max depth is not reached.
	 * 
	 * @param player           {@link PredictivePlayer} to generate children with
	 * @param startProbability double probability value of the given
	 *                         {@link PredictivePlayer}
	 * @param depth            current depth of the calculation
	 * @param maxDepth         max depth the calculation should reach
	 */
	private void doRecursiveStep(final PredictivePlayer player, final double startProbability, final int depth,
			final int maxDepth) {

		final List<PredictivePlayer> validChildren = getValidChildren(player);
		if (validChildren.size() == 0)
			return;
		final double probabilityFactor = 1. / validChildren.size();
		final double childProbability = startProbability * probabilityFactor;

		for (final PredictivePlayer child : validChildren) {

			for (final Point2i point : child.getShortTail()) {
				probabilities.add(point, childProbability);
				minSteps.min(point, depth);
			}

			if (depth <= maxDepth)
				doRecursiveStep(child, childProbability, depth + 1, maxDepth);
			else
				floodFillPoints.add(new FloodFillPoint(depth, child.getPosition(), child.getSpeed()));
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
	 * Executes flood fill for the remaining minimum steps.
	 */
	private void floodFill() {
		while (!floodFillPoints.isEmpty()) {
			final FloodFillPoint current = floodFillPoints.remove(floodFillPoints.size() - 1);

			final List<Point2i> nextPoints = current.getPosition().vonNeumannNeighborhood();

			for (final Point2i nextPoint : nextPoints) {
				if (board.isOnBoard(nextPoint)) {
					final FloodFillPoint next = current.next(nextPoint);
					if (next.getRound() < minSteps.getValue(nextPoint)) {
						minSteps.setValue(next.getPosition(), next.getRound());
						floodFillPoints.add(next);
					}
				}
			}
		}
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
	 * 
	 * @return min steps {@link FloatMatrix}
	 */
	public FloatMatrix getMinStepsMatrix() {
		return minSteps;
	}

}
