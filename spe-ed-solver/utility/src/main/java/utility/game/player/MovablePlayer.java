package utility.game.player;

/**
 * MovablePlayer
 */
public abstract class MovablePlayer implements IPlayer, IMovable {

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
     * Calls {@link #doAction()} and {@link #doMove()} and increases the
     * Round-number
     */
    public final void doActionAndMove() {
        doAction();
        doMove();
        this.round++;
    }

    public final int getRound() {
        return this.round;
    }
}