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

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import webcommunication.webservice.parser.GameStepParser;
import webcommunication.webservice.parser.ResponseParser;

/**
 * {@link WebSocket} annotated websocket to connect a webservice client to the
 * spe_ed webservice.
 */
@WebSocket
public class SpeedWebSocket {

	private static final long JETTY_WEBSOCKET_TIMEOUT = 3600000;

	private final GameStepParser gameStepParser;
	private final ResponseParser responseParser;

	private final Function<GameStep, PlayerAction> handleStepFunction;

	private final CountDownLatch closeLatch = new CountDownLatch(1);

	private int roundCounter = 0;

	/**
	 * Creates a new {@link SpeedClientSocket} which is able to connect to a spe_ed
	 * webservice server.
	 * 
	 * @param handleStepFunction {@link Function} which handles a single game step
	 * @param gameStepParser     {@link GameStepParser} to parse a JSON game step
	 * @param responseParser     {@link ResponseParser} to parse JSON responses
	 * @throws ConnectionInitializationException
	 */
	public SpeedWebSocket(final Function<GameStep, PlayerAction> handleStepFunction,
			final GameStepParser gameStepParser, final ResponseParser responseParser)
			throws ConnectionInitializationException {

		this.gameStepParser = gameStepParser;
		this.responseParser = responseParser;

		this.handleStepFunction = handleStepFunction;

	}

	@OnWebSocketConnect
	public void onOpen(final Session session) {
		session.setIdleTimeout(JETTY_WEBSOCKET_TIMEOUT);
		System.out.println("Connection opened!");
	}

	@OnWebSocketMessage
	public void onMessage(final Session session, final String message) throws MessageSendingException {
		System.out.println("request(" + roundCounter + "):\t" + message);
		final GameStep gameStep = gameStepParser.parseGameStep(message, roundCounter);
		final PlayerAction responseAction = handleStepFunction.apply(gameStep);
		String responseText = responseParser.parseResponse(responseAction);
		System.out.println("response:\t" + responseText);
		if (gameStep.isRunning()) {
			try {
				session.getRemote().sendString(responseText);
			} catch (IOException e) {
				throw new MessageSendingException("Could not sent response: " + responseText, e);
			}
		}
		roundCounter++;
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