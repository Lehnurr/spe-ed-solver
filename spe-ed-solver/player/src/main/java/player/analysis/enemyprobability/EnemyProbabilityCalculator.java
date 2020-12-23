package player.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;

/**
 * Calculator class that can calculate a {@link PredictionResult} for the future
 * enemy behavior.
 */
public class EnemyProbabilityCalculator {

	/**
	 * Returns a {@link PredictionResult} for the given {@link Collection} of
	 * {@link IPlayer players} on the {@link Board}. The search depth determines how
	 * many future rounds the calculation uses.
	 * 
	 * @param enemies     {@link Collection} of {@link IPlayer} enemies
	 * @param board       {@link Board} the enemies move on
	 * @param searchDepth future rounds to use for the calculation
	 * @return {@link PredictionResult} of the calculation
	 */
	public PredictionResult performCalculation(final Collection<IPlayer> enemies, final Board<Cell> board,
			final int searchDepth) {

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
		
		predictions.get(0).getPredictionResult();
		
		PredictionResult combinedResult = predictions.remove(0).getPredictionResult();
		for (final SingleEnemyPrediction prediction : predictions) {
			combinedResult = new PredictionResult(combinedResult, prediction.getPredictionResult());
		}
		return combinedResult;

	}

}
