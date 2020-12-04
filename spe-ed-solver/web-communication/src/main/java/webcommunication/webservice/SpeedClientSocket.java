package webcommunication.webservice;

import java.io.IOException;
import java.util.function.Function;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import utility.game.GameStepInfo;
import utility.game.player.PlayerAction;

/**
 * {@link WebSocket} annotated websocket to connect a webservice client to the
 * spe_ed webservice.
 */
@WebSocket
public class SpeedClientSocket {

	private final Function<GameStepInfo, PlayerAction> handleStepFunction;

	/**
	 * Creates a new {@link SpeedClientSocket} which is able to connect to a spe_ed
	 * webservice server.
	 * 
	 * @param handleStepFunction {@link Function} which handles a single game step
	 * @throws ConnectionInitializationException
	 */
	public SpeedClientSocket(final Function<GameStepInfo, PlayerAction> handleStepFunction)
			throws ConnectionInitializationException {

		this.handleStepFunction = handleStepFunction;

	}

	/**
	 * Method to connect to a spe_ed webserver and returning the session of the
	 * newly opened connection.
	 * 
	 * @param webserviceConnectionURI
	 * @return {@link Session} of the opened connection
	 * @throws ConnectionInitializationException
	 */
	public void connectToServer(final WebserviceConnectionURI webserviceConnectionURI)
			throws ConnectionInitializationException {

		final SslContextFactory sslContextFactory = new SslContextFactory.Client();
		sslContextFactory.setTrustAll(true);

		final HttpClient httpClient = new HttpClient(sslContextFactory);

		WebSocketClient client = new WebSocketClient(httpClient);

		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectionInitializationException("Starting the spe_ed socket not possible!",
					e);
		}

		try {
			client.connect(this, webserviceConnectionURI.getURI());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionInitializationException("Initializing connection to spe_ed webservice not possible!",
					e);
		}
	}

	@OnWebSocketConnect
	public void onOpen(final Session session) {
		System.out.println("Connection opened!");
	}

	@OnWebSocketMessage
	public void onMessage(final Session session, final String message) throws MessageSendingException {
		System.out.println(message);
		// TODO parsing request String to {@link GameStepInfo}
		// PlayerAction responseAction = handleStepFunction.apply(null);
		// TODO parsing {@link PlayerAction} to String response
		String responseText = "{\"action\":" + PlayerAction.CHANGE_NOTHING.getName() + "}";

		try {
			session.getRemote().sendString(responseText);
		} catch (IOException e) {
			throw new MessageSendingException("Could not sent response: " + responseText, e);
		}
	}

	@OnWebSocketClose
	public void onClose(final Session session, final int closeCode, final String closeReason) {
		System.out.println("Connection closed!");
		System.out.println(closeReason);
	}

	@OnWebSocketError
	public void onError(final Session session, final Throwable throwable) {
		throwable.printStackTrace();
	}

}
