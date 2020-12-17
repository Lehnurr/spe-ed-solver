package webcommunication.webservice.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utility.game.player.PlayerAction;
import utility.game.stepinformation.GameStepInformation;

/**
 * Responsible for parsing the information of a single game step received from
 * the webservice by transforming a JSON {@link String} to a
 * {@link GameStepInformation}.
 */
public class GameStepInformationParser {

	private final Gson gson;

	/**
	 * Creates a new @{@link GameStepInformationParser} with {@link Gson} object by
	 * using the default {@link GsonBuilder}.
	 */
	public GameStepInformationParser() {
		this(new GsonBuilder().create());
	}

	/**
	 * Creates a new {@link GameStepInformationParser} with a given {@link Gson}
	 * object.
	 * 
	 * @param gson the {@link Gson} object to use for parsing
	 */
	public GameStepInformationParser(final Gson gson) {
		this.gson = gson;
	}

	/**
	 * Transforms a given {@link PlayerAction} to a valid JSON response in
	 * {@link String} format.
	 * 
	 * @param playerAction {@link PlayeAction} to respond with
	 * @return {@link String JSON String} of the answer
	 */
	public GameStepInformation parseGameStepInformation(final String jsonString) {
		
		final JSONGameStepInformation jsonObject = gson.fromJson(jsonString, JSONGameStepInformation.class);
				
		final GameStepInformation result = new GameStepInformation();

		return result;
	}

}
