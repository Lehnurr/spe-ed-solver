package player.boardevaluation.graph;

import java.util.HashMap;

import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * provides a Look-Up-Table to determine all Edges that Use a specific Node
 */
public final class AffectedEdgesLookUpTable {

    /**
     * Determines for a node with changed x or y position the edges that pass
     * through the affected (0, 0) node
     */
    private static HashMap<Point2i, int[]> abstractEdgeDescriptions = new HashMap<>();

    static {
        // Affected Edges of west Nodes
        abstractEdgeDescriptions.put(new Point2i(-1, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 1),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 2),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, true, 10),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 1),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 2),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-2, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 2),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 2),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-3, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-4, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-5, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-6, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-7, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-8, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-9, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Point2i(-10, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 10),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
    }
}
