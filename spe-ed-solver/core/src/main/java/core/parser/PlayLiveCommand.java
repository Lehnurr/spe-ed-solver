package core.parser;

import core.modes.LiveMode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import player.PlayerType;
import picocli.CommandLine.ParameterException;

/**
 * {@link Command} which runs a {@link LiveMode} with the given command line
 * arguments. <br>
 * This {@link Command} is set as default by the {@link CommandLineParser} and
 * will get executed automatically if no arguments were given to the
 * application. Therefore fulfilling the requirements to be able to run in the
 * desired docker container.
 */
@Command(name = "live", description = "Starts a game of spe_ed on the given webservice with one player instance.")
public class PlayLiveCommand implements Runnable {

	@Spec
	private CommandSpec spec;

	private boolean viewerEnabled = false;

	private PlayerType playerType = PlayerType.REACHABLE_POINTS;

	@Option(names = { "-v", "--viewer" }, description = "If specified the viewer will be enabled.")
	public void setViewerEnabled(final boolean viewerEnabled) {
		this.viewerEnabled = viewerEnabled;
	}

	@Option(names = { "-p", "--player" }, description = "Sets the player type to play the game with.")
	public void setPlayerType(final PlayerType playerType) {
		this.playerType = playerType;
	}

	@Option(names = { "-l", "--logFileDirecotry" },
			description = "If specified, a log file with all possible outputs will be created in the specified directory.")
	public void setLogFilePath(final String logDirectory) {
		ApplicationLogger.setLogFilePath(logDirectory);
	}

	@Option(names = { "-c", "--consoleLoggingLevel" },
			description = "Limits the outputs in the console, a higher level includes all lower levels.\r\n"
					+ "GAME_INFO = 1\r\n" + "INFO = 2\r\n" + "WARNING = 3\r\n" + "ERROR = 4\r\n"
					+ "FATAL_ERROR = 5\r\n",
			defaultValue = "2")
	public void setConsoleOutputMethod(final String loggingLevel) {
		try {
			// Try parsing to an integer
			int ordinalLevel = Integer.parseInt(loggingLevel);
			if (ordinalLevel >= 1 && ordinalLevel <= 5) {
				ApplicationLogger.setConsoleLoggingLevel(LoggingLevel.fromInteger(ordinalLevel));
				return;
			}
		} catch (IllegalArgumentException e) {
			// try getting the Level by name
			for (LoggingLevel level : LoggingLevel.values()) {
				if (level.name().equals(loggingLevel)) {
					ApplicationLogger.setConsoleLoggingLevel(level);
					return;
				}
			}
		}

		throw new ParameterException(spec.commandLine(), loggingLevel + " Is not a valid logging level!");
	}

	@Override
	public void run() {
		new LiveMode(viewerEnabled, playerType).run();
	}

}
