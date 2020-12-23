package player.analysis.enemyprobability;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.geometry.Point2i;

/**
 * Does the enemy forward prediction for a single {@link IPlayer}.
 */
public class SingleEnemyPrediction {

	private final Board<Cell> board;

	private final IPlayer player;

	private final PathBoundProbability[][] probabilities;

	/**
	 * Creates a new {@link SingleEnemyForwardPrediction} calculation object for the
	 * given {@link IPlayer}.
	 * 
	 * @param board    {@link Board} the player moves on
	 * @param player   {@link IPlayer} the calculation is for
	 * @param playerId any unique id for a player != 0
	 */
	public SingleEnemyPrediction(final Board<Cell> board, final IPlayer player) {
		this.board = board;
		this.probabilities = new PathBoundProbability[board.getHeight()][board.getWidth()];
		this.player = player;
	}

	/**
	 * Performs the enemy prediction by recursively searching forward until a
	 * certain depth is reached.
	 * 
	 * @param maxDepth the depth to search with
	 */
	public void doCalculation(final int maxDepth) {
		clearPathBoundProbabilities();
		final PredictivePlayer startPlayer = new PredictivePlayer(player);
		final PathDescriptor startDescriptor = new PathDescriptor(player.getPlayerId());
		final PathBoundProbability startValue = new PathBoundProbability(startDescriptor, 1f);
		doRecursiveStep(startPlayer, startValue, 0, maxDepth);
	}

	/**
	 * Clears all {@link PathBoundProbability} objects and initializes them with a
	 * probability of zero.
	 */
	private void clearPathBoundProbabilities() {
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				final PathDescriptor pathDescriptor = new PathDescriptor(player.getPlayerId());
				this.probabilities[y][x] = new PathBoundProbability(pathDescriptor, 0f);
			}
		}
	}

	/**
	 * Does performs a recursive search step on the given depth level.
	 * 
	 * @param player               {@link PredictivePlayer} to start the step with
	 * @param pathBoundProbability {@link PathBoundProbability} to start the step
	 *                             with
	 * @param depth                current depth of the search step
	 * @param maxDepth             maximum depth of the search
	 */
	private void doRecursiveStep(final PredictivePlayer player, final PathBoundProbability pathBoundProbability,
			final int depth, final int maxDepth) {

		final Map<PlayerAction, PredictivePlayer> validActionMap = getValidActionMap(player);
		final float probabilityFactor = 1f / validActionMap.size();
		final float childProbability = pathBoundProbability.getProbability() * probabilityFactor;

		for (final Entry<PlayerAction, PredictivePlayer> entry : validActionMap.entrySet()) {

			final PlayerAction childAction = entry.getKey();
			final PredictivePlayer childPlayer = entry.getValue();

			final PathDescriptor childDescriptor = pathBoundProbability.getPathDescriptor().append(childAction);
			final PathBoundProbability childResult = new PathBoundProbability(childDescriptor, childProbability);

			for (final Point2i point : childPlayer.getShortTail()) {
				final int x = point.getX();
				final int y = point.getY();
				probabilities[y][x] = probabilities[y][x].combine(childResult);
			}

			if (depth < maxDepth)
				doRecursiveStep(childPlayer, childResult, depth + 1, maxDepth);
		}
	}

	/**
	 * Returns a {@link Map} mapping a {@link PlayerAction} to a
	 * {@link PredictivePlayer} when the given {@link PlayerAction} is valid for the
	 * given {@link PredictivePlayer}.
	 * 
	 * @param player {@link PredictivePlayer} to validate with
	 * @return {@link Map} mapping valid {@link PlayerAction actions} to
	 *         {@link PredictivePlayer} objects
	 */
	private Map<PlayerAction, PredictivePlayer> getValidActionMap(final PredictivePlayer player) {
		final Map<PlayerAction, PredictivePlayer> validActionMap = new HashMap<>();
		for (final PlayerAction action : PlayerAction.values()) {
			final PredictivePlayer child = new PredictivePlayer(player, action, board);
			if (child.isActive())
				validActionMap.put(action, child);
		}
		return validActionMap;
	}

	/**
	 * Returns the {@link PredictionResult} of the last performed search.
	 * 
	 * @return last {@link PredictionResult}
	 */
	public PredictionResult getPredictionResult() {
		return new PredictionResult(probabilities);
	}

}
