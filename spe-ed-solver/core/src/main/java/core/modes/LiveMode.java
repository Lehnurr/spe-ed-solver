package core.modes;

import core.parser.EnvironmentVariableParser;
import core.parser.EnvrionmentVariableParseException;
import utility.game.player.PlayerAction;
import webcommunication.webservice.ConnectionInitializationException;
import webcommunication.webservice.ConnectionTerminationException;
import webcommunication.webservice.SpeedConnectionManager;
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

			final WebserviceConnectionURI webserviceConnectionURI = environmentVariableParser
					.getWebserviceConnectionUri();

			final SpeedConnectionManager connectionManager = new SpeedConnectionManager(webserviceConnectionURI);

			connectionManager.play(step -> PlayerAction.CHANGE_NOTHING);

		} catch (ConnectionInitializationException | EnvrionmentVariableParseException
				| ConnectionTerminationException e) {
			e.printStackTrace();
		}
	}

}
