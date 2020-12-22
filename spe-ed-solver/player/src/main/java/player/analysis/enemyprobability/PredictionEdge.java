package player.analysis.enemyprobability;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * Edge of a graph consisting out of {@link PredictionVertex
 * PredictionVertices}.
 */
public class PredictionEdge {

	private final PredictionVertex targetVertex;

	private final PlayerAction action;

	private final Collection<Point2i> points;

	private final boolean valid;

	/**
	 * Creates a new {@link PredictionEdge} for the graph with a given
	 * {@link PlayerAction}.
	 * 
	 * @param board       {@link Board} board to check for collisions
	 * @param startVertex start {@link PredictionVertex} of the edge
	 * @param action      {@link PlayerAction} which causes this edge
	 */
	public PredictionEdge(final Board<Cell> board, final PredictionVertex startVertex, final PlayerAction action) {

		this.action = action;

		final int targetSpeed = calculateTargetSpeed(startVertex.getSpeed(), action);
		final PlayerDirection targetDirection = startVertex.getDirection().doAction(action);
		final Point2i targetPosition = calculateTargetPoint(startVertex.getPosition(), targetDirection, targetSpeed);
		final int targetRound = startVertex.getRound() + 1;

		this.points = calculatePoints(targetDirection, startVertex.getPosition(), targetPosition, targetSpeed,
				targetRound);

		final Set<Point2i> targetRequiredPoints = new HashSet<>(startVertex.getRequiredPoints());
		targetRequiredPoints.addAll(this.points);

		this.targetVertex = new PredictionVertex(board, targetPosition, targetDirection, targetSpeed, targetRound,
				targetRequiredPoints);

		this.valid = calculateValid(board, targetSpeed, targetPosition, this.points, startVertex.getRequiredPoints());
	}

	/**
	 * Calculates the target speed of the edge out of the base speed and a given
	 * {@link PlayerAction}.
	 * 
	 * @param startSpeed speed before the action is performed
	 * @param action     {@link PlayerAction} action to calculate with
	 * @return speed after the action is performed
	 */
	private int calculateTargetSpeed(final int startSpeed, final PlayerAction action) {
		int nextSpeed = startSpeed;
		if (action == PlayerAction.SPEED_UP)
			nextSpeed += 1;
		else if (action == PlayerAction.SLOW_DOWN)
			nextSpeed -= 1;
		return nextSpeed;
	}

	/**
	 * Calculates the target position of the edge with a given
	 * {@link PlayerDirection} and the speed the player moves with.
	 * 
	 * @param startPoint {@link Point2i} of the edge start position
	 * @param direction  {@link PlayerDirection} the player moves in
	 * @param speed      speed the player moves with
	 * @return {@link Point2i} of the edge end position
	 */
	private Point2i calculateTargetPoint(final Point2i startPoint, final PlayerDirection direction, final int speed) {
		final Vector2i movementVector = direction.getDirectionVector().multiply(speed);
		final Point2i targetPosition = startPoint.translate(movementVector);
		return targetPosition;
	}

	/**
	 * Calculates all points contained in the {@link PredictionEdge}. It is not
	 * granted that all {@link Point2i points} are on the {@link Board}.
	 * 
	 * @param targetDirection {@link PlayerDirection} the direction after the edge
	 * @param startPosition   {@link Point2i} of the edge start position
	 * @param targetPosition  {@link Point2i} of the edge end position
	 * @param targetSpeed     speed the player has after the edge
	 * @param targetRound     round after the edge
	 * @return {@link Collection} of contained {@link Point2i points}
	 */
	private Collection<Point2i> calculatePoints(final PlayerDirection targetDirection, final Point2i startPosition,
			final Point2i targetPosition, final int targetSpeed, final int targetRound) {

		final Vector2i directionVector = targetDirection.getDirectionVector();
		final Point2i firstPointInDirection = startPosition.translate(directionVector);

		final List<Point2i> points = firstPointInDirection.pointsInRectangle(targetPosition);
		if (targetRound % 6 == 0 && targetSpeed > 2) {
			return Arrays.asList(points.get(0), points.get(points.size() - 1));
		}
		return points;
	}

	/**
	 * Checks if the {@link PredictionEdge} has a valid state. If valid, the
	 * {@link PredictionVertex targetVertex} can be reached on the given
	 * {@link Board}.
	 * 
	 * @param targetSpeed    speed after the edge
	 * @param board          {@link Board} to move on
	 * @param targetPosition {@link Point2i} position of the {@link PredictionVertex
	 *                       targetVertex}
	 * @param edgePoints     {@link Point2i points} of the edge
	 * @param collisionSet   {@link Point2i points} to check for additional
	 *                       collisions
	 * @return true if {@link PredictionVertex targetVertex} can be reached
	 */
	private boolean calculateValid(final Board<Cell> board, final int targetSpeed, final Point2i targetPosition,
			final Collection<Point2i> edgePoints, final Set<Point2i> collisionSet) {

		if (targetSpeed > IPlayer.MAX_SPEED && targetSpeed < IPlayer.MIN_SPEED) {
			return false;
		}
		if (!board.isOnBoard(targetPosition)) {
			return false;
		}
		return hasNoCollisions(board, edgePoints, collisionSet);
	}

	/**
	 * Checks if the {@link PredictionEdge} collides with its ancestors or with
	 * occupied cells on the {@link Board}.
	 * 
	 * @param board        {@link Board} to check for collisions
	 * @param edgePoints   {@link Point2i} points on the edge
	 * @param collisionSet {@link Point2i} points to check for additional collision
	 * @return true if no collision is detected
	 */
	private boolean hasNoCollisions(final Board<Cell> board, final Collection<Point2i> edgePoints,
			final Set<Point2i> collisionSet) {

		for (final Point2i point : edgePoints) {
			if (!board.getBoardCellAt(point).isEmpty())
				return false;
			if (collisionSet.contains(point))
				return false;
		}
		return true;
	}

	/**
	 * @return the action
	 */
	public PlayerAction getAction() {
		return action;
	}

	/**
	 * @return the targetVertex
	 */
	public PredictionVertex getTargetVertex() {
		return targetVertex;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

}
