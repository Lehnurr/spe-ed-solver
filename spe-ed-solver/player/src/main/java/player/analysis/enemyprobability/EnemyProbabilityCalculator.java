package player.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Performs the {@link SingleEnemyPrediction} for each given player and combines
 * the results. The results are stored locally until they are updated.
 */
public class EnemyProbabilityCalculator {

	private FloatMatrix probabilities;
	private FloatMatrix minSteps;

	/**
	 * Calculates the probabilities and min steps for each of the given
	 * {@link IPlayer} enemies.
	 * 
	 * @param enemies     {@link IPlayer players} to calculate the probabilities for
	 * @param board       {@link Board} to check for collisions
	 * @param searchDepth amount of recursive steps to be taken for each player
	 */
	public void performCalculation(final Collection<IPlayer> enemies, final Board<Cell> board, final int searchDepth) {

		final List<SingleEnemyPrediction> predictions = new ArrayList<>();
		final List<Thread> threads = new ArrayList<>();

		for (final IPlayer enemy : enemies) {
			final SingleEnemyPrediction prediction = new SingleEnemyPrediction(board, enemy);
			predictions.add(prediction);
			final Thread thread = new Thread(() -> prediction.doCalculation(searchDepth));
			thread.start();
			threads.add(thread);
		}

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO warning to logger but ignore
				e.printStackTrace();
			}
		}

		final SingleEnemyPrediction firstElement = predictions.remove(0);
		probabilities = firstElement.getProbabilitiesMatrix();
		minSteps = firstElement.getMinStepsMatrix();

		for (final SingleEnemyPrediction prediction : predictions) {
			probabilities = probabilities.max(prediction.getProbabilitiesMatrix());
			minSteps = minSteps.min(prediction.getMinStepsMatrix());
		}

		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				final Point2i position = new Point2i(x, y);
				if (!board.getBoardCellAt(position).isEmpty()) {
					probabilities.setValue(position, 1);
					minSteps.setValue(position, 0);
				}
			}
		}
	}

	/**
	 * Returns the probability result of the combined calculations as
	 * {@link FloatMatrix}.
	 * 
	 * @return probability result as {@link FloatMatrix}
	 */
	public FloatMatrix getProbabilitiesMatrix() {
		return probabilities;
	}

	/**
	 * Returns the minimum step amount for each position as {@link FloatMatrix}
	 * 
	 * @return min steps {@link FloatMatrix}
	 */
	public FloatMatrix getMinStepsMatrix() {
		return minSteps;
	}

}
