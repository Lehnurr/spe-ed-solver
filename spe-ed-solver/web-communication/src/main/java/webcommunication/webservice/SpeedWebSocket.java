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
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;
import webcommunication.webservice.parser.GameStepParser;
import webcommunication.webservice.parser.ResponseParser;

/**
 * {@link WebSocket} annotated websocket to connect a webservice client to the
 * spe_ed webservice.
 */
@WebSocket
public class SpeedWebSocket {

	private static final long JETTY_WEBSOCKET_TIMEOUT = 3_600_000;

	private final GameStepParser gameStepParser;
	private final ResponseParser responseParser;

	private final Function<GameStep, PlayerAction> handleStepFunction;

	private final CountDownLatch closeLatch = new CountDownLatch(1);

	private int roundCounter = 0;

	/**
	 * Creates a new {@link SpeedWebSocket} which is able to connect to a spe_ed
	 * webservice server.
	 * 
	 * @param handleStepFunction {@link Function} which handles a single game step
	 * @param gameStepParser     {@link GameStepParser} to parse a JSON game step
	 * @param responseParser     {@link ResponseParser} to parse JSON responses
	 */
	public SpeedWebSocket(final Function<GameStep, PlayerAction> handleStepFunction,
			final GameStepParser gameStepParser, final ResponseParser responseParser) {

		this.gameStepParser = gameStepParser;
		this.responseParser = responseParser;

		this.handleStepFunction = handleStepFunction;

	}

	@OnWebSocketConnect
	public void onOpen(final Session session) {
		session.setIdleTimeout(JETTY_WEBSOCKET_TIMEOUT);
		ApplicationLogger.logInformation("Connection opened");
	}

	@OnWebSocketMessage
	public void onMessage(final Session session, final String message) throws MessageSendingException {

		ApplicationLogger.logInformation("request(" + roundCounter + ") received from the server");
		ApplicationLogger.logFileInformation("received message from the server: " + message);

		final GameStep gameStep = gameStepParser.parseGameStep(message, roundCounter);
		final PlayerAction responseAction = handleStepFunction.apply(gameStep);
		final String responseText = responseParser.parseResponse(responseAction);
		
		if (gameStep.isRunning()) {
			
			try {
				session.getRemote().sendString(responseText);
			} catch (IOException e) {
				throw new MessageSendingException("Could not sent response: " + responseText, e);
			}
		}
		
		ApplicationLogger.logInformation("response " + responseText + " sent to the server");

		roundCounter++;
	}

	@OnWebSocketClose
	public void onClose(final Session session, final int closeCode, final String closeReason) {
		closeLatch.countDown();
		ApplicationLogger.logInformation("Connection closed!");
	}

	@OnWebSocketError
	public void onError(final Throwable t) {
		ApplicationLogger.logException(t, LoggingLevel.ERROR);
		ApplicationLogger.logError(
				"An error was thrown while communicating with the spe_ed server! The connection will be terminated!");
		closeLatch.countDown();
	}

	/**
	 * Asynchronous wait for the closure of the {@link SpeedWebSocket}. Blocks until
	 * the {@link SpeedWebSocket} is closed.
	 * 
	 * @throws InterruptedException thrown when the await is interrupted by another
	 *                              thread
	 */
	public void awaitClosure() throws InterruptedException {
		try {
			closeLatch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw e;
		}
	}

}