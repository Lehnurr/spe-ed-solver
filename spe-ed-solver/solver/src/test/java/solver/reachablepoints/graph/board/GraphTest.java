package solver.reachablepoints.graph.board;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import solver.MockPlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

public class GraphTest {
    @Test
    public void testUpdateGraph() {
        // Generate graph for testing
        Graph graph = new Graph(new Node[5][5]);

        // Generate board for testing
        var boardCells = new Cell[5][5];
        for (int i = 0; i < boardCells.length; i++)
            for (int j = 0; j < boardCells[i].length; j++)
                boardCells[i][j] = new Cell(CellValue.EMPTY_CELL);

        List<IPlayer> enemies = new ArrayList<>();
        var player = new MockPlayer(1, PlayerDirection.UP, 1, new Point2i(0, 2), 1, true);
        boardCells[2][0] = new Cell(CellValue.PLAYER_ONE);
        boardCells[3][0] = new Cell(CellValue.PLAYER_ONE);
        var enemy1 = new MockPlayer(2, PlayerDirection.DOWN, 1, new Point2i(2, 2), 1, true);
        boardCells[2][2] = new Cell(CellValue.PLAYER_TWO);
        boardCells[1][2] = new Cell(CellValue.PLAYER_TWO);
        enemies.add(enemy1);
        var enemy2 = new MockPlayer(3, PlayerDirection.RIGHT, 1, new Point2i(4, 4), 1, true);
        boardCells[4][4] = new Cell(CellValue.PLAYER_THREE);
        boardCells[4][3] = new Cell(CellValue.PLAYER_THREE);
        enemies.add(enemy2);

        Board<Cell> board = new Board<>(boardCells);

        graph.updateGraph(board, player, enemies);

        // try moving from the players position
        var playerPositionNode = graph.getBoardCellAt(player.getPosition());
        assertNotNull("move up to (0 | 1) must be allowed", playerPositionNode.getEdge(PlayerDirection.UP, false, 1));

        // try moving from the players last position
        var playerLastPositionNode = graph.getBoardCellAt(new Point2i(0, 3));
        assertNull("move from old position is not ok", playerLastPositionNode.getEdge(PlayerDirection.RIGHT, false, 1));
        assertNull("move from old position is not ok", playerLastPositionNode.getEdge(PlayerDirection.RIGHT, true, 4));

        // Try moving to the players position
        var nodeRightFromPlayer = graph.getBoardCellAt(player.getPosition().translate(new Vector2i(1, 0)));
        assertNull("move to player-position is not ok", nodeRightFromPlayer.getEdge(PlayerDirection.LEFT, false, 1));

        // Try jumping over Player2
        assertNotNull("jump over player2 must be allowed", playerPositionNode.getEdge(PlayerDirection.RIGHT, true, 3));
        assertNotNull("jump over player2 must be allowed", playerPositionNode.getEdge(PlayerDirection.RIGHT, true, 4));

        // Crash into Player2
        assertNull("Collide with player2 is not ok", playerPositionNode.getEdge(PlayerDirection.RIGHT, false, 3));

        // request same edge and check if it is the same
        var edgeA = graph.getBoardCellAt(new Point2i(0, 0)).getEdge(PlayerDirection.RIGHT, false, 3);
        var edgeB = graph.getBoardCellAt(new Point2i(0, 0)).getEdge(PlayerDirection.RIGHT, false, 3);
        assertSame(edgeA, edgeB);

        // next round
        enemies = new ArrayList<>();
        player = new MockPlayer(1, PlayerDirection.UP, 1, new Point2i(0, 1), 1, true);
        boardCells[1][0] = new Cell(CellValue.PLAYER_ONE);
        enemy1 = new MockPlayer(2, PlayerDirection.DOWN, 1, new Point2i(2, 3), 1, true);
        boardCells[3][2] = new Cell(CellValue.PLAYER_TWO);
        enemies.add(enemy1);
        enemy2 = new MockPlayer(3, PlayerDirection.UP, 1, new Point2i(4, 3), 1, true);
        boardCells[3][4] = new Cell(CellValue.PLAYER_THREE);
        enemies.add(enemy2);
        board = new Board<>(boardCells);
        graph.updateGraph(board, player, enemies);

        // try moving from the players position
        playerPositionNode = graph.getBoardCellAt(player.getPosition());
        assertNotNull("move right to (1 | 1) must be allowed",
                playerPositionNode.getEdge(PlayerDirection.RIGHT, false, 1));

        // try moving from the players last position
        playerLastPositionNode = graph.getBoardCellAt(new Point2i(0, 2));
        assertNull("move from old position is not ok", playerLastPositionNode.getEdge(PlayerDirection.RIGHT, false, 1));
        assertNull("move from old position is not ok", playerLastPositionNode.getEdge(PlayerDirection.RIGHT, true, 4));

        // Try moving to the players position
        nodeRightFromPlayer = graph.getBoardCellAt(player.getPosition().translate(new Vector2i(1, 0)));
        assertNull("move to player-position is not ok", nodeRightFromPlayer.getEdge(PlayerDirection.LEFT, false, 1));

        // Try jumping over Player2
        assertNotNull("jump over player2 must be allowed", playerPositionNode.getEdge(PlayerDirection.RIGHT, true, 3));
        assertNotNull("jump over player2 must be allowed", playerPositionNode.getEdge(PlayerDirection.RIGHT, true, 4));

        // Crash into Player2
        assertNull("Collide with player2 is not ok", playerPositionNode.getEdge(PlayerDirection.RIGHT, false, 3));

        // request same edge and check if it is the same
        var edgeC = graph.getBoardCellAt(new Point2i(0, 0)).getEdge(PlayerDirection.RIGHT, false, 3);
        assertSame(edgeA, edgeC);
    }
}
