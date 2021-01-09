package visualisation.files;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import visualisation.NamedImage;

public class ImageSavingService {

	private static final String DEFAULT_FILE_EXTENSION = "png";

	public void saveImage(final File parent, final NamedImage image) throws ImageSavingException {
		final File targetFile = new File(parent.toString() + "_" + image.getName() + "." + DEFAULT_FILE_EXTENSION);
		try {
			ImageIO.write(image.getImage(), DEFAULT_FILE_EXTENSION, targetFile);
		} catch (final IOException e) {
			throw new ImageSavingException("File could not be saved!", e);
		}
	}

}
