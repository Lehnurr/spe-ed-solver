package simulation;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonParser;

import utility.extensions.EnumExtensions;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.FloatMatrix;
import utility.geometry.Point2i;

/**
 * Game
 */
public class Game {
    private static final int JUMP_FREQUENCY = 6;

    private final FloatMatrix board;
    private final SimulationPlayer[] players;
    private final Random random;
    private OffsetTime deadline;
    private int round = 1;

    public Game(int height, int width, int playerCount) {
        this.board = new FloatMatrix(width, height);
        this.players = new SimulationPlayer[playerCount];
        this.random = new Random();
    }

    /**
     * Starts the simulation
     * 
     * @return Initial Gamestate
     */
    public String startSimulation() {
        // Initialize Players with a random startposition and a random direction
        Set<Point2i> notAvailableStartPositions = new HashSet<>();
        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {

            Point2i randomStartPosition;
            do {
                var randomX = random.nextInt(board.getWidth());
                var randomY = random.nextInt(board.getWidth());
                randomStartPosition = new Point2i(randomX, randomY);
            } while (notAvailableStartPositions.contains(randomStartPosition));

            // Add the new positions neighbors and the neighbor-neighbors to the not allowed
            // start-points
            notAvailableStartPositions.addAll(randomStartPosition.vonNeumannNeighborhood().stream()
                    .flatMap(l -> l.vonNeumannNeighborhood().stream()).collect(Collectors.toSet()));

            PlayerDirection randomStartDirection = EnumExtensions.getRandomValue(PlayerDirection.class);

            this.players[playerIndex] = new SimulationPlayer(playerIndex + 1, randomStartPosition, randomStartDirection,
                    SimulationPlayer.MIN_SPEED);
        }

        resetDeadLine();
        return getGameState();
    }

    private void resetDeadLine() {
        // random number of seconds between 2 (inclusive) and 15 (inclusive)
        final int MIN = 2;
        final int MAX = 15;

        var deadLineSeconds = random.nextInt(MAX - MIN + 1) + MIN;
        this.deadline = OffsetTime.now().plusSeconds(deadLineSeconds);
    }

    private String getGameState() {
        // TODO: implement json-String return
        return random.nextInt() + "";
    }

    public String getServerTime() {
        JsonParser jsonParser = new JsonParser();
        String currentTime = OffsetTime.now().format(DateTimeFormatter.ISO_INSTANT);

        return jsonParser.parse(currentTime).getAsString();
    }

    /**
     * Applies an action to a simulated player. The Player dies if the deadline
     * exceeded or if this is the second action in the same turn. When all Players
     * applied an Action, a new round will start.
     * 
     * @param playerId the Players Id (1 - 6)
     * @param action   the action to be applied
     * @return The next Game-State, if all Players sent an {@link PlayerAction
     *         action} for this round. Else {@code null}
     */
    public String doAction(int playerId, PlayerAction action) {
        var currentPlayer = this.players[playerId - 1];

        if (OffsetTime.now().isAfter(this.deadline)) {
            currentPlayer.die();
        }

        if (currentPlayer.isActive()) {
            currentPlayer.setAction(action);
        }

        // return if not all players have sent an action
        if (!Arrays.stream(players).allMatch(SimulationPlayer::isReadyToMove)) {
            return null;
        }

        // Do Actions & move the player
        Arrays.stream(players).forEach(SimulationPlayer::doActionAndMove);

        HashMap<Point2i, Integer> newSteps = new HashMap<>();
        // Update board and maybe kill player
        for (var player : this.players) {
            if (!player.isActive())
                continue;

            // calculate the Vector to the first step by moving back (speed-1) steps
            var firstStepVector = player.getDirection().getDirectionVector().multiply((player.getSpeed() - 1) * -1);
            var firstStepPosition = player.getPosition().translate(firstStepVector);

            // calculate the passed steps
            List<Point2i> passedSteps = getPassedSteps(firstStepPosition, player.getPosition());

            for (var step : passedSteps) {
                if (setCell(step, player.getPlayerId()) == -1) {
                    player.die();
                }

                newSteps.computeIfPresent(step, (key, value) -> {
                    player.die();
                    if (value > 0)
                        players[value].die();
                    return -1;
                });

                newSteps.computeIfAbsent(step, k -> player.getPlayerId());

            }

        }

        round++;
        return getGameState();

    }

    private int setCell(Point2i point, int value) {
        if ((int) board.getValue(point) == 0)
            board.setValue(point, value);
        else
            board.setValue(point, -1);

        return (int) board.getValue(point);
    }

    private List<Point2i> getPassedSteps(Point2i positionA, Point2i positionB) {
        if (round % JUMP_FREQUENCY == 0)
            return positionB.pointsInRectangle(positionA);
        else
            return Arrays.asList(positionA, positionB);
    }
}