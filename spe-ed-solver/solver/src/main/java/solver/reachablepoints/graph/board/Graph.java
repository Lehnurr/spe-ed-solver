package solver.reachablepoints.graph.board;

import java.util.Arrays;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.board.IBoardCell;
import utility.game.player.IPlayer;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * A {@link Board board} that interprets the game as a graph. the
 * {@link IBoardCell cells} are {@link Node nodes} and an {@link ConcreteEdge
 * edge} is a possible path that a player can travel.
 */
public class Graph extends Board<Node> {

    /**
     * Initilizes a {@link Graph graph board}.
     * 
     * @param emptyNodes a 2D-Array of empty {@link Node nodes} to fill with the
     *                   actual {@link Node nodes}
     */
    public Graph(Node[][] emptyNodes) {
        super(emptyNodes);

        // initilize the graph with the same abstract edges for all Nodes
        final AbstractEdge defaultAbstractEdge = new AbstractEdge();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {

                final Point2i nodePosition = new Point2i(col, row);
                // magic number
                final IEdge[] abstractEdges = new IEdge[80];
                Arrays.fill(abstractEdges, defaultAbstractEdge);
                final Node node = new Node(this, nodePosition, abstractEdges);

                emptyNodes[row][col] = node;
            }
        }
    }

    /**
     * Removes all unavailable {@link ConcreteEdge edges} from the {@link Graph
     * graph}.
     * 
     * @param board   the new {@link Board game board} with the updated {@link Cell
     *                cells}
     * @param self    the current {@link IPlayer player}
     * @param enemies the enemies of current
     */
    public void updateGraph(final Board<Cell> board, final IPlayer self, final Iterable<IPlayer> enemies) {

        // remove outgoing and incoming edges for the passed steps
        // (excluding the new end position, but including the previous end position)
        final CellValue selfCellValue = CellValue.fromInteger(self.getPlayerId());
        for (int i = 1; i <= self.getSpeed(); i++) {
            final Vector2i speedDirectionVector = self.getDirection().getDirectionVector().multiply(-1 * i);
            final Point2i affectedPosition = self.getPosition().translate(speedDirectionVector);
            final Node affectedNode = getBoardCellAt(affectedPosition);

            // set the node value, if the cell is passed by any player (otherwise the cell
            // was not passed)
            if (affectedNode != null && board.getBoardCellAt(affectedPosition).getCellValue() != CellValue.EMPTY_CELL)
                affectedNode.setCellValue(selfCellValue, true);
        }

        // remove all incomming edges for the current player position
        getBoardCellAt(self.getPosition()).setCellValue(selfCellValue, false);

        // remove all outgoing and incoming edges for the passed steps by enemies
        for (final IPlayer player : enemies) {
            final CellValue enemyCellValue = CellValue.fromInteger(player.getPlayerId());

            // remove outgoing and incoming edges for the passed steps
            for (int i = 0; i <= player.getSpeed(); i++) {
                final Vector2i speedDirectionVector = player.getDirection().getDirectionVector().multiply(-1 * i);
                final Point2i affectedPosition = player.getPosition().translate(speedDirectionVector);
                final Node affectedNode = getBoardCellAt(affectedPosition);

                if (affectedNode == null)
                    continue;

                final CellValue affectedCellValue = board.getBoardCellAt(affectedPosition).getCellValue();

                if (affectedCellValue == enemyCellValue || affectedCellValue == CellValue.MULTIPLE_PLAYER)
                    affectedNode.setCellValue(enemyCellValue, true);
            }
        }
    }

}
