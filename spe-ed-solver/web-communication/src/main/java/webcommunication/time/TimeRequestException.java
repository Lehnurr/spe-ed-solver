package webcommunication.time;

/**
 * {@link Exception} thrown when the spe_ed time API could not be reached and no
 * server time could be received.
 */
public class TimeRequestException extends Exception {
	
	public TimeRequestException(final String message) {
		super(message);
	}
	
	public TimeRequestException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

}
