package player.boardevaluation.graph;

import utility.game.board.Board;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * AbstractEdge
 */
public final class AbstractEdge implements IEdge {

    private final int stepCount;
    private final int[] abstractPath;

    public AbstractEdge(int[] abstractPath) {
        this.stepCount = abstractPath.length;
        this.abstractPath = abstractPath;
    }

    /**
     * Calculates the {@link IConcreteEdge} from the {@link IAbstractEdge}
     * 
     * @param board     The {@link Board} with all {@link Node Nodes}
     * @param startNode The {@link Node} where the player must be to use this edge
     * @param direction The {@link PlayerDirection Players direction}
     */
    public ConcreteEdge calculatePath(Board<Node> board, Node startNode, PlayerDirection direction) {
        Node[] path = new Node[this.stepCount];
        Node[] invertedPath = new Node[this.stepCount];

        // Scan the stepped nodes and set the path and the inverted path
        for (int stepIndex = 0; stepIndex < this.stepCount; stepIndex++) {
            final Vector2i deltaX = direction.getDirectionVector().multiply(abstractPath[stepIndex]);
            final Node stepNode = board.getBoardCellAt(startNode.getPosition().translate(deltaX));
            if (stepNode == null)
                return null;
            path[stepIndex] = stepNode;
            invertedPath[this.stepCount - 1 - stepIndex] = stepNode;
        }

        final ConcreteEdge edge = new ConcreteEdge(path);

        final int speed = abstractPath[this.stepCount - 1];
        final boolean doJump = speed != this.stepCount;
        final Point2i endPosition = path[this.stepCount - 1].getPosition();
        final Point2i invertedStartPosition = endPosition.translate(direction.getDirectionVector());
        final Node invertedStartNode = board.getBoardCellAt(invertedStartPosition);

        if (invertedStartNode != null) {
            final ConcreteEdge invertedEdge = new ConcreteEdge(invertedPath);
            edge.setInvertedEdge(invertedEdge);
            invertedEdge.setInvertedEdge(edge);

            invertedStartNode.setEdge(direction.getInversion(), doJump, speed, invertedEdge);
        }

        return edge;
    }

    public int getStepCount() {
        return stepCount;
    }

}
