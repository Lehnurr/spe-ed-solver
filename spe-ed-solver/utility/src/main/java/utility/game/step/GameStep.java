package utility.game.step;

import java.util.Collections;
import java.util.Map;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;

/**
 * Class for storing all informations available to the players in one game step.
 * This contains the updated {@link Board}, deadlines and information about you
 * and all the {@link IPlayer players}.
 */
public class GameStep {

	private final IPlayer self;

	private final Map<Integer, IPlayer> enemies;

	private final IDeadline deadline;

	private final Board<Cell> board;

	private final boolean running;

	/**
	 * Initilizes a new {@link GameStep}.
	 * 
	 * @param self     the {@link IPlayer} that received the {@link GameStep}
	 * @param enemies  a {@link Map} of {@link IPlayer self's} enemies
	 * @param deadline the {@link IDeadline} for the current game round
	 * @param board    the {@link Board} with all set {@link Cell cells}
	 * @param running  true, if the game is still runnning, false if the game is
	 *                 finished
	 */
	public GameStep(final IPlayer self, final Map<Integer, IPlayer> enemies, final IDeadline deadline,
			final Board<Cell> board, final boolean running) {
		this.self = self;
		this.enemies = Collections.unmodifiableMap(enemies);
		this.deadline = deadline;
		this.board = board;
		this.running = running;
	}

	/**
	 * @return the {@link IPlayer} that received the {@link GameStep}
	 */
	public IPlayer getSelf() {
		return self;
	}

	/**
	 * @return a {@link Map} of {@link GameStep#getSelf() self's} enemies
	 */
	public Map<Integer, IPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * @return the {@link IDeadline} for the current game round
	 */
	public IDeadline getDeadline() {
		return deadline;
	}

	/**
	 * @return the {@link Board} with all set {@link Cell cells}
	 */
	public Board<Cell> getBoard() {
		return board;
	}

	/**
	 * @return true, if the game is still runnning, false if the game is finished
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * The number of dead and alive players that are part of the game.
	 * 
	 * @return the playercount (2, 3, 4, 5 or 6)
	 */
	public int getPlayerCount() {
		return this.getEnemies().size() + 1;
	}

}
