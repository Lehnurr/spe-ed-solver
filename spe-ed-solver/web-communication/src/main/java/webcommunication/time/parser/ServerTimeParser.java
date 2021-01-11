package webcommunication.time.parser;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Responsible for parsing a {@link JSONServerTime}.
 */
public class ServerTimeParser {

	// UTC is default of spe_ed server
	private static final String SERVER_TIME_FORMAT = "UTC";

	private final Gson gson;

	/**
	 * Creates a new @{@link ServerTimeParser} with {@link Gson} object by using the
	 * default {@link GsonBuilder}.
	 */
	public ServerTimeParser() {
		this(new GsonBuilder().create());
	}

	/**
	 * Creates a new {@link ServerTimeParser} with a given {@link Gson} object.
	 * 
	 * @param gson the {@link Gson} object to use for parsing
	 */
	public ServerTimeParser(final Gson gson) {
		this.gson = gson;
	}

	/**
	 * Parses a given response as {@link String} and returns the received server
	 * time.
	 * 
	 * @param responseString server response as {@link String}
	 * @return received server time
	 */
	public ZonedDateTime parseTimeAPIResponse(final String responseString) {

		final JSONServerTime jsonObject = gson.fromJson(responseString, JSONServerTime.class);

		final Date baseDate = jsonObject.time;
		final ZonedDateTime baseTime = ZonedDateTime.ofInstant(baseDate.toInstant(), ZoneId.of(SERVER_TIME_FORMAT));

		final long additionalNanos = jsonObject.milliseconds * 1000000;
		final ZonedDateTime adaptedTime = baseTime.plusNanos(additionalNanos);

		return adaptedTime;
	}

}
