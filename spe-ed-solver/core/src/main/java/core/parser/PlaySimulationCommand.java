package core.parser;

import core.modes.SimulationMode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

/**
 * {@link Command} which runs a {@link SimulationMode} which simulates the game
 * spe_ed with the given command line arguments.
 */
@Command(name = "simulated", description = "Starts a game of spe_ed locally with the given players and simulates the game.")
public class PlaySimulationCommand implements Runnable {

	@Spec
	private CommandSpec spec;

	private boolean viewerEnabled = false;

	private int boardWidth;
	private int boardHeight;
	private int playerCount;

	@Option(names = { "-v", "--viewer" }, description = "If specified the viewer will be enabled.")
	public void setViewerEnabled(final boolean viewerEnabled) {
		this.viewerEnabled = viewerEnabled;
	}

	@Option(names = { "-w", "--width" }, description = "The width of the game board.")
	public void setBoardWidth(final int boardWidth) {
		if (boardWidth <= 0) {
			// no logging needed; handled by picocli
			throw new ParameterException(spec.commandLine(), "The board width must be > 0!");
		}
		this.boardWidth = boardWidth;
	}

	@Option(names = { "-h", "--height" }, description = "The height of the game board.")
	public void setBoardHeight(final int boardHeight) {
		if (boardHeight <= 0) {
			// no logging needed; handled by picocli
			throw new ParameterException(spec.commandLine(), "The board height must be > 0!");
		}
		this.boardHeight = boardHeight;
	}

	@Option(names = { "-p", "--playerCount" }, description = "The amount of players to play the simulation with.")
	public void setPlayerCount(final int playerCount) {
		if (playerCount < 2) {
			// no logging needed; handled by picocli
			throw new ParameterException(spec.commandLine(), "You need at least 2 players to simulate a game!");
		}
		if (playerCount > 6) {
			// no logging needed; handled by picocli
			throw new ParameterException(spec.commandLine(), "You can't play spe_ed with more than 6 players!");
		}
		this.playerCount = playerCount;
	}

	@Option(names = { "-l",
			"--logFileDirecotry" }, description = "If specified, a log file with all possible outputs will be created in the specified directory.")
	public void setLogFilePath(final String logDirectory) {
		ApplicationLogger.setLogFilePath(logDirectory);
	}

	@Option(names = { "-c",
			"--consoleLoggingLevel" }, description = "Limits the outputs in the console, a higher level includes all lower levels.\r\n"
					+ "GAME_INFO = 1\r\n" + "INFO = 2\r\n" + "WARNING = 3\r\n" + "ERROR = 4\r\n"
					+ "FATAL_ERROR = 5\r\n", defaultValue = "2")
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
		// no logging needed; handled by picocli
		throw new ParameterException(spec.commandLine(), loggingLevel + " Is not a valid logging level!");
	}

	@Override
	public void run() {
		new SimulationMode(boardHeight, boardWidth, playerCount, viewerEnabled).run();
	}

}
