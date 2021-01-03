package player.analysis.graph;

import java.util.ArrayList;
import java.util.List;

import player.boardevaluation.graph.ConcreteEdge;
import player.boardevaluation.graph.Graph;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * RatedPredictiveGraphPlayer for success and cutting off enemies ratings, based
 * on the graph board.
 */
public final class RatedPredictiveGraphPlayer implements IPlayer {

	private final int playerId;
	private final PlayerDirection direction;
	private final int speed;
	private Point2i position;
	private boolean active;
	private final int round;
	private final int relativeRound;
	private final PlayerAction initialAction;
	private final List<ConcreteEdge> edgeTail;
	private float successRating;
	private float cutOffRating;

	/**
	 * Creates a new {@link RatedPredictiveGraphPlayer} from a given
	 * {@link RatedPredictiveGraphPlayer RatedPredictiveGraphPlayer-Parent}
	 * 
	 * @param parent        The previous player
	 * @param speed         The changed speed value
	 * @param direction     The changed direction value
	 * @param initialAction The action that the player should perform
	 * @param relativeRound The number of rounds passed since the initialAction was
	 *                      set
	 */
	private RatedPredictiveGraphPlayer(IPlayer parent, int speed, PlayerDirection direction, PlayerAction initialAction,
			int relativeRound) {

		this.playerId = parent.getPlayerId();
		this.direction = direction;
		this.speed = speed;
		this.position = parent.getPosition();
		this.active = parent.isActive();
		this.round = parent.getRound();

		this.initialAction = initialAction;
		this.edgeTail = new ArrayList<>();
		this.relativeRound = relativeRound + 1;
	}

	/**
	 * Calculates from the possible actions the children that survive
	 * 
	 * @param graph         The Graph-board for collision detection
	 * @param probabilities The Enemy-Pobability matrix
	 * @param minSteps      The Min-Steps matrix
	 * @return All valid Children
	 */
	public List<RatedPredictiveGraphPlayer> getValidChildren(Graph graph, FloatMatrix probabilities,
			FloatMatrix minSteps) {
		return getValidChildren(this, this.getSuccessRating(), this.getInitialAction(), this.getRelativeRound(), graph,
				probabilities, minSteps);
	}

	/**
	 * Calculates from the possible actions the children that survive
	 * 
	 * @param parent        The previous status of the player
	 * @param graph         The Graph-board for collision detection
	 * @param probabilities The Enemy-Pobability matrix
	 * @param minSteps      The Min-Steps matrix
	 * @return All valid Children
	 */
	public static List<RatedPredictiveGraphPlayer> getValidChildren(IPlayer parent, Graph graph,
			FloatMatrix probabilities, FloatMatrix minSteps) {
		return getValidChildren(parent, 1, null, 0, graph, probabilities, minSteps);
	}

	/**
	 * Calculates from the possible actions the children that survive
	 * 
	 * @param parent              The previous status of the player
	 * @param parentSuccessRating The parent's successRating (1 if there is no
	 *                            parent)
	 * @param initialAction       The first action of the currently calculated path
	 *                            (null if there is no first action)
	 * @param relativeRound       The number of rounds since the initialAction
	 * @param graph               The Graph-board for collision detection
	 * @param probabilities       The Enemy-Pobability matrix
	 * @param minSteps            The Min-Steps matrix
	 * @return All valid Children
	 */
	private static List<RatedPredictiveGraphPlayer> getValidChildren(IPlayer parent, float parentSuccessRating,
			PlayerAction initialAction, int relativeRound, Graph graph, FloatMatrix probabilities,
			FloatMatrix minSteps) {

		List<RatedPredictiveGraphPlayer> children = new ArrayList<>();
		boolean doJump = (parent.getRound() + 1) % 6 == 0 && parent.getSpeed() > 2;

		for (var action : PlayerAction.values()) {

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

			final var child = new RatedPredictiveGraphPlayer(parent, childSpeed, childDirection, childInitialAction,
					relativeRound);

			final ConcreteEdge edge = graph.getBoardCellAt(parent.getPosition()).getEdge(childDirection, doJump,
					childSpeed);

			// Check if the move is possible, try to add the Edge and update Ratings
			if (edge != null && child.addEdgeAndCalculateRating(parentSuccessRating, probabilities, minSteps, edge))
				children.add(child);
		}

		return children;
	}

	/**
	 * Calculates the new success rating based on the success rating of the parent.
	 * 
	 * @param parentSuccessRating success rating of the parent
	 * @param probabilities       {@link FloatMatrix} with enemy probabilities to
	 *                            consider
	 * @param minSteps            {@link FloatMatrix} with the enemy minimum steps
	 *                            to consider
	 * @param lastEdge            {@link ConcreteEdge Edge} of the last step from
	 *                            the parent to the child
	 * @return new success rating for the child
	 */
	private float calculateSuccessRating(final float parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final ConcreteEdge lastEdge) {
		float successFactor = 1;
		for (final var node : lastEdge.getPath()) {
			if (relativeRound >= minSteps.getValue(node.getPosition()))
				successFactor = Math.min(successFactor, 1 - probabilities.getValue(node.getPosition()));
		}
		return parentSuccessRating * successFactor;
	}

	/**
	 * Calculates the cut off rating based on the probabilities on the short tail.
	 * 
	 * @param probabilities {@link FloatMatrix} with enemy probabilities to consider
	 * @param minSteps      {@link FloatMatrix} with the enemy minimum steps to
	 *                      consider
	 * @param lastEdge      {@link ConcreteEdge Edge} of the last step from the
	 *                      parent to the child
	 * @return new cut off rating for the child
	 */
	private float calculateCutOffRating(final FloatMatrix probabilities, final FloatMatrix minSteps,
			final ConcreteEdge lastEdge) {

		float cutOff = 0;
		for (final var node : lastEdge.getPath()) {
			if (relativeRound < minSteps.getValue(node.getPosition())) {
				cutOff = Math.max(cutOff, probabilities.getValue(node.getPosition()));
			}
		}
		return cutOff;
	}

	/**
	 * Adds an edge to the Edge-Tail of this Player
	 * 
	 * @param edge the new passed edge
	 * @return true if the edge was added successfully. false if the edge intersects
	 *         another passed edge
	 */
	private boolean addEdge(ConcreteEdge edge) {

		for (var tailPart : this.edgeTail) {
			if (tailPart.intersect(edge))
				return false;
		}

		this.edgeTail.add(edge);
		this.position = edge.getEndNode().getPosition();
		return true;
	}

	/**
	 * Calculates the Success and CutOff Rating with the given Parameters
	 * 
	 * @param parentSuccessRating The parents successration (base for the childs
	 *                            rating)
	 * @param probabilities       The enemy probabilities matrix
	 * @param minSteps            the min-steps-matrix
	 * @param newEdge             the new passsed edge
	 */
	private void calculateRating(final float parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final ConcreteEdge newEdge) {
		if (isActive()) {
			this.successRating = calculateSuccessRating(parentSuccessRating, probabilities, minSteps, newEdge);
			this.cutOffRating = calculateCutOffRating(probabilities, minSteps, newEdge);
		} else {
			this.successRating = 0;
			this.cutOffRating = 0;
		}
	}

	/**
	 * Adds an Edge to the Edge-Tail (if possible) and recalculates the Succedd- &
	 * Cut-Off ratings, if the Edge-addition was successfull
	 * 
	 * @param parentSuccessRating The parents successration (base for the childs
	 *                            rating)
	 * @param probabilities       The enemy probabilities matrix
	 * @param minSteps            the min-steps-matrix
	 * @param edge                the new passsed edge
	 * @return true if the edge has been added and the results are updated. false if
	 *         the edge has not been added and the results have not changed
	 */
	public boolean addEdgeAndCalculateRating(final float parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final ConcreteEdge edge) {
		if (addEdge(edge)) {
			calculateRating(parentSuccessRating, probabilities, minSteps, edge);
			return true;
		}
		return false;
	}

	/**
	 * Returns the success rating.
	 * 
	 * @return success rating
	 */
	public float getSuccessRating() {
		return successRating;
	}

	/**
	 * Returns the cut off rating.
	 * 
	 * @return cut off rating
	 */
	public float getCutOffRating() {
		return cutOffRating;
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
	 * Returns the action with which the path was started to reach this state
	 * 
	 * @return The initial playeraction for the path that contains this Player
	 */
	public PlayerAction getInitialAction() {
		return this.initialAction;
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