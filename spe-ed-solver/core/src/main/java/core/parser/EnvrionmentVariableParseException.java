package core.parser;

/**
 * Exception thrown when environment variables cannot be used (e.g. invalid
 * syntax).
 */
@SuppressWarnings("serial")
public class EnvrionmentVariableParseException extends Exception {

	public EnvrionmentVariableParseException(String message) {
		super(message);
	}

	public EnvrionmentVariableParseException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
