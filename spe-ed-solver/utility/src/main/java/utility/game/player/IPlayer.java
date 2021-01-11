package utility.game.player;

import utility.geometry.Point2i;

/**
 * IPlayer
 */
public interface IPlayer {

	public static final int MAX_SPEED = 10;
	public static final int MIN_SPEED = 1;

	/**
	 * @return Returns the integer-ID of the Player
	 */
	int getPlayerId();

	/**
	 * Returns the players current direction
	 * 
	 * @return the {@link PlayerDirection}
	 */
	PlayerDirection getDirection();

	/**
	 * Returns the players current speed
	 * 
	 * @return speed as int
	 */
	int getSpeed();

	/**
	 * Returns the players current position
	 * 
	 * @return the {@link Point2i} of the position
	 */
	Point2i getPosition();

	/**
	 * @return the round the {@link IPlayer} is valid for
	 */
	int getRound();

	/**
	 * Indicates if the Player is "alive".
	 * 
	 * @return true for alive, false for dead
	 */
	boolean isActive();
}