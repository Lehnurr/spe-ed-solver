package webcommunication.webservice;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import utility.logging.ApplicationLogger;

public class WebserviceConnectionURI {

	// name of the parameter used to identify the APi key for the webservice
	private static final String KEY_PARAMETER_NAME = "key";

	// encoding used to encode the api key
	private static final String KEY_ENCODING = StandardCharsets.UTF_8.toString();

	private final URI uri;

	/**
	 * Creates a {@link WebserviceConnectionURI} containing a {@link URI} which is
	 * able to connect to a spe_ed webservice. This is achieved by combining the
	 * {@link URI} of the webservice with the API Key.
	 * 
	 * @param baseUri {@link URI} of the webservice
	 * @param apiKey  unique apiKey for using the webservice
	 * @throws MalformedURLException thrown when the given parameters cannot be
	 *                               combined
	 */
	public WebserviceConnectionURI(final URI baseUri, final String apiKey) throws MalformedURLException {
		final String encodedKey;
		try {
			encodedKey = URLEncoder.encode(apiKey, KEY_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw ApplicationLogger.logAndThrowException(
					new IllegalStateException("The default webservice key encoding is set to an invalid value!", e));
		}
		try {
			uri = new URI(baseUri.toString() + "?" + KEY_PARAMETER_NAME + "=" + encodedKey);
		} catch (URISyntaxException e) {
			throw ApplicationLogger.logAndThrowException(new MalformedURLException(
					"The given URI and API Key could not be merged to a valid webservice connection URI!", e));
		}
	}

	/**
	 * Returns the generated {@link URI} made from the given webservice {@link URI}
	 * and a unique API Key.
	 * 
	 * @return {@link URI} which is able to connect to a spe_ed webservice.
	 */
	public URI getURI() {
		return uri;
	}

}
