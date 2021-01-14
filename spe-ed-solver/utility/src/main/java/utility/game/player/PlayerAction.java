package utility.game.player;

/**
 * Enum to describe every possible action a player can do in a game round.
 */
public enum PlayerAction {

	TURN_LEFT("turn_left"), TURN_RIGHT("turn_right"), SLOW_DOWN("slow_down"), SPEED_UP("speed_up"),
	CHANGE_NOTHING("change_nothing");

	private final String name;

	private PlayerAction(String name) {
		this.name = name;
	}

	/**
	 * The textual representation of the action type.
	 * 
	 * @return name of the action type
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * The ordinal next {@link PlayerAction}. The first {@link PlayerAction} if this is the last one.
	 * 
	 * @return the next {@link PlayerAction}
	 */
	public PlayerAction getNext(){
		return values()[(ordinal() + 1) % values().length];
	}

}
