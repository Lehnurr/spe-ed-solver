package player.analysis.enemyprobability;

public class PathBoundedProbability {

	private final PathDescriptor pathDescriptor;

	private final float probability;

	/**
	 * Creates a new {@link PathBoundedProbability} for a {@link Path} with a
	 * probability value.
	 * 
	 * @param pathDescriptor
	 * @param probability
	 */
	public PathBoundedProbability(final PathDescriptor pathDescriptor, final float probability) {
		this.pathDescriptor = pathDescriptor;
		this.probability = probability;
	}

	/**
	 * @return the {@link PathDescriptor} of the probability
	 */
	public PathDescriptor getPathDescriptor() {
		return pathDescriptor;
	}

	/**
	 * @return the probability value
	 */
	public float getProbability() {
		return probability;
	}

}
