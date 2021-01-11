package webcommunication.webservice;

import java.io.IOException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import webcommunication.JettyHttpClientFactory;

/**
 * A {@link SpeedWebSocketClient} to connect {@link SpeedWebSocket}.
 */
public class SpeedWebSocketClient {

	private final WebSocketClient websocketClient;

	/**
	 * Creates a new {@link SpeedWebSocketClient} with the configured properties for
	 * a spe_ed webservice and initializes the {@link WebSocketClient} to connect
	 * {@link SpeedWebSocket}.
	 * 
	 * @throws ConnectionInitializationException
	 */
	public SpeedWebSocketClient() throws ConnectionInitializationException {

		final JettyHttpClientFactory factory = new JettyHttpClientFactory();
		final HttpClient httpClient = factory.getNewHttpClient();

		websocketClient = new WebSocketClient(httpClient);

	}

	/**
	 * Starts the connection to the {@link WebSocketClient} if it is not started
	 * already.
	 * 
	 * @throws ConnectionInitializationException thrown when the connection could
	 *                                           not be initialized
	 */
	private synchronized void assureStarted() throws ConnectionInitializationException {

		if (!(websocketClient.isStarted() || websocketClient.isStarting())) {
			try {
				websocketClient.start();
			} catch (Exception e) {
				throw new ConnectionInitializationException("Starting the spe_ed socket not possible!", e);
			}
		}
	}

	/**
	 * Assures that the {@link WebSocketClient} is closed. This includes closing all
	 * {@link Session}.
	 * 
	 * @throws ConnectionTerminationException thrown when the connection could not
	 *                                        be terminated
	 */
	public synchronized void assureStopped() throws ConnectionTerminationException {
		for (final Session session : websocketClient.getOpenSessions()) {
			session.close();
		}

		try {
			websocketClient.stop();
		} catch (Exception e) {
			throw new ConnectionTerminationException(
					"Unable to close stop the websocket client after no session was opened!", e);
		}
	}

	/**
	 * Connects a {@link SpeedWebSocket} to a webservice server with the given
	 * {@link WebserviceConnectionURI}.
	 * 
	 * @param speedWebSocket          the {@link SpeedWebSocket} which should be
	 *                                connected
	 * @param webserviceConnectionURI the {@link WebserviceConnectionURI} of the
	 *                                webserver
	 * @throws ConnectionInitializationException thrown when no connection can be
	 *                                           initialized
	 */
	public void connectToServer(final SpeedWebSocket speedWebSocket,
			final WebserviceConnectionURI webserviceConnectionURI) throws ConnectionInitializationException {

		assureStarted();

		try {
			websocketClient.connect(speedWebSocket, webserviceConnectionURI.getURI());
		} catch (IOException e) {
			throw new ConnectionInitializationException("Initializing connection to spe_ed webservice not possible!",
					e);
		}

	}

}
