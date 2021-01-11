package player.analysis.cutoff;

import java.util.EnumMap;
import java.util.Map;

import player.analysis.ActionsRating;
import utility.game.player.PlayerAction;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Calculation Class to combine different cut off calculations
 */
public class CutOffCalculation {
	private Map<PlayerAction, FloatMatrix> matrixResult;
	private ActionsRating ratingsResult;

	/**
	 * Initializes a new calculator
	 * 
	 * @param width  the width of the board and the resulting {@link FloatMatrix}
	 * @param height the height of the board and the resulting {@link FloatMatrix}
	 */
	public CutOffCalculation(int width, int height) {
		ratingsResult = new ActionsRating();
		this.matrixResult = new EnumMap<>(PlayerAction.class);

		for (final PlayerAction action : PlayerAction.values()) {
			matrixResult.put(action, new FloatMatrix(width, height, 0));
		}
	}

	/**
	 * Adds a new rating to the result
	 * 
	 * @param action       The initial action the rating is valid for
	 * @param position     The position the rating is valid for
	 * @param cutOffRating The new calculated cut-off-rating
	 */
	public void add(PlayerAction action, Point2i position, float cutOffRating) {
		matrixResult.get(action).max(position, cutOffRating);
	}

	/**
	 * Combines the calculations of two {@link CutOffCalculation}
	 * 
	 * @param other a {@link CutOffCalculation} whose results should be added to
	 *              this results
	 */
	public void add(CutOffCalculation other) {
		for (final PlayerAction action : PlayerAction.values()) {
			final FloatMatrix selfMatrix = this.matrixResult.get(action);
			final FloatMatrix otherMatrix = other.matrixResult.get(action);

			matrixResult.computeIfPresent(action, (k, v) -> v.max(otherMatrix));

			final float maxRating = Math.max(selfMatrix.max(), otherMatrix.max());
			ratingsResult.setRating(action, maxRating);
		}
	}

	/**
	 * Returns the calculated ActsionRating
	 * 
	 * @return the {@link ActionsRating}
	 */
	public ActionsRating getRatingResult() {
		return ratingsResult;
	}

	/**
	 * Returns the {@link FloatMatrix ResultMatrix} for a specific initial action
	 * 
	 * @param action The initial action the matrix is needed for
	 * @return the {@link FloatMatrix ResultMatrix}
	 */
	public FloatMatrix getMatrixResult(PlayerAction action) {
		return matrixResult.get(action);
	}
}
