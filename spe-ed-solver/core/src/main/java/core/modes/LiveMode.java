package core.modes;

import java.net.URI;

import core.parser.EnvironmentVariableParser;
import core.parser.EnvrionmentVariableParseException;
import core.player.GameController;
import utility.logging.ApplicationLogger;
import webcommunication.webservice.ConnectionInitializationException;
import webcommunication.webservice.ConnectionTerminationException;
import webcommunication.webservice.SpeedConnectionManager;
import webcommunication.webservice.WebserviceConnectionURI;

/**
 * {@link Runnable} for the live play mode to play spe_ed on an online
 * webservice.
 */
public class LiveMode implements Runnable {

	private final boolean viewerEnabled;

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed on an
	 * online webservice. The constructor is used to set starting parameters.
	 * 
	 * @param viewerEnabled true if a viewer window should be shown to the user
	 */
	public LiveMode(final boolean viewerEnabled) {
		this.viewerEnabled = viewerEnabled;
	}

	@Override
	public void run() {
		try {
			final EnvironmentVariableParser environmentVariableParser = new EnvironmentVariableParser();

			final WebserviceConnectionURI webserviceConnectionURI = environmentVariableParser
					.getWebserviceConnectionUri();

			final URI timeApiURI = environmentVariableParser.getTimeUrl();

			final SpeedConnectionManager connectionManager = new SpeedConnectionManager(webserviceConnectionURI,
					timeApiURI);

			final GameController gameController = new GameController(viewerEnabled);

			connectionManager.play(gameController::sendGameStep);

		} catch (ConnectionInitializationException | EnvrionmentVariableParseException
				| ConnectionTerminationException e) {
			ApplicationLogger.logException(e);
		}
	}

}
