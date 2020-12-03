package utility.game.player;

/**
 * Enum to describe every possible action a player can do in a game round.
 */
public enum PlayerAction {

	TURN_LEFT("TURN_LEFT"), TURN_RIGHT("TURN_RIGHT"), SLOW_DOWN("SLOW_DOWN"), SPEED_UP("SPEED_UP"),
	CHANGE_NOTHING("CHANGE_NOTHING");

	// textual representation of the action type
	private final String name;

	private PlayerAction(String name) {
		this.name = name;
	}

	/**
	 * @return name of the action type
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}
