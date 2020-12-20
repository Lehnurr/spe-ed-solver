package core.player;

import java.util.HashMap;
import java.util.Map;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * GameController for multiple Player
 */
public class GameController {

    private final Map<Integer, PlayerController> players;

    /**
     * A Controller to Control multiple PlayerController
     * 
     */
    public GameController() {
        this.players = new HashMap<>();
    }

    /**
     * Sends the new GameStep to the player of the GameStep
     * 
     * @param gameStep The new GameStep
     */
    public PlayerAction sendGameStep(GameStep gameStep) {
        final int playerId = gameStep.getSelf().getPlayerId();

        if (!this.players.containsKey(playerId))
            this.players.put(playerId, new PlayerController(playerId));

        return this.players.get(playerId).calculateAction(gameStep);
    }
}
