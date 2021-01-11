package utility.game.player;

import utility.geometry.Point2i;

/**
 * The state of a spe-ed-player in a specific round.
 */
public interface IPlayer {

	public static final int MAX_SPEED = 10;
	public static final int MIN_SPEED = 1;

	/**
	 * The Players unique Id.
	 * 
	 * @return returns the integer-ID of the {@link IPlayer Player}
	 */
	int getPlayerId();

	/**
	 * The players current {@link PlayerDirection}.
	 * 
	 * @return the {@link PlayerDirection}
	 */
	PlayerDirection getDirection();

	/**
	 * The players current speed.
	 * 
	 * @return speed as int
	 */
	int getSpeed();

	/**
	 * The players current {@link Point2i Position}.
	 * 
	 * @return the {@link Point2i} of the position
	 */
	Point2i getPosition();

	/**
	 * The round in which the {@link IPlayer} is.
	 * 
	 * @return the current round (>=0)
	 */
	int getRound();

	/**
	 * Indicates if the Player is "alive".
	 * 
	 * @return true for alive, false for dead
	 */
	boolean isActive();
}