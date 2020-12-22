package player.analysis.range;

import java.util.ArrayList;
import java.util.List;

import utility.game.board.Board;
import utility.game.player.IMovablePlayer;
import utility.game.player.PlayerAction;

/**
 * DirectRange to calculate all steps, which can be derived from a Player
 * (Without the movement of the enemies)
 */
public final class DirectRange {

    private DirectRange() {
    }

    /**
     * Calculates the Result for all possible Actions
     * 
     * @param board The Board of the Current GameStep
     * @param state A State of a Movable
     * @return A List of all Possible targets
     */
    public static List<RangeResult> calculateDirectRange(Board<?> board, IMovablePlayer state) {
        List<RangeResult> validResults = new ArrayList<>();

        for (var action : PlayerAction.values()) {
            RangeResult result = doActionAndMove(board, action, state);
            if (result != null)
                validResults.add(result);
        }

        // return valid Results
        return validResults;
    }

    private static RangeResult doActionAndMove(Board<?> board, PlayerAction action, IMovablePlayer state) {
        RangeResult result;
        if (state instanceof RangeResult)
            result = (RangeResult) state.copy();
        else
            result = new RangeResult(action, state);

        result.setNextAction(action);
        result.doAction();
        result.doMove();

        // Check if speed is valid & check for no self collision
        if (!result.isActive()) {
            return null;
        }

        // check for collisions with already set cells on the board
        for (var passedPoint : result.getLastPassedCells()) {
            var passedCell = board.getBoardCellAt(passedPoint);
            if (passedCell == null || !passedCell.isEmpty()) {
                return null;
            }
        }

        return result;
    }
}
