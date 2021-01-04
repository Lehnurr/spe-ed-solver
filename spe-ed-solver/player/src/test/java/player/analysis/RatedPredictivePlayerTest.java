package player.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);

		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);

		assertEquals(0, startPlayer.getRelativeRound());
	}

	@Test
	public void testChildRelativeRound() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
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
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 4, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5f);
		minSteps.setValue(new Point2i(5, 4), 1);
		probabilitites.setValue(new Point2i(5, 3), 0.7f);
		minSteps.setValue(new Point2i(5, 3), 2);
		probabilitites.setValue(new Point2i(5, 2), 0.3f);
		minSteps.setValue(new Point2i(5, 2), 0);
		
		RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.5f, nextPlayer.getSuccessRating(), 0.001f);
	}
	
	@Test
	public void testChildSuccessCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5f);
		minSteps.setValue(new Point2i(5, 4), 1);
		probabilitites.setValue(new Point2i(5, 2), 0.2f);
		minSteps.setValue(new Point2i(5, 2), 0);
		
		RatedPredictivePlayer nextPlayer0 = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);
		RatedPredictivePlayer nextPlayer1 = new RatedPredictivePlayer(nextPlayer0, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.4f, nextPlayer1.getSuccessRating(), 0.001f);
	}
	
	@Test
	public void testCutOffCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 4, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5f);
		minSteps.setValue(new Point2i(5, 4), 1);
		probabilitites.setValue(new Point2i(5, 3), 0.2f);
		minSteps.setValue(new Point2i(5, 3), 2);
		probabilitites.setValue(new Point2i(5, 2), 0.3f);
		minSteps.setValue(new Point2i(5, 2), 2);

		RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.3f, nextPlayer.getCutOffRating(), 0.001f);
	}
	
	@Test
	public void testChildCutOffCalculation() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(player);
		FloatMatrix probabilitites = new FloatMatrix(10, 10, 0);
		FloatMatrix minSteps = new FloatMatrix(10, 10, 0);

		probabilitites.setValue(new Point2i(5, 4), 0.5f);
		minSteps.setValue(new Point2i(5, 4), 10);
		probabilitites.setValue(new Point2i(5, 2), 0.2f);
		minSteps.setValue(new Point2i(5, 2), 10);
		
		RatedPredictivePlayer nextPlayer0 = new RatedPredictivePlayer(startPlayer, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);
		RatedPredictivePlayer nextPlayer1 = new RatedPredictivePlayer(nextPlayer0, PlayerAction.CHANGE_NOTHING, board,
				probabilitites, minSteps);

		assertEquals(0.2f, nextPlayer1.getCutOffRating(), 0.001f);
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
