package core;

import core.parser.CommandLineParser;
import picocli.CommandLine;
import utility.logging.LoggingExceptionHandler;

/**
 * Responsible for starting the application in its different modes by delegating
 * to the Picocli {@link CommandLine}.
 */
public class Main {

	public static void main(String[] args) {
		// set the default exception handler for Logging
		Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler());

		// Parse the commandline commands
		CommandLineParser commandLineParser = new CommandLineParser();
		new CommandLine(commandLineParser).execute(args);
	}

}
