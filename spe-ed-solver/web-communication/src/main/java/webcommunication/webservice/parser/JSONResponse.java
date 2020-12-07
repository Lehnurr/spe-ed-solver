package webcommunication.webservice.parser;

import utility.game.player.PlayerAction;

/**
 * Object representing the JSON response send to the spe_ed server after a
 * single game step. The Class is designed to be parsable to a JSON string.
 */
public class JSONResponse {

	public String action;

	/**
	 * Initializes a new {@link JSONResponse} with a {@link PlayerAction}.
	 * 
	 * @param playerAction {@link PlayerAction} to respond with
	 */
	public JSONResponse(final PlayerAction playerAction) {
		this.action = playerAction.getName();
	}

}
