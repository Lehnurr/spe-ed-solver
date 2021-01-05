package player.analysis.reachablepoints.singlethreaded;

import java.util.Collection;

import player.analysis.LimitedQueue;
import player.analysis.RatedPredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

public class GradualReachablePointsCalculation {

	private static final int QUEUE_SIZE = 10000;

	private final Board<Cell> board;

	private final FloatMatrix probabilities;
	private final FloatMatrix minSteps;

	private final LimitedQueue<RatedPredictivePlayer> queue;

	private final FloatMatrix successMatrixResult;
	private final FloatMatrix cutOffMatrixResult;

	private int calculatedPathsCount = 0;

	public GradualReachablePointsCalculation(final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final RatedPredictivePlayer startPlayer) {

		this.board = board;
		this.probabilities = probabilities;
		this.minSteps = minSteps;

		this.queue = new LimitedQueue<RatedPredictivePlayer>(RatedPredictivePlayer.class, QUEUE_SIZE);

		this.successMatrixResult = new FloatMatrix(board.getWidth(), board.getHeight(), 0);
		this.cutOffMatrixResult = new FloatMatrix(board.getWidth(), board.getHeight(), 0);

		if (startPlayer.isActive()) {
			queue.add(startPlayer);
			successMatrixResult.max(startPlayer.getPosition(), startPlayer.getSuccessRating());
			cutOffMatrixResult.max(startPlayer.getPosition(), startPlayer.getCutOffRating());
		}
	}

	public void performSingleStep() {

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

	/**
	 * Determines if there is nothing more to be calculated.
	 * 
	 * @return true if nothing can be calculated anymore
	 */
	public boolean isFinished() {
		return queue.isEmpty();
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
