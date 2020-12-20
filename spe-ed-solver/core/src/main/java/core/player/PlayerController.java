package core.player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import player.SpeedSolverPlayer;
import utility.game.player.PlayerAction;

/**
 * PlayerController for multiple Player
 */
public class PlayerController {

    private final Map<Integer, SpeedSolverPlayer> players;
    private final BiConsumer<Integer, PlayerAction> doPlayerActionAction;

    /**
     * A Controller to Control multiple Speeed-Solver-Player
     * 
     * @param doPlayerActionAction A Function-Reference for the Player to send their
     *                             PlayerId and the next Action they want to do
     */
    public PlayerController(BiConsumer<Integer, PlayerAction> doPlayerActionAction) {
        this.players = new HashMap<>();
        this.doPlayerActionAction = doPlayerActionAction;
    }

    /**
     * Adds a new Player to the Controller with the given ID
     * 
     * @return true if the player was added, false if the player already existed
     */
    public boolean addPlayer(int playerId) {
        if (this.players.containsKey(playerId))
            return false;

        this.players.computeIfAbsent(playerId, SpeedSolverPlayer::new);
        return true;
    }

    /**
     * Sends the new GameState to a specific single Player
     * 
     * @param gameState The new JSON-String-Game-State
     */
    public void sendGameState(String gameState, int playerId) {
        var player = this.players.get(playerId);
        if (player == null)
            return;

        PlayerAction action = player.calculateAction(gameState);
        doPlayerActionAction.accept(playerId, action);
    }

    /**
     * Sends the new GameState to all controlled Players
     * 
     * @param gameState The new JSON-String-Game-State
     */
    public void sendGameState(String gameState) {
        // TODO: translate the "you" variable to the playerId
        this.players.keySet().forEach(playerId -> sendGameState(gameState, playerId));
    }
}
