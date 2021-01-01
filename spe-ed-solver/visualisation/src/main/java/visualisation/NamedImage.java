package visualisation;

import java.awt.image.BufferedImage;

/**
 * Class for storing {@link BufferedImage images} with a name for additional
 * context.
 */
public class NamedImage {

	private final String name;

	private final BufferedImage image;

	/**
	 * Generates a new {@link NamedImage} with the unchangeable given information.
	 * 
	 * @param name  name of the image
	 * @param image actual {@link BufferedImage}
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
	 * @return managed {@link BufferedImage}
	 */
	public BufferedImage getImage() {
		return image;
	}

}
