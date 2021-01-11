package solver.reachablepoints.graph.board;

import java.util.Objects;
import java.util.Map.Entry;

import utility.game.board.*;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * Node
 */
public class Node implements IBoardCell<CellValue> {

	private static final int PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT = Integer
			.highestOneBit(PlayerDirection.values().length - 1);

	private final Graph graph;
	private final Point2i position;
	private IEdge[] edges;
	private CellValue value;

	/**
	 * Initilizes a {@link Node} with all possible {@link IEdge Edges}
	 * 
	 * @param graph    The {@link Graph} with all {@link Node Nodes}
	 * @param position the position where the {@link Node} is located on the
	 *                 {@link Board}
	 * @param edges    All {@link IEdge Edges} that start at this Node.
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
	 * Determines by a Players Location and State the edge he will travel
	 * 
	 * @param direction The Players {@link PlayerDirection#PlayerDirection
	 *                  Direction}
	 * @param doJump    true for jump over the cells (when round % 6 == 0), else
	 *                  false
	 * @param speed     The Players Speed (1 &lt;= speed &gt;= 10)
	 * @return The Concrete Edge or null if the requested Edge is not passable
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

	public void setEdge(PlayerDirection direction, boolean doJump, int speed, ConcreteEdge edge) {
		setEdge(getIntegerIndex(direction, doJump, speed), edge);
	}

	public void setEdge(int edgeIntegerIndex, ConcreteEdge edge) {
		if (this.edges != null)
			this.edges[edgeIntegerIndex] = edge;
	}

	/**
	 * Calculates the integer index for the edge with the passed parameters
	 * 
	 * @param direction the {@link PlayerDirection}
	 * @param doJump    whether a jump is done
	 * @param speed     the speed
	 * @return edge-Array-index for the edge matching the passed parameters
	 */
	public static Integer getIntegerIndex(PlayerDirection direction, boolean doJump, int speed) {
		int index = direction.ordinal();
		index += (doJump ? 1 : 0) << PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT;
		index += (speed - 1) << (PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT + 1);
		return index;
	}

	public CellValue getCellValue() {
		return value;
	}

	/**
	 * Sets the value of the Cell and deletes all affected Edges
	 * 
	 * @param value The new Value for the Cell ({@link CellValue#EMPTY_CELL} is not
	 *              possible)
	 * 
	 */
	public void setCellValue(CellValue value) {
		setCellValue(value, true);
	}

	/**
	 * Sets the value of the Cell and deletes affected Edges
	 * 
	 * @param value                   The new Value for the Cell
	 *                                ({@link CellValue#EMPTY_CELL} is not possible)
	 * @param removeEdgesStartingHere Specifies whether edges that start at this
	 *                                node should also be removed. The edges must be
	 *                                kept for the time being if there is currently
	 *                                a player there.
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
