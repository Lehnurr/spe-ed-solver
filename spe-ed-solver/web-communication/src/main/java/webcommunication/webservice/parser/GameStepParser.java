package webcommunication.webservice.parser;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.IDeadline;
import utility.game.step.GameStep;
import webcommunication.time.TimeSynchronizationManager;
import webcommunication.webservice.parser.jsonobject.JSONGameStep;
import webcommunication.webservice.parser.jsonobject.JSONGameStepPlayer;

/**
 * Responsible for parsing the information of a single game step received from
 * the webservice by transforming a JSON {@link String} to a {@link GameStep}.
 */
public class GameStepParser {

	private static final String SPE_ED_TIME_FORMAT = "UTC";

	private final Gson gson;
	private final TimeSynchronizationManager timeSynchronizationManager;

	/**
	 * Creates a new @{@link GameStepParser} with {@link Gson} object by using the
	 * default {@link GsonBuilder}.
	 * 
	 * @param timeSynchronizationManager the {@link TimeSynchronizationManager} to
	 *                                   synchronize incoming times with
	 */
	public GameStepParser(final TimeSynchronizationManager timeSynchronizationManager) {
		this.gson = new GsonBuilder().create();
		this.timeSynchronizationManager = timeSynchronizationManager;
	}

	/**
	 * Transforms a given {@link PlayerAction} to a valid JSON response in
	 * {@link String} format.
	 * 
	 * @param jsonString {@link String JSON String} of the {@link GameStep}
	 * @param round      round which has to be parsed
	 * @return the {@link GameStep} result
	 */
	public GameStep parseGameStep(final String jsonString, final int round) {

		final JSONGameStep jsonObject = gson.fromJson(jsonString, JSONGameStep.class);

		final IDeadline deadline;
		if (jsonObject.running) {
			final ZonedDateTime deadlineTime = ZonedDateTime.ofInstant(jsonObject.deadline.toInstant(),
					ZoneId.of(SPE_ED_TIME_FORMAT));
			deadline = timeSynchronizationManager.createDeadline(deadlineTime);
		} else {
			// if the game is over, no deadline is available
			deadline = () -> 0;
		}

		final int boardWidth = jsonObject.cells[0].length;
		final int boardHeight = jsonObject.cells.length;

		final Cell[][] cells = new Cell[boardHeight][boardWidth];
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				CellValue cellValue = CellValue.fromInteger(jsonObject.cells[y][x]);
				cells[y][x] = new Cell(cellValue);
			}
		}
		final Board<Cell> board = new Board<>(cells);

		final boolean running = jsonObject.running;

		final int jsonSelfId = jsonObject.you;
		final JSONGameStepPlayer jsonSelf = jsonObject.players.remove(Integer.toString(jsonSelfId));
		final GameStepPlayer self = new GameStepPlayer(jsonSelfId, jsonSelf, round);

		final Map<Integer, IPlayer> enemies = new HashMap<>();
		for (final Entry<String, JSONGameStepPlayer> entry : jsonObject.players.entrySet()) {
			final int playerId = Integer.parseInt(entry.getKey());
			final JSONGameStepPlayer jsonPlayer = entry.getValue();
			final GameStepPlayer gameStepPlayer = new GameStepPlayer(playerId, jsonPlayer, round);
			enemies.put(playerId, gameStepPlayer);
		}

		return new GameStep(self, enemies, deadline, board, running);
	}

}