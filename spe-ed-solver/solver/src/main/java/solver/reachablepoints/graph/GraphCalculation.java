package solver.reachablepoints.graph;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import solver.analysis.cutoff.CutOffCalculation;
import solver.analysis.success.SuccessCalculation;
import solver.reachablepoints.LimitedQueue;
import solver.reachablepoints.graph.board.ConcreteEdge;
import solver.reachablepoints.graph.board.Node;
import solver.reachablepoints.graph.importance.EdgeImportance;
import utility.game.board.Board;
import utility.game.player.PlayerAction;
import utility.game.step.IDeadline;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Calculation object for calculating success and cut off ratings based on some
 * initial players until a {@link IDeadline} is reached. Calculations are
 * graph-based and queued up based on their local solution improvement and a
 * small random value.
 */
public class GraphCalculation {

	private static final int DEADLINE_MILLISECOND_BUFFER = 500;
	private static final int DEFAULT_QUEUE_SIZE = 10000;

	/**
	 * Maps for each possible {@link PlayerAction initial player action} a
	 * {@link LimitedQueue}
	 */
	private final Map<PlayerAction, LimitedQueue<RatedPredictiveGraphPlayer>> queues;

	/**
	 * All {@link PlayerAction player actions} that are a key in
	 * {@link GraphCalculation#queues}. Can be used for index-based access to the
	 * available key values.
	 */
	private PlayerAction[] queuedActions;

	private final FloatMatrix probabilities;
	private final FloatMatrix minSteps;

	private final IDeadline deadline;

	private SuccessCalculation successCalculation;
	private CutOffCalculation cutOffCalculation;
	private EdgeImportance edgeImportance;

	private int calculatedPathsCount = 0;
	private final Board<Node> graph;

	/**
	 * Creates a new {@link GraphCalculation} object.
	 * 
	 * @param graph         the Graph board to find the edges
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param initialEdges  all possible Edges the player can do for the current
	 *                      round
	 * @param deadline      {@link IDeadline} to limit execution time
	 * @param queueSize     the maximum Size of elements for each of the 5 queues
	 */
	public GraphCalculation(final Board<Node> graph, final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Map<PlayerAction, ConcreteEdge> initialEdges, final IDeadline deadline, int queueSize) {

		this.probabilities = probabilities;
		this.minSteps = minSteps;

		this.deadline = deadline;
		this.graph = graph;

		queues = new EnumMap<>(PlayerAction.class);
		queuedActions = PlayerAction.values();
		for (final PlayerAction action : PlayerAction.values()) {
			queues.put(action, new LimitedQueue<>(RatedPredictiveGraphPlayer.class, queueSize));
		}

		successCalculation = new SuccessCalculation(graph.getWidth(), graph.getHeight());
		cutOffCalculation = new CutOffCalculation(graph.getWidth(), graph.getHeight());
		edgeImportance = new EdgeImportance(graph.getWidth(), graph.getHeight(), initialEdges);
	}

	/**
	 * Creates a new {@link GraphCalculation} object and sets the size of the queue
	 * to {@link GraphCalculation#DEFAULT_QUEUE_SIZE}.
	 * 
	 * @param graph         the Graph board to find the edges
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps * @param
	 * @param initialEdges  all possible Edges the player can do for the current
	 *                      round
	 * @param deadline      {@link IDeadline} to limit execution time
	 */
	public GraphCalculation(Board<Node> graph, FloatMatrix probabilities, FloatMatrix minSteps,
			final Map<PlayerAction, ConcreteEdge> initialEdges, IDeadline deadline) {
		this(graph, probabilities, minSteps, initialEdges, deadline, DEFAULT_QUEUE_SIZE);
	}

	/**
	 * Adds a {@link RatedPredictiveGraphPlayer player} to the correct
	 * {@link GraphCalculation#queues queue}
	 * 
	 * @param player The {@link RatedPredictiveGraphPlayer player} to add
	 */
	public void addPlayerToQueue(RatedPredictiveGraphPlayer player) {
		final PlayerAction action = player.getInitialAction();
		final Point2i position = player.getPosition();

		successCalculation.add(action, position, player.getSuccessRating());
		cutOffCalculation.add(action, position, player.getCutOffRating());

		queues.get(player.getInitialAction()).add(player);
		calculatedPathsCount++;
	}

	/**
	 * Starts the execution for the {@link GraphCalculation} with the given
	 * startPlayers. Ends when no steps can be found or the Deadline exceeds the
	 * {@link GraphCalculation#DEADLINE_MILLISECOND_BUFFER}
	 */
	public void executeDeadline() {
		while (queuesHasNext() && deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER) {
			executeStep();
		}
	}

	/**
	 * Calculates the valid Children for the next execition step and adds them to
	 * the queue
	 */
	public void executeStep() {
		final RatedPredictiveGraphPlayer parent = queuesPoll();
		final List<RatedPredictiveGraphPlayer> children = parent.getValidChildren(graph, probabilities, minSteps);

		for (final RatedPredictiveGraphPlayer child : children) {
			edgeImportance.add(parent.getInitialEdgeIncrements());
			addPlayerToQueue(child);
		}
	}

	/**
	 * The amount of remaining {@link RatedPredictiveGraphPlayer}.
	 * 
	 * @return An integer equal or higher than 0 and less than
	 *         {@link GraphCalculation#DEFAULT_QUEUE_SIZE QUEUE_SIZE} * 5
	 */
	public int queuesRemaining() {
		int remaining = 0;

		for (final LimitedQueue<?> queue : queues.values())
			remaining += queue.remaining();

		return remaining;
	}

	/**
	 * Determines if any of the {@link GraphCalculation#queues} has a next value.
	 * 
	 * @return true if any of the {@link GraphCalculation#queues} has a next value
	 */
	public boolean queuesHasNext() {
		return queues.values().stream().anyMatch(LimitedQueue::hasNext);
	}

	/**
	 * Returns the next value for a random {@link GraphCalculation#queues queue}.
	 * 
	 * @return the next {@link RatedPredictiveGraphPlayer}
	 */
	public RatedPredictiveGraphPlayer queuesPoll() {

		if (queues.isEmpty())
			throw new NoSuchElementException("Tried to poll from an empty list!");

		final int randomActionIndex = new Random().nextInt(queuedActions.length);
		final PlayerAction randomAction = queuedActions[randomActionIndex];

		if (queues.get(randomAction).hasNext())
			return queues.get(randomAction).poll();
		else {
			queues.remove(randomAction);
			queuedActions = queues.keySet().toArray(new PlayerAction[queues.size()]);
			return queuesPoll();
		}
	}

	/**
	 * Returns the {@link SuccessCalculation} of the success rating calculation.
	 * 
	 * @return success result
	 */
	public SuccessCalculation getSuccessCalculation() {
		return successCalculation;
	}

	/**
	 * Returns the {@link CutOffCalculation} of the cut off rating calculation.
	 * 
	 * @return cut off result
	 */
	public CutOffCalculation getCutOffCalculation() {
		return cutOffCalculation;
	}

	/**
	 * Returns the {@link EdgeImportance} of the edge importance calculation.
	 * 
	 * @return edge importance result
	 */
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
}
