package core.modes;

import java.net.URI;

import core.parser.EnvironmentVariableParser;
import core.parser.EnvrionmentVariableParseException;
import core.player.PlayerController;
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

			final URI timeApiURI = environmentVariableParser.getTimeUrl();

			final SpeedConnectionManager connectionManager = new SpeedConnectionManager(webserviceConnectionURI,
					timeApiURI);

			final PlayerController playerController = new PlayerController();

			connectionManager.play(step -> playerController.sendGameStep(step, step.getSelf().getPlayerId()));

		} catch (ConnectionInitializationException | EnvrionmentVariableParseException
				| ConnectionTerminationException e) {
			e.printStackTrace();
		}
	}

}
