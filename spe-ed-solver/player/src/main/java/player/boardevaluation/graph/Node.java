package player.boardevaluation.graph;

import utility.game.board.*;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Node
 */
public class Node implements IBoardCell<CellValue> {

    private static final int PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT = Integer
            .highestOneBit(PlayerDirection.values().length - 1);

    private final Board<Node> board;
    private final Point2i position;
    private IEdge[] edges;
    private CellValue value;

    /**
     * Initilizes a {@link Node} with all possible {@link IEdge Edges}
     * 
     * @param graphBoard The {@link Board} with all {@link Node Nodes}
     * @param position   the position where the {@link Node} is located on the
     *                   {@link Board}
     * @param edges      All {@link IEdge Edges} that start at this Node.
     */
    public Node(final Board<Node> graphBoard, final Point2i position, final IEdge[] edges) {
        this.board = graphBoard;
        this.position = position;
        this.edges = edges;
    }

    public Point2i getPosition() {
        return this.position;
    }

    /**
     * Determines by a Players Location & State the edge he will travel
     * 
     * @param direction The Players {@link PlayerDirection#PlayerDirection
     *                  Direction}
     * @param doJump    true for jump over the cells (when round % 6 == 0), else
     *                  false
     * @param speed     The Players Speed (1 <= speed <= 10)
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
            edge = ((AbstractEdge) edge).calculatePath(board, this, direction);
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
     * Sets the value of the Cell and deletes affected Edges
     * 
     * @param value The new Value for the Cell ({@link CellValue#EMPTY_CELL} is not
     *              possible)
     */
    public void setCellValue(CellValue value) {
        if (this.value != value && value != CellValue.EMPTY_CELL) {

            // Destroy all edges that pass this node or end here
            final var passingEdges = AffectedEdgesLookUpTable.getAbstractPassingEdges();
            for (var edge : passingEdges) {
                Point2i edgeStartingPosition = this.position.translate(edge.getKey());
                Node edgeStartingNode = board.getBoardCellAt(edgeStartingPosition);
                for (int edgeIndex : edge.getValue())
                    edgeStartingNode.setEdge(edgeIndex, null);
            }

            // If the value is not empty, this node will not return any edges
            this.edges = null;
            this.value = value;
        }
    }

    public boolean isEmpty() {
        return value == CellValue.EMPTY_CELL;
    }

}
