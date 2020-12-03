package webcommunication.webservice;

/**
 * Exception wrapper thrown when the connection to a web application could not
 * be initialized.
 */
public class ConnectionInitializationException extends Exception {

	public ConnectionInitializationException(String message) {
		super(message);
	}

	public ConnectionInitializationException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
