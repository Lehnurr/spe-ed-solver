package core;

import java.io.IOException;
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

		// Enable Logging
		ApplicationLogger.setLogInConsole(true);
		try {
			ApplicationLogger.setLogFilePath("log/", ZonedDateTime.now());
		} catch (IOException ex) {
			ApplicationLogger.logError("Writing a log file is not possible");
			ex.printStackTrace();
		}

		CommandLineParser commandLineParser = new CommandLineParser();
		new CommandLine(commandLineParser).execute(args);
	}

}
