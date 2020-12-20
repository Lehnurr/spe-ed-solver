package player.boardevaluation.graph;

import utility.game.board.*;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Node
 */
public class Node implements IBoardCell<Cell> {

    private static final int PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT = Integer
            .highestOneBit(PlayerDirection.values().length - 1);

    private final Board<Node> board;
    private final Point2i position;
    private final IEdge[] edges;
    private int value;

    /**
     * Initilizes a {@link Node} with all possible {@link IEdge Edges}
     * 
     * @param board    The {@link Board} with all {@link Node Nodes}
     * @param position the position where the {@link Node} is located on the
     *                 {@link Board}
     * @param edges    All {@link IEdge Edges} that start at this Node.
     */
    public Node(final Board<Node> board, final Point2i position, final IEdge[] edges) {
        this.board = board;
        this.position = position;
        this.edges = edges;
    }

    public Point2i getPosition() {
        return this.position;
    }

    public ConcreteEdge getEdge(PlayerDirection direction, boolean doJump, int speed) {
        IEdge edge = this.edges[getIntegerIndex(direction, doJump, speed)];

        if (edge instanceof AbstractEdge) {
            edge = ((AbstractEdge) edge).calculatePath(board, this, direction);
            setEdge(direction, doJump, speed, (ConcreteEdge) edge);
        }

        return (ConcreteEdge) edge;
    }

    public void setEdge(PlayerDirection direction, boolean doJump, int speed, ConcreteEdge edge) {
        this.edges[getIntegerIndex(direction, doJump, speed)] = edge;
    }

    /**
     * Calculates the integer index for the edge with the passed parameters
     * 
     * @return edge-Array-index for the edge matching the passed parameters
     */
    private static Integer getIntegerIndex(PlayerDirection direction, boolean doJump, int speed) {
        int index = direction.ordinal();
        index += (doJump ? 1 : 0) << PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT;
        index += (speed - 1) << (PLAYER_DIRECTION_ORDINAL_HIGHEST_ONE_BIT + 1);
        return index;
    }

    public Cell getCellValue() {
        return new Cell(value);
    }

    public void setCellValue(Cell value) {
        // if (this.value != value && value != 0) {
        // TODO: Destroy edges
        // destroy all edges starting here
        // destroy all edges ending here (inverted edges)
        // destroy all edges that pass this node (maybe save performance wehn doing the
        // half + inversions) (LUT)
        // }

        // this.value = value;

    }

}
