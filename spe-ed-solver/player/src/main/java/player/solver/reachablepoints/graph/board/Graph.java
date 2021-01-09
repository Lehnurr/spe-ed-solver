package player.solver.reachablepoints.graph.board;

import java.util.Arrays;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.geometry.Point2i;

public class Graph extends Board<Node> {

    public Graph(Node[][] emptyNodes) {
        super(emptyNodes);

        // initilize the graph with the same abstract edges for all Nodes
        var defaultAbstractEdge = new AbstractEdge();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {

                var nodePosition = new Point2i(col, row);
                // magic number
                IEdge[] abstractEdges = new IEdge[80];
                Arrays.fill(abstractEdges, defaultAbstractEdge);
                var node = new Node(this, nodePosition, abstractEdges);

                emptyNodes[row][col] = node;
            }
        }
    }

    /**
     * Removes all unavailable edges from the graph
     * 
     * @param board   the new Gameboard with the updated cells
     * @param self    the current Player
     * @param enemies the enemies
     */
    public void updateGraph(Board<Cell> board, final IPlayer self, final Iterable<IPlayer> enemies) {

        // remove outgoing and incoming edges for the passed steps
        // (excluding the new end position, but including the previous end position)
        var selfCellValue = CellValue.fromInteger(self.getPlayerId());
        for (int i = 1; i <= self.getSpeed(); i++) {
            var speedDirectionVector = self.getDirection().getDirectionVector().multiply(-1 * i);
            var affectedPosition = self.getPosition().translate(speedDirectionVector);
            var affectedNode = getBoardCellAt(affectedPosition);

            // set the node value, if the cell is passed by any player (otherwise the cell
            // was not passed)
            if (affectedNode != null && board.getBoardCellAt(affectedPosition).getCellValue() != CellValue.EMPTY_CELL)
                affectedNode.setCellValue(selfCellValue, true);
        }

        // remove all incomming edges for the current player position
        getBoardCellAt(self.getPosition()).setCellValue(selfCellValue, false);

        // remove all outgoing and incoming edges for the passed steps by enemies
        for (var player : enemies) {
            final CellValue enemyCellValue = CellValue.fromInteger(player.getPlayerId());

            // remove outgoing and incoming edges for the passed steps
            for (int i = 0; i <= player.getSpeed(); i++) {
                var speedDirectionVector = player.getDirection().getDirectionVector().multiply(-1 * i);
                var affectedPosition = player.getPosition().translate(speedDirectionVector);
                var affectedNode = getBoardCellAt(affectedPosition);

                if (affectedNode == null)
                    continue;

                final CellValue affectedCellValue = board.getBoardCellAt(affectedPosition).getCellValue();

                if (affectedCellValue == enemyCellValue || affectedCellValue == CellValue.MULTIPLE_PLAYER)
                    affectedNode.setCellValue(enemyCellValue, true);
            }
        }
    }

}
