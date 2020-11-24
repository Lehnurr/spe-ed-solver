package player.boardevaluation.graph;

/**
 * IConcreteEdge
 */
public interface IConcreteEdge extends IEdge {
    /**
     * 
     * @return All passed Nodes (not the jumped over nodes)
     */
    Node[] getPath();

    IConcreteEdge getInvertedEdge();

    void setInvertedEdge(IConcreteEdge invertedEdge);
}
