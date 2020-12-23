package player.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.Arrays;
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
 * Player class which implements the {@link IPlayer} interface. The player can
 * be used to predict movements on a {@link Board}.
 */
public class PredictivePlayer implements IPlayer {

	private final int playerId;
	private final PlayerDirection direction;
	private final int speed;
	private final Point2i position;
	private final boolean active;
	private final int round;

	private final List<Point2i> shortTail;
	private final Set<Point2i> longTail;

	/**
	 * Initializes a new {@link PredictivePlayer} from a {@link IPlayer}.
	 * 
	 * @param player abstract {@link IPlayer} as initialization base
	 * @param round  round the {@link IPlayer} is relevant for
	 */
	public PredictivePlayer(final IPlayer player) {

		this.playerId = player.getPlayerId();
		this.direction = player.getDirection();
		this.speed = player.getSpeed();
		this.position = player.getPosition();
		this.active = player.isActive();
		this.round = player.getRound();

		this.shortTail = new ArrayList<>();
		this.longTail = new HashSet<>();
	}

	/**
	 * Creates a new {@link PredictivePlayer} as child of another
	 * {@link PredictivePlayer} with a given {@link PlayerAction}. The
	 * {@link PlayerAction} will be validated on the given {@link Board}.
	 * 
	 * @param parent {@link PredictivePlayer parent} of the object
	 * @param action {@link PlayerAction} to generate the child with
	 * @param board  {@link Board} to validate the action on
	 */
	public PredictivePlayer(final PredictivePlayer parent, final PlayerAction action, final Board<Cell> board) {

		this.playerId = parent.getPlayerId();
		this.direction = parent.getDirection().doAction(action);
		this.speed = calculateChildSpeed(action);
		this.position = calculateChildPosition(direction, speed);
		this.round = parent.getRound() + 1;

		this.shortTail = calculateShortTail(parent.getPosition(), position);

		final Set<Point2i> parentLongTail = parent.getLongTail();
		parentLongTail.addAll(parent.getShortTail());
		this.longTail = parentLongTail;

		this.active = parent.isActive() && isSpeedValid() && isOnBoard(board) && (!isTailColliding());

	}

	/**
	 * Calculates the target speed of the {@link PredictivePlayer} in the next game
	 * step.
	 * 
	 * @param action {@link PlayerAction} action to calculate with
	 * @return speed after the action is performed
	 */
	private int calculateChildSpeed(final PlayerAction action) {
		int nextSpeed = this.speed;
		if (action == PlayerAction.SPEED_UP)
			nextSpeed += 1;
		else if (action == PlayerAction.SLOW_DOWN)
			nextSpeed -= 1;
		return nextSpeed;
	}

	/**
	 * Calculates the target position of the child with a given
	 * {@link PlayerDirection} and the speed the child moves with.
	 * 
	 * @param childDirection {@link PlayerDirection} the child moves in
	 * @param childSpeed     speed the child moves with
	 * @return {@link Point2i} of the edge end position
	 */
	private Point2i calculateChildPosition(final PlayerDirection childDirection, final int childSpeed) {
		final Vector2i childMovementVector = childDirection.getDirectionVector().multiply(childSpeed);
		final Point2i childPosition = position.translate(childMovementVector);
		return childPosition;
	}

	/**
	 * Calculates the short tail of the {@link PreddictivePlayer} as {@link List} of
	 * {@link Point2i points}.
	 * 
	 * @param startPosition  {@link Point2i} position of the parent
	 * @param targetPosition {@link Point2i} position of the child
	 * @return {@link Point2i points} of the tail as {@link List}
	 */
	private List<Point2i> calculateShortTail(final Point2i startPosition, final Point2i targetPosition) {
		final List<Point2i> points = startPosition.pointsInRectangle(targetPosition);
		if (round % 6 == 0 && speed > 2) {
			return Arrays.asList(points.get(0), points.get(points.size() - 1));
		}
		return points;
	}

	/**
	 * @return true if the speed has a valid value
	 */
	private boolean isSpeedValid() {
		if (speed > IPlayer.MAX_SPEED)
			return false;
		if (speed < IPlayer.MIN_SPEED)
			return false;
		else
			return true;
	}

	/**
	 * Validates a {@link PredictivePlayer} on the given {@link Board}.
	 * 
	 * @param board {@link Board} to validate on
	 * @return result of the calculation
	 */
	private boolean isOnBoard(final Board<Cell> board) {
		return board.isOnBoard(position);
	}

	/**
	 * Tests if the short tail is colliding with the long tail.
	 * 
	 * @return true if a self collision is detected
	 */
	private boolean isTailColliding() {
		for (final Point2i point : shortTail) {
			if (longTail.contains(point))
				return true;
		}
		return false;
	}

	/**
	 * @return the shortTail
	 */
	public List<Point2i> getShortTail() {
		return shortTail;
	}

	/**
	 * @return the longTail
	 */
	public Set<Point2i> getLongTail() {
		return longTail;
	}

	@Override
	public int getRound() {
		return round;
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
	public boolean isActive() {
		return active;
	}

}
