package utility.game.player;

/**
 * Enum to describe every possible action a player can do in a game round.
 */
public enum PlayerAction {

	TURN_LEFT("TURN LEFT"), TURN_RIGHT("TURN RIGHT"), SLOW_DOWN("SLOW DOWN"), SPEED_UP("SPEED UP"),
	CHANGE_NOTHING("CHANGE NOTHING");

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
