package player.solver.reachablepoints;

import java.util.Collection;

import player.analysis.ActionsRating;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.FloatMatrix;

/**
 * Interface for reachable points calculations. The instances are responsible
 * for calculating the success and cut off {@link ActionsRating ratings} for the
 * given information. Results should be stored until they are updated.
 */
public interface IReachablePoints {

	/**
	 * Performs the calculation with the given values and updates the stored
	 * results.
	 * 
	 * @param gameStep      {@link IPlayer} of yourself in the spe_ed game
	 * 
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 */
	void performCalculation(GameStep gameStep, FloatMatrix probabilities, FloatMatrix minSteps);

	/**
	 * Combines the last calculated {@link ActionsRating}.
	 * 
	 * @param aggressiveWeight Weight for the {@link ActionsRating}, which
	 *                         stimulates aggresive actions
	 * @param defensiveWeight  Weight for the {@link ActionsRating}, which
	 *                         stimulates defensive actions
	 * @return The combined ratings
	 */
	public ActionsRating combineActionsRating(float aggressiveWeight, float defensiveWeight);

	/**
	 * Logs the last calculated Ratings and the combined rating as GameInformation
	 * 
	 * @param combinedActionsRating the combined Rating to log
	 */
	public void logGameInformation(ActionsRating combinedActionsRating);

	/**
	 * Creates for all last calculated Matrix-Results a
	 * {@link ContextualFloatMatrix}
	 * 
	 * @param action for Matrix-Results grouped by Collection the Matrix for the
	 *               given action will be used
	 * 
	 * @return A {@link Collection} of {@link ContextualFloatMatrix
	 *         ContextualFloatMatrices}
	 */
	public Collection<ContextualFloatMatrix> getContextualFloatMatrices(PlayerAction action);
}