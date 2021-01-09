package player.analysis.solver.reachablepoints.graph.board;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import player.solver.reachablepoints.graph.board.Graph;
import player.solver.reachablepoints.graph.board.Node;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

public class ConcreteEdgeTest {

    @Test
    public void testIntersect() {
        Graph graph = new Graph(new Node[5][5]);

        var nodeA = graph.getBoardCellAt(new Point2i(0, 0));
        var edgeA1 = nodeA.getEdge(PlayerDirection.DOWN, true, 4);
        var edgeA2 = nodeA.getEdge(PlayerDirection.DOWN, false, 4);

        var nodeB = graph.getBoardCellAt(new Point2i(3, 3));
        var edgeB1 = nodeB.getEdge(PlayerDirection.LEFT, false, 2);
        var edgeB2 = nodeB.getEdge(PlayerDirection.UP, false, 3);
        var edgeB3 = nodeB.getEdge(PlayerDirection.DOWN, false, 1);

        var nodeC = graph.getBoardCellAt(new Point2i(0, 3));
        var edgeC1 = nodeC.getEdge(PlayerDirection.UP, false, 1);
        var edgeC2 = nodeC.getEdge(PlayerDirection.RIGHT, true, 4);

        // Intersect all edges with all (each check twice: the edge as instance and as
        // parameter)

        // Intersect EdgeA1 with all other Edges
        assertTrue("A1/A1: Edge must intersect itself", edgeA1.intersect(edgeA1));
        assertTrue("A1/A2: End and Startpoint of jumped-paths are intersect relevant", edgeA1.intersect(edgeA2));
        assertFalse("A1/B1: No intersection expected", edgeA1.intersect(edgeB1));
        assertFalse("A1/B2: No intersection expected", edgeA1.intersect(edgeB2));
        assertFalse("A1/B3: No intersection expected", edgeA1.intersect(edgeB3));
        assertFalse("A1/C1: Jumped over edge is not intersected", edgeA1.intersect(edgeC1));
        assertFalse("A1/C2: Jumped over edge is not intersected", edgeA1.intersect(edgeC2));

        // Intersect EdgeA2 with all other Edges
        assertTrue("A2/A1: End and Startpoint of jumped-paths are intersect relevant", edgeA2.intersect(edgeA1));
        assertTrue("A2/A2: Edge must intersect itself", edgeA2.intersect(edgeA2));
        assertFalse("A2/B1: No intersection expected", edgeA2.intersect(edgeB1));
        assertFalse("A2/B2: No intersection expected", edgeA2.intersect(edgeB2));
        assertFalse("A2/B3: No intersection expected", edgeA2.intersect(edgeB3));
        assertTrue("A2/C1: Intersection expected", edgeA2.intersect(edgeC1));
        assertFalse("A2/C2: Startnode is not part of the path", edgeA2.intersect(edgeC2));

        // Intersect EdgeB1 with all other Edges
        assertFalse("B1/A1: No intersection expected", edgeB1.intersect(edgeA1));
        assertFalse("B1/A2: No intersection expected", edgeB1.intersect(edgeA2));
        assertTrue("B1/B1: Edge must intersect itself", edgeB1.intersect(edgeB1));
        assertFalse("B1/B2: No intersection expected", edgeB1.intersect(edgeB2));
        assertFalse("B1/B3: No intersection expected", edgeB1.intersect(edgeB3));
        assertFalse("B1/C1: No intersection expected", edgeB1.intersect(edgeC1));
        assertTrue("B1/C2: End and Startpoint of jumped-paths are intersect relevant", edgeB1.intersect(edgeC2));

        // Intersect EdgeB2 with all other Edges
        assertFalse("B2/A1: No intersection expected", edgeB2.intersect(edgeA1));
        assertFalse("B2/A2: No intersection expected", edgeB2.intersect(edgeA2));
        assertFalse("B2/B1: No intersection expected", edgeB2.intersect(edgeB1));
        assertTrue("B2/B2: Edge must intersect itself", edgeB2.intersect(edgeB2));
        assertFalse("B2/B3: No intersection expected", edgeB2.intersect(edgeB3));
        assertFalse("B2/C1: No intersection expected", edgeB2.intersect(edgeC1));
        assertFalse("B2/C2: No intersection expected", edgeB2.intersect(edgeC2));

        // B3 does not generate new cases

        // Intersect EdgeC1 with all other Edges
        assertFalse("C1/A1: Jumped over edge is not intersected", edgeC1.intersect(edgeA1));
        assertTrue("C1/A2: Intersection expected", edgeC1.intersect(edgeA2));
        assertFalse("C1/B1: No intersection expected", edgeC1.intersect(edgeB1));
        assertFalse("C1/B2: No intersection expected", edgeC1.intersect(edgeB2));
        assertFalse("C1/B3: No intersection expected", edgeC1.intersect(edgeB3));
        assertTrue("C1/C1: Edge must intersect itself", edgeC1.intersect(edgeC1));
        assertFalse("C1/C2: No intersection expected", edgeC1.intersect(edgeC2));

        // Intersect EdgeC2 with all other Edges
        assertFalse("C2/A1: Jumped over edge is not intersected", edgeC2.intersect(edgeA1));
        assertFalse("C2/A2: No intersection expected", edgeC2.intersect(edgeA2));
        assertTrue("C2/B1: End and Startpoint of jumped-paths are intersect relevant", edgeC2.intersect(edgeB1));
        assertFalse("C2/B2: No intersection expected", edgeC2.intersect(edgeB2));
        assertFalse("C2/B3: No intersection expected", edgeC2.intersect(edgeB3));
        assertFalse("C2/C1: No intersection expected", edgeC2.intersect(edgeC1));
        assertTrue("C2/C2: Edge must intersect itself", edgeC2.intersect(edgeC2));
    }
}
