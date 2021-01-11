package webcommunication.webservice;

/**
 * Exception wrapper thrown when the a URL has an invalid format.
 */
@SuppressWarnings("serial")
public class MalformedURLException extends Exception {

	public MalformedURLException(String message) {
		super(message);
	}

	public MalformedURLException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
