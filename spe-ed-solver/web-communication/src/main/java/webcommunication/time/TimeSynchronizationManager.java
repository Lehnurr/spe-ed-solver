package webcommunication.time;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;

import webcommunication.time.parser.ServerTimeParser;

public class TimeSynchronizationManager {
	
	public static void main(String[] args) throws URISyntaxException, TimeRequestException {
		
		URI uri = new URI("https://msoll.de/spe_ed_time");
		ServerTimeParser timeAPIResponseParser = new ServerTimeParser();
		
		TimeAPIClient timeAPIClient = new TimeAPIClient(timeAPIResponseParser);
		
		ZonedDateTime serverTime = timeAPIClient.getServerTime(uri);
		
		System.out.println("result:\t" + serverTime);
	}

}
