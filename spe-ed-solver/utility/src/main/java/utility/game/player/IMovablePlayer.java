package utility.game.player;

/**
 * IMovablePlayer
 */
public interface IMovablePlayer extends IPlayer {
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