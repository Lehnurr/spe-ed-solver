package visualisation.files;

/**
 * {@link Exception} thrown when an image could not be saved.
 */
@SuppressWarnings("serial")
public class ImageSavingException extends Exception {

	public ImageSavingException(final String message, final Exception e) {
		super(message, e);
	}

}
