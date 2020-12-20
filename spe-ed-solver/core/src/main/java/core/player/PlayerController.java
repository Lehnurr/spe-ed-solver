package core.player;

import player.SpeedSolverPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * PlayerController
 */
public class PlayerController {

    private final SpeedSolverPlayer player;

    public PlayerController(int playerId) {
        this.player = new SpeedSolverPlayer(playerId);
    }

    /**
     * Sends the new GameStep to the Player and returns the chosen Action
     * 
     * @param gameStep The current Game Step
     * @return The action chosen by the player
     */
    public PlayerAction calculateAction(GameStep gameStep) {
        return player.calculateAction(gameStep);
    }
}
