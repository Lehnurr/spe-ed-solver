package player.analysis.enemyprobability.combiner;

import player.analysis.enemyprobability.PathBoundProbability;

/**
 * Class extending the {@link PathBoundProbabilityCombiner} for the rating of
 * cutting another player path off.
 */
public class CutOffValueCombiner extends PathBoundProbabilityCombiner<CutOffValueCombiner> {

	private static final float DEFAULT_VALUE = 0f;

	public CutOffValueCombiner() {
		super(DEFAULT_VALUE);
	}

	public CutOffValueCombiner(final float defaultValue) {
		super(defaultValue);
	}

	@Override
	public boolean appliesFilter(final PathBoundProbability pathBoundProbability, final int relativeRound) {
		return relativeRound < pathBoundProbability.getDepth();
	}

	@Override
	public void addProbability(final float probability) {
		this.setValue(this.getValue() + probability);
	}

	@Override
	public void removeProbability(final float probability) {
		this.setValue(this.getValue() - probability);
	}

	@Override
	public CutOffValueCombiner copy() {
		return new CutOffValueCombiner(getValue());
	}

}
