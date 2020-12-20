package player;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * SpeedSolverPlayer
 */
public class SpeedSolverPlayer {

    private final int playerId;

    public SpeedSolverPlayer(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Starts the Player to calculate a Action for the given GameStep
     * 
     * @param gameStep              the new gameStep
     * @param doPlayerActionActionA A Function-Reference for the Player to send the
     *                              next Action
     */
    public PlayerAction calculateAction(GameStep gameStep) {

        // Send the Calculated Action
        return PlayerAction.CHANGE_NOTHING;
    }

    public int getPlayerId() {
        return this.playerId;
    }
}