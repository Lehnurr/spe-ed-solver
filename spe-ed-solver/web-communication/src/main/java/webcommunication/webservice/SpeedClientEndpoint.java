package webcommunication.webservice;

import java.io.IOException;
import java.util.function.Function;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import utility.game.GameStepInfo;
import utility.game.player.PlayerAction;
import webcommunication.ConnectionInitializationException;

/**
 * {@link ClientEndpoint} annotated endpoint to connect a webservice client to
 * the spe_ed webservice.
 */
@ClientEndpoint
public class SpeedClientEndpoint {

	// session of the endpoint
	private final Session session;

	private final Function<GameStepInfo, PlayerAction> handleStepFunction;

	/**
	 * Creates a new {@link SpeedClientEndpoint} and opens a connection to a spe_ed
	 * webserver with the given {@link WebserviceConnectionURI}.
	 * 
	 * @param webserviceConnectionURI {@link WebserviceConnectionURI} to connect to
	 *                                the webserver
	 * @param handleStepFunction      {@link Function} which handles a single game
	 *                                step
	 * @throws ConnectionInitializationException
	 */
	public SpeedClientEndpoint(final WebserviceConnectionURI webserviceConnectionURI,
			final Function<GameStepInfo, PlayerAction> handleStepFunction) throws ConnectionInitializationException {

		this.handleStepFunction = handleStepFunction;

		this.session = connectToServer(webserviceConnectionURI);
	}

	/**
	 * Function to connect to a spe_ed webserver and returning the session of the
	 * newly opened connection.
	 * 
	 * @param webserviceConnectionURI
	 * @return {@link Session} of the opened connection
	 * @throws ConnectionInitializationException
	 */
	private Session connectToServer(final WebserviceConnectionURI webserviceConnectionURI)
			throws ConnectionInitializationException {
		final ClientManager clientManager = ClientManager.createClient();

		try {
			return clientManager.connectToServer(this, webserviceConnectionURI.getURI());
		} catch (DeploymentException exception) {
			throw new ConnectionInitializationException("Initializing connection to spe_ed webservice not possible.",
					exception);
		}
	}

	@OnOpen
	public void onOpen(Session session) {

	}

	@OnMessage
	public void onMessage(String message, Session session) throws MessageSendingException {
		// TODO parsing request String to {@link GameStepInfo}
		// PlayerAction responseAction = handleStepFunction.apply(null);
		// TODO parsing {@link PlayerAction} to String response
		String responseText = "{\"action\":" + PlayerAction.CHANGE_NOTHING.getName() + "}";
		
		try {
			session.getBasicRemote().sendText(responseText);
		} catch (IOException e) {
			throw new MessageSendingException("Could not sent response: " + responseText, e);
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}

}
