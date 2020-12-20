package player;

import utility.game.player.PlayerAction;

/**
 * SpeedSolverPlayer
 */
public class SpeedSolverPlayer {

    private final int playerId;

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
    public PlayerAction calculateAction(String gameState) {

        // Send the Calculated Action
        return PlayerAction.CHANGE_NOTHING;
    }

    public int getPlayerId() {
        return this.playerId;
    }
}