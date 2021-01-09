package player.solver.random;

import java.util.function.Consumer;

import player.ISpeedSolverPlayer;
import utility.extensions.EnumExtensions;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * A Player that sends a random Action
 */
public final class RandomPlayer implements ISpeedSolverPlayer {

    @Override
    public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {
        return EnumExtensions.getRandomValue(PlayerAction.class);
    }

}
