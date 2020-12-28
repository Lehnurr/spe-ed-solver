package player.analysis.range.parallel;

import java.util.ArrayList;
import java.util.List;

import player.analysis.range.DirectRange;
import player.analysis.range.RangeResult;
import utility.game.board.Board;

/**
 * Calculates out of some initial Results some more Results
 */
public class PartialResultRunnable implements Runnable {

    private final Board<?> board;

    private final List<RangeResult> initialResults;
    private int initialResultsIndex;
    private final int initialResultsIncrement;

    private final List<RangeResult> endResults;

    /**
     * Initializes a parallel executable range calculation
     * 
     * @param board          The board for the path calculations
     * @param initialResults A given list of results as base for the further
     *                       calculation
     * @param startIndex     The index for the initialResults at which to start
     *                       calculating new results
     * @param increment      The number of entries in the list to be skipped, as
     *                       these are processed by other workers
     */
    public PartialResultRunnable(Board<?> board, List<RangeResult> initialResults, int startIndex, int increment) {
        this.board = board;
        this.initialResults = initialResults;
        this.endResults = new ArrayList<>();
        this.initialResultsIndex = startIndex;
        initialResultsIncrement = increment;
    }

    @Override
    /**
     * Performs the calculation of new results based on the initial results.
     */
    public void run() {
        // calculate the given initial results
        for (; initialResultsIndex < initialResults.size(); initialResultsIndex += initialResultsIncrement) {
            endResults.addAll(DirectRange.calculateDirectRange(board, initialResults.get(initialResultsIndex)));
        }

        // calculate the results of the already calculated endResults
        for (int endResultsIndex = 0; endResultsIndex < endResults.size(); endResultsIndex++) {
            endResults.addAll(DirectRange.calculateDirectRange(board, endResults.get(endResultsIndex)));
        }
    }

    /**
     * All {@link RangeResult#RangeResult results} calculated by this thread
     */
    public List<RangeResult> getResults() {
        return this.endResults;
    }

}
