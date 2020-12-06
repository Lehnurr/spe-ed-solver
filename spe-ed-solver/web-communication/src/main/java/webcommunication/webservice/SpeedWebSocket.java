package webcommunication.webservice;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import utility.game.GameStepInfo;
import utility.game.player.PlayerAction;

/**
 * {@link WebSocket} annotated websocket to connect a webservice client to the
 * spe_ed webservice.
 */
@WebSocket
public class SpeedWebSocket {

	private static final long JETTY_WEBSOCKET_TIMEOUT = 3600000;

	private final Function<GameStepInfo, PlayerAction> handleStepFunction;

	private final CountDownLatch closeLatch = new CountDownLatch(1);

	/**
	 * Creates a new {@link SpeedClientSocket} which is able to connect to a spe_ed
	 * webservice server.
	 * 
	 * @param handleStepFunction {@link Function} which handles a single game step
	 * @throws ConnectionInitializationException
	 */
	public SpeedWebSocket(final Function<GameStepInfo, PlayerAction> handleStepFunction)
			throws ConnectionInitializationException {

		this.handleStepFunction = handleStepFunction;

	}

	@OnWebSocketConnect
	public void onOpen(final Session session) {
		session.setIdleTimeout(JETTY_WEBSOCKET_TIMEOUT);
		System.out.println("Connection opened!");
	}

	@OnWebSocketMessage
	public void onMessage(final Session session, final String message) throws MessageSendingException {
		System.out.println("request:\t" + message);
		// TODO parsing request String to {@link GameStepInfo}
		// PlayerAction responseAction = handleStepFunction.apply(null);
		// TODO parsing {@link PlayerAction} to String response
		String responseText = "{\"action\":\"" + PlayerAction.CHANGE_NOTHING.getName() + "\"}";
		System.out.println("response:\t" + responseText);
		try {
			session.getRemote().sendString(responseText);
		} catch (IOException e) {
			throw new MessageSendingException("Could not sent response: " + responseText, e);
		}
	}

	@OnWebSocketClose
	public void onClose(final Session session, final int closeCode, final String closeReason) {
		closeLatch.countDown();
		System.out.println("Connection closed!");
	}

	@OnWebSocketError
	public void onError(final Session session, final Throwable t) {
		t.printStackTrace();
	}

	/**
	 * Asynchronous wait for the closure of the {@link SpeedWebSocket}. Blocks until
	 * the {@link SpeedWebSocket} is closed.
	 */
	public void awaitClosure() {
		try {
			closeLatch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

}
