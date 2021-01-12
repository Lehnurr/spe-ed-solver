package solver.reachablepoints;

import java.util.Collection;

import solver.analysis.ActionsRating;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.FloatMatrix;
import utility.logging.LoggingLevel;

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
	 * @param aggressiveWeight weight for the {@link ActionsRating}, which
	 *                         stimulates aggresive decisions
	 * @param defensiveWeight  weight for the {@link ActionsRating}, which
	 *                         stimulates defensive decisions
	 * @return the combined ratings
	 */
	public ActionsRating combineActionsRating(float aggressiveWeight, float defensiveWeight);

	/**
	 * Logs the last calculated Ratings and the combined rating as
	 * {@link LoggingLevel#GAME_INFO}.
	 * 
	 * @param combinedActionsRating the combined Rating to log
	 */
	public void logGameInformation(ActionsRating combinedActionsRating);

	/**
	 * Creates for all last calculated Matrix-Results a
	 * {@link ContextualFloatMatrix}.
	 * 
	 * @param action for {@link ContextualFloatMatrix matrix results} grouped by
	 *               Collection the {@link ContextualFloatMatrix matrix} for the
	 *               given {@link PlayerAction action} will be used
	 * 
	 * @return a {@link Collection} of {@link ContextualFloatMatrix
	 *         ContextualFloatMatrices}
	 */
	public Collection<ContextualFloatMatrix> getContextualFloatMatrices(PlayerAction action);
}