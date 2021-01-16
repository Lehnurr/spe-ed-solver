package webcommunication.webservice.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.game.step.GameStep;
import utility.geometry.Point2i;

public class ResponseParserTest {

	@Test
	public void testParseGameStep() {

		ResponseParser parser = new ResponseParser();

		String result = parser.parseResponse(PlayerAction.SPEED_UP);

		assertEquals("{\"action\":\"speed_up\"}", result);
	}

}
