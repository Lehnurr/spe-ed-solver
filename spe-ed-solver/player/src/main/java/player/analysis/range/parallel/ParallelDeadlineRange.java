package player.analysis.range.parallel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import player.analysis.range.DirectRange;
import player.analysis.range.RangeResult;
import utility.game.board.Board;
import utility.game.player.IMovablePlayer;
import utility.game.step.Deadline;

public class ParallelDeadlineRange {
    private Deadline deadline;

    /**
     * 
     * @param deadline The Deadline for the Result
     */
    public ParallelDeadlineRange(Deadline deadline) {
        this.deadline = deadline;
    }

    /**
     * Calculates as many reachable points as possible in the predefined time with
     * multiple threads
     * 
     * @param board The Board of the Current GameStep
     * @param state A State of a {@link #IMovablePlayer}
     * @return A List of all Possible targets
     */
    public List<RangeResult> calculateRange(Board<?> board, IMovablePlayer state) {
        final int NUMBER_OF_THREADS = 4;
        final int BASE_PER_THREAD = 500;

        var initialResults = DirectRange.calculateDirectRange(board, state);

        // calculate Ranges until numberOfThreads * basePerThread uncalculated results
        // are available
        final int neededUnsolvedResults = NUMBER_OF_THREADS * BASE_PER_THREAD;
        int currentResultIndex;
        for (currentResultIndex = 0; deadline.getRemainingMilliseconds() > 10
                && currentResultIndex < initialResults.size()
                && initialResults.size() - currentResultIndex < neededUnsolvedResults; currentResultIndex++) {
            initialResults.addAll(DirectRange.calculateDirectRange(board, initialResults.get(currentResultIndex)));
        }

        // Lock writing on the totalResults
        initialResults = Collections.unmodifiableList(initialResults);

        //
        var tasks = new ArrayList<PartialResultRunnable>();

        // Distribute the tasks to be done
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int threadNo = 0; threadNo < NUMBER_OF_THREADS; threadNo++) {
            final int threadStartIndex = currentResultIndex + threadNo;
            final int increment = NUMBER_OF_THREADS;
            var runnable = new PartialResultRunnable(board, initialResults, threadStartIndex, increment);
            tasks.add(runnable);
            executor.execute(runnable);
        }

        executor.shutdownNow();

        // get results
        for (var x : tasks) {
            System.out.println(x.getResults().size());
        }

        return initialResults;
    }

}
