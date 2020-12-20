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
     * Indicates if the Player is "alive"
     * 
     * @return true for alive, false for dead
     */
    boolean isActive();

    /**
     * Returns the players current direction
     */
    PlayerDirection getDirection();

    /**
     * Returns the players current speed
     */
    int getSpeed();

    /**
     * Returns the players current position
     */
    Point2i getPosition();

}