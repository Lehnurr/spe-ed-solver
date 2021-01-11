package webcommunication;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Class responsible for wrapping the production of {@link HttpClient
 * HttpClients} with a {@link SslContextFactory}.
 */
public class JettyHttpClientFactory {

	private static final String JETTY_ENDPOINT_IDENTIFICATION_ALGORITHM = "HTTPS";

	/**
	 * Returns a new {@link HttpClient} with a {@link SslContextFactory} able to
	 * send HTTPS requests.
	 * 
	 * @return the new {@link HttpClient}
	 */
	public HttpClient getNewHttpClient() {
		final SslContextFactory sslContextFactory = new SslContextFactory.Client();
		sslContextFactory.setEndpointIdentificationAlgorithm(JETTY_ENDPOINT_IDENTIFICATION_ALGORITHM);

		return new HttpClient(sslContextFactory);
	}

}
