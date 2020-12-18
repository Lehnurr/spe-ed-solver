package webcommunication.webservice;

import java.util.function.Function;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * Class responsible for managing the life cycle of a game of spe_ed on a given
 * server. Done by connecting a {@link SpeedWebSocket} to a
 * {@link SpeedWebSocketClient}.
 */
public class SpeedConnectionManager {

	final WebserviceConnectionURI webserviceConnectionURI;

	/**
	 * Creates a new {@link SpeedConnectionManager} with a given
	 * {@link WebserviceConnectionURI} containing the URI of the server to play
	 * spe_ed on.
	 * 
	 * @param webserviceConnectionURI of the spe_ed server
	 */
	public SpeedConnectionManager(final WebserviceConnectionURI webserviceConnectionURI) {
		this.webserviceConnectionURI = webserviceConnectionURI;
	}

	/**
	 * Blocking method, which plays a single game of spe_ed on a server. The given
	 * {@link Function} must handle a single {@link GameStep} by processing its
	 * contents and returning a {@link PlayerAction}.
	 * 
	 * @param gameStepHandler {@link Function} to handle a {@link GameStep} by
	 *                        processing its contents and returning a
	 *                        {@link PlayerAction}
	 * @throws ConnectionInitializationException
	 * @throws ConnectionTerminationException
	 */
	public void play(final Function<GameStep, PlayerAction> gameStepHandler)
			throws ConnectionInitializationException, ConnectionTerminationException {

		final SpeedWebSocket socket = new SpeedWebSocket(gameStepHandler);
		
		final SpeedWebSocketClient socketClient = new SpeedWebSocketClient();

		socketClient.connectToServer(socket, webserviceConnectionURI);
		socket.awaitClosure();
		socketClient.assureStopped();

	}

}
