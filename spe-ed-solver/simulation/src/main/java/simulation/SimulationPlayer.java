package simulation;

import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

public class SimulationPlayer implements IPlayer {

    private int playerId;
    private Point2i position;
    private PlayerDirection direction;
    private PlayerAction lastSetAction;
    private boolean isActive;
    private int speed;

    SimulationPlayer(int playerId, Point2i initialPosition, PlayerDirection initialDirection, int initialSpeed) {
        this.playerId = playerId;
        this.position = initialPosition;
        this.direction = initialDirection;
        this.speed = initialSpeed;
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
            this.isActive = false;

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

    @Override
    public void setAction(PlayerAction action) {
        if (this.lastSetAction != null)
            die();

        if (isActive())
            this.lastSetAction = action;
    }

    @Override
    public void doActionAndMove() {
        doAction();
        doMove();
    }

    private void doAction() {
        if (!isActive())
            return;

        if (this.lastSetAction == PlayerAction.SPEED_UP)
            this.speed++;
        else if (this.lastSetAction == PlayerAction.SLOW_DOWN)
            this.speed--;
        else
            this.direction.doAction(this.lastSetAction);
    }

    private void doMove() {
        if (!isActive())
            return;

        var speedDirectionVector = this.direction.getDirectionVector().multiply(this.speed);
        this.position = this.position.translate(speedDirectionVector);
    }

}
