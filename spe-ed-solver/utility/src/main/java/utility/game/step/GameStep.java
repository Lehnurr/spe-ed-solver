package utility.game.step;

/**
 * Class for storing all informations available to the players in one game step.
 * This contains the updated {@link InputBoard}, deadlines and information about
 * you and all the {@link InputPlayer}.
 */
public class GameStep {

    private int youId;

    public GameStep() {
    }

    public int getYouId() {
        return youId;
    }

}
