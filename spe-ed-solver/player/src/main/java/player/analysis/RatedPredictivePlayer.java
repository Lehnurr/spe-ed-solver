package player.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Class extending a {@link PredictivePlayer} with rating capability. This
 * includes ratings for success and cutting off enemies.
 */
public class RatedPredictivePlayer extends PredictivePlayer {

	private final float successRating;
	private final float cutOffRating;

	private final int relativeRound;

	/**
	 * Creates a new {@link RatedPredictivePlayer} from a standard {@link IPlayer}
	 * instance.
	 * 
	 * @param player {@link IPlayer} to initialize
	 */
	public RatedPredictivePlayer(final IPlayer player) {
		super(player);
		this.successRating = 1;
		this.cutOffRating = 0;
		this.relativeRound = 0;
	}

	/**
	 * Creates a new {@link RatedPredictivePlayer} from a given
	 * {@link RatedPredictivePlayer} parent and an {@link PlayerAction} which is
	 * performed by the parent to generate the child.
	 * 
	 * @param parent        {@link RatedPredictivePlayer} parent to perform the
	 *                      action
	 * @param action        {@link PlayerAction} performed by the parent
	 * @param board         {@link Board} to perform the action on
	 * @param probabilities {@link FloatMatrix} containing enemy probabilities
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each point
	 */
	public RatedPredictivePlayer(final RatedPredictivePlayer parent, final PlayerAction action, final Board<Cell> board,
			final FloatMatrix probabilities, final FloatMatrix minSteps) {
		super(parent, action, board);

		this.relativeRound = parent.getRelativeRound() + 1;

		if (isActive()) {
			this.successRating = calculateSuccessRating(parent.getSuccessRating(), probabilities, minSteps,
					getShortTail(), relativeRound);
			this.cutOffRating = calculateCutOffRating(probabilities, minSteps, getShortTail(), relativeRound);
		} else {
			this.successRating = 0;
			this.cutOffRating = 0;
		}
	}

	/**
	 * Calculates the new success rating based on the success rating of the parent.
	 * 
	 * @param parentSuccessRating success rating of the parent
	 * @param probabilities       {@link FloatMatrix} with enemy probabilities to
	 *                            consider
	 * @param minSteps            {@link FloatMatrix} with the enemy minimum steps
	 *                            to consider
	 * @param shortTail           {@link Point2i points} of the last step from the
	 *                            parent to the child
	 * @param relativeRound       amount of round which are predictive
	 * @return new success rating for the child
	 */
	private float calculateSuccessRating(final float parentSuccessRating, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final Collection<Point2i> shortTail, final int relativeRound) {
		float successFactor = 1;
		for (final Point2i point : shortTail) {
			if (relativeRound >= minSteps.getValue(point))
				successFactor = Math.min(successFactor, 1 - probabilities.getValue(point));
		}
		return parentSuccessRating * successFactor;
	}

	/**
	 * Calculates the cut off rating based on the probabilities on the short tail.
	 * 
	 * @param probabilities {@link FloatMatrix} with enemy probabilities to consider
	 * @param minSteps      {@link FloatMatrix} with the enemy minimum steps to
	 *                      consider
	 * @param shortTail     {@link Point2i points} of the last step from the parent
	 *                      to the child
	 * @param relativeRound amount of round which are predictive
	 * @return new cut off rating for the child
	 */
	private float calculateCutOffRating(final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Collection<Point2i> shortTail, final int relativeRound) {

		float cutOff = 0;
		for (final Point2i point : shortTail) {
			if (relativeRound < minSteps.getValue(point)) {
				final float probabilityValue = probabilities.getValue(point);
				final float localCutOff = (float) (-2 * Math.pow(probabilityValue - 1, 2) + 1);
				cutOff = Math.max(cutOff, localCutOff);
			}
		}
		return cutOff;
	}

	/**
	 * Returns a {@link Collection} of all the valid {@link RatedPredictivePlayer}
	 * children of this {@link RatedPredictivePlayer}.
	 * 
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} with enemy probabilities
	 * @param minSteps      {@link FloatMatrix} with the enemy minimum steps for
	 *                      each point
	 * @return {@link RatedPredictivePlayer children} with valid last steps
	 */
	public Collection<RatedPredictivePlayer> getValidChildren(final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {

		final List<RatedPredictivePlayer> result = new ArrayList<>();
		for (final PlayerAction action : PlayerAction.values()) {
			final RatedPredictivePlayer child = new RatedPredictivePlayer(this, action, board, probabilities, minSteps);
			if (child.isActive())
				result.add(child);
		}
		return result;
	}

	/**
	 * Returns the success rating.
	 * 
	 * @return success rating
	 */
	public float getSuccessRating() {
		return successRating;
	}

	/**
	 * Returns the cut off rating.
	 * 
	 * @return cut off rating
	 */
	public float getCutOffRating() {
		return cutOffRating;
	}

	/**
	 * Returns the amount of predictive round as relative round.
	 * 
	 * @return relative round
	 */
	public int getRelativeRound() {
		return relativeRound;
	}

}
