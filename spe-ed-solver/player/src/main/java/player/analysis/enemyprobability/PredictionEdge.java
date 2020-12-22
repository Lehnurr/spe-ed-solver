package player.analysis.enemyprobability;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

	private final PredictionVertex startVertex;
	private final PredictionVertex targetVertex;

	private final PlayerAction action;

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
		this.startVertex = startVertex;

		int nextSpeed = startVertex.getSpeed();
		if (action == PlayerAction.SPEED_UP)
			nextSpeed += 1;
		else if (action == PlayerAction.SLOW_DOWN)
			nextSpeed -= 1;

		PlayerDirection nextDirection = startVertex.getDirection().doAction(action);

		Vector2i movementVector = nextDirection.getDirectionVector().multiply(nextSpeed);
		Point2i targetPosition = startVertex.getPosition().translate(movementVector);

		if (nextSpeed > IPlayer.MAX_SPEED || nextSpeed < IPlayer.MIN_SPEED || board.isOnBoard(targetPosition)) {
			this.valid = false;
		} else {
			this.valid = true;
		}

		final int nextRound = startVertex.getRound() + 1;

		this.targetVertex = new PredictionVertex(board, targetPosition, nextDirection, nextSpeed, nextRound);
	}

	/**
	 * Returns all {@link Point2i Point2i}s of the {@link PredictionEdge}. If the
	 * {@link PredictionEdge} is not valid, an empty collection is returned.
	 * 
	 * @return {@link Point2i}s of the edge
	 */
	public Collection<Point2i> getPoints() {
		if (valid) {
			final Vector2i directionVector = targetVertex.getDirection().getDirectionVector();
			final Point2i firstPointInDirection = startVertex.getPosition().translate(directionVector);
			final List<Point2i> points = firstPointInDirection.pointsInRectangle(targetVertex.getPosition());
			if (targetVertex.getRound() % 6 == 0 && targetVertex.getSpeed() > 2) {
				points.remove(points.size() - 1);
				points.remove(0);
			}
			return points;
		} else {
			return Collections.emptySet();
		}
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
