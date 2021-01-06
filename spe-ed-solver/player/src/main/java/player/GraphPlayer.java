package player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import player.analysis.ActionsRating;
import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import player.analysis.graph.GraphCalculator;
import player.boardevaluation.graph.Graph;
import player.boardevaluation.graph.Node;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.logging.GameLogger;

/**
 * GraphPlayer
 */
public class GraphPlayer implements ISpeedSolverPlayer {
        private final EnemyProbabilityCalculator enemyProbabilityCalculator = new EnemyProbabilityCalculator();
        private final GraphCalculator graphCalculator = new GraphCalculator();

        private final int enemySearchDepth;
        private final float cutOffWeight;
        private final float importanceWeight;

        private Graph graph;
        private int[] activeEnemiesIds;

        public GraphPlayer(final int enemySearchDepth, final float cutOffWeight, final float importanceWeight) {
                this.enemySearchDepth = enemySearchDepth;
                this.cutOffWeight = cutOffWeight;
                this.importanceWeight = importanceWeight;
        }

        @Override
        public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {
                if (!gameStep.getSelf().isActive())
                        return PlayerAction.CHANGE_NOTHING;

                enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(), 5);

                if (this.graph == null) {
                        // Initialize the graph with an empty Node-Array
                        var emptyNodes = new Node[gameStep.getBoard().getHeight()][gameStep.getBoard().getWidth()];
                        this.graph = new Graph(emptyNodes);

                        // Initialize the alive player
                        this.activeEnemiesIds = gameStep.getEnemies().keySet().stream().mapToInt(Integer::intValue)
                                        .toArray();
                }

                // Merge all players that were active last round into one list
                List<IPlayer> enemies = new ArrayList<>();
                for (int playerId : this.activeEnemiesIds)
                        enemies.add(gameStep.getEnemies().get(playerId));

                // Transfer the new occupied cells to the graph
                graph.updateGraph(gameStep.getBoard(), gameStep.getSelf(), enemies);

                // Determine the currently dead players to ignore them in the following round
                // for the graph-update.
                this.activeEnemiesIds = enemies.stream().filter(IPlayer::isActive).mapToInt(IPlayer::getPlayerId)
                                .toArray();

                // Calculate the action
                enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(),
                                enemySearchDepth);

                graphCalculator.performCalculation(gameStep.getSelf(),
                                enemyProbabilityCalculator.getProbabilitiesMatrix(),
                                enemyProbabilityCalculator.getMinStepsMatrix(), gameStep.getDeadline(), graph);

                // Calculate and combine the action ratings
                final ActionsRating successActionsRating = graphCalculator.getSuccessRatingsResult();
                final ActionsRating cutOffActionsRating = graphCalculator.getCutOffRatingsResult();
                final ActionsRating importanceResult = graphCalculator.getInvertedImportanceResult();
                final ActionsRating combinedActionsRating = successActionsRating
                                .combine(cutOffActionsRating, cutOffWeight).combine(importanceResult, importanceWeight);

                // Log calculated Rating-Information
                GameLogger.logGameInformation(String.format("success-rating:\t%s", successActionsRating));
                GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffActionsRating));
                GameLogger.logGameInformation(String.format("inverted-importance-rating:\t%s", importanceResult));
                GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));

                // get the best action
                final PlayerAction actionToTake = combinedActionsRating.maxAction();

                sendViewerData(boardRatingConsumer, actionToTake);

                // Send the Calculated Action
                return actionToTake;
        }

        private void sendViewerData(final Consumer<ContextualFloatMatrix> boardRatingConsumer,
                        final PlayerAction actionToTake) {
                var probabilitiesNamedMatrix = new ContextualFloatMatrix("probability",
                                enemyProbabilityCalculator.getProbabilitiesMatrix(), 0, 1);
                boardRatingConsumer.accept(probabilitiesNamedMatrix);

                var minStepsNamedMatrix = new ContextualFloatMatrix("min steps",
                                enemyProbabilityCalculator.getMinStepsMatrix());
                boardRatingConsumer.accept(minStepsNamedMatrix);

                var successNamedMatrix = new ContextualFloatMatrix("success",
                                graphCalculator.getNormalizedSuccessMatrixResult(actionToTake), 0, 1);
                boardRatingConsumer.accept(successNamedMatrix);

                var cutOffNamedMatrix = new ContextualFloatMatrix("cut off",
                                graphCalculator.getNormalizedCutOffMatrixResult(actionToTake), 0, 1);
                boardRatingConsumer.accept(cutOffNamedMatrix);

                var importanceNamedMatrix = new ContextualFloatMatrix("inverted importance",
                                graphCalculator.getNormalizedInvertedImportanceMatrixResult(), 0, 1);
                boardRatingConsumer.accept(importanceNamedMatrix);
        }

}