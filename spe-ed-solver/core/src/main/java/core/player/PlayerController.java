package core.player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import player.SpeedSolverPlayer;

/**
 * PlayerController for multiple Player
 */
public class PlayerController {

    private Map<Integer, SpeedSolverPlayer> players;
    private BiConsumer<Integer, String> doPlayerActionAction;

    /**
     * A Controller to Control multiple Speeed-Solver-Player
     * 
     * @param doPlayerActionAction A Function-Reference for the Player to send their
     *                             PlayerId and the next Action they want to do
     */
    public PlayerController(BiConsumer<Integer, String> doPlayerActionAction) {
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
     * Sends the new GameState to all controlled Players
     * 
     * @param gameState The new JSON-String-Game-State
     */
    public void sendGameState(String gameState) {
        for (SpeedSolverPlayer player : this.players.values()) {
            player.doRound(gameState, action -> doPlayerActionAction.accept(player.getPlayerId(), action));
        }
    }
}
