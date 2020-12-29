package player;

import java.util.function.Consumer;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

public interface ISpeedSolverPlayer {

	/**
	 * Starts the Player to calculate a Action for the given GameStep
	 * 
	 * @param gameStep            The new gameStep
	 * @param boardRatingConsumer {@link Consumer} which consumes
	 *                            {@link ContextualFloatMatrix} for documentation of
	 *                            the decision made
	 */
	PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer);


}