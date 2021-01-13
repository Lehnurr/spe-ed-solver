package core.parser;

import core.modes.LiveMode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import solver.SolverType;
import picocli.CommandLine.ParameterException;

/**
 * {@link Command} which runs a {@link LiveMode} with the given command line
 * arguments. <br>
 * This {@link Command} is set as default by the {@link CommandLineParser} and
 * will get executed automatically if no arguments were given to the
 * application. Therefore fulfilling the requirements to be able to run in the
 * desired docker container.
 */
@Command(name = "live", description = "Starts a game of spe_ed on the given webservice with one solver instance.")
public class PlayLiveCommand implements Runnable {

	@Spec
	private CommandSpec spec;

	private boolean viewerEnabled = false;

	private SolverType solverType = SolverType.getDefault();

	private int maxThreadCount = 1;

	private String logDirectory = "log";

	@Option(names = { "-v", "--viewer" }, description = "If specified the viewer will be enabled.")
	public void setViewerEnabled(final boolean viewerEnabled) {
		this.viewerEnabled = viewerEnabled;
	}

	@Option(names = { "-s", "--solver" }, description = "Sets the solver type to play the game with.")
	public void setSolverType(final SolverType solverType) {
		this.solverType = solverType;
	}

	@Option(names = { "-l",
			"--logFileDirecotry" }, description = "Changes the directory where all possible outputs will be saved. '/' will disable the file logging", defaultValue = "log")
	public void setLogFilePath(final String logDirectory) {
		this.logDirectory = "/".equals(logDirectory) ? null : logDirectory;
	}

	@Option(names = { "-c",
			"--consoleLoggingLevel" }, description = "Limits the outputs in the console, a higher level includes all lower levels.\r\n"
					+ "ERROR = 0\r\n" + "WARNING = 1\r\n" + "GAME_INFO = 2\r\n" + "INFO = 3\r\n", defaultValue = "3")
	public void setConsoleOutputMethod(final String loggingLevel) {
		try {
			// Try parsing to an integer
			int ordinalLevel = Integer.parseInt(loggingLevel);
			if (ordinalLevel > LoggingLevel.ERROR.getLevel() && ordinalLevel < LoggingLevel.FILE_INFO.getLevel()) {
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

	@Option(names = { "-m",
			"--max-thread-count" }, description = "Specifies the maximum number of concurrent threads for the solver.", defaultValue = "1")
	public void setMaxThreadCount(final int maxThreadCount) {
		final int availableThreads = Runtime.getRuntime().availableProcessors();
		if (maxThreadCount < 1 || maxThreadCount > availableThreads)
			throw new ParameterException(spec.commandLine(),
					"The  maximum number of concurrent threads must be between 1 and " + availableThreads
							+ " (given by your system specifications)!");

		this.maxThreadCount = maxThreadCount;
	}

	@Override
	public void run() {
		ApplicationLogger.setLogFilePath(logDirectory);
		new LiveMode(viewerEnabled, solverType, maxThreadCount).run();
	}

}
