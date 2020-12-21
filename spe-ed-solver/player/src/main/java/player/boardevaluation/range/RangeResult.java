package player.boardevaluation.range;

import java.util.ArrayList;
import java.util.List;

import utility.game.player.IMovable;
import utility.game.player.MovablePlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Result of a Range Calculation
 */
public final class RangeResult implements IMovable {

    private int round;
    private final PlayerAction initialAction;
    private PlayerAction nextAction;
    private final List<Point2i> passedCells;
    private final PlayerDirection targetDirection;
    private int targetSpeed;
    private Point2i targetPosition;

    public RangeResult(int currentRound, PlayerAction chosenAction, List<Point2i> passedCells,
            PlayerDirection targetDirection, int targetSpeed, Point2i targetPosition) {
        this.round = currentRound;
        this.initialAction = chosenAction;
        this.passedCells = passedCells;
        this.targetDirection = targetDirection;
        this.targetSpeed = targetSpeed;
        this.targetPosition = targetPosition;
    }

    public RangeResult(PlayerAction chosenAction, MovablePlayer player) {
        this(player.getRound(), chosenAction, new ArrayList<>(), player.getDirection(), player.getSpeed(),
                player.getPosition());
    }

    public PlayerAction getInitialAction() {
        return this.initialAction;
    }

    public List<Point2i> getPassedCells() {
        return this.passedCells;
    }

    public PlayerDirection getTargetDirection() {
        return this.targetDirection;
    }

    public int getTargetSpeed() {
        return this.targetSpeed;
    }

    public Point2i getTargetPosition() {
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

        this.nextAction = null;
    }

    @Override
    public void doMove() {
        var speedDirectionVector = this.targetDirection.getDirectionVector().multiply(this.targetSpeed);
        var newPosition = this.targetPosition.translate(speedDirectionVector);
        var newPassedCells = targetPosition.pointsInRectangle(newPosition);

        while (round % 6 == 0 && newPassedCells.size() > 2) {
            newPassedCells.remove(1);
        }

        this.targetPosition = newPosition;
        this.passedCells.addAll(newPassedCells);
        this.round++;
    }

    @Override
    public RangeResult copy() {
        return new RangeResult(this.round, this.initialAction, new ArrayList<>(this.passedCells), targetDirection,
                targetSpeed, targetPosition);
    }
}
