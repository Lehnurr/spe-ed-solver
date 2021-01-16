package webcommunication.webservice.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utility.game.player.PlayerAction;

public class ResponseParserTest {

	@Test
	public void testParseGameStep() {

		ResponseParser parser = new ResponseParser();

		String result = parser.parseResponse(PlayerAction.SPEED_UP);

		assertEquals("{\"action\":\"speed_up\"}", result);
	}

}
