package player.solver.reachablepoints.graph.board;

import java.util.Arrays;
import java.util.Objects;

import utility.geometry.LineSegment2i;

/**
 * A ConcreteEdge linkes two nodes in the board graph via the properties speed,
 * direciotn and doJump. It is a Segment but it is possible that the path does
 * not contain all points in the segment (this is the case wehn the edge is a
 * jump-over-edge)
 */
public final class ConcreteEdge extends LineSegment2i implements IEdge {
	private final int stepCount;
	private final Node[] path;
	private final Node startNode;
	private ConcreteEdge invertedEdge;

	public ConcreteEdge(final Node startNode, final Node[] path) {
		super(path[0].getPosition(), path[path.length - 1].getPosition());
		this.stepCount = path.length;
		this.path = path;
		this.startNode = startNode;
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

	public Node getStartNode() {
		return startNode;
	}

	public Node getEndNode() {
		return path[path.length - 1];
	}

	/**
	 * checks whether two edges intersect
	 * 
	 * @param other the other {@link ConcreteEdge} to check for intersections
	 * @return true if the {@link ConcreteEdge edges} intersect
	 */
	public boolean intersect(final ConcreteEdge other) {

		if (this.stepCount > 2 && other.stepCount > 2) {
			// no jump over, segment intersect is possible
			return super.intersect(other);
		}

		ConcreteEdge[] edges = { this, other };
		// sort so that the shorter path comes first
		Arrays.sort(edges, (a, b) -> a.stepCount - b.stepCount);

		// The first path is jumped over or consists of only a few steps
		if (edges[1].stepCount > 2) {
			for (final Node node : edges[0].getPath()) {
				if (edges[1].contains(node.getPosition()))
					return true;
			}
		} else {
			// both paths are jumped over or consists of only a few steps
			for (Node node : edges[0].getPath()) {
				if (Arrays.stream(edges[1].getPath()).anyMatch(node::equals))
					return true;
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ConcreteEdge)) {
			return false;
		}
		ConcreteEdge concreteEdge = (ConcreteEdge) o;
		return stepCount == concreteEdge.stepCount && Objects.equals(path, concreteEdge.path)
				&& Objects.equals(invertedEdge, concreteEdge.invertedEdge);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stepCount, path);
	}

}
