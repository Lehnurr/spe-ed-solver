package webcommunication.webservice;

import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import webcommunication.ConnectionInitializationException;
import webcommunication.MalformedURLException;

/**
 * {@link ClientEndpoint} annotated endpoint to connect a webservice client to
 * the spe_ed webservice.
 */
@ClientEndpoint
public class SpeedClientEndpoint {

	// name of the parameter used to identify the APi key for the webservice
	private static final String KEY_PARAMETER_NAME = "key";

	// session of the endpoint
	private final Session session;

	/**
	 * Initializes the connection to the webservice.
	 * 
	 * @param webserviceUrl url of the webservice
	 * @param apiKey        API key to use the webservice
	 * @throws MalformedURLException             thrown when the URL is invalid
	 * @throws ConnectionInitializationException thrown when connecting to the
	 *                                           webservice was not possible
	 */
	public SpeedClientEndpoint(String webserviceUrl, String apiKey)
			throws MalformedURLException, ConnectionInitializationException {
		URI uri;
		try {
			uri = new URI(webserviceUrl + "?" + KEY_PARAMETER_NAME + "=" + apiKey);
		} catch (URISyntaxException exception) {
			throw new MalformedURLException("The given webservice URL is not valid!", exception);
		}

		final ClientManager clientManager = ClientManager.createClient();

		try {
			session = clientManager.connectToServer(this, uri);
		} catch (DeploymentException exception) {
			throw new ConnectionInitializationException("Initializing connection to spe_ed webservice not possible.",
					exception);
		}
	}

	@OnOpen
	public void onOpen(Session session) {

	}

	@OnMessage
	public void onMessage(String message, Session session) {
		// session.getBasicRemote().sendText("");
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
	}

}
