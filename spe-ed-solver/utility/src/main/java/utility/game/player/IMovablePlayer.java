package utility.game.player;

/**
 * A mutable {@link IPlayer} that can do {@link PlayerAction actions} and move.
 */
public interface IMovablePlayer extends IPlayer {

    /**
     * Sets the to do {@link PlayerAction action}. The {@link SimulationPlayer} dies, if
     * an {@link PlayerAction action} is already set.
     * 
     * @param action the action that the player should do
     */
    void setNextAction(PlayerAction action);

    /**
     * The {@link PlayerAction} that was set as next action.
     * 
     * @return the last set {@link PlayerAction}
     */
    PlayerAction getNextAction();

    /**
     * Applies the last set {@link PlayerAction action} to the Player. This affects
     * the speed or {@link PlayerDirection}.
     */
    void doAction();

    /**
     * Changes the {@link IPlayer players} Position depending on the
     * {@link IPlayer#getDirection()} and the {@link IPlayer#getSpeed()}, if the
     * player {@link IPlayer#isActive() is active}
     */
    void doMove();

    /**
     * Creates a Copy of the {@link IMovablePlayer}.
     * 
     * @return the Copy of the {@link IMovablePlayer}
     */
    IMovablePlayer copy();
}
