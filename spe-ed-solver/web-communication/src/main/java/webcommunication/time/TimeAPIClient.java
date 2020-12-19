package webcommunication.time;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

import webcommunication.JettyHttpClientFactory;
import webcommunication.time.parser.ServerTimeParser;

/**
 * Class responsible for requesting the server time.
 */
public class TimeAPIClient {

	private final ServerTimeParser responseParser;

	/**
	 * Creates a new {@link TimeAPIClient} with a given
	 * {@link ServerTimeParser}.
	 * 
	 * @param responseParser {@link ServerTimeParser} responsible for parsing
	 *                       the time API responses
	 */
	public TimeAPIClient(final ServerTimeParser responseParser) {
		this.responseParser = responseParser;
	}

	/**
	 * Responsible for requesting the the server time of the given spe_ed time API
	 * with a HTTP GET request.
	 * 
	 * @param targetUri {@link URI} of the time API
	 * @return {@link ZonedDateTime} of server time
	 * @throws TimeRequestException
	 */
	public ZonedDateTime getServerTime(final URI targetUri) throws TimeRequestException {

		final HttpClient client = getStartedHttpClient();

		final String responseString = getResponseString(targetUri, client);

		closeHttpClient(client);

		ZonedDateTime time = responseParser.parseTimeAPIResponse(responseString);

		return time;
	}

	/**
	 * Creates a new {@link HttpClient} and starts it.
	 * 
	 * @return the started {@link HttpClient}
	 * @throws TimeRequestException
	 */
	private HttpClient getStartedHttpClient() throws TimeRequestException {
		
		final JettyHttpClientFactory factory = new JettyHttpClientFactory();
		final HttpClient client = factory.getNewHttpClient();

		try {
			client.start();
		} catch (Exception e) {
			throw new TimeRequestException(
					"The jetty http client could not get started with the following reason: " + e.getMessage(), e);
		}

		return client;
	}

	/**
	 * Sends a new HTTP GET request to the given server and returns the received
	 * response as {@link String}.
	 * 
	 * @param uri {@link URI} of the server
	 * @return response as {@link String}
	 * @throws TimeRequestException
	 */
	private String getResponseString(final URI uri, final HttpClient client) throws TimeRequestException {

		final ContentResponse response;

		try {
			response = client.GET(uri);
		} catch (InterruptedException e) {
			throw new TimeRequestException("The time request was interrupted!", e);
		} catch (ExecutionException e) {
			throw new TimeRequestException("The time request could not be executed!", e);
		} catch (TimeoutException e) {
			throw new TimeRequestException("The time request timed out!", e);
		}

		return response.getContentAsString();
	}

	/**
	 * Closes the given {@link HttpClient}.
	 * 
	 * @param client
	 * @throws TimeRequestException
	 */
	private void closeHttpClient(final HttpClient client) throws TimeRequestException {

		try {
			client.stop();
		} catch (Exception e) {
			throw new TimeRequestException("The jetty http client could not get closed!", e);
		}

	}

}
