package player.analysis.enemyprobability.combiner;

import player.analysis.enemyprobability.PathBoundProbability;

/**
 * Class extending the {@link PathBoundProbabilityCombiner} for the success
 * probability. The available value represents the chance that the player path
 * is successful and does not interfere with any enemy.
 */
public class SuccessProbabilityCombiner extends PathBoundProbabilityCombiner<SuccessProbabilityCombiner> {

	private static final float DEFAULT_VALUE = 1f;

	public SuccessProbabilityCombiner() {
		super(DEFAULT_VALUE);
	}

	public SuccessProbabilityCombiner(final float defaultValue) {
		super(defaultValue);
	}

	@Override
	public boolean appliesFilter(final PathBoundProbability pathBoundProbability, final int relativeRound) {
		return pathBoundProbability.getDepth() <= relativeRound;
	}

	@Override
	public void addProbability(final float probability) {
		this.setValue(this.getValue() * probability);
	}

	@Override
	public void removeProbability(final float probability) {
		this.setValue(this.getValue() / probability);
	}

	@Override
	public SuccessProbabilityCombiner copy() {
		return new SuccessProbabilityCombiner(getValue());
	}

}
