package player.boardevaluation.graph;

/**
 * ConcreteEdge
 */
public final class ConcreteEdge implements IEdge {

    private final int stepCount;
    private final Node[] path;
    private ConcreteEdge invertedEdge;

    public ConcreteEdge(final Node[] path) {
        this.stepCount = path.length;
        this.path = path;
    }

    /**
     * 
     * @return All passed Nodes (not the jumped over nodes)
     */
    public Node[] getPath() {
        return this.path;
    }

    public ConcreteEdge getInvertedEdge() {
        return invertedEdge;
    }

    public void setInvertedEdge(ConcreteEdge invertedEdge) {
        this.invertedEdge = invertedEdge;
    }

    public int getStepCount() {
        return stepCount;
    }
}
