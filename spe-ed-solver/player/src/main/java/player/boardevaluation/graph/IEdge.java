package player.boardevaluation.graph;

/**
 * IEdge
 */
public interface IEdge {
    /**
     * Number of the steps of this {@link IEdge Edge}. Jumped over steps are not
     * included.
     */
    int getStepCount();
}
