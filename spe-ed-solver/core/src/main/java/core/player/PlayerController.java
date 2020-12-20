package core.player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import player.SpeedSolverPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * PlayerController for multiple Player
 */
public class PlayerController {

    private final Map<Integer, SpeedSolverPlayer> players;

    /**
     * A Controller to Control multiple Speeed-Solver-Player
     * 
     * @param doPlayerActionAction A Function-Reference for the Player to send their
     *                             PlayerId and the next Action they want to do
     */
    public PlayerController() {
        this.players = new HashMap<>();
    }

    /**
     * Adds a new Player to the Controller with the given ID
     * 
     * @return true if the player was added, false if the player already existed
     */
    public SpeedSolverPlayer addPlayer(int playerId) {
        if (this.players.containsKey(playerId))
            return null;

        return this.players.computeIfAbsent(playerId, SpeedSolverPlayer::new);
    }

    /**
     * Sends the new GameStep to a specific single Player. Adds the player if it
     * does not already exist
     * 
     * @param gameStep The new GameStep
     */
    public PlayerAction sendGameStep(GameStep gameStep, int playerId) {
        var player = this.players.get(playerId);

        if (player == null)
            player = addPlayer(playerId);

        return player.calculateAction(gameStep);
    }

    /**
     * Sends the new GameStep to all controlled Players
     * 
     * @param gameStep             The new GameStep
     * @param doPlayerActionAction The function that accepts the new Action for each
     *                             Player
     */
    public void sendGameStep(GameStep gameStep, BiConsumer<Integer, PlayerAction> doPlayerActionAction) {
        this.players.keySet().forEach(playerId -> {
            // TODO: translate the "you" variable to the playerId
            PlayerAction action = sendGameStep(gameStep, playerId);
            doPlayerActionAction.accept(playerId, action);
        });
    }
}
