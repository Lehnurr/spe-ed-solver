package webcommunication.webservice;

/**
 * Exception wrapper thrown when a webservice client tried to send a message,
 * which could not be sent.
 */
public class MessageSendingException extends Exception {

	public MessageSendingException(String message) {
		super(message);
	}

	public MessageSendingException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
