package webcommunication.webservice;

/**
 * Exception wrapper thrown when the connection to a web application could not
 * be terminated.
 */
@SuppressWarnings("serial")
public class ConnectionTerminationException extends Exception {

	public ConnectionTerminationException(String message) {
		super(message);
	}

	public ConnectionTerminationException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
