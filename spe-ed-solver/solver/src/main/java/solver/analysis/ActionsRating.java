package solver.analysis;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import utility.game.player.PlayerAction;

/**
 * Class representing a rating for all possible {@link PlayerAction} values.
 * Each rating is represented as double value.
 */
public class ActionsRating {

	private final Map<PlayerAction, Double> ratingMap;

	/**
	 * Creates an empty {@link ActionsRating} with a value of 0 for all
	 * {@link PlayerAction} values.
	 */
	public ActionsRating() {
		this.ratingMap = new EnumMap<>(PlayerAction.class);
		for (final PlayerAction action : PlayerAction.values()) {
			ratingMap.put(action, 0.);
		}
	}

	/**
	 * Sets a new value for a given {@link PlayerAction}.
	 * 
	 * @param playerAction {@link PlayerAction} to change the value for
	 * @param ratingValue  new value to set
	 */
	public void setRating(final PlayerAction playerAction, final double ratingValue) {
		ratingMap.put(playerAction, ratingValue);
	}

	/**
	 * Returns the rating value for a given {@link PlayerAction}.
	 * 
	 * @param playerAction {@link PlayerAction} to get the rating value for
	 * @return rating value
	 */
	public double getRating(final PlayerAction playerAction) {
		return ratingMap.get(playerAction);
	}

	/**
	 * Determines the {@link PlayerAction} with the highest rating value. If
	 * multiple {@link PlayerAction actions} have the same rating value the first
	 * found {@link PlayerAction} will be returned.
	 * 
	 * @return {@link PlayerAction} with the maximum rating value
	 */
	public PlayerAction maxAction() {
		PlayerAction maxAction = PlayerAction.CHANGE_NOTHING;
		double maxValue = Float.MIN_VALUE;
		for (final PlayerAction action : PlayerAction.values()) {
			final double newValue = ratingMap.get(action);
			if (maxValue < newValue) {
				maxValue = newValue;
				maxAction = action;
			}
		}
		return maxAction;
	}

	/**
	 * Determines the maximum rating value of all stored rating values and returns
	 * it.
	 * 
	 * @return maximum rating value
	 */
	public double maxRating() {
		return ratingMap.get(maxAction());
	}

	/**
	 * Normalizes the {@link ActionsRating} by dividing all rating values by the
	 * maximum value of all rating values.
	 */
	public void normalize() {
		final double maxValue = maxRating();

		if (Double.isNaN(0 / maxValue))
			return;

		for (final PlayerAction action : PlayerAction.values()) {
			final double oldValue = getRating(action);
			setRating(action, oldValue / maxValue);
		}
	}

	/**
	 * Combines this {@link ActionsRating} with another {@link ActionsRating} by
	 * adding each rating with a weight to the object's rating.
	 * 
	 * @param other       {@link ActionsRating} to combine with
	 * @param otherWeight weight for the other object
	 * @return combined {@link ActionsRating}
	 */
	public ActionsRating combine(final ActionsRating other, final double otherWeight) {
		final ActionsRating returnValue = new ActionsRating();
		for (final PlayerAction action : PlayerAction.values()) {
			final double otherWeightedValue = other.getRating(action) * otherWeight;
			final double combinedValue = this.getRating(action) + otherWeightedValue;
			returnValue.setRating(action, combinedValue);
		}
		return returnValue;
	}

	@Override
	public String toString() {
		return ratingMap.entrySet().stream().map(e -> String.format("%s=%.3f", e.getKey(), e.getValue()))
				.collect(Collectors.toList()).toString();
	}

}
