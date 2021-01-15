package solver.reachablepoints.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import solver.reachablepoints.graph.board.ConcreteEdge;
import solver.reachablepoints.graph.board.Node;
import utility.game.board.Board;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * {@link RatedPredictiveGraphPlayer} for success and cutting off enemies
 * ratings, based on the {@link solver.reachablepoints.graph.board.Graph graph
 * board}.
 */
public final class RatedPredictiveGraphPlayer implements IPlayer {
	
	private final static double SUCCESS_BOOST = 0.8;

	private final int playerId;
	private final PlayerDirection direction;
	private final int speed;
	private Point2i position;
	private boolean active;
	private final int round;
	private final int relativeRound;
	private final PlayerAction initialAction;
	private final List<ConcreteEdge> edgeTail;
	private double successRating;
	private double cutOffRating;
	private Map<ConcreteEdge, Integer> initialEdgeIncrements;

	/**
	 * Creates a new {@link RatedPredictiveGraphPlayer} from a given
	 * {@link RatedPredictiveGraphPlayer RatedPredictiveGraphPlayer-Parent}.
	 * 
	 * @param parent         the previous {@link IPlayer player}
	 * @param speed          the changed speed value
	 * @param direction      the changed {@link PlayerDirection direction} value
	 * @param initialAction  the {@link PlayerAction action} that the {@link IPlayer
	 *                       player} should perform
	 * @param relativeRound  the number of rounds passed since the
	 *                       {@link PlayerAction initial action} was set
	 * @param parentEdgeTail the tail as {@link List} of {@link ConcreteEdge edges}
	 */
	private RatedPredictiveGraphPlayer(IPlayer parent, int speed, PlayerDirection direction, PlayerAction initialAction,
			int relativeRound, List<ConcreteEdge> parentEdgeTail) {

		this.playerId = parent.getPlayerId();
		this.direction = direction;
		this.speed = speed;
		this.position = parent.getPosition();
		this.active = parent.isActive();
		this.round = parent.getRound() + 1;

		this.initialAction = initialAction;
		this.edgeTail = new ArrayList<>(parentEdgeTail);
		this.relativeRound = relativeRound + 1;
	}

	/**
	 * Calculates from the possible {@link PlayerAction actions} the children that
	 * survive.
	 * 
	 * @param graph         the {@link solver.reachablepoints.graph.board.Graph
	 *                      graph board} for collision detection
	 * @param probabilities the Enemy-Pobability {@link FloatMatrix matrix}
	 * @param minSteps      the Min-Steps {@link FloatMatrix matrix}
	 * @return all valid children
	 */
	public List<RatedPredictiveGraphPlayer> getValidChildren(Board<Node> graph, FloatMatrix probabilities,
			FloatMatrix minSteps) {
		return getValidChildren(this, graph, probabilities, minSteps);
	}

	/**
	 * Calculates from the possible {@link PlayerAction actions} the children that
	 * survive.
	 * 
	 * @param parent        the previous status of the {@link IPlayer player}
	 * @param graph         the {@link solver.reachablepoints.graph.board.Graph
	 *                      graph board} for collision detection
	 * @param probabilities the Enemy-Pobability {@link FloatMatrix matrix}
	 * @param minSteps      the Min-Steps {@link FloatMatrix matrix}
	 * @return all valid children
	 */
	public static List<RatedPredictiveGraphPlayer> getValidChildren(IPlayer parent, Board<Node> graph,
			FloatMatrix probabilities, FloatMatrix minSteps) {

		boolean doJump = (parent.getRound() + 1) % 6 == 0;
		List<RatedPredictiveGraphPlayer> children = new ArrayList<>();
		List<ConcreteEdge> childEdges = new ArrayList<>();

		PlayerAction initialAction = null;
		int relativeRound = 0;
		double parentSuccessRating = 1;
		List<ConcreteEdge> parentEdgeTail = new ArrayList<>();
		Map<ConcreteEdge, Integer> parentInitialEdgeIncrements = new HashMap<>();

		if (parent instanceof RatedPredictiveGraphPlayer) {
			var graphParent = (RatedPredictiveGraphPlayer) parent;
			initialAction = graphParent.initialAction;
			relativeRound = graphParent.relativeRound;
			parentSuccessRating = graphParent.successRating;
			parentEdgeTail = graphParent.edgeTail;
			parentInitialEdgeIncrements = graphParent.initialEdgeIncrements;
		}

		for (final PlayerAction action : PlayerAction.values()) {
			int childSpeed = parent.getSpeed();
			PlayerDirection childDirection = parent.getDirection();

			if (action == PlayerAction.SPEED_UP) {
				childSpeed++;
			} else if (action == PlayerAction.SLOW_DOWN) {
				childSpeed--;
			} else {
				childDirection = childDirection.doAction(action);
			}

			if (childSpeed > IPlayer.MAX_SPEED || childSpeed < IPlayer.MIN_SPEED)
				continue;

			PlayerAction childInitialAction = initialAction == null ? action : initialAction;

			final RatedPredictiveGraphPlayer child = new RatedPredictiveGraphPlayer(parent, childSpeed, childDirection,
					childInitialAction, relativeRound, parentEdgeTail);

			final ConcreteEdge edge = graph.getBoardCellAt(parent.getPosition()).getEdge(childDirection, doJump,
					childSpeed);

			// Check if the move is possible, try to add the Edge and update Ratings
			if (edge != null && child.addEdgeAndCalculateRating(parentSuccessRating, probabilities, minSteps,
					parentInitialEdgeIncrements, edge)) {
				children.add(child);
				childEdges.add(edge);
			}
		}

		if (parentInitialEdgeIncrements.isEmpty()) {
			for (var child : children)
				for (var edge : childEdges)
					child.initialEdgeIncrements.put(edge, 0);
		}

		return children;
	}

	/**
	 * Calculates the new success rating based on the success rating of the
	 * {@link RatedPredictiveGraphPlayer parent}.
	 * 
	 * @param parentSuccessRating success rating of the
	 *                            {@link RatedPredictiveGraphPlayer parent}
	 * @param probabilities       {@link FloatMatrix} with enemy probabilities to
	 *                            consider
	 * @param minSteps            {@link FloatMatrix} with the enemy minimum steps
	 *                            to consider
	 * @param lastEdge            {@link ConcreteEdge Edge} of the last step from
	 *                            the {@link RatedPredictiveGraphPlayer parent} to
	 *                            the {@link RatedPredictiveGraphPlayer child}
	 * @return new success rating for the {@link RatedPredictiveGraphPlayer child}
	 */
	private double calculateSuccessRating(final double parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final ConcreteEdge lastEdge) {
		double successFactor = 0;
		for (final Node node : lastEdge.getPath()) {
			if (relativeRound >= minSteps.getValue(node.getPosition()))
				successFactor = Math.max(successFactor, probabilities.getValue(node.getPosition()));
		}
		return (double) (parentSuccessRating * (1 - Math.pow(successFactor, SUCCESS_BOOST)));
	}

	/**
	 * Calculates the cut off rating based on the probabilities on the short tail.
	 * 
	 * @param probabilities {@link FloatMatrix} with enemy probabilities to consider
	 * @param minSteps      {@link FloatMatrix} with the enemy minimum steps to
	 *                      consider
	 * @param lastEdge      {@link ConcreteEdge Edge} of the last step from the
	 *                      {@link RatedPredictiveGraphPlayer parent} to the
	 *                      {@link RatedPredictiveGraphPlayer child}
	 * @param successRating the probability to reach the given state
	 * @return new cut off rating for the {@link RatedPredictiveGraphPlayer child}
	 */
	private double calculateCutOffRating(final FloatMatrix probabilities, final FloatMatrix minSteps,
			final ConcreteEdge lastEdge, final double successRating) {

		double cutOff = 0;
		for (final Node node : lastEdge.getPath()) {
			if (relativeRound < minSteps.getValue(node.getPosition())) {
				cutOff = Math.max(cutOff, probabilities.getValue(node.getPosition()));
			}
		}
		return cutOff * successRating;
	}

	/**
	 * Calculates the increment-amount for the initial edge importance by applying a
	 * {@link ConcreteEdge new passed edge}.
	 * 
	 * @param parentInitialEdgeIncrements the increment values from the parent
	 * @param newEdge                     the passed edge
	 * @return a new {@link Map InitialEdgeIncrements-MAp} if the
	 *         {@link ConcreteEdge edge} intersects an {@link ConcreteEdge initial
	 *         edge}; else the parents map
	 */
	private Map<ConcreteEdge, Integer> calculateInitialEdgeIncrements(
			Map<ConcreteEdge, Integer> parentInitialEdgeIncrements, ConcreteEdge newEdge) {

		Map<ConcreteEdge, Integer> increments = parentInitialEdgeIncrements;

		for (final ConcreteEdge initialEdge : parentInitialEdgeIncrements.keySet()) {
			if (initialEdge.intersect(newEdge)) {
				if (increments == parentInitialEdgeIncrements) {
					// Overwrite the increment-value-map with a copy of it (only once)
					increments = new HashMap<>();
					increments.putAll(parentInitialEdgeIncrements);
				}
				// for the initial-edge add the increment value / increase it by 1
				increments.compute(initialEdge, (k, v) -> v == null ? 1 : (v + 1));
			}
		}

		return increments;
	}

	/**
	 * Adds an {@link ConcreteEdge edge} to the Edge-Tail of this
	 * {@link RatedPredictiveGraphPlayer player}.
	 * 
	 * @param edge the new passed {@link ConcreteEdge edge}
	 * @return true if the {@link ConcreteEdge edge} was added successfully. false
	 *         if the {@link ConcreteEdge edge} intersects another passed
	 *         {@link ConcreteEdge edge}
	 */
	private boolean addEdge(ConcreteEdge edge) {

		for (final ConcreteEdge tailPart : this.edgeTail) {
			if (tailPart.intersect(edge))
				return false;
		}

		this.edgeTail.add(edge);
		this.position = edge.getEndNode().getPosition();
		return true;
	}

	/**
	 * Calculates the Success and CutOff Rating with the given Parameters.
	 * 
	 * @param parentSuccessRating         the successration (base for the childs
	 *                                    rating) of the
	 *                                    {@link RatedPredictiveGraphPlayer parent}
	 * @param probabilities               the enemy probabilities {@link FloatMatrix
	 *                                    matrix}
	 * @param minSteps                    the min-steps-{@link FloatMatrix matrix}
	 * @param parentInitialEdgeIncrements the initial increments from the parent
	 * @param newEdge                     the new passsed {@link ConcreteEdge edge}
	 */
	private void calculateRating(final double parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final Map<ConcreteEdge, Integer> parentInitialEdgeIncrements,
			final ConcreteEdge newEdge) {
		if (isActive()) {
			this.successRating = calculateSuccessRating(parentSuccessRating, probabilities, minSteps, newEdge);
			this.cutOffRating = calculateCutOffRating(probabilities, minSteps, newEdge, successRating);
			this.initialEdgeIncrements = calculateInitialEdgeIncrements(parentInitialEdgeIncrements, newEdge);
		} else {
			this.successRating = 0;
			this.cutOffRating = 0;
		}
	}

	/**
	 * Adds an {@link ConcreteEdge edge } to the Edge-Tail (if possible) and
	 * recalculates the Success- and Cut-Off ratings, if the {@link ConcreteEdge
	 * edge}-addition was successfull.
	 * 
	 * @param parentSuccessRating         the successration (base for the childs
	 *                                    rating) of the
	 *                                    {@link RatedPredictiveGraphPlayer parent}
	 * @param probabilities               the enemy probabilities {@link FloatMatrix
	 *                                    matrix}
	 * @param minSteps                    the min-steps-{@link FloatMatrix matrix}
	 * @param parentInitialEdgeIncrements the initial increments from the parent
	 * @param edge                        the new passsed {@link ConcreteEdge edge}
	 * @return true if the {@link ConcreteEdge edge} has been added and the results
	 *         are updated. false if the {@link ConcreteEdge edge} has not been
	 *         added and the results have not changed
	 */
	private boolean addEdgeAndCalculateRating(final double parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final Map<ConcreteEdge, Integer> parentInitialEdgeIncrements,
			final ConcreteEdge edge) {
		if (addEdge(edge)) {
			calculateRating(parentSuccessRating, probabilities, minSteps, parentInitialEdgeIncrements, edge);
			return true;
		}
		return false;
	}

	/**
	 * Returns the success rating.
	 * 
	 * @return success rating
	 */
	public double getSuccessRating() {
		return successRating;
	}

	/**
	 * Returns the cut off rating.
	 * 
	 * @return cut off rating
	 */
	public double getCutOffRating() {
		return cutOffRating;
	}

	/**
	 * Returns the increment values for the {@link ConcreteEdge initial edges}
	 * 
	 * @return a {@link Map} which specifies the value to be incremented for the
	 *         {@link ConcreteEdge intial edges}
	 */
	public Map<ConcreteEdge, Integer> getInitialEdgeIncrements() {
		return this.initialEdgeIncrements;
	}

	/**
	 * Returns the amount of predictive round as relative round.
	 * 
	 * @return relative round
	 */
	public int getRelativeRound() {
		return relativeRound;
	}

	/**
	 * Returns the {@link PlayerAction} with which the path was started to reach
	 * this state.
	 * 
	 * @return the {@link PlayerAction initial action} for the path that contains
	 *         this {@link RatedPredictiveGraphPlayer}
	 */
	public PlayerAction getInitialAction() {
		return this.initialAction;
	}

	/**
	 * Returns a list of all passed {@link ConcreteEdge edges} for the relative
	 * rounds.
	 * 
	 * @return the passed edges
	 */
	public List<ConcreteEdge> getEdgeTail() {
		return this.edgeTail;
	}

	@Override
	public int getPlayerId() {
		return playerId;
	}

	@Override
	public PlayerDirection getDirection() {
		return direction;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public Point2i getPosition() {
		return position;
	}

	@Override
	public int getRound() {
		return round;
	}

	@Override
	public boolean isActive() {
		return active;
	}

}
