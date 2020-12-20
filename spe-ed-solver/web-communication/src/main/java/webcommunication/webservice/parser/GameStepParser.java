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
import utility.game.step.Deadline;
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
	 */
	public GameStepParser(final TimeSynchronizationManager timeSynchronizationManager) {
		this.gson = new GsonBuilder().create();
		this.timeSynchronizationManager = timeSynchronizationManager;
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

		final Deadline deadline;
		if (jsonObject.running) { // if game is over, no deadline is available
			final ZonedDateTime deadlineTime = ZonedDateTime.ofInstant(jsonObject.deadline.toInstant(),
					ZoneId.of(SPE_ED_TIME_FORMAT));
			deadline = timeSynchronizationManager.createDeadline(deadlineTime);
		} else {
			deadline = new Deadline() {
				@Override
				public long getRemainingMilliseconds() {
					return 0;
				}
			};
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
		final Board<Cell> board = new Board<Cell>(cells);

		final boolean running = jsonObject.running;

		final int jsonSelfId = jsonObject.you;
		final JSONGameStepPlayer jsonSelf = jsonObject.players.remove(Integer.toString(jsonSelfId));
		final GameStepPlayer self = new GameStepPlayer(jsonSelfId, jsonSelf);

		final Map<Integer, IPlayer> enemies = new HashMap<>();
		for (final Entry<String, JSONGameStepPlayer> entry : jsonObject.players.entrySet()) {
			final int playerId = Integer.parseInt(entry.getKey());
			final JSONGameStepPlayer jsonPlayer = entry.getValue();
			final GameStepPlayer gameStepPlayer = new GameStepPlayer(playerId, jsonPlayer);
			enemies.put(playerId, gameStepPlayer);
		}

		final GameStep result = new GameStep(self, enemies, deadline, board, running);

		return result;
	}

}