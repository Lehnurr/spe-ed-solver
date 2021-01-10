package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import utility.extensions.EnumExtensions;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.game.step.GameStep;
import utility.geometry.Point2i;
import utility.geometry.Vector2i;

/**
 * A simulated spe_ed Game
 */
public final class Game {
    private static final int JUMP_FREQUENCY = 6;

    private final Board<Cell> board;
    private final SimulationPlayer[] players;
    private final Random random;
    private SimulationDeadline deadline;
    private int round = 1;

    /**
     * Initilizes a new Simulated Spe-ed Game
     * 
     * @param height      Height of the Board
     * @param width       Widht of the Board
     * @param playerCount Number of Simulated Players
     */
    public Game(final int height, final int width, final int playerCount) {
        final Cell[][] cells = new Cell[height][width];
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                cells[row][col] = new Cell(0);

        this.board = new Board<>(cells);
        this.players = new SimulationPlayer[playerCount];
        this.random = new Random();
        this.deadline = new SimulationDeadline();
    }

    /**
     * Starts the simulation
     * 
     * @return Initial GameStep for each Player
     */
    public List<GameStep> startSimulation() {
        // Initialize Players with a random startposition and a random direction
        final Set<Point2i> notAvailableStartPositions = new HashSet<>();
        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {

            Point2i randomStartPosition;
            do {
                final int randomX = random.nextInt(board.getWidth());
                final int randomY = random.nextInt(board.getHeight());
                randomStartPosition = new Point2i(randomX, randomY);
            } while (notAvailableStartPositions.contains(randomStartPosition));

            // Add the new positions neighbors and the neighbor-neighbors to the not allowed
            // start-points
            notAvailableStartPositions.addAll(randomStartPosition.vonNeumannNeighborhood().stream()
                    .flatMap(l -> l.vonNeumannNeighborhood().stream()).collect(Collectors.toSet()));

            PlayerDirection randomStartDirection = EnumExtensions.getRandomValue(PlayerDirection.class);

            final int playerId = playerIndex + 1;
            this.players[playerIndex] = new SimulationPlayer(playerId, randomStartPosition, randomStartDirection,
                    SimulationPlayer.MIN_SPEED, 0);
            setCell(randomStartPosition, playerId);
        }

        deadline.resetDeadLine();

        return generateGameSteps();
    }

    /**
     * Applies an action to a simulated player. The Player dies if this is the
     * second action in the same turn. When all Players applied an Action, a new
     * round will start.
     * 
     * @param playerId the Players Id (1 - 6)
     * @param action   the action to be applied
     * @return The next Game-State, if all Players sent an {@link PlayerAction
     *         action} for this round. Else {@code null}
     */
    public List<GameStep> setAction(final int playerId, final PlayerAction action) {
        final SimulationPlayer currentPlayer = this.players[playerId - 1];

        currentPlayer.setNextAction(action);

        // End round, if all players sent an Action
        if (Arrays.stream(players).allMatch(SimulationPlayer::isReadyToMove)) {
            // Do Actions & move the player
            Arrays.stream(players).forEach(SimulationPlayer::doActionAndMove);
            // update the board and the players alive-state
            updateGameState();
            // reset the deadline
            deadline.resetDeadLine();
            // return the new GameState as JSON
            return generateGameSteps();
        } else {
            // return new list, because no new gameState is available
            return new ArrayList<>();
        }
    }

    private List<GameStep> generateGameSteps() {
        final List<GameStep> gameSteps = new ArrayList<>();

        // is running when more then one player is active
        boolean isRunning = Arrays.stream(players).filter(SimulationPlayer::isActive).count() > 1;

        long remainingDeadlineMilliseconds = deadline.getRemainingMilliseconds();
        for (final SimulationPlayer player : this.players) {
            final Map<Integer, IPlayer> enemies = Arrays.stream(this.players).filter(p -> p != player)
                    .collect(Collectors.toMap(SimulationPlayer::getPlayerId, IPlayer.class::cast));

            final PlayerDeadline individualDeadline = new PlayerDeadline(remainingDeadlineMilliseconds);

            gameSteps.add(new GameStep((IPlayer) player, enemies, individualDeadline, board, isRunning));
        }

        return gameSteps;
    }

    /**
     * Applies the did steps to the board, processes collisions and increse round
     */
    private void updateGameState() {
        HashMap<Point2i, Integer> newPassedCells = new HashMap<>();

        for (final SimulationPlayer player : this.players) {
            if (!player.isActive())
                continue;

            // calculate the Vector to the first step by moving back (speed - 1) steps
            final Vector2i firstStepVector = player.getDirection().getDirectionVector()
                    .multiply((player.getSpeed() - 1) * -1);
            final Point2i firstStepPosition = player.getPosition().translate(firstStepVector);

            applyPassedSteps(player, firstStepPosition, newPassedCells);
        }

        round++;
    }

    /**
     * Applys the last steps from a player to the Board. The Player dies, if he
     * causes a collision
     * 
     * @param player               the player whose steps are to be applied
     * @param oldPosition          the players position at the start of this round
     * @param passedCellsThisRound Cells that have already been used by a player in
     *                             this round
     */
    private void applyPassedSteps(final SimulationPlayer player, final Point2i oldPosition,
            final HashMap<Point2i, Integer> passedCellsThisRound) {
        // calculate the passed steps
        final List<Point2i> steps = getPassedSteps(oldPosition, player.getPosition());

        for (final Point2i step : steps) {
            if (setCell(step, player.getPlayerId()) == CellValue.MULTIPLE_PLAYER) {
                player.die();
            }

            passedCellsThisRound.computeIfPresent(step, (key, value) -> {
                player.die();
                if (value > 0)
                    players[value - 1].die();
                return -1;
            });

            passedCellsThisRound.computeIfAbsent(step, k -> player.getPlayerId());

        }
    }

    /**
     * Calculates the passed Cells between two positions
     * 
     * @return The passed Cells (Jumped over cells are excluded)
     */
    private List<Point2i> getPassedSteps(final Point2i positionA, final Point2i positionB) {
        if (positionA.equals(positionB))
            return Arrays.asList(positionA);
        else if (round % JUMP_FREQUENCY == 0)
            return Arrays.asList(positionA, positionB);
        else
            return positionB.pointsInRectangle(positionA);
    }

    /**
     * Sets a Board Cell with a value (playerId) and returns the new value
     * 
     * @param point    The point to set
     * @param playerId The new playerId to set
     * @return The actual new value
     */
    private CellValue setCell(final Point2i point, final int playerId) {
        if (!board.isOnBoard(point))
            return CellValue.MULTIPLE_PLAYER;
        else if (board.getBoardCellAt(point).getCellValue() == CellValue.EMPTY_CELL)
            board.getBoardCellAt(point).setCellValue(playerId);
        else
            board.getBoardCellAt(point).setCellValue(CellValue.MULTIPLE_PLAYER.getIntegerValue());

        return board.getBoardCellAt(point).getCellValue();
    }
}