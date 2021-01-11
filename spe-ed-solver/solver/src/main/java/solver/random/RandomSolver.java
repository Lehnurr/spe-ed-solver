package solver.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import solver.ISpeedSolver;
import solver.analysis.PredictivePlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * A Player that sends a random Action that doesn't cause suicide
 */
public final class RandomSolver implements ISpeedSolver {

    private static final Collector<?, ?, ?> SHUFFLER = Collectors
            .collectingAndThen(Collectors.toCollection(ArrayList::new), list -> {
                Collections.shuffle(list);
                return list;
            });

    final Random random = new Random();

    @Override
    public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {
        final PredictivePlayer self = new PredictivePlayer(gameStep.getSelf());

        return Arrays.stream(PlayerAction.values())
                .filter(action -> new PredictivePlayer(self, action, gameStep.getBoard()).isActive()).collect(shuffle())
                .stream().findFirst().orElse(PlayerAction.CHANGE_NOTHING);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collector<T, ?, List<T>> shuffle() {
        return (Collector<T, ?, List<T>>) SHUFFLER;
    }
}
