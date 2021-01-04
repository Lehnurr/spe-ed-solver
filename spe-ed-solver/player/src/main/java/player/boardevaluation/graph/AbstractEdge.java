package player.boardevaluation.graph;

import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * AbstractEdge
 */
public final class AbstractEdge implements IEdge {

    public AbstractEdge() {

    }

    /**
     * Calculates the {@link IConcreteEdge} from the {@link IAbstractEdge}
     * 
     * @param graph     The {@link Graph} with all {@link Node Nodes}
     * @param startNode The {@link Node} where the player must be to use this edge
     * @param direction The {@link PlayerDirection Players direction}
     * @param dojump    specifies if the searched edge is a jump-over edge
     * @param speed     specifies the speed for the searched edge
     */
    public ConcreteEdge calculatePath(Graph graph, Node startNode, PlayerDirection direction, boolean doJump,
            int speed) {

        int stepCount;
        int[] abstractPath;
        if (doJump && speed > 2) {
            stepCount = 2;
            abstractPath = new int[] { 1, speed };
        } else {
            stepCount = speed;
            abstractPath = new int[speed];
            for (int i = 0; i < speed; i++) {
                abstractPath[i] = i + 1;
            }
        }

        Node[] path = new Node[stepCount];
        Node[] invertedPath = new Node[stepCount];

        // Scan the stepped nodes and set the path and the inverted path
        for (int stepIndex = 0; stepIndex < stepCount; stepIndex++) {
            final Vector2i deltaX = direction.getDirectionVector().multiply(abstractPath[stepIndex]);
            final Node stepNode = graph.getBoardCellAt(startNode.getPosition().translate(deltaX));
            if (stepNode == null)
                return null;
            path[stepIndex] = stepNode;
            invertedPath[stepCount - 1 - stepIndex] = stepNode;
        }

        final Point2i endPosition = path[stepCount - 1].getPosition();

        final ConcreteEdge edge = new ConcreteEdge(startNode, path);

        final Point2i invertedStartPosition = endPosition.translate(direction.getDirectionVector());
        final Node invertedStartNode = graph.getBoardCellAt(invertedStartPosition);

        if (invertedStartNode != null) {
            final ConcreteEdge invertedEdge = new ConcreteEdge(invertedStartNode, invertedPath);
            edge.setInvertedEdge(invertedEdge);
            invertedEdge.setInvertedEdge(edge);

            invertedStartNode.setEdge(direction.getInversion(), doJump, speed, invertedEdge);
        }

        return edge;
    }

}
