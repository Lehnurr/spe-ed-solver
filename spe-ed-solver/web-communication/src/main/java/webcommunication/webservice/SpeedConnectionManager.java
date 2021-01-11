package webcommunication.webservice;

import java.net.URI;
import java.util.function.Function;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import webcommunication.time.TimeAPIClient;
import webcommunication.time.TimeRequestException;
import webcommunication.time.TimeSynchronizationManager;
import webcommunication.time.parser.ServerTimeParser;
import webcommunication.webservice.parser.GameStepParser;
import webcommunication.webservice.parser.ResponseParser;

/**
 * Class responsible for managing the life cycle of a game of spe_ed on a given
 * server. Done by connecting a {@link SpeedWebSocket} to a
 * {@link SpeedWebSocketClient}.
 */
public class SpeedConnectionManager {

	final WebserviceConnectionURI webserviceConnectionURI;
	final TimeSynchronizationManager timeSynchronizationManager;

	/**
	 * Creates a new {@link SpeedConnectionManager} with a given
	 * {@link WebserviceConnectionURI} containing the URI of the server to play
	 * spe_ed on.
	 * 
	 * @param webserviceConnectionURI of the spe_ed server
	 * @param timeApiUri              {@link URI} of the API for the spe_ed_server
	 *                                time
	 */
	public SpeedConnectionManager(final WebserviceConnectionURI webserviceConnectionURI, final URI timeApiUri) {
		this.webserviceConnectionURI = webserviceConnectionURI;

		final ServerTimeParser serverTimeParser = new ServerTimeParser();
		final TimeAPIClient timeApiClient = new TimeAPIClient(serverTimeParser, timeApiUri);
		this.timeSynchronizationManager = new TimeSynchronizationManager(timeApiClient);
	}

	/**
	 * Blocking method, which plays a single game of spe_ed on a server. The given
	 * {@link Function} must handle a single {@link GameStep} by processing its
	 * contents and returning a {@link PlayerAction}.
	 * 
	 * @param gameStepHandler {@link Function} to handle a {@link GameStep} by
	 *                        processing its contents and returning a
	 *                        {@link PlayerAction}
	 * @throws ConnectionInitializationException thrown when the connection to the
	 *                                           server could not be initialized
	 * @throws ConnectionTerminationException    thrown when the connection to the
	 *                                           server could not be terminated as
	 *                                           planned
	 * @throws InterruptedException              thrown when the await for the
	 *                                           server closure was interrupted
	 */
	public void play(final Function<GameStep, PlayerAction> gameStepHandler)
			throws ConnectionInitializationException, ConnectionTerminationException, InterruptedException {

		final GameStepParser gameStepParser = new GameStepParser(timeSynchronizationManager);
		final ResponseParser responseParser = new ResponseParser();

		final SpeedWebSocket socket = new SpeedWebSocket(gameStepHandler, gameStepParser, responseParser);

		final SpeedWebSocketClient socketClient = new SpeedWebSocketClient();

		socketClient.connectToServer(socket, webserviceConnectionURI);
		socket.awaitClosure();
		socketClient.assureStopped();
	}

}