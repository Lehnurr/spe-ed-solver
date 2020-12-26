package core.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import utility.logging.ApplicationLogger;
import webcommunication.webservice.MalformedURLException;
import webcommunication.webservice.WebserviceConnectionURI;

/**
 * A parser class which instances parse spe_ed specific environment variables to
 * data types which can be used in the application.
 */
public class EnvironmentVariableParser {

	private static final String WEBSERVICE_URL_ENV_NAME = "URL";
	private static final String API_KEY_ENV_NAME = "KEY";

	private static final String TIME_URL_ENV_NAME = "TIME_URL";

	private final Map<String, String> environmentVariables;

	/**
	 * Creates a new instance of the {@link EnvironmentVariableParser} which is able
	 * to deliver spe_ed specific environment variables as fitting data types.
	 */
	public EnvironmentVariableParser() {
		this.environmentVariables = System.getenv();
	}

	/**
	 * Searches for the environment variable by the given name and returns its
	 * content as {@link String}.
	 * 
	 * @param name the name of the variable in the system
	 * @return {@link String} representation of the environment variable
	 * @throws EnvrionmentVariableParseException
	 */
	private String getEnvironmentVariableAsString(final String name) throws EnvrionmentVariableParseException {
		final String variable = environmentVariables.getOrDefault(name, "");
		if ("".equals(variable)) {
			ApplicationLogger.logAndThrowException(new EnvrionmentVariableParseException(
					"The environment variable \"" + name + "\" cannot be found!"));
		}
		return variable;
	}

	/**
	 * Returns the URL of the spe_ed webservice as {@link URI}.
	 * 
	 * @return URL of the webservice as {@link URI}
	 * @throws EnvrionmentVariableParseException
	 */
	public URI getWebserviceUrl() throws EnvrionmentVariableParseException {
		final String stringValue = getEnvironmentVariableAsString(WEBSERVICE_URL_ENV_NAME);
		final URI parsedValue;
		try {
			parsedValue = new URI(stringValue);
		} catch (URISyntaxException e) {
			var exception = new EnvrionmentVariableParseException(
					"The environment variable \"" + WEBSERVICE_URL_ENV_NAME + "\" is not an valid URI!");
			ApplicationLogger.logException(exception);
			throw exception;
		}
		return parsedValue;
	}

	/**
	 * Returns the unique API Key of the spe_ed user for connecting to the spe_ed
	 * webservice.
	 * 
	 * @return API Key as {@link String}
	 * @throws EnvrionmentVariableParseException
	 */
	public String getApiKey() throws EnvrionmentVariableParseException {
		final String stringValue = getEnvironmentVariableAsString(API_KEY_ENV_NAME);
		return stringValue;
	}

	/**
	 * Returns the {@link WebserviceConnectionURI} which can be used to connect to
	 * the spe_ed webservice server.
	 * 
	 * @return the {@link WebserviceConnectionURI} for the webservice
	 * @throws EnvrionmentVariableParseException
	 */
	public WebserviceConnectionURI getWebserviceConnectionUri() throws EnvrionmentVariableParseException {
		final URI webserviceURI = getWebserviceUrl();
		final String apiKey = getApiKey();
		try {
			return new WebserviceConnectionURI(webserviceURI, apiKey);
		} catch (MalformedURLException e) {
			var exception = new EnvrionmentVariableParseException(
					"The environment variables for the URL and API Key of the webservice are not formatted a valid way.",
					e);
			ApplicationLogger.logException(exception);
			throw exception;
		}
	}

	/**
	 * Returns the URL of the spe_ed time API as {@link URI}.
	 * 
	 * @return URL of the time API as {@link URI}
	 * @throws EnvrionmentVariableParseException
	 */
	public URI getTimeUrl() throws EnvrionmentVariableParseException {
		final String stringValue = getEnvironmentVariableAsString(TIME_URL_ENV_NAME);
		final URI parsedValue;
		try {
			parsedValue = new URI(stringValue);
		} catch (URISyntaxException e) {
			var exception = new EnvrionmentVariableParseException(
					"The environment variable \"" + TIME_URL_ENV_NAME + "\" is not an valid URI!");
			ApplicationLogger.logException(exception);
			throw exception;
		}
		return parsedValue;
	}

}
