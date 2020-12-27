package player.analysis.reachablepoints;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Random;

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
public class ReachablePointsCalculation {

	private static final int DEADLINE_MILLISECOND_BUFFER = 200;

	private final Board<Cell> board;

	private final FloatMatrix probabilities;
	private final FloatMatrix minSteps;

	private final RatedPredictivePlayer startPlayer;

	private final Deadline deadline;

	private final FloatMatrix successMatrixResult;
	private final FloatMatrix cutOffMatrixResult;

	/**
	 * Creates a new {@link ReachablePointsCalculation} object.
	 * 
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} with probabilities
	 * @param minSteps      {@link FloatMatrix} with minimum steps
	 * @param startPlayer   {@link RatedPredictivePlayer} to start with
	 * @param deadline      {@link Deadline} to limit execution time
	 */
	public ReachablePointsCalculation(final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final RatedPredictivePlayer startPlayer, final Deadline deadline) {

		this.board = board;
		this.probabilities = probabilities;
		this.minSteps = minSteps;
		this.startPlayer = startPlayer;

		this.deadline = deadline;

		this.successMatrixResult = new FloatMatrix(board.getWidth(), board.getHeight());
		this.cutOffMatrixResult = new FloatMatrix(board.getWidth(), board.getHeight());
	}

	/**
	 * Starts the execution for the {@link ReachablePointsCalculation} object.
	 */
	public void execute() {

		final Random random = new Random();

		final PriorityQueue<PriorityObject<RatedPredictivePlayer>> calculationPlayers = new PriorityQueue<>();

		final PriorityObject<RatedPredictivePlayer> initialQueueObject = new PriorityObject<>(0, this.startPlayer);
		calculationPlayers.add(initialQueueObject);

		while (calculationPlayers.size() > 0 && deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER) {
			final RatedPredictivePlayer calculationPlayer = calculationPlayers.remove().getValue();
			final Collection<RatedPredictivePlayer> children = calculationPlayer.getValidChildren(board, probabilities,
					minSteps);

			for (final RatedPredictivePlayer child : children) {
				final Point2i position = child.getPosition();

				final float oldSuccessRating = successMatrixResult.getValue(position);
				final float oldCutOffRating = cutOffMatrixResult.getValue(position);

				final float newSuccessRating = child.getSuccessRating();
				final float newCutOffRating = child.getCutOffRating();

				successMatrixResult.max(position, newSuccessRating);
				cutOffMatrixResult.max(position, newCutOffRating);

				final float successDelta = oldSuccessRating - newSuccessRating;
				final float cutOffDelta = oldCutOffRating - newCutOffRating;

				final float randomPriorityOffset = (float) (random.nextGaussian() * 0.1);
				final float priority = successDelta + cutOffDelta + randomPriorityOffset;

				calculationPlayers.add(new PriorityObject<RatedPredictivePlayer>(priority, child));
			}
		}
	}

}
