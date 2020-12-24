package player.analysis.enemyprobability;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.game.step.GameStep;

/**
 * Class managing {@link PathBoundProbability} objects which are collected by a
 * player. The {@link PathBoundProbabilitySet} can be used to manage the
 * multiple probabilities of enemies encountered in multiple {@link GameStep
 * game steps}.
 */
public class PathBoundProbabilitySet {

	private final float successProbability;
	private final float cuttingProbability;

	private final Set<PathBoundProbability> pathBoundProbabilities;

	/**
	 * Creates an empty {@link PathBoundProbabilitySet}.
	 */
	public PathBoundProbabilitySet() {
		this.successProbability = 1f;
		this.cuttingProbability = 0f;
		this.pathBoundProbabilities = new HashSet<>();
	}

	/**
	 * Private constructor used to create {@link PathBoundProbabilitySet} objects
	 * from existing data.
	 * 
	 * @param successProbability     the new success probability value
	 * @param cuttingProbability     the new cutting probability value
	 * @param pathBoundProbabilities a {@link Set} of {@link PathBoundProbability}
	 *                               objects represented by the new
	 *                               {@link PathBoundProbabilitySet}
	 */
	private PathBoundProbabilitySet(final float successProbability, final float cuttingProbability,
			final Set<PathBoundProbability> pathBoundProbabilities) {
		this.successProbability = successProbability;
		this.cuttingProbability = cuttingProbability;
		this.pathBoundProbabilities = pathBoundProbabilities;
	}

	/**
	 * Generates a new {@link PathBoundProbabilitySet} from the existing one and
	 * additional given {@link PathBoundProbability} values.
	 * 
	 * @param enemyValues {@link PathBoundProbability} values to consider
	 * @return generated {@link PathBoundProbabilitySet}
	 */
	public PathBoundProbabilitySet getUpdatedSet(final List<PathBoundProbability> enemyValues) {

		if (enemyValues.size() == 0) {
			return this;
		}

		final Set<PathBoundProbability> nextPathBoundProbabilities = new HashSet<>(pathBoundProbabilities);
		float nextSuccessProbability = successProbability;
		float nextCuttingProbability = cuttingProbability;

		final Set<PathBoundProbability> enemyValueSet = new HashSet<>();
		for (final PathBoundProbability enemyValue : enemyValueSet) {
			for (final PathBoundProbability containedValue : new HashSet<>(nextPathBoundProbabilities)) {

				if (!enemyValue.dependsOn(containedValue)) {
					if (enemyValue.upgrades(containedValue)) {
						nextSuccessProbability /= containedValue.getProbability();
						nextCuttingProbability -= containedValue.getProbability();
						nextPathBoundProbabilities.remove(containedValue);
					}

					nextSuccessProbability *= enemyValue.getProbability();
					nextCuttingProbability += enemyValue.getProbability();
					nextPathBoundProbabilities.add(enemyValue);
				}
			}
		}

		return new PathBoundProbabilitySet(nextSuccessProbability, nextCuttingProbability, nextPathBoundProbabilities);
	}

	/**
	 * @return the successProbability
	 */
	public float getSuccessProbability() {
		return successProbability;
	}

	/**
	 * @return the cuttingProbability
	 */
	public float getCuttingProbability() {
		return cuttingProbability;
	}

}
