package solver.reachablepoints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import solver.MockPlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

public class RatedPredictivePlayerTest {

	@Test
	public void testInitialRelativeRound() {
		IPlayer player = new MockPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);

		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);

		assertEquals(0, startPlayer.getRelativeRound());
	}

	@Test
	public void testChildRelativeRound() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new MockPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(1, nextPlayer.getRelativeRound());
	}

	@Test
	public void testSuccessCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new MockPlayer(0, PlayerDirection.UP, 4, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5);
		minSteps.setValue(new Point2i(5, 4), 1);
		probabilitites.setValue(new Point2i(5, 3), 0.7);
		minSteps.setValue(new Point2i(5, 3), 2);
		probabilitites.setValue(new Point2i(5, 2), 0.3);
		minSteps.setValue(new Point2i(5, 2), 0);

		RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.425, nextPlayer.getSuccessRating(), 0.01);
	}

	@Test
	public void testChildSuccessCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new MockPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5);
		minSteps.setValue(new Point2i(5, 4), 1);
		probabilitites.setValue(new Point2i(5, 2), 0.2);
		minSteps.setValue(new Point2i(5, 2), 0);

		RatedPredictivePlayer nextPlayer0 = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);
		RatedPredictivePlayer nextPlayer1 = new RatedPredictivePlayer(nextPlayer0, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.3082, nextPlayer1.getSuccessRating(), 0.001);
	}

	@Test
	public void testCutOffCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new MockPlayer(0, PlayerDirection.UP, 4, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5);
		minSteps.setValue(new Point2i(5, 4), 1);
		probabilitites.setValue(new Point2i(5, 3), 0.2);
		minSteps.setValue(new Point2i(5, 3), 2);
		probabilitites.setValue(new Point2i(5, 2), 0.3);
		minSteps.setValue(new Point2i(5, 2), 2);

		RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.1277, nextPlayer.getCutOffRating(), 0.001);
	}

	@Test
	public void testChildCutOffCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new MockPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5);
		minSteps.setValue(new Point2i(5, 4), 10);
		probabilitites.setValue(new Point2i(5, 2), 0.2);
		minSteps.setValue(new Point2i(5, 2), 10);

		RatedPredictivePlayer nextPlayer0 = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);
		RatedPredictivePlayer nextPlayer1 = new RatedPredictivePlayer(nextPlayer0, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.2, nextPlayer1.getCutOffRating(), 0.001);
	}

	private Board<Cell> createEmptyTestBoard() {
		Cell[][] cells = new Cell[10][10];
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[0].length; x++) {
				cells[y][x] = new Cell(0);
			}
		}
		return new Board<Cell>(cells);
	}

}
