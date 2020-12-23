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
public class EnemyForwardPrediction {

	private final Board<Cell> board;

	private final IPlayer player;
	private final int round;
	private final int playerId;

	private final PathBoundProbability[][] probabilities;

	/**
	 * Creates a new {@link EnemyForwardPrediction} calculation object for the given
	 * {@link IPlayer}.
	 * 
	 * @param board    {@link Board} the player moves on
	 * @param player   {@link IPlayer} the calculation is for
	 * @param playerId any unique id for a player != 0
	 */
	public EnemyForwardPrediction(final Board<Cell> board, final IPlayer player, final int round, final int playerId) {
		this.board = board;
		this.probabilities = new PathBoundProbability[board.getHeight()][board.getWidth()];
		this.player = player;
		this.round = round;
		this.playerId = playerId;
	}

	public void doCalculation(final int maxDepth) {
		clearPathBoundProbabilities();
		final PredictivePlayer startPlayer = new PredictivePlayer(player, round);
		final PathDescriptor startDescriptor = new PathDescriptor(playerId);
		final PathBoundProbability startValue = new PathBoundProbability(startDescriptor, 1f);
		doRecursiveStep(startPlayer, startValue, 0, maxDepth);
	}

	private void clearPathBoundProbabilities() {
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				final PathDescriptor pathDescriptor = new PathDescriptor(playerId);
				this.probabilities[y][x] = new PathBoundProbability(pathDescriptor, 0f);
			}
		}
	}

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
				doRecursiveStep(childPlayer, pathBoundProbability, depth + 1, maxDepth);
		}
	}

	private Map<PlayerAction, PredictivePlayer> getValidActionMap(final PredictivePlayer player) {
		final Map<PlayerAction, PredictivePlayer> validActionMap = new HashMap<>();
		for (final PlayerAction action : PlayerAction.values()) {
			final PredictivePlayer child = new PredictivePlayer(player, action, board);
			if (child.isActive())
				validActionMap.put(action, child);
		}
		return validActionMap;
	}

}
