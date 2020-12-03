package player;

import java.util.function.Consumer;

/**
 * SpeedSolverPlayer
 */
public class SpeedSolverPlayer {

    private int playerId;

    public SpeedSolverPlayer(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Starts the Player to calculate a Action for the given GameState
     * 
     * @param gameState             the new gameState as JSON-String
     * @param doPlayerActionActionA A Function-Reference for the Player to send the
     *                              next Action
     */
    public void doRound(String gameState, Consumer<String> doPlayerActionAction) {

        // Send the Calculated Action
        doPlayerActionAction.accept("{ CALCULATED_ACTION}");

    }

    public int getPlayerId() {
        return this.playerId;
    }
}