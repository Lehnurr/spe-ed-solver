package webcommunication.webservice.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utility.game.player.PlayerAction;

/**
 * Responsible for parsing the response to send to the spe_ed server from a
 * given {@link PlayerAction}.
 */
public class ResponseParser {

	private final Gson gson;

	/**
	 * Creates a new @{@link ResponseParser} with {@link Gson} object by using the
	 * default {@link GsonBuilder}.
	 */
	public ResponseParser() {
		this(new GsonBuilder().create());
	}

	/**
	 * Creates a new {@link ResponseParser} with a given {@link Gson} object.
	 * 
	 * @param gson the {@link Gson} object to use for parsing
	 */
	public ResponseParser(final Gson gson) {
		this.gson = gson;
	}

	/**
	 * Transforms a given {@link PlayerAction} to a valid JSON response in
	 * {@link String} format.
	 * 
	 * @param playerAction {@link PlayeAction} to respond with
	 * @return {@link String JSON String} of the answer 
	 */
	public String parseResponse(final PlayerAction playerAction) {
		final JSONResponse jsonResponse = new JSONResponse(playerAction.getName());
		return gson.toJson(jsonResponse).toString();
	}

}
