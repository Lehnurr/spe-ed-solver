package webcommunication.webservice.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerDirection;
import utility.game.step.GameStep;
import utility.geometry.Point2i;

public class GameStepParserTest {

	@Test
	public void testParseGameStep() {

		String jsonString = "{\"width\":4," + "\"height\":5," + "\"players\":{"
				+ "\"1\":{\"x\":1,\"y\":2,\"direction\":\"up\",\"speed\":4,\"active\":true},"
				+ "\"2\":{\"x\":3,\"y\":4,\"direction\":\"down\",\"speed\":5,\"active\":true}" + "},"
				+ "\"cells\": [[0,1,2,3],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]," + "\"running\":false," + "\"you\":1"
				+ "}";
		GameStepParser parser = new GameStepParser(null);

		GameStep result = parser.parseGameStep(jsonString, 0);

		List<IPlayer> enemies = new ArrayList<>(result.getEnemies().values());
		assertEquals(1, enemies.size());
		IPlayer enemy = enemies.remove(0);
		IPlayer self = result.getSelf();

		assertEquals(4, result.getBoard().getWidth());
		assertEquals(5, result.getBoard().getHeight());
		assertEquals(false, result.isRunning());
		assertEquals(new Point2i(1, 2), self.getPosition());
		assertEquals(new Point2i(3, 4), enemy.getPosition());
		assertEquals(PlayerDirection.UP, self.getDirection());
		assertEquals(PlayerDirection.DOWN, enemy.getDirection());
		assertEquals(4, self.getSpeed());
		assertEquals(5, enemy.getSpeed());
		assertEquals(true, self.isActive());
		assertEquals(true, enemy.isActive());
		assertEquals(CellValue.EMPTY_CELL, result.getBoard().getBoardCellAt(new Point2i(0, 0)).getCellValue());
		assertEquals(CellValue.PLAYER_ONE, result.getBoard().getBoardCellAt(new Point2i(1, 0)).getCellValue());
		assertEquals(CellValue.PLAYER_TWO, result.getBoard().getBoardCellAt(new Point2i(2, 0)).getCellValue());
		assertEquals(CellValue.PLAYER_THREE, result.getBoard().getBoardCellAt(new Point2i(3, 0)).getCellValue());
	}

}
