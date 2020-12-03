package webcommunication.webservice;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
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

/**
 * {@link ClientEndpoint} annotated endpoint to connect a webservice client to
 * the spe_ed webservice.
 */
@ClientEndpoint
public class SpeedClientEndpoint {

	private final Function<GameStepInfo, PlayerAction> handleStepFunction;

	/**
	 * Creates a new {@link SpeedClientEndpoint} which is able to connect to a
	 * spe_ed webservice server.
	 * 
	 * @param handleStepFunction {@link Function} which handles a single game step
	 * @throws ConnectionInitializationException
	 */
	public SpeedClientEndpoint(final Function<GameStepInfo, PlayerAction> handleStepFunction)
			throws ConnectionInitializationException {

		this.handleStepFunction = handleStepFunction;

	}

	/**
	 * Function to connect to a spe_ed webserver and returning the session of the
	 * newly opened connection.
	 * 
	 * @param webserviceConnectionURI
	 * @return {@link Session} of the opened connection
	 * @throws ConnectionInitializationException
	 */
	public Session connectToServer(final WebserviceConnectionURI webserviceConnectionURI)
			throws ConnectionInitializationException {
		final ClientManager clientManager = ClientManager.createClient();

		final Session session;
		final CountDownLatch latch = new CountDownLatch(1);

		try {
			session = clientManager.connectToServer(this, webserviceConnectionURI.getURI());
			latch.await();
		} catch (DeploymentException exception) {
			throw new ConnectionInitializationException("Initializing connection to spe_ed webservice not possible.",
					exception);
		} catch (InterruptedException e) {
			throw new ConnectionInitializationException("The countdown latch for the webservice was interrupted!", e);
		}

		return session;
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connection opened!");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws MessageSendingException {
		System.out.println(message);
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
		System.out.println("Connection closed!");
		System.out.println(closeReason);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}

}
