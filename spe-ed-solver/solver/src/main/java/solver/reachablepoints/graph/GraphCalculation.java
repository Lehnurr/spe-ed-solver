package solver.reachablepoints.graph;

import java.util.List;
import java.util.Map;

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
	
	private final Map<PlayerAction, LimitedQueue<RatedPredictiveGraphPlayer>> queues;
	private PlayerAction currentQueueAction = PlayerAction.TURN_LEFT;

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
	 * @param queueSize     the maximum Size of elements in the queue
	 */
	public GraphCalculation(final Board<Node> graph, final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Map<PlayerAction, ConcreteEdge> initialEdges, final IDeadline deadline, int queueSize) {

		this.probabilities = probabilities;
		this.minSteps = minSteps;

		this.deadline = deadline;
		this.graph = graph;

		queues = new EnumMap<>(PlayerAction.class);
		for(final PlayerAction action : PlayerAction.values()){
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
	 * Adds a {@link RatedPredictiveGraphPlayer player} to the
	 * {@link GraphCalculation#queue}
	 * 
	 * @param startPlayer The {@link RatedPredictiveGraphPlayer player} to add
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
		while (queueHasNext() && deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER) {
			executeStep();
		}
	}

	/**
	 * Calculates the valid Children for the next execition step and adds them to
	 * the queue
	 */
	public void executeStep() {
		final RatedPredictiveGraphPlayer parent = queuePoll();
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
	public int queueRemaining() {
		int remaining = 0;
		for(final PlayerAction action : PlayerAction.values())
			remaining += queues.get(action).remaining();
		return remaining;
	}

	/**
	 * Determines if the {@link GraphCalculation#queue} has a next value.
	 * 
	 * @return true if the {@link GraphCalculation#queue} has a next value
	 */
	public boolean queueHasNext() {
		for(final PlayerAction action : PlayerAction.values())
			if(queues.get(action).hasNext())
				return true;
		return false;
	}

	/**
	 * Returns the next value of the {@link GraphCalculation#queue}.
	 * 
	 * @return the next value
	 */
	public RatedPredictiveGraphPlayer queuePoll() {
		
		PlayerAction action = currentQueueAction;
		do{
			if(queues.get(action).hasNext()){
				currentQueueAction = currentQueueAction.getNext();
				return queues.get(action).poll()
			}
			action = action.getNext();
		}while(!action.equals(currentQueueAction);
		
		
		
		throw new NoSuchElementException("Tried to poll from an empty list!");
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
