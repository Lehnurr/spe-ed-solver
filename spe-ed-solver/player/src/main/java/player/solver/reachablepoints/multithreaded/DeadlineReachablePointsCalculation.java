package player.solver.reachablepoints.multithreaded;

import java.util.Collection;

import player.analysis.LimitedQueue;
import player.analysis.RatedPredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Calculation object for calculating success and cut off ratings based on an
 * initial player until a {@link Deadline} is reached. Calculations are queued
 * up based on their local solution improvement and a small random value.
 */
public class DeadlineReachablePointsCalculation {

	private static final int DEADLINE_MILLISECOND_BUFFER = 500;
	private static final int QUEUE_SIZE = 10000;

	private final Board<Cell> board;

	private final FloatMatrix probabilities;
	private final FloatMatrix minSteps;

	private final RatedPredictivePlayer startPlayer;

	private final Deadline deadline;

	private final FloatMatrix successMatrixResult;
	private final FloatMatrix cutOffMatrixResult;

	private int calculatedPathsCount = 0;

	/**
	 * Creates a new {@link DeadlineReachablePointsCalculation} object.
	 * 
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param startPlayer   {@link RatedPredictivePlayer} to start with
	 * @param deadline      {@link Deadline} to limit execution time
	 */
	public DeadlineReachablePointsCalculation(final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final RatedPredictivePlayer startPlayer, final Deadline deadline) {

		this.board = board;
		this.probabilities = probabilities;
		this.minSteps = minSteps;
		this.startPlayer = startPlayer;

		this.deadline = deadline;

		this.successMatrixResult = new FloatMatrix(board.getWidth(), board.getHeight(), 0);
		this.cutOffMatrixResult = new FloatMatrix(board.getWidth(), board.getHeight(), 0);
	}

	/**
	 * Starts the execution for the {@link DeadlineReachablePointsCalculation}
	 * object.
	 */
	public void execute() {

		final LimitedQueue<RatedPredictivePlayer> queue = new LimitedQueue<>(RatedPredictivePlayer.class, QUEUE_SIZE);

		final RatedPredictivePlayer nextPlayer = this.startPlayer;
		if (this.startPlayer.isActive()) {
			queue.add(nextPlayer);
			successMatrixResult.max(nextPlayer.getPosition(), nextPlayer.getSuccessRating());
			cutOffMatrixResult.max(nextPlayer.getPosition(), nextPlayer.getCutOffRating());
		}

		while (queue.hasNext() && deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER) {
			final RatedPredictivePlayer calculationPlayer = queue.poll();
			final Collection<RatedPredictivePlayer> children = calculationPlayer.getValidChildren(board, probabilities,
					minSteps);

			for (final RatedPredictivePlayer child : children) {
				final Point2i position = child.getPosition();

				successMatrixResult.max(position, child.getSuccessRating());
				cutOffMatrixResult.max(position, child.getCutOffRating());

				queue.add(child);
				calculatedPathsCount++;
			}
		}
	}

	/**
	 * Returns the {@link FloatMatrix} of the success rating calculation.
	 * 
	 * @return success {@link FloatMatrix} result
	 */
	public FloatMatrix getSuccessMatrixResult() {
		return successMatrixResult;
	}

	/**
	 * Returns the {@link FloatMatrix} of the cut off rating calculation.
	 * 
	 * @return cut off {@link FloatMatrix} result
	 */
	public FloatMatrix getCutOffMatrixResult() {
		return cutOffMatrixResult;
	}

	/**
	 * Returns the amount of calculated paths made.
	 * 
	 * @return amount of calculated paths
	 */
	public int getCalculatedPathsCount() {
		return calculatedPathsCount;
	}

}
