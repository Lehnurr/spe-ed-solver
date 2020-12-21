package player.boardevaluation.range;

import java.util.ArrayList;
import java.util.List;

import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Result of a Range Calculation
 */
public final class RangeResult {

    private final PlayerAction chosenAction;
    private final List<Point2i> passedCells;
    private final PlayerDirection targetDirection;
    private final int targetSpeed;
    private final Point2i targetPosition;

    public RangeResult(PlayerAction chosenAction, List<Point2i> passedCells, PlayerDirection targetDirection,
            int targetSpeed, Point2i targetPosition) {
        this.chosenAction = chosenAction;
        this.passedCells = passedCells;
        this.targetDirection = targetDirection;
        this.targetSpeed = targetSpeed;
        this.targetPosition = targetPosition;
    }

    public RangeResult(PlayerAction chosenAction, IPlayer player) {
        this(chosenAction, new ArrayList<>(), player.getDirection(), player.getSpeed(), player.getPosition());
    }

    public PlayerAction getChosenAction() {
        return this.chosenAction;
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

    public RangeResult addResult(List<Point2i> newPassedCells, PlayerDirection targetDirection, int targetSpeed,
            Point2i targetPosition) {

        // Create new Rangeresult with same initial Action
        var newResult = new RangeResult(this.chosenAction, this.passedCells, targetDirection, targetSpeed,
                targetPosition);

        // Add the new passed cells to thepassedCells
        newResult.passedCells.addAll(newPassedCells);

        return newResult;
    }
}
