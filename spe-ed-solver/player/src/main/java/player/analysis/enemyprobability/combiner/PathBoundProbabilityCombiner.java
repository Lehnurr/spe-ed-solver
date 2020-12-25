package player.analysis.enemyprobability.combiner;

import player.analysis.enemyprobability.PathBoundProbability;

/**
 * Class used to combine multiple {@link PathBoundProbability} objects to one
 * float value.
 */
public abstract class PathBoundProbabilityCombiner<CopyResult extends PathBoundProbabilityCombiner<CopyResult>> {

	private float value;

	/**
	 * Creates a new {@link PathBoundProbabilityCombiner} with an initial value.
	 * 
	 * @param defaultValue initial value
	 */
	protected PathBoundProbabilityCombiner(final float defaultValue) {
		setValue(defaultValue);
	}

	/**
	 * Returns the combined float probability value.
	 * 
	 * @return the combined float probability value
	 */
	public final float getValue() {
		return value;
	}

	/**
	 * Sets a new internal value.
	 * 
	 * @param newValue new value to set
	 */
	protected final void setValue(final float newValue) {
		this.value = newValue;
	}

	/**
	 * Determines if the combination calculation can be applied to the current
	 * value.
	 * 
	 * @param pathBoundProbability {@link PathBoundProbability} to combine
	 * @param relativeRound        relative round of the combination
	 * @return true if the calculation can be applied
	 */
	public abstract boolean appliesFilter(final PathBoundProbability pathBoundProbability, final int relativeRound);

	/**
	 * Abstract method for adding a value to the combined value. The addition should
	 * be commutative. The addition must be resettable by the
	 * {@link PathBoundProbabilityCombiner#removeProbability(float)} method.
	 * 
	 * @param probability probability to add to the combined value
	 */
	public abstract void addProbability(final float probability);

	/**
	 * Abstract method for removing a value from the combined value. The remove
	 * operation should be commutative. The remove operation must be resettable by
	 * the {@link PathBoundProbabilityCombiner#addProbability(float)} method.
	 * 
	 * @param probability probability to remove from the combined value
	 */
	public abstract void removeProbability(final float probability);
	
	/**
	 * Returns a copy of the derived {@link PathBoundProbabilityCombiner}.
	 * 
	 * @return copy of the derived {@link PathBoundProbabilityCombiner}
	 */
	public abstract CopyResult copy();

}
