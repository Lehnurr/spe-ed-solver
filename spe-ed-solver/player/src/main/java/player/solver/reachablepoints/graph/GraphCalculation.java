package player.solver.reachablepoints.graph;

import java.util.Map;

import player.analysis.cutoff.CutOffCalculation;
import player.analysis.success.SuccessCalculation;
import player.solver.reachablepoints.LimitedQueue;
import player.solver.reachablepoints.graph.board.ConcreteEdge;
import player.solver.reachablepoints.graph.board.Node;
import player.solver.reachablepoints.graph.importance.EdgeImportance;
import utility.game.board.Board;
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

	private SuccessCalculation successCalculation;
	private CutOffCalculation cutOffCalculation;
	private EdgeImportance edgeImportance;

	private int calculatedPathsCount = 0;
	private final Board<Node> graph;

	/**
	 * Creates a new {@link GraphCalculation} object.
	 * 
	 * @param graph         The Graph board to find the edges
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param initialEdges  All possible Edges the player can do for the current
	 *                      round
	 * @param deadline      {@link Deadline} to limit execution time
	 * @param queueSize     The maximum Size of elements in the queue
	 */
	public GraphCalculation(final Board<Node> graph, final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Map<PlayerAction, ConcreteEdge> initialEdges, final Deadline deadline, int queueSize) {

		this.probabilities = probabilities;
		this.minSteps = minSteps;

		this.deadline = deadline;
		this.graph = graph;

		queue = new LimitedQueue<>(RatedPredictiveGraphPlayer.class, queueSize);

		successCalculation = new SuccessCalculation(graph.getWidth(), graph.getHeight());
		cutOffCalculation = new CutOffCalculation(graph.getWidth(), graph.getHeight());
		edgeImportance = new EdgeImportance(graph.getWidth(), graph.getHeight(), initialEdges);
	}

	/**
	 * Creates a new {@link GraphCalculation} object and sets the size of the queue
	 * to {@link GraphCalculation#DEFAULT_QUEUE_SIZE}
	 * 
	 * @param graph         The Graph board to find the edges
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps * @param
	 * @param initialEdges  All possible Edges the player can do for the current
	 *                      round
	 * @param deadline      {@link Deadline} to limit execution time
	 */
	public GraphCalculation(Board<Node> graph, FloatMatrix probabilities, FloatMatrix minSteps,
			final Map<PlayerAction, ConcreteEdge> initialEdges, Deadline deadline) {
		this(graph, probabilities, minSteps, initialEdges, deadline, DEFAULT_QUEUE_SIZE);
	}

	public void addPlayerToQueue(RatedPredictiveGraphPlayer startPlayer) {
		final PlayerAction action = startPlayer.getInitialAction();
		final Point2i position = startPlayer.getPosition();

		successCalculation.add(action, position, startPlayer.getSuccessRating());
		cutOffCalculation.add(action, position, startPlayer.getCutOffRating());

		queue.add(startPlayer);
		calculatedPathsCount++;
	}

	/**
	 * Starts the execution for the {@link GraphCalculation} with the given
	 * startPlayers. Ends when no steps can be found or the Deadline exceeds the
	 * {@link GraphCalculation#DEADLINE_MILLISECOND_BUFFER}
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
		var parent = queue.poll();
		final var children = parent.getValidChildren(graph, probabilities, minSteps,
				edgeImportance.getInitialEdgeArray());

		for (final var child : children) {
			edgeImportance.add(parent.getInitialEdgeIncrements());
			addPlayerToQueue(child);
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
	public SuccessCalculation getSuccessCalculation() {
		return successCalculation;
	}

	/**
	 * Returns the {@link FloatMatrix} of the cut off rating calculation.
	 * 
	 * @return cut off {@link FloatMatrix} result
	 */
	public CutOffCalculation getCutOffCalculation() {
		return cutOffCalculation;
	}

	public EdgeImportance getEdgeImportance() {
		return this.edgeImportance;
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
