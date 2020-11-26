package simulation;

import utility.extensions.EnumExtensions;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

/**
 * Game
 */
public class Game {

    private final Board<Cell> board;
    private final SimulationPlayer[] players;

    public Game(int height, int width, int playerCount) {
        // Initialize Board
        final Cell[][] cells = new Cell[height][width];
        this.board = new Board<>(cells);

        // Initialize Player with random startposition and direction
        this.players = new SimulationPlayer[playerCount];
        for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {

            // TODO: select a random startposition
            Point2i randomStartPosition = new Point2i(0, playerIndex);
            PlayerDirection randomStartDirection = EnumExtensions.getRandomValue(PlayerDirection.class);

            this.players[playerIndex] = new SimulationPlayer(playerIndex + 1, randomStartPosition, randomStartDirection,
                    SimulationPlayer.MIN_SPEED);
        }
    }

    // TODO: Game Logic
}