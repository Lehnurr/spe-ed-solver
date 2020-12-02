package webcommunication.webservice;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import webcommunication.ConnectionInitializationException;

/**
 * {@link ClientEndpoint} annotated endpoint to connect a webservice client to
 * the spe_ed webservice.
 */
@ClientEndpoint
public class SpeedClientEndpoint {

	// session of the endpoint
	private final Session session;

	/**
	 * Initializes the connection to the webservice.
	 * 
	 * @param webserviceUrl url of the webservice
	 * @param apiKey        API key to use the webservice
	 * @throws ConnectionInitializationException thrown when connecting to the
	 *                                           webservice was not possible
	 */
	public SpeedClientEndpoint(final WebserviceConnectionURI webserviceConnectionURI)
			throws ConnectionInitializationException {

		final ClientManager clientManager = ClientManager.createClient();

		try {
			session = clientManager.connectToServer(this, webserviceConnectionURI.getURI());
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

	@OnError
	public void onClose(Session session, Throwable throwable) {
	}

}
