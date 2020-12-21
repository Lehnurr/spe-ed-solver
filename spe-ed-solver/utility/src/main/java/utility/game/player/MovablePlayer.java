package utility.game.player;

/**
 * MovablePlayer
 */
public abstract class MovablePlayer implements IPlayer {

    private int round;

    /**
     * Iitilizes a new movable Player.
     * 
     * @param round The current Game Round
     */
    protected MovablePlayer(int round) {
        this.round = round;
    }

    /**
     * Sets the IsActive-State of the Player to false
     */
    public abstract void die();

    /**
     * Sets the Action that the Player will do
     * 
     * @param action
     */
    public abstract void setAction(PlayerAction action);

    /**
     * Calls {@link #doAction()} and {@link #doMove()} and increases the
     * Round-number
     */
    public final void doActionAndMove() {
        doAction();
        doMove();
        this.round++;
    }

    /**
     * Applies the last set {@link PlayerAction action}
     */
    protected abstract void doAction();

    /**
     * moves the player for speed Cells forward
     */
    protected abstract void doMove();

    public final int getRound() {
        return this.round;
    }
}