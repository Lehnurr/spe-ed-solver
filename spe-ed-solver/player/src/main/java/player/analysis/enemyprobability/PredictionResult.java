package player.analysis.enemyprobability;

import utility.game.board.Board;
import utility.game.player.IPlayer;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Class representing a prediction of {@link IPlayer} behavior with
 * {@link PathBoundProbability} objects for each cell of a {@link Board}.
 */
public class PredictionResult {

	private final PathBoundProbability[][] pathBoundProbabilities;
	
	/**
	 * Creates a new {@link PredictionResult} with an already existing array of
	 * {@link PathBoundProbability} objects.
	 * 
	 * @param pathBoundProbabilities array of {@link PathBoundProbability} objects
	 */
	public PredictionResult(final PathBoundProbability[][] pathBoundProbabilities) {
		this.pathBoundProbabilities = pathBoundProbabilities;
	}

	/**
	 * Creates a new {@link PredictionResult} combining two other
	 * {@link PredictionResult} objects.
	 * 
	 * @param result0 {@link PredictionResult} first object to combine
	 * @param result1 {@link PredictionResult} second object to combine
	 */
	public PredictionResult(final PredictionResult result0, final PredictionResult result1) {
		assert result0.getHeight() == result1.getHeight() : "The results must have matching heights!";
		assert result0.getWidth() == result1.getWidth() : "The results must have matching widths!";
		this.pathBoundProbabilities = new PathBoundProbability[result0.getHeight()][result0.getWidth()];
		for (int y = 0; y < result0.getHeight(); y++) {
			for (int x = 0; x < result0.getWidth(); x++) {
				final Point2i point = new Point2i(x, y);
				final PathBoundProbability value0 = result0.getPathBoundProbability(point);
				final PathBoundProbability value1 = result1.getPathBoundProbability(point);
				pathBoundProbabilities[y][x] = value0.combine(value1);
			}
		}
	}

	/**
	 * Returns the {@link PathBoundProbability} value at a given position.
	 * 
	 * @param point {@link Point2i} of the position
	 * @return {@link PathBoundProbability} at given {@link Point2i}
	 */
	public PathBoundProbability getPathBoundProbability(final Point2i point) {
		return pathBoundProbabilities[point.getY()][point.getX()];
	}

	/**
	 * Extracts the probability information of each {@link PathBoundProbability} and
	 * returns a {@link FloatMatrix} containing the float value of the
	 * probabilities.
	 * 
	 * @return {@link FloatMatrix} of float probabilities values
	 */
	public FloatMatrix getProbabilityMatrix() {
		final FloatMatrix resultMatrix = new FloatMatrix(getWidth(), getHeight());
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				final float probabilityValue = this.pathBoundProbabilities[y][x].getProbability();
				resultMatrix.setValue(x, y, probabilityValue);
			}
		}
		return resultMatrix;
	}

	/**
	 * Returns the height of the result.
	 * 
	 * @return height of the result
	 */
	public int getHeight() {
		return pathBoundProbabilities.length;
	}

	/**
	 * Returns the width of the result.
	 * 
	 * @return width of the result
	 */
	public int getWidth() {
		return pathBoundProbabilities[0].length;
	}

}
