package utility.game.player;

/**
 * IPlayer
 */
public interface IMovablePlayer extends IPlayer {

	/**
	 * Sets the IsActive-State of the Player to false
	 */
	void die();

	/**
	 * Sets the Action that the Player will do
	 * 
	 * @param action
	 */
	void setAction(PlayerAction action);

	/**
	 * Applies the last set {@link PlayerAction action} and moves the player for
	 * speed Cells forward
	 */
	void doActionAndMove();

}