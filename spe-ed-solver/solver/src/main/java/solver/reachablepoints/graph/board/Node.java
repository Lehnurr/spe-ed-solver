package solver.reachablepoints.graph.board;

import java.util.Objects;
import java.util.Map.Entry;

import utility.game.board.*;
import utility.game.player.IPlayer;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * A {@link IBoardCell cell} of a {@link Board board} that holds information
 * about all possible paths starting here. A path is represented as an
 * {@link ConcreteEdge edge}. All {@link ConcreteEdge edges} are hold in an
 * array and the index of the edge indicates the speed, direction and
 * round-number you need to travel by this {@link ConcreteEdge edge}
 */
public class Node implements IBoardCell<CellValue> {

	private static final int PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT = Integer
			.highestOneBit(PlayerDirection.values().length - 1);

	private final Graph graph;
	private final Point2i position;
	private IEdge[] edges;
	private CellValue value;

	/**
	 * Initilizes a {@link Node} with all possible {@link IEdge edges}.
	 * 
	 * @param graph    the {@link Graph} with all {@link Node nodes}
	 * @param position the {@link Point2i position} where the {@link Node} is
	 *                 located on the {@link Board}
	 * @param edges    all {@link IEdge edges} that start at this {@link Node
	 *                 nodes}.
	 */
	public Node(final Graph graph, final Point2i position, final IEdge[] edges) {
		this.graph = graph;
		this.position = position;
		this.edges = edges;
		this.value = CellValue.EMPTY_CELL;
	}

	public Point2i getPosition() {
		return this.position;
	}

	/**
	 * Determines by a {@link IPlayer players} {@link Point2i location} and State
	 * the {@link ConcreteEdge edge} he will travel.
	 * 
	 * @param direction the {@link IPlayer players}
	 *                  {@link PlayerDirection#PlayerDirection direction}
	 * @param doJump    true for jump over the cells (when round % 6 == 0), else
	 *                  false
	 * @param speed     the {@link IPlayer players} speed (1 &lt;= speed &gt;= 10)
	 * @return the {@link ConcreteEdge} or null if the requested {@link ConcreteEdge
	 *         edge} is not passable
	 */
	public ConcreteEdge getEdge(PlayerDirection direction, boolean doJump, int speed) {

		// if the edge is not available -> return null
		if (this.edges == null || value != CellValue.EMPTY_CELL) {
			return null;
		}

		IEdge edge = this.edges[getIntegerIndex(direction, doJump, speed)];

		// If any passed Node is not Empty -> return null
		if (edge == null) {
			return null;
		}

		if (edge instanceof AbstractEdge) {
			edge = ((AbstractEdge) edge).calculatePath(graph, this, direction, doJump, speed);
			setEdge(direction, doJump, speed, (ConcreteEdge) edge);
		}

		return (ConcreteEdge) edge;

	}

	/**
	 * Sets an {@link ConcreteEdge} for this {@link Node node}.
	 * 
	 * @param direction the {@link IPlayer players}
	 *                  {@link PlayerDirection#PlayerDirection direction}
	 * @param doJump    true for jump over the cells (when round % 6 == 0), else
	 *                  false
	 * @param speed     the {@link IPlayer players} speed (1 &lt;= speed &gt;= 10)
	 * @param edge      the new {@link ConcreteEdge} to set for the given
	 *                  specifications
	 */
	public void setEdge(PlayerDirection direction, boolean doJump, int speed, ConcreteEdge edge) {
		setEdge(getIntegerIndex(direction, doJump, speed), edge);
	}

	/**
	 * Sets an {@link ConcreteEdge} for this {@link Node node}.
	 * 
	 * @param edgeIntegerIndex the Edge-Index calculated by
	 *                         {@link Node#getIntegerIndex(PlayerDirection, boolean, int)
	 *                         getIntegerIndex()}
	 * @param edge             the new {@link ConcreteEdge} to set for the given
	 *                         index
	 */
	public void setEdge(int edgeIntegerIndex, ConcreteEdge edge) {
		if (this.edges != null)
			this.edges[edgeIntegerIndex] = edge;
	}

	/**
	 * Calculates the integer index for the edge with the passed parameters.
	 * 
	 * @param direction the {@link PlayerDirection}
	 * @param doJump    whether a jump is done
	 * @param speed     the speed
	 * @return edge-Array-index for the {@link ConcreteEdge} matching the passed
	 *         parameters
	 */
	public static Integer getIntegerIndex(PlayerDirection direction, boolean doJump, int speed) {
		int index = direction.ordinal();
		index += (doJump ? 1 : 0) << PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT;
		index += (speed - 1) << (PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT + 1);
		return index;
	}

	@Override
	public CellValue getCellValue() {
		return value;
	}

	@Override
	public void setCellValue(CellValue value) {
		setCellValue(value, true);
	}

	/**
	 * Sets the value of the Cell and deletes affected Edges
	 * 
	 * @param value                   the new value for the {@link Node node}
	 *                                ({@link CellValue#EMPTY_CELL} is not possible)
	 * @param removeEdgesStartingHere specifies whether {@link ConcreteEdge edges}
	 *                                that start at this {@link Node node} should
	 *                                also be removed. The {@link ConcreteEdge
	 *                                edges} must be kept for the time being if
	 *                                there is currently a player there
	 */
	public void setCellValue(CellValue value, boolean removeEdgesStartingHere) {
		if (this.value != value && value != CellValue.EMPTY_CELL) {

			// Destroy all edges that pass this node or end here
			for (final Entry<Vector2i, int[]> edge : AffectedEdgesLookUpTable.getAbstractPassingEdges()) {
				Point2i edgeStartingPosition = this.position.translate(edge.getKey());
				Node edgeStartingNode = graph.getBoardCellAt(edgeStartingPosition);

				if (edgeStartingNode == null)
					continue;

				for (int edgeIndex : edge.getValue()) {
					edgeStartingNode.setEdge(edgeIndex, null);
				}
			}
		}

		if (removeEdgesStartingHere && value != CellValue.EMPTY_CELL) {
			if (this.value != CellValue.EMPTY_CELL)
				this.value = CellValue.MULTIPLE_PLAYER;
			else
				this.value = value;
			this.edges = null;
		}
	}

	@Override
	public boolean isEmpty() {
		return value == CellValue.EMPTY_CELL;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Node)) {
			return false;
		}
		Node node = (Node) o;
		return Objects.equals(graph, node.graph) && Objects.equals(position, node.position)
				&& Objects.equals(value, node.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(graph, position, value);
	}

}
