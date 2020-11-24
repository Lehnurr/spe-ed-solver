package utility.game;

public enum PlayerDirection {

	UP,
	RIGHT,
	DOWN, 
	LEFT;

	private PlayerDirection() {
	}

	/**
	 * Applies an {@link PlayerAction} to the current direction and returns the new
	 * adapted direction.
	 * 
	 * @param playerAction
	 * @return new direction
	 */
	public PlayerDirection doTurn(PlayerAction playerAction) {

		int offset = 0;

		if (playerAction == PlayerAction.TURN_RIGHT) {
			offset = 1;
		} else if (playerAction == PlayerAction.TURN_RIGHT) {
			offset = -1;
		}

		return PlayerDirection.values()[this.ordinal() + offset + PlayerDirection.values().length];
	}

}
