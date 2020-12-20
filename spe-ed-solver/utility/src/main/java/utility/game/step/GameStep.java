package utility.game.step;

import java.util.Collections;
import java.util.Map;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Class for storing all informations available to the players in one game step.
 * This contains the updated {@link InputBoard}, deadlines and information about
 * you and all the {@link InputPlayer}.
 */
public class GameStep {

	private final IPlayer self;

	private final Map<Integer, IPlayer> enemies;

	private final Deadline deadline;

	private final Board<Cell> board;

	private final boolean running;

	public GameStep(final IPlayer self, final Map<Integer, IPlayer> enemies, final Deadline deadline,
			final Board<Cell> board, final boolean running) {
		this.self = self;
		this.enemies = Collections.unmodifiableMap(enemies);
		this.deadline = deadline;
		this.board = board;
		this.running = running;
	}

	/**
	 * @return the self
	 */
	public IPlayer getSelf() {
		return self;
	}

	/**
	 * @return the enemies
	 */
	public Map<Integer, IPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * @return the deadline
	 */
	public Deadline getDeadline() {
		return deadline;
	}

	/**
	 * @return the board
	 */
	public Board<Cell> getBoard() {
		return board;
	}

	/**
	 * Generates a {@link ContextualFloatMatrix} out of the internal {@link Board}
	 * with the given name.
	 * 
	 * @param name name of the matrix
	 * @return {@link ContextualFloatMatrix} as result
	 */
	public ContextualFloatMatrix getBoardAsMatrix(final String name) {

		final FloatMatrix matrix = new FloatMatrix(board.getWidth(), board.getHeight());

		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				final Point2i point = new Point2i(x, y);
				final float value = board.getBoardCellAt(point).getCellValue().getIntegerValue();
				matrix.setValue(new Point2i(x, y), value);
			}
		}

		final float maxValue = CellValue.PLAYER_SIX.getIntegerValue();
		final float minValue = CellValue.MULTIPLE_PLAYER.getIntegerValue();

		return new ContextualFloatMatrix(name, matrix, minValue, maxValue);
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	public int getPlayerCount() {
		return this.getEnemies().size() + 1;
	}

}
