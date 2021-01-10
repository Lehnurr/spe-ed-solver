package player.analysis.success;

import java.util.EnumMap;
import java.util.Map;

import player.analysis.ActionsRating;
import utility.game.player.PlayerAction;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Calculation Class to combine different success calculations
 */
public class SuccessCalculation {
    private Map<PlayerAction, FloatMatrix> matrixResult;
    private ActionsRating ratingsResult;

    /**
     * Initializes a new calculator
     * 
     * @param width  the width of the board and the resulting {@link FloatMatrix}
     * @param height the height of the board and the resulting {@link FloatMatrix}
     */
    public SuccessCalculation(int width, int height) {
        ratingsResult = new ActionsRating();
        this.matrixResult = new EnumMap<>(PlayerAction.class);

        for (final PlayerAction action : PlayerAction.values()) {
            matrixResult.put(action, new FloatMatrix(width, height, 0));
        }
    }

    /**
     * Adds a new rating to the result
     * 
     * @param action        The initial action the rating is valid for
     * @param position      The position the rating is valid for
     * @param successRating The new calculated successrating
     */
    public void add(PlayerAction action, Point2i position, float successRating) {
        matrixResult.get(action).max(position, successRating);
    }

    /**
     * Combines the calculations of two {@link SuccessCalculation}
     * 
     * @param other a {@link SuccessCalculation} whose results should be added to
     *              this results
     */
    public void add(SuccessCalculation other) {
        for (final PlayerAction action : PlayerAction.values()) {
            final FloatMatrix selfMatrix = this.matrixResult.get(action);
            final FloatMatrix otherMatrix = other.matrixResult.get(action);

            matrixResult.computeIfPresent(action, (k, v) -> v.max(otherMatrix));

            final float maxRating = Math.max(selfMatrix.sum(), otherMatrix.sum());
            ratingsResult.setRating(action, maxRating);
        }
    }

    /**
     * Normalizes the Actionrating if necessary and returns it.
     * 
     * @return the normalized {@link ActionsRating}
     */
    public ActionsRating getRatingResult() {
        if (ratingsResult.maxRating() != 1)
            ratingsResult.normalize();

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
