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
     * Sets the IsActive-State of the Player to false
     */
    void die();

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