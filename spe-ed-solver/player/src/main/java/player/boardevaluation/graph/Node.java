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
    private final IEdge[] edges;
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

            // TODO: .....
            // destroy all edges starting here
            // destroy all edges ending here (inverted edges)
            // destroy all edges that pass this node (maybe save performance when doing the
            // half + inversions) (LUT)
            //

            // alle llöschen die hier starten
            // lookuptabelle für welcher node und welche kante gelöscht werden muss!

            this.value = value;
        }
    }

    public boolean isEmpty() {
        return value == CellValue.EMPTY_CELL;
    }

}
