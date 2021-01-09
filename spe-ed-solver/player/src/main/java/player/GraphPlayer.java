package player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import player.analysis.ActionsRating;
import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import player.solver.reachablepoints.graph.GraphCalculator;
import player.solver.reachablepoints.graph.board.Graph;
import player.solver.reachablepoints.graph.board.Node;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * GraphPlayer
 */
public class GraphPlayer implements ISpeedSolverPlayer {
        private final EnemyProbabilityCalculator enemyProbabilityCalculator = new EnemyProbabilityCalculator();
        private final GraphCalculator graphCalculator = new GraphCalculator();

        private final int enemySearchDepth;
        private float cutOffWeight;
        private float importanceWeight;

        private Graph graph;
        private int[] activeEnemiesIds;
        private final boolean importanceIsDynamic;
        private final boolean cutOffIsDynamic;

        public GraphPlayer(final int enemySearchDepth, final float cutOffWeight, final float importanceWeight) {
                this.enemySearchDepth = enemySearchDepth;
                this.cutOffWeight = cutOffWeight;
                this.importanceWeight = importanceWeight;
                this.cutOffIsDynamic = cutOffWeight < 0;
                this.importanceIsDynamic = importanceWeight < 0;
        }

        @Override
        public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {
                if (!gameStep.getSelf().isActive())
                        return PlayerAction.CHANGE_NOTHING;

                updateDynamicWeights();

                updateGraph(gameStep);

                // Calculate enemyProbability
                enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(),
                                enemySearchDepth);

                // Calculate the Action
                graphCalculator.performCalculation(gameStep.getSelf(),
                                enemyProbabilityCalculator.getProbabilitiesMatrix(),
                                enemyProbabilityCalculator.getMinStepsMatrix(), gameStep.getDeadline(), graph);

                // Combine the results
                final ActionsRating combinedActionsRating = graphCalculator.combineActionsRating(importanceWeight,
                                cutOffWeight);

                // Log the results
                graphCalculator.logGameInformation(combinedActionsRating);

                // get the best action
                final PlayerAction actionToTake = combinedActionsRating.maxAction();

                // send the Data to the viewer
                sendViewerData(boardRatingConsumer, actionToTake);

                // send the Calculated Action
                return actionToTake;
        }

        private void updateDynamicWeights() {
                if (importanceIsDynamic)
                        importanceWeight = activeEnemiesIds == null ? 0 : (1 - (activeEnemiesIds.length / 10f));
                if (cutOffIsDynamic)
                        cutOffWeight = activeEnemiesIds == null ? 1 : (activeEnemiesIds.length / 12f);
        }

        private void updateGraph(GameStep gameStep) {
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
                                graphCalculator.getSuccessMatrixResult(actionToTake), 0, 1);
                boardRatingConsumer.accept(successNamedMatrix);

                var cutOffNamedMatrix = new ContextualFloatMatrix("cut off",
                                graphCalculator.getCutOffMatrixResult(actionToTake), 0, 1);
                boardRatingConsumer.accept(cutOffNamedMatrix);

                var importanceNamedMatrix = new ContextualFloatMatrix("inverted importance",
                                graphCalculator.getNormalizedInvertedImportanceMatrixResult(), 0, 1);
                boardRatingConsumer.accept(importanceNamedMatrix);
        }

}