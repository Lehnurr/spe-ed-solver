package player.analysis.enemyprobability;

public class PathBoundProbability {

	private final PathDescriptor pathDescriptor;

	private final float probability;

	/**
	 * Creates a new {@link PathBoundProbability} for a {@link Path} with a
	 * probability value.
	 * 
	 * @param pathDescriptor
	 * @param probability
	 */
	public PathBoundProbability(final PathDescriptor pathDescriptor, final float probability) {
		this.pathDescriptor = pathDescriptor;
		this.probability = probability;
	}

	/**
	 * Combines a {@link PathBoundProbability} with another one. The probabilities
	 * are summed up and the best fitting {@link PathDescriptor} is chosen to
	 * represent both of the old values.
	 * 
	 * @param other {@link PathBoundProbability} to combine with
	 * @return {@link PathBoundProbability} as result
	 */
	public PathBoundProbability combine(final PathBoundProbability other) {

		final float newProbability = this.getProbability() + other.getProbability();

		final PathDescriptor newPathDescriptor;
		if (this.getDepth() < other.getDepth()) {
			newPathDescriptor = this.getPathDescriptor();
		} else if (other.getDepth() < this.getDepth()) {
			newPathDescriptor = other.pathDescriptor;
		} else {
			if (this.getProbability() > other.getProbability()) {
				newPathDescriptor = this.getPathDescriptor();
			} else {
				newPathDescriptor = other.getPathDescriptor();
			}
		}

		return new PathBoundProbability(newPathDescriptor, newProbability);
	}

	/**
	 * Delegates the request to the underlying {@link PathDescriptor} objects.
	 * Thereby this call is equal to:
	 * {@code this.getPathDescriptor().dependsOn(other.getPathDescriptor())}
	 * 
	 * @param other {@link PathBoundProbability} to compare with
	 * @return true if the probability depends on the other one
	 */
	public boolean dependsOn(final PathBoundProbability other) {
		return getPathDescriptor().dependsOn(other.getPathDescriptor());
	}

	/**
	 * Delegates the request to the underlying {@link PathDescriptor} objects.
	 * Thereby this call is equal to:
	 * {@code this.getPathDescriptor().upgrades(other.getPathDescriptor())}
	 * 
	 * @param other {@link PathBoundProbability} to compare with
	 * @return true if the probability upgrades the other one
	 */
	public boolean upgrades(final PathBoundProbability other) {
		return getPathDescriptor().upgrades(other.getPathDescriptor());
	}

	/**
	 * @return the {@link PathDescriptor} of the probability
	 */
	public PathDescriptor getPathDescriptor() {
		return pathDescriptor;
	}

	/**
	 * @return the depth of the path
	 */
	public int getDepth() {
		return pathDescriptor.getDepth();
	}

	/**
	 * @return the probability value
	 */
	public float getProbability() {
		return probability;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pathDescriptor == null) ? 0 : pathDescriptor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathBoundProbability other = (PathBoundProbability) obj;
		if (pathDescriptor == null) {
			if (other.pathDescriptor != null)
				return false;
		} else if (!pathDescriptor.equals(other.pathDescriptor))
			return false;
		return true;
	}

}
