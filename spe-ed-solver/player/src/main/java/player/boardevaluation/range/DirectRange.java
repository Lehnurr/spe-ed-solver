package player.boardevaluation.range;

import java.util.ArrayList;
import java.util.List;

import utility.game.board.Board;
import utility.game.player.IMovable;
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
    public static List<RangeResult> calculateDirectRange(Board<?> board, IMovable state) {

        List<RangeResult> validResults = new ArrayList<>();

        for (var action : PlayerAction.values()) {

            var copy = state.copy();
            copy.setNextAction(action);
            copy.doAction();
            copy.doMove();
            // TODO: check if speed is valid;
            // TODO: check if all new steps on board & for no collision & for no self
            // collision
            // validResults.add(copy);
        }

        // return valid Results
        return validResults;
    }
}
