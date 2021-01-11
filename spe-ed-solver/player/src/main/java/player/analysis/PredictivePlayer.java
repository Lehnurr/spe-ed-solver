package player.analysis;

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
		this.speed = calculateChildSpeed(parent.getSpeed(), action);
		this.position = calculateChildPosition(parent.getPosition(), direction, speed);
		this.round = parent.getRound() + 1;

		this.shortTail = calculateShortTail(parent.getPosition(), position, round, speed, direction);

		final Set<Point2i> parentLongTail = new HashSet<>(parent.getLongTail());
		parentLongTail.addAll(parent.getShortTail());
		this.longTail = parentLongTail;

		this.active = parent.isActive() && isSpeedValid(speed) && isOnBoard(board, position)
				&& (!isTailColliding(shortTail, longTail, board));

	}

	/**
	 * Calculates the target speed of the {@link PredictivePlayer} in the next game
	 * step.
	 * 
	 * @param parentSpeed speed of the parent
	 * @param action      {@link PlayerAction} action to calculate with
	 * @return speed after the action is performed
	 */
	private int calculateChildSpeed(final int parentSpeed, final PlayerAction action) {
		int nextSpeed = parentSpeed;
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
	 * @param parentPosition {@link Point2i} position of the parent
	 * @param childDirection {@link PlayerDirection} the child moves in
	 * @param childSpeed     speed the child moves with
	 * @return {@link Point2i} of the edge end position
	 */
	private Point2i calculateChildPosition(final Point2i parentPosition, final PlayerDirection childDirection,
			final int childSpeed) {
		final Vector2i childMovementVector = childDirection.getDirectionVector().multiply(childSpeed);
		final Point2i childPosition = parentPosition.translate(childMovementVector);
		return childPosition;
	}

	/**
	 * Calculates the short tail of the {@link PredictivePlayer} as {@link List} of
	 * {@link Point2i points}.
	 * 
	 * @param parentPosition  {@link Point2i} position of the parent
	 * @param targetPosition  {@link Point2i} position of the child
	 * @param targetRound     round of the player
	 * @param targetSpeed     speed of the player
	 * @param targetDirection {@link PlayerDirection} of the player
	 * @return {@link Point2i points} of the tail as {@link List}
	 */
	private List<Point2i> calculateShortTail(final Point2i parentPosition, final Point2i targetPosition,
			final int targetRound, final int targetSpeed, final PlayerDirection targetDirection) {
		final Point2i startPosition = parentPosition.translate(targetDirection.getDirectionVector());
		final List<Point2i> points = startPosition.pointsInRectangle(targetPosition);
		if (targetRound % 6 == 0 && targetSpeed > 2) {
			return Arrays.asList(points.get(0), points.get(points.size() - 1));
		}
		return points;
	}

	/**
	 * Tests if the given speed value is valid.
	 * 
	 * @param speed speed to test
	 * @return true if the speed has a valid value
	 */
	private boolean isSpeedValid(final int speed) {
		if (speed > IPlayer.MAX_SPEED)
			return false;
		if (speed < IPlayer.MIN_SPEED)
			return false;
		else
			return true;
	}

	/**
	 * Validates if a {@link Point2i} position is on the given {@link Board}.
	 * 
	 * @param board    {@link Board} to validate on
	 * @param position {@link Point2i} position on the {@link Board}
	 * @return result of the calculation
	 */
	private boolean isOnBoard(final Board<Cell> board, final Point2i position) {
		return board.isOnBoard(position);
	}

	/**
	 * Tests if the short tail is colliding with the long tail or the {@link Board}.
	 * 
	 * @param shortTail short tail as {@link List}
	 * @param longTail  long tail as {@link Set}
	 * @param board     the {@link Board} to check for collisions
	 * @return true if a self collision is detected
	 */
	private boolean isTailColliding(final List<Point2i> shortTail, final Set<Point2i> longTail,
			final Board<Cell> board) {
		for (final Point2i point : shortTail) {
			if (longTail.contains(point))
				return true;
			if (board.isOnBoard(point)) {
				if (!board.getBoardCellAt(point).isEmpty())
					return true;
			}
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
