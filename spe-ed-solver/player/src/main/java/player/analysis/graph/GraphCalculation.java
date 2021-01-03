package player.analysis.graph;

import java.util.EnumMap;
import java.util.Map;

import player.analysis.LimitedQueue;
import player.boardevaluation.graph.Graph;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Calculation object for calculating success and cut off ratings based on some
 * initial players until a {@link Deadline} is reached. Calculations are
 * graph-based and queued up based on their local solution improvement and a
 * small random value.
 */
public class GraphCalculation {

	private static final int DEADLINE_MILLISECOND_BUFFER = 500;
	private static final int DEFAULT_QUEUE_SIZE = 10000;
	private final LimitedQueue<RatedPredictiveGraphPlayer> queue;

	private final FloatMatrix probabilities;
	private final FloatMatrix minSteps;

	private final Deadline deadline;

	public final Map<PlayerAction, FloatMatrix> successMatrixResult;
	private final Map<PlayerAction, FloatMatrix> cutOffMatrixResult;

	private int calculatedPathsCount = 0;
	private final Graph graph;

	/**
	 * Creates a new {@link ReachablePointsCalculation} object.
	 * 
	 * @param graph         The Graph board to find the edges
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param deadline      {@link Deadline} to limit execution time
	 * @param queueSize     The maximum Size of elements in the queue
	 */
	public GraphCalculation(final Graph graph, final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Deadline deadline, int queueSize) {

		this.probabilities = probabilities;
		this.minSteps = minSteps;

		this.deadline = deadline;
		this.graph = graph;

		this.successMatrixResult = new EnumMap<>(PlayerAction.class);
		this.cutOffMatrixResult = new EnumMap<>(PlayerAction.class);
		queue = new LimitedQueue<>(RatedPredictiveGraphPlayer.class, queueSize);

		for (var x : PlayerAction.values()) {
			successMatrixResult.put(x, new FloatMatrix(graph.getWidth(), graph.getHeight(), 0));
			cutOffMatrixResult.put(x, new FloatMatrix(graph.getWidth(), graph.getHeight(), 0));
		}
	}

	/**
	 * Creates a new {@link ReachablePointsCalculation} object and sets the size of
	 * the queue to {@link GraphCalculation#DEFAULT_QUEUE_SIZE}
	 * 
	 * @param graph         The Graph board to find the edges
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param deadline      {@link Deadline} to limit execution time
	 */
	public GraphCalculation(Graph graph, FloatMatrix probabilities, FloatMatrix minSteps, Deadline deadline) {
		this(graph, probabilities, minSteps, deadline, DEFAULT_QUEUE_SIZE);
	}

	public void addStartPlayer(RatedPredictiveGraphPlayer startPlayer) {
		final PlayerAction action = startPlayer.getInitialAction();
		final Point2i position = startPlayer.getPosition();

		getSuccessMatrixResult(action).max(position, startPlayer.getSuccessRating());
		getCutOffMatrixResult(action).max(position, startPlayer.getCutOffRating());

		queue.add(startPlayer);
		calculatedPathsCount++;
	}

	/**
	 * Starts the execution for the {@link ReachablePointsCalculation} with the
	 * given startPlayers. Ends when no steps can be found or the Deadline exceeds
	 * the {@link GraphCalculation#DEADLINE_MILLISECOND_BUFFER}
	 */
	public void executeDeadline() {
		while (queue.hasNext() && deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER) {
			executeStep();
		}
	}

	/**
	 * Calculates the valid Children for the next execition step and adds them to
	 * the queue
	 * 
	 * @throws NoSuchElementException if no next element is available
	 */
	public void executeStep() {
		final var children = queue.poll().getValidChildren(graph, probabilities, minSteps);

		for (final var child : children) {

			getSuccessMatrixResult(child.getInitialAction()).max(child.getPosition(), child.getSuccessRating());
			getCutOffMatrixResult(child.getInitialAction()).max(child.getPosition(), child.getCutOffRating());

			queue.add(child);
			calculatedPathsCount++;
		}
	}

	/**
	 * The amount of remaining {@link RatedPredictiveGraphPlayer}.
	 * 
	 * @return An integer equal or higher than 0 and less than
	 *         {@link GraphCalculation#DEFAULT_QUEUE_SIZE QUEUE_SIZE}
	 */
	public int queueRemaining() {
		return queue.remaining();
	}

	/**
	 * Determines if the {@link GraphCalculation#queue} has a next value.
	 * 
	 * @return true if the {@link GraphCalculation#queue} has a next value
	 */
	public boolean queueHasNext() {
		return queue.hasNext();
	}

	/**
	 * Returns the next value of the {@link GraphCalculation#queue}.
	 * 
	 * @return the next value
	 * @throws NoSuchElementException if no next element is available
	 */
	public RatedPredictiveGraphPlayer queuePoll() {
		return queue.poll();
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
