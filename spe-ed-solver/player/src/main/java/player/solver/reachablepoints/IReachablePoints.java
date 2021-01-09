package player.solver.reachablepoints;

import java.util.Map;

import player.analysis.ActionsRating;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.IBoardCell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;

/**
 * Interface for reachable points calculations. The instances are responsible
 * for calculating the success and cut off {@link ActionsRating ratings} for the
 * given information. Results should be stored until they are updated.
 */
public interface IReachablePoints<CellType extends IBoardCell<?>> {

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
	void performCalculation(IPlayer self, Board<CellType> board, FloatMatrix probabilities, FloatMatrix minSteps,
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
	 * Returns a {@link FloatMatrix} containing the success ratings the given action
	 * 
	 * @param action the action for which the matrix is to be retrieved
	 * @return success matrix for the fiven action
	 */
	FloatMatrix getSuccessMatrixResult(PlayerAction action);

	/**
	 * Returns a {@link FloatMatrix} containing the cut off ratings the given action
	 * 
	 * @param action the action for which the matrix is to be retrieved
	 * @return cut off matrix for the fiven action
	 */
	FloatMatrix getCutOffMatrixResult(PlayerAction action);
}