package core.modes;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.controller.GameController;
import core.parser.EnvironmentVariableParser;
import core.parser.EnvrionmentVariableParseException;
import solver.SolverType;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;
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

	private final SolverType solverType;

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed on an
	 * online webservice. The constructor is used to set starting parameters.
	 * 
	 * @param viewerEnabled true if a viewer window should be shown to the user
	 * @param solverType    {@link SolverType} of the player participating in the
	 *                      spe_ed game
	 */
	public LiveMode(final boolean viewerEnabled, final SolverType solverType) {
		this.viewerEnabled = viewerEnabled;
		this.solverType = solverType;
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

			final List<SolverType> solverTypes = new ArrayList<>(Arrays.asList(solverType));
			final GameController gameController = new GameController(viewerEnabled, solverTypes);

			connectionManager.play(gameController::handleGameStep);

		} catch (ConnectionInitializationException | EnvrionmentVariableParseException | ConnectionTerminationException
				| InterruptedException e) {
			ApplicationLogger.logException(e, LoggingLevel.ERROR);
			ApplicationLogger.logError("The application will be shut down due to an unrecoverable error!");
		}
	}

}
