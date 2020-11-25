package visualisation;

import java.awt.image.BufferedImage;

/**
 * Class for storing images with a name for additional context.
 */
public class NamedImage {

	// name of the image
	private final String name;

	// underlying image data
	private final BufferedImage image;

	/**
	 * Generates a new {@link NamedImage} with the unchangeable given information.
	 * 
	 * @param name
	 * @param image
	 */
	public NamedImage(final String name, final BufferedImage image) {
		this.name = name;
		this.image = image;
	}

	/**
	 * Returns the name of the image, describing its contents.
	 * 
	 * @return name of the image
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the actual instance of the {@link BufferedImage}.
	 * 
	 * @return image
	 */
	public BufferedImage getImage() {
		return image;
	}

}
