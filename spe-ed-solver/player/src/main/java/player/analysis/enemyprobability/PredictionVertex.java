package player.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Vertices which represent a graph of a spe_ed player moving on the board. The
 * {@link PredictionVertex PredictionVertices} are connected by
 * {@link PredictionEdge PredictionEdges}.
 */
public class PredictionVertex {

	private final Board<Cell> board;

	private final Point2i position;
	private final PlayerDirection direction;
	private final int speed;

	private final int round;

	private final Set<Point2i> requiredPoints;

	/**
	 * Creates a new {@link PredictionVertex} with the given information.
	 * 
	 * @param board     {@link Board} to test for collisions with
	 * @param position  {@link Point2i} of the {@link PredictionVertex} position
	 * @param direction input {@link PlayerDirection} of the vertex
	 * @param speed     input speed of the vertex
	 * @param round     round the vertex is relevant for
	 */
	public PredictionVertex(final Board<Cell> board, final Point2i position, final PlayerDirection direction,
			final int speed, final int round) {
		this.board = board;
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		this.round = round;
		this.requiredPoints = Collections.emptySet();
	}

	/**
	 * Creates a new {@link PredictionVertex} with the given information.
	 * 
	 * @param board          {@link Board} to test for collisions with
	 * @param position       {@link Point2i} of the {@link PredictionVertex}
	 *                       position
	 * @param direction      input {@link PlayerDirection} of the vertex
	 * @param speed          input speed of the vertex
	 * @param round          round the vertex is relevant for
	 * @param requiredPoints {@link Point2i} points required to reach this vertex
	 */
	public PredictionVertex(final Board<Cell> board, final Point2i position, final PlayerDirection direction,
			final int speed, final int round, final Set<Point2i> requiredPoints) {
		this.board = board;
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		this.round = round;
		this.requiredPoints = Collections.emptySet();
	}

	/**
	 * Creates a new {@link PredictionVertex} with the given information.
	 * 
	 * @param board  {@link Board} to test for collisions with
	 * @param player {@link IPlayer} to initiate the vertex with
	 * @param round  round the vertex is relevant for
	 */
	public PredictionVertex(final Board<Cell> board, final IPlayer player, final int round) {
		this.board = board;
		this.position = player.getPosition();
		this.direction = player.getDirection();
		this.speed = player.getSpeed();
		this.round = round;
		this.requiredPoints = Collections.emptySet();
	}

	public List<PredictionEdge> getValidEdges() {

		final List<PredictionEdge> result = new ArrayList<>(PlayerAction.values().length);

		for (final PlayerAction action : PlayerAction.values()) {
			final PredictionEdge predictionEdge = new PredictionEdge(board, this, action);
			if (predictionEdge.isValid()) {
				result.add(predictionEdge);
			}
		}

		return result;
	}

	/**
	 * @return the position
	 */
	public Point2i getPosition() {
		return position;
	}

	/**
	 * @return the direction
	 */
	public PlayerDirection getDirection() {
		return direction;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @return the requiredPoints
	 */
	public Set<Point2i> getRequiredPoints() {
		return requiredPoints;
	}

}
