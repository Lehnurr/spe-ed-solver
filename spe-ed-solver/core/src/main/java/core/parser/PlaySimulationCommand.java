package core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.modes.SimulationMode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import solver.SolverType;
import simulation.SimulationDeadline;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;

/**
 * {@link Command} which runs a {@link SimulationMode} which simulates the game
 * spe_ed with the given command line arguments. Throws
 * {@link ParameterException } in case of a wrong input.
 */
@Command(name = "simulated", description = "Starts a game of spe_ed locally with the given solvers and simulates the game.")
public class PlaySimulationCommand implements Runnable {

	@Spec
	private CommandSpec spec;

	private boolean viewerEnabled = false;

	private int boardWidth;
	private int boardHeight;

	private List<SolverType> solverTypes = Arrays.asList(SolverType.getDefault(), SolverType.getDefault());

	@Option(names = { "-v", "--viewer" }, description = "If specified the viewer will be enabled.")
	public void setViewerEnabled(final boolean viewerEnabled) {
		this.viewerEnabled = viewerEnabled;
	}

	@Option(names = { "-w", "--width" }, description = "The width of the game board.")
	public void setBoardWidth(final int boardWidth) {
		if (boardWidth <= 0) {
			throw new ParameterException(spec.commandLine(), "The board width must be > 0!");
		}
		this.boardWidth = boardWidth;
	}

	@Option(names = { "-h", "--height" }, description = "The height of the game board.")
	public void setBoardHeight(final int boardHeight) {
		if (boardHeight <= 0) {
			throw new ParameterException(spec.commandLine(), "The board height must be > 0!");
		}
		this.boardHeight = boardHeight;
	}

	@Option(names = { "-s",
			"--solvers" }, description = "Sets the solver types to play the game with, seperated with \",\"")
	public void setSolverTypes(final String solverTypesString) {

		final List<SolverType> solverTypes = new ArrayList<>();

		final String[] solverTypeStrings = solverTypesString.split(",");
		for (final String solverTypeString : solverTypeStrings) {
			try {
				solverTypes.add(SolverType.valueOf(solverTypeString));
			} catch (final IllegalArgumentException e) {
				throw new ParameterException(spec.commandLine(),
						String.format("The %s solver type is unknown! Valid values are %s seperated by \",\".",
								solverTypeString, Arrays.asList(SolverType.values())));
			}
		}

		if (solverTypes.size() < 2)
			throw new ParameterException(spec.commandLine(), "You need at least 2 solvers to simulate a game!");

		if (solverTypes.size() > 6)
			throw new ParameterException(spec.commandLine(), "You can't play spe_ed with more than 6 solvers!");

		this.solverTypes = solverTypes;
	}

	@Option(names = { "-l",
			"--logFileDirecotry" }, description = "If specified, a log file with all possible outputs will be created in the specified directory.")
	public void setLogFilePath(final String logDirectory) {
		ApplicationLogger.setLogFilePath(logDirectory);
	}

	@Option(names = { "-c",
			"--consoleLoggingLevel" }, description = "Limits the outputs in the console, a higher level includes all lower levels.\r\n"
					+ "GAME_INFO = 1\r\n" + "INFO = 2\r\n" + "WARNING = 3\r\n", defaultValue = "2")
	public void setConsoleOutputMethod(final String loggingLevel) {
		try {
			// Try parsing to an integer
			int ordinalLevel = Integer.parseInt(loggingLevel);
			if (ordinalLevel > LoggingLevel.ERROR.getLevel() && ordinalLevel <= LoggingLevel.WARNING.getLevel()) {
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

	@Option(names = { "-d",
			"--debug" }, description = "If specified the application will show available debug information.")
	public void setDebugEnabled(final boolean debugEnabled) {
		ApplicationLogger.setDebugMode(debugEnabled);
	}

	@Option(names = {
			"--lower-deadline" }, description = "Sets the lower second-limit for the deadline.", defaultValue = "2")
	public void setDeadlineLowerLimit(final int deadlineLowerLimit) {
		if (deadlineLowerLimit < 0)
			throw new ParameterException(spec.commandLine(),
					"The lower second-limit for the deadline must be positive!");
		SimulationDeadline.setLowerTimeLimit(deadlineLowerLimit);
	}

	@Option(names = {
			"--upper-deadline" }, description = "Sets the upper second-limit for the deadline.", defaultValue = "15")
	public void setDeadlineUpperLimit(final int deadlineUpperLimit) {
		if (deadlineUpperLimit < 0)
			throw new ParameterException(spec.commandLine(),
					"The upper second-limit for the deadline must be positive!");

		SimulationDeadline.setUpperTimeLimit(deadlineUpperLimit);
	}

	@Override
	public void run() {
		new SimulationMode(boardHeight, boardWidth, solverTypes, viewerEnabled).run();
	}

}
