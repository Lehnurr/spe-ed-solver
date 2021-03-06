package utility.game.player;

import utility.geometry.Vector2i;

/**
 * Enum to describe every possible {@link PlayerDirection} a player can look at.
 */
public enum PlayerDirection {

	UP(new Vector2i(0, -1)), RIGHT(new Vector2i(1, 0)), DOWN(new Vector2i(0, 1)), LEFT(new Vector2i(-1, 0));

	/**
	 * Vector of the direction on the game board.
	 */
	private final Vector2i vector;

	private PlayerDirection(Vector2i vector) {
		this.vector = vector;
	}

	/**
	 * Applies an {@link PlayerAction} to the current direction and returns the new
	 * adapted direction.
	 * 
	 * @param playerAction the {@link PlayerAction} to execute
	 * @return the resulting {@link PlayerDirection}
	 */
	public PlayerDirection doAction(PlayerAction playerAction) {

		int offset = 0;

		if (playerAction == PlayerAction.TURN_RIGHT) {
			offset = 1;
		} else if (playerAction == PlayerAction.TURN_LEFT) {
			offset = -1;
		}

		int directionCount = PlayerDirection.values().length;
		return PlayerDirection.values()[(this.ordinal() + offset + directionCount) % directionCount];
	}

	/**
	 * Returns a {@link Vector2i} fitting for the current direction with the
	 * magnitude of 1.
	 * 
	 * @return vector of the direction
	 */
	public Vector2i getDirectionVector() {
		return this.vector;
	}

	/**
	 * Inverts the direction (180° turn).
	 * 
	 * @return inverted direction
	 */
	public PlayerDirection getInversion() {
		final int FULL_TURN = 2;
		int directionCount = PlayerDirection.values().length;
		return PlayerDirection.values()[(this.ordinal() + FULL_TURN + directionCount) % directionCount];
	}
}
