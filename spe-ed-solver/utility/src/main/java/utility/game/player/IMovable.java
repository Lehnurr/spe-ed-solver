package utility.game.player;

public interface IMovable {

    /**
     * Sets the Action that the Player will do
     * 
     * @param action The ToDo-Action
     */
    void setNextAction(PlayerAction action);

    /**
     * Applies the last set {@link PlayerAction action}
     */
    void doAction();

    /**
     * Moves the player for speed Cells forward
     */
    void doMove();

    /**
     * Creates a Copy of the Movable
     * 
     * @return The Copy of the IMovable
     */
    IMovable copy();
}
