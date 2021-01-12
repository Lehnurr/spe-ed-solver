package solver;

import java.util.function.Consumer;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * Interface for a solver that gets {@link GameStep game information} and
 * returns a {@link PlayerAction}
 */
public interface ISpeedSolver {

	/**
	 * Starts the {@link ISpeedSolver solver} to calculate a Action for the given
	 * GameStep
	 * 
	 * @param gameStep            The new gameStep
	 * @param boardRatingConsumer {@link Consumer} which consumes
	 *                            {@link ContextualFloatMatrix} for documentation of
	 *                            the decision made
	 * @return the {@link PlayerAction} result
	 */
	PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer);

}