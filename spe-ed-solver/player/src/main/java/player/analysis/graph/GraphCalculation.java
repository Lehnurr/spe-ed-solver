package player.analysis.graph;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import player.analysis.LimitedQueue;
import player.boardevaluation.graph.Graph;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;

/**
 * Calculation object for calculating success and cut off ratings based on some
 * initial players until a {@link Deadline} is reached. Calculations are
 * graph-based and queued up based on their local solution improvement and a
 * small random value.
 */
public class GraphCalculation {

	private static final int DEADLINE_MILLISECOND_BUFFER = 500;
	private static final int QUEUE_SIZE = 10000;

	private final FloatMatrix probabilities;
	private final FloatMatrix minSteps;

	private final List<RatedPredictiveGraphPlayer> startPlayers;

	private final Deadline deadline;

	public final Map<PlayerAction, FloatMatrix> successMatrixResult;
	private final Map<PlayerAction, FloatMatrix> cutOffMatrixResult;

	private int calculatedPathsCount = 0;
	private final Graph graph;

	/**
	 * Creates a new {@link ReachablePointsCalculation} object.
	 * 
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param startPlayers  {@link RatedPredictiveGraphPlayer
	 *                      RatedPredictiveGraphPlayers} to start with
	 * @param deadline      {@link Deadline} to limit execution time
	 * @param graph         The Graph board to find the edges
	 */
	public GraphCalculation(final FloatMatrix probabilities, final FloatMatrix minSteps,
			final List<RatedPredictiveGraphPlayer> startPlayers, final Deadline deadline, final Graph graph) {

		this.probabilities = probabilities;
		this.minSteps = minSteps;
		this.startPlayers = startPlayers;

		this.deadline = deadline;
		this.graph = graph;

		this.successMatrixResult = new EnumMap<>(PlayerAction.class);
		this.cutOffMatrixResult = new EnumMap<>(PlayerAction.class);

		for (var x : PlayerAction.values()) {
			successMatrixResult.put(x, new FloatMatrix(graph.getWidth(), graph.getHeight(), 0));
			cutOffMatrixResult.put(x, new FloatMatrix(graph.getWidth(), graph.getHeight(), 0));
		}
	}

	/**
	 * Starts the execution for the {@link ReachablePointsCalculation} object.
	 */
	public void execute() {
		final LimitedQueue<RatedPredictiveGraphPlayer> queue = new LimitedQueue<>(RatedPredictiveGraphPlayer.class,
				QUEUE_SIZE);
		for (var initialQueueObject : this.startPlayers)
			queue.add(initialQueueObject);

		while (queue.hasNext() && deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER) {
			var self = queue.poll();
			boolean doJump = (self.getRound() + 1) % 6 == 0 && self.getSpeed() > 2;

			for (var action : PlayerAction.values()) {
				var child = self.calculateChild(action);
				if (child == null)
					continue;

				var edge = graph.getBoardCellAt(self.getPosition()).getEdge(child.getDirection(), doJump,
						child.getSpeed());

				// Check if the move is possible, try to add the Edge and update Ratings
				if (edge == null
						|| !child.addEdgeAndCalculateRating(self.getSuccessRating(), probabilities, minSteps, edge))
					continue;

				successMatrixResult.get(child.getInitialAction()).max(child.getPosition(), child.getSuccessRating());
				cutOffMatrixResult.get(child.getInitialAction()).max(child.getPosition(), child.getCutOffRating());

				queue.add(child);
				calculatedPathsCount++;
			}
		}
	}

	/**
	 * Returns the {@link FloatMatrix} of the success rating calculation.
	 * 
	 * @return success {@link FloatMatrix} result
	 */
	public FloatMatrix getSuccessMatrixResult(PlayerAction initialAction) {
		return successMatrixResult.get(initialAction);
	}

	/**
	 * Returns the {@link FloatMatrix} of the cut off rating calculation.
	 * 
	 * @return cut off {@link FloatMatrix} result
	 */
	public FloatMatrix getCutOffMatrixResult(PlayerAction initialAction) {
		return cutOffMatrixResult.get(initialAction);
	}

	/**
	 * Returns the amount of calculated paths made.
	 * 
	 * @return amount of calculated paths
	 */
	public int getCalculatedPathsCount() {
		return calculatedPathsCount;
	}

	public void incrementCalculatedPathsCount() {
		calculatedPathsCount++;
	}

}
