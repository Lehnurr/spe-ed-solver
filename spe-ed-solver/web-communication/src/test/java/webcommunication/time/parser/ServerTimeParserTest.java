package webcommunication.time.parser;

import static org.junit.Assert.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class ServerTimeParserTest {

	@Test
	public void test() {
		ServerTimeParser parser = new ServerTimeParser();
		String jsonString = "{\"time\":\"1999-04-10T03:50:37Z\",\"milliseconds\":123}";
		
		ZonedDateTime result = parser.parseTimeAPIResponse(jsonString);
		
		assertEquals(result, ZonedDateTime.of(1999, 4, 10, 3, 50, 37, 123_000_000, ZoneId.of("UTC")));		
	}

}
