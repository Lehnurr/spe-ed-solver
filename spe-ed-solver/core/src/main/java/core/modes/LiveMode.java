package core.modes;

import java.util.function.Function;

import core.parser.EnvironmentVariableParser;
import core.parser.EnvrionmentVariableParseException;
import utility.game.player.PlayerAction;
import utility.game.stepinformation.GameStepInformation;
import webcommunication.webservice.ConnectionInitializationException;
import webcommunication.webservice.ConnectionTerminationException;
import webcommunication.webservice.SpeedWebSocket;
import webcommunication.webservice.SpeedWebSocketClient;
import webcommunication.webservice.WebserviceConnectionURI;

/**
 * {@link Runnable} for the live play mode to play spe_ed on an online
 * webservice.
 */
public class LiveMode implements Runnable {

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed on an
	 * online webservice. The constructor is used to set starting parameters.
	 */
	public LiveMode() {
	}

	@Override
	public void run() {
		try {
			final EnvironmentVariableParser environmentVariableParser = new EnvironmentVariableParser();
			
			final WebserviceConnectionURI webserviceConnectionURI = environmentVariableParser.getWebserviceConnectionUri();
			
			final SpeedWebSocket socket = new SpeedWebSocket(new Function<GameStepInformation, PlayerAction>() {
				@Override
				public PlayerAction apply(GameStepInformation arg0) {
					return PlayerAction.CHANGE_NOTHING;
				}
			});
			final SpeedWebSocketClient socketClient = new SpeedWebSocketClient();
			
			socketClient.connectToServer(socket, webserviceConnectionURI);
			socket.awaitClosure();
			socketClient.assureStopped();
			
		} catch (ConnectionInitializationException e) {
			e.printStackTrace();
		} catch (EnvrionmentVariableParseException e) {
			e.printStackTrace();
		} catch (ConnectionTerminationException e) {
			e.printStackTrace();
		}
	}

}
