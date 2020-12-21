package simulation;

import utility.game.player.*;
import utility.geometry.Point2i;

public class SimulationPlayer extends MovablePlayer {

    private int playerId;
    private Point2i position;
    private PlayerDirection direction;
    private PlayerAction lastSetAction;
    private boolean isActive;
    private int speed;

    public SimulationPlayer(int playerId, Point2i initialPosition, PlayerDirection initialDirection, int initialSpeed) {
        this(playerId, initialPosition, initialDirection, initialSpeed, 1);
    }

    public SimulationPlayer(int playerId, Point2i initialPosition, PlayerDirection initialDirection, int initialSpeed,
            int initialRound) {
        super(initialRound);
        this.playerId = playerId;
        this.position = initialPosition;
        this.direction = initialDirection;
        this.speed = initialSpeed;
        this.isActive = true;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void die() {
        this.isActive = false;
    }

    @Override
    public boolean isActive() {
        if (this.isActive && (getSpeed() > MAX_SPEED || getSpeed() < MIN_SPEED))
            die();

        return this.isActive;
    }

    @Override
    public PlayerDirection getDirection() {
        return this.direction;
    }

    @Override
    public int getSpeed() {
        return this.speed;
    }

    @Override
    public Point2i getPosition() {
        return position;
    }

    public boolean isReadyToMove() {
        return this.lastSetAction != null || !this.isActive();
    }

    @Override
    /**
     * Sets the to do action. The Player dies, if an action is already set.
     */
    public void setAction(PlayerAction action) {
        // in the simulation it is not allowed to overwrite the set action
        if (this.lastSetAction != null)
            die();

        if (isActive())
            this.lastSetAction = action;
    }

    @Override
    /**
     * Applies the {@link SimulationPlayer#lastSetAction} to the Player, if the
     * player is alive and resets the Action
     */
    protected void doAction() {
        if (!isActive())
            return;

        if (this.lastSetAction == PlayerAction.SPEED_UP)
            this.speed++;
        else if (this.lastSetAction == PlayerAction.SLOW_DOWN)
            this.speed--;
        else
            this.direction.doAction(this.lastSetAction);

        this.lastSetAction = null;
    }

    @Override
    /**
     * Changes the Players Position depending on direction and speed, if the player
     * is alive
     */
    protected void doMove() {
        if (!isActive())
            return;

        var speedDirectionVector = this.direction.getDirectionVector().multiply(this.speed);
        this.position = this.position.translate(speedDirectionVector);
    }
}
