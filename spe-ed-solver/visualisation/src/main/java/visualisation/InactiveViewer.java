package visualisation;

import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerAction;
import utility.geometry.ContextualFloatMatrix;

/**
 * Implementation for the {@link IViewer} interface which doesn't do anything.
 * Can be used if no {@link Viewer} should be used since the viewer is disabled.
 */
public class InactiveViewer implements IViewer {

	@Override
	public void commitRound(int playerId, double availableTime, PlayerAction performedAction, double requiredTime,
			Board<Cell> board, List<ContextualFloatMatrix> boardRatings) {
	}

}
