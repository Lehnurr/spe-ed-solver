package player.analysis.reachablepoints;

import java.util.Map;

import player.analysis.ActionsRating;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;

public interface IReachablePoints {

	/**
	 * Performs the calculation with the given values and updates the stored
	 * results.
	 * 
	 * @param self          {@link IPlayer} of yourself in the spe_ed game
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 * @param deadline      {@link Deadline} which must not be exceeded
	 */
	void performCalculation(IPlayer self, Board<Cell> board, FloatMatrix probabilities, FloatMatrix minSteps,
			Deadline deadline);

	/**
	 * Returns the {@link ActionsRating} for the success ratings for each
	 * {@link PlayerAction}.
	 * 
	 * @return success ratings
	 */
	ActionsRating getSuccessRatingsResult();

	/**
	 * Returns the {@link ActionsRating} for the cut off ratings for each
	 * {@link PlayerAction}.
	 * 
	 * @return cut off ratings
	 */
	ActionsRating getCutOffRatingsResult();

	/**
	 * Returns a {@link Map} mapping each {@link PlayerAction} to a
	 * {@link FloatMatrix} containing the success ratings for each element.
	 * 
	 * @return success matrices map
	 */
	Map<PlayerAction, FloatMatrix> getSuccessMatrixResult();

	/**
	 * Returns a {@link Map} mapping each {@link PlayerAction} to a
	 * {@link FloatMatrix} containing the cut off ratings for each element.
	 * 
	 * @return cut off matrices map
	 */
	Map<PlayerAction, FloatMatrix> getCutOffMatrixResult();

}