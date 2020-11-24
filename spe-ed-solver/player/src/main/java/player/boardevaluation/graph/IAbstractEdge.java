package player.boardevaluation.graph;

import utility.game.board.Board;
import utility.game.player.PlayerDirection;

/**
 * IAbstractEdge
 */
public interface IAbstractEdge extends IEdge {

    /**
     * Calculates the {@link IConcreteEdge} from the {@link IAbstractEdge}
     * 
     * @param board     The {@link Board} with all {@link Node Nodes}
     * @param startNode The {@link Node} where the player must be to use this edge
     * @param direction The {@link PlayerDirection Players direction}
     */
    IConcreteEdge calculatePath(Board<Node> board, Node startNode, PlayerDirection direction);
}