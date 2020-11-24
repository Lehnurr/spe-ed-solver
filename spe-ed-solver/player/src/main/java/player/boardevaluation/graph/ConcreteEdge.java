package player.boardevaluation.graph;

/**
 * ConcreteEdge
 */
public final class ConcreteEdge implements IConcreteEdge {

    private final int stepCount;
    private final Node[] path;
    private IConcreteEdge invertedEdge;

    public ConcreteEdge(final Node[] path) {
        this.stepCount = path.length;
        this.path = path;
    }

    @Override
    public Node[] getPath() {
        return this.path;
    }

    @Override
    public IConcreteEdge getInvertedEdge() {
        return invertedEdge;
    }

    @Override
    public void setInvertedEdge(IConcreteEdge invertedEdge) {
        this.invertedEdge = invertedEdge;
    }

    @Override
    public int getStepCount() {
        return stepCount;
    }
}
