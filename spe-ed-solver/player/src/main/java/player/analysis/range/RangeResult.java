package player.analysis.range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.game.player.IMovablePlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Result of a Range Calculation
 */
public final class RangeResult implements IMovablePlayer {

    private int round;
    private final PlayerAction initialAction;
    private PlayerAction nextAction;
    private final Set<Point2i> allPassedCells;
    private List<Point2i> lastPassedCells;
    private final PlayerDirection targetDirection;
    private int targetSpeed;
    private Point2i targetPosition;
    private boolean isDead;

    public RangeResult(int currentRound, PlayerAction chosenAction, Set<Point2i> passedCells,
            PlayerDirection targetDirection, int targetSpeed, Point2i targetPosition) {
        this.round = currentRound;
        this.initialAction = chosenAction;
        this.allPassedCells = passedCells;
        this.targetDirection = targetDirection;
        this.targetSpeed = targetSpeed;
        this.targetPosition = targetPosition;
    }

    public RangeResult(PlayerAction chosenAction, IMovablePlayer player) {
        this(player.getRound(), chosenAction, new HashSet<>(), player.getDirection(), player.getSpeed(),
                player.getPosition());
    }

    public PlayerAction getInitialAction() {
        return this.initialAction;
    }

    public Set<Point2i> getAllPassedCells() {
        return this.allPassedCells;
    }

    public List<Point2i> getLastPassedCells() {
        return this.lastPassedCells;
    }

    public PlayerDirection getDirection() {
        return this.targetDirection;
    }

    public int getSpeed() {
        return this.targetSpeed;
    }

    public Point2i getPosition() {
        return this.targetPosition;
    }

    @Override
    public void setNextAction(final PlayerAction action) {
        this.nextAction = action;
    }

    @Override
    public void doAction() {
        if (this.nextAction == PlayerAction.SPEED_UP)
            this.targetSpeed++;
        else if (this.nextAction == PlayerAction.SLOW_DOWN)
            this.targetSpeed--;
        else
            this.targetDirection.doAction(this.nextAction);

        if (this.targetSpeed < 1 || this.targetSpeed > 10)
            this.isDead = true;

        this.nextAction = null;
    }

    @Override
    public void doMove() {
        if (isDead)
            return;

        var speedDirectionVector = this.targetDirection.getDirectionVector().multiply(this.targetSpeed);
        var newPosition = this.targetPosition.translate(speedDirectionVector);
        this.lastPassedCells = targetPosition.pointsInRectangle(newPosition);

        while (round % 6 == 0 && lastPassedCells.size() > 2) {
            lastPassedCells.remove(1);
        }

        this.targetPosition = newPosition;
        for (var cell : lastPassedCells)
            isDead |= !this.allPassedCells.add(cell);

        this.round++;
    }

    @Override
    public RangeResult copy() {
        return new RangeResult(this.round, this.initialAction, new HashSet<>(this.allPassedCells), targetDirection,
                targetSpeed, targetPosition);
    }

    @Override
    /**
     * Always -1
     */
    public int getPlayerId() {
        return -1;
    }

    @Override
    /**
     * Indicates if the Player is "alive".
     * 
     * @return true for alive, false for dead (because of self collision or speed
     *         violation)
     */
    public boolean isActive() {
        return !isDead;
    }

    @Override
    public PlayerAction getNextAction() {
        return this.nextAction;
    }

    @Override
    public int getRound() {
        return round;
    }
}
