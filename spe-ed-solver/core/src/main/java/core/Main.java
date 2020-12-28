package core;

import java.time.ZonedDateTime;

import core.parser.CommandLineParser;
import picocli.CommandLine;
import utility.logging.ApplicationLogger;

/**
 * Responsible for starting the application in its different modes by delegating
 * to the Picocli {@link CommandLine}.
 */
public class Main {

	public static void main(String[] args) {

		// Enable Console Logging
		ApplicationLogger.setLogInConsole(true);
		// Enable Log-File Logging
		ApplicationLogger.setLogFilePath("log/", ZonedDateTime.now());

		CommandLineParser commandLineParser = new CommandLineParser();
		new CommandLine(commandLineParser).execute(args);
	}

}
