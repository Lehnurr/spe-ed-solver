package webcommunication.webservice.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import webcommunication.webservice.parser.jsonobject.JSONGameStep;

/**
 * Responsible for parsing the information of a single game step received from
 * the webservice by transforming a JSON {@link String} to a {@link GameStep}.
 */
public class GameStepParser {

	private final Gson gson;

	/**
	 * Creates a new @{@link GameStepParser} with {@link Gson} object by using the
	 * default {@link GsonBuilder}.
	 */
	public GameStepParser() {
		this(new GsonBuilder().create());
	}

	/**
	 * Creates a new {@link GameStepParser} with a given {@link Gson} object.
	 * 
	 * @param gson the {@link Gson} object to use for parsing
	 */
	public GameStepParser(final Gson gson) {
		this.gson = gson;
	}

	/**
	 * Transforms a given {@link PlayerAction} to a valid JSON response in
	 * {@link String} format.
	 * 
	 * @param playerAction {@link PlayeAction} to respond with
	 * @return {@link String JSON String} of the answer
	 */
	public GameStep parseGameStep(final String jsonString) {

		final JSONGameStep jsonObject = gson.fromJson(jsonString, JSONGameStep.class);

		final GameStep result = new GameStep();

		return result;
	}

}
