package player.analysis.range;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import utility.game.board.Board;
import utility.game.player.IMovablePlayer;
import utility.game.step.Deadline;

/**
 * DeadlineRange to calculate up to a deadline all steps, which can be derived
 * from a Player (Without the movement of the enemies) in N-Rounds
 */
public final class DeadlineRange {

    private Deadline deadline;

    /**
     * 
     * @param deadline The Deadline for the Result
     */
    public DeadlineRange(Deadline deadline) {
        this.deadline = deadline;
    }

    /**
     * Calculates as many reachable points as possible in the predefined time
     * 
     * @param board The Board of the Current GameStep
     * @param state A State of a {@link #IMovablePlayer}
     * @return A List of all Possible targets
     */
    public List<RangeResult> calculateRange(Board<?> board, IMovablePlayer state) {

        var totalResults = DirectRange.calculateDirectRange(board, state);

        // calculate Ranges until only 10 miliseconds are left
        for (int i = 0; deadline.getRemainingMilliseconds() > 10 && i < totalResults.size(); i++) {
            totalResults.addAll(DirectRange.calculateDirectRange(board, totalResults.get(i)));
        }

        return totalResults;
    }

}
