package visualisation.files;

/**
 * {@link Exception} thrown when an image could not be saved.
 */
public class ImageSavingException extends Exception {
	
	public ImageSavingException(final String message, final Exception e) {
		super(message, e);
	}
	
}
