package player.analysis.graph;

import java.util.ArrayList;
import java.util.List;

import player.boardevaluation.graph.ConcreteEdge;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * RatedPredictiveGraphPlayer for success and cutting off enemies ratings, based
 * on the graph board.
 */
public class RatedPredictiveGraphPlayer implements IPlayer {

	private final int playerId;
	private final PlayerDirection direction;
	private final int speed;
	private Point2i position;
	private boolean active;
	private final int round;
	private final PlayerAction initialAction;
	private final List<ConcreteEdge> edgeTail;
	private final int relativeRound;
	private float successRating;
	private float cutOffRating;

	/**
	 * Creates a new {@link RatedPredictiveGraphPlayer} from a standard
	 * {@link IPlayer} instance.
	 * 
	 * @param player {@link IPlayer} to initialize
	 */
	public RatedPredictiveGraphPlayer(final IPlayer player) {
		this.playerId = player.getPlayerId();
		this.direction = player.getDirection();
		this.speed = player.getSpeed();
		this.position = player.getPosition();
		this.active = player.isActive();
		this.round = player.getRound();
		this.successRating = 1;
		this.cutOffRating = 0;
		this.relativeRound = 0;
		this.initialAction = null;
		this.edgeTail = new ArrayList<>();
	}

	private RatedPredictiveGraphPlayer(RatedPredictiveGraphPlayer parent, PlayerAction initAction, int speed,
			PlayerDirection direction) {

		this.playerId = parent.getPlayerId();
		this.position = parent.position;
		this.speed = speed;
		this.direction = direction;

		this.round = parent.getRound();
		this.initialAction = initAction;
		this.edgeTail = new ArrayList<>();
		this.relativeRound = parent.getRelativeRound() + 1;

		this.active = parent.isActive();

	}

	/**
	 * Calculate a child by doing a specific action
	 * 
	 * @param doAction the actio to do
	 * @return A Child that has the action applied or null if the child would die
	 */
	public RatedPredictiveGraphPlayer calculateChild(PlayerAction doAction) {
		int childSpeed = this.getSpeed();
		PlayerDirection childDirection = this.getDirection();
		if (doAction == PlayerAction.SPEED_UP && childSpeed < 10) {
			childSpeed++;
		} else if (doAction == PlayerAction.SLOW_DOWN && childSpeed > 1) {
			childSpeed--;
		} else if (doAction == PlayerAction.TURN_RIGHT || doAction == PlayerAction.TURN_LEFT
				|| doAction == PlayerAction.CHANGE_NOTHING) {
			childDirection = childDirection.doAction(doAction);
		} else {
			return null;
		}

		return new RatedPredictiveGraphPlayer(this, doAction, childSpeed, childDirection);
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
