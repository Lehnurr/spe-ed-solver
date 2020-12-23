package player.boardevaluation.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import utility.game.player.PlayerDirection;
import utility.geometry.Vector2i;

/**
 * provides a Look-Up-Table to determine all Edges that Use a specific Node.
 * This singleton does nothing except initilizing the Look-Up-Table
 */
public final class AffectedEdgesLookUpTable {

    /**
     * Determines for a node with changed x or y position the edges that pass
     * through the affected (0, 0) node
     */
    private static Map<Vector2i, int[]> abstractEdgeDescriptions = new HashMap<>();

    private AffectedEdgesLookUpTable() {
    }

    /**
     * Fills the Look-Up-Table
     */
    static {

        // #region Add affected Edges of west Nodes
        abstractEdgeDescriptions.put(new Vector2i(-1, 0),
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
        abstractEdgeDescriptions.put(new Vector2i(-2, 0),
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
        abstractEdgeDescriptions.put(new Vector2i(-3, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-4, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-5, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-6, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-7, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-8, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-9, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(-10, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.RIGHT, true, 10),
                        Node.getIntegerIndex(PlayerDirection.RIGHT, false, 10) });
        // #endregion

        // #region Add affected Edges of north Nodes
        abstractEdgeDescriptions.put(new Vector2i(0, -1),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 1),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 2),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 3),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 4),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, true, 10),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 1),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 2),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 3),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 4),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -2),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 2),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 2),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 3),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 4),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -3),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 3),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 3),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 4),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -4),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 4),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 4),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -5),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 5),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -6),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 6),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -7),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 7),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -8),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 8),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -9),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 9),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, -10),
                new int[] { Node.getIntegerIndex(PlayerDirection.DOWN, true, 10),
                        Node.getIntegerIndex(PlayerDirection.DOWN, false, 10) });
        // #endregion

        // #region Add affected Edges of east Nodes
        abstractEdgeDescriptions.put(new Vector2i(1, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 1),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 2),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 3),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 4),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, true, 10),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 1),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 2),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(2, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 2),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 2),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(3, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 3),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 3),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(4, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 4),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 4),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(5, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 5),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(6, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 6),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(7, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 7),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(8, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 8),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(9, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 9),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(10, 0),
                new int[] { Node.getIntegerIndex(PlayerDirection.LEFT, true, 10),
                        Node.getIntegerIndex(PlayerDirection.LEFT, false, 10) });
        // #endregion

        // #region Add affected Edges of south Nodes
        abstractEdgeDescriptions.put(new Vector2i(0, 1), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 1),
                Node.getIntegerIndex(PlayerDirection.UP, true, 2), Node.getIntegerIndex(PlayerDirection.UP, true, 3),
                Node.getIntegerIndex(PlayerDirection.UP, true, 4), Node.getIntegerIndex(PlayerDirection.UP, true, 5),
                Node.getIntegerIndex(PlayerDirection.UP, true, 6), Node.getIntegerIndex(PlayerDirection.UP, true, 7),
                Node.getIntegerIndex(PlayerDirection.UP, true, 8), Node.getIntegerIndex(PlayerDirection.UP, true, 9),
                Node.getIntegerIndex(PlayerDirection.UP, true, 10), Node.getIntegerIndex(PlayerDirection.UP, false, 1),
                Node.getIntegerIndex(PlayerDirection.UP, false, 2), Node.getIntegerIndex(PlayerDirection.UP, false, 3),
                Node.getIntegerIndex(PlayerDirection.UP, false, 4), Node.getIntegerIndex(PlayerDirection.UP, false, 5),
                Node.getIntegerIndex(PlayerDirection.UP, false, 6), Node.getIntegerIndex(PlayerDirection.UP, false, 7),
                Node.getIntegerIndex(PlayerDirection.UP, false, 8), Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 2), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 2),
                Node.getIntegerIndex(PlayerDirection.UP, false, 2), Node.getIntegerIndex(PlayerDirection.UP, false, 3),
                Node.getIntegerIndex(PlayerDirection.UP, false, 4), Node.getIntegerIndex(PlayerDirection.UP, false, 5),
                Node.getIntegerIndex(PlayerDirection.UP, false, 6), Node.getIntegerIndex(PlayerDirection.UP, false, 7),
                Node.getIntegerIndex(PlayerDirection.UP, false, 8), Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 3), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 3),
                Node.getIntegerIndex(PlayerDirection.UP, false, 3), Node.getIntegerIndex(PlayerDirection.UP, false, 4),
                Node.getIntegerIndex(PlayerDirection.UP, false, 5), Node.getIntegerIndex(PlayerDirection.UP, false, 6),
                Node.getIntegerIndex(PlayerDirection.UP, false, 7), Node.getIntegerIndex(PlayerDirection.UP, false, 8),
                Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 4), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 4),
                Node.getIntegerIndex(PlayerDirection.UP, false, 4), Node.getIntegerIndex(PlayerDirection.UP, false, 5),
                Node.getIntegerIndex(PlayerDirection.UP, false, 6), Node.getIntegerIndex(PlayerDirection.UP, false, 7),
                Node.getIntegerIndex(PlayerDirection.UP, false, 8), Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 5), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 5),
                Node.getIntegerIndex(PlayerDirection.UP, false, 5), Node.getIntegerIndex(PlayerDirection.UP, false, 6),
                Node.getIntegerIndex(PlayerDirection.UP, false, 7), Node.getIntegerIndex(PlayerDirection.UP, false, 8),
                Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 6), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 6),
                Node.getIntegerIndex(PlayerDirection.UP, false, 6), Node.getIntegerIndex(PlayerDirection.UP, false, 7),
                Node.getIntegerIndex(PlayerDirection.UP, false, 8), Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 7), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 7),
                Node.getIntegerIndex(PlayerDirection.UP, false, 7), Node.getIntegerIndex(PlayerDirection.UP, false, 8),
                Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 8), new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 8),
                Node.getIntegerIndex(PlayerDirection.UP, false, 8), Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 9),
                new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 9),
                        Node.getIntegerIndex(PlayerDirection.UP, false, 9),
                        Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        abstractEdgeDescriptions.put(new Vector2i(0, 10),
                new int[] { Node.getIntegerIndex(PlayerDirection.UP, true, 10),
                        Node.getIntegerIndex(PlayerDirection.UP, false, 10) });
        // #endregion

        abstractEdgeDescriptions = Collections.unmodifiableMap(abstractEdgeDescriptions);
    }

    /**
     * A static, unmodifiable Look-Up-Table that provides for all Nodes (relative to
     * a center-Node) the Edge-Array-Indices that passes the center-Node
     * 
     * @return A unmodifiable {@link java.util.HashMap#HashMap} with a
     *         {@link utility.geometry.Vector2i#Vector2i difference-Vector} to
     *         determine the Nodes that have {@link IEdge#IEdge Edges} to a
     *         {@link Node#Node Node}. The {@link java.util.HashMap#HashMap}
     *         Contains the indices of the Edges that passes the {@link Node#Node
     *         Node}
     */
    public static final Map<Vector2i, int[]> getAbstractPassingEdges() {
        return abstractEdgeDescriptions;
    }
}
