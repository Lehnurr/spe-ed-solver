package solver.reachablepoints.graph.board;

import java.util.Arrays;
import java.util.Objects;

import utility.game.board.IBoardCell;
import utility.game.player.PlayerDirection;
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

	/**
	 * Creates a new {@link ConcreteEdge edge} with a
	 * {@link ConcreteEdge#getStartNode() start node} and a specific
	 * {@link ConcreteEdge#getPath() path}.
	 * 
	 * @param startNode the {@link Node node} where this {@link ConcreteEdge edge}
	 *                  starts. This {@link Node node} is not part of the
	 *                  {@link ConcreteEdge#getPath() path}, because it is the
	 *                  {@link Node start node}
	 * @param path      an array of passed {@link Node nodes} (excluding jumped over
	 *                  cells)
	 */
	public ConcreteEdge(final Node startNode, final Node[] path) {
		super(path[0].getPosition(), path[path.length - 1].getPosition());
		this.stepCount = path.length;
		this.path = path;
		this.startNode = startNode;
	}

	/**
	 * Returns the passed Nodes by travelling this {@link ConcreteEdge edge}.
	 * 
	 * @return all passed Nodes (not the jumped over nodes)
	 */
	public Node[] getPath() {
		return this.path;
	}

	/**
	 * The {@link ConcreteEdge edge} that uses the same
	 * {@link ConcreteEdge#getPath() path}.
	 * 
	 * @return the inverted {@link ConcreteEdge edge}
	 */
	public ConcreteEdge getInvertedEdge() {
		return invertedEdge;
	}

	/**
	 * Sets the {@link ConcreteEdge edge} that have the same
	 * {@link ConcreteEdge#getPath() path}, but inverted.
	 * 
	 * @param invertedEdge an {@link ConcreteEdge edge} with inverted
	 *                     {@link PlayerDirection direction}, same speed. The
	 *                     {@link ConcreteEdge#getStartNode() start node } is not
	 *                     the {@link ConcreteEdge#getEndNode() end node}, because
	 *                     the {@link ConcreteEdge#getPath() path} is the same!
	 */
	public void setInvertedEdge(ConcreteEdge invertedEdge) {
		this.invertedEdge = invertedEdge;
	}

	/**
	 * The number of passed {@link IBoardCell cells} (excluding jumped over cells).
	 * 
	 * @return the amount of travelles steps
	 */
	public int getStepCount() {
		return stepCount;
	}

	/**
	 * The {@link Node node} where the {@link ConcreteEdge edge} starts. This
	 * {@link Node node} is not part of the {@link ConcreteEdge#getPath() path},
	 * because it is the {@link Node start node}.
	 * 
	 * @return the {@link Node node} where the player have to be to use this
	 *         {@link ConcreteEdge edge}
	 */
	public Node getStartNode() {
		return startNode;
	}

	/**
	 * The {@link Node node} where the {@link ConcreteEdge edge} ends.
	 * 
	 * @return the last {@link Node node} in the {@link ConcreteEdge#getPath() path}
	 */
	public Node getEndNode() {
		return path[path.length - 1];
	}

	/**
	 * Checks whether two {@link ConcreteEdge edges} intersect.
	 * 
	 * @param other the other {@link ConcreteEdge} to check for intersection
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
