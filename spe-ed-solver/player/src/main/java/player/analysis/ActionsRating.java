package player.analysis;

import java.util.EnumMap;
import java.util.Map;

import utility.game.player.PlayerAction;

/**
 * Class representing a rating for all possible {@link PlayerAction} values.
 * Each rating is represented as float value.
 */
public class ActionsRating {

	private final Map<PlayerAction, Float> ratingMap;

	/**
	 * Creates an empty {@link ActionsRating} with a value of 0 for all
	 * {@link PlayerAction} values.
	 */
	public ActionsRating() {
		this.ratingMap = new EnumMap<>(PlayerAction.class);
		for (final PlayerAction action : PlayerAction.values()) {
			ratingMap.put(action, 0f);
		}
	}

	/**
	 * Sets a new value for a given {@link PlayerAction}.
	 * 
	 * @param playerAction {@link PlayerAction} to change the value for
	 * @param ratingValue  new value to set
	 */
	public void setRating(final PlayerAction playerAction, final float ratingValue) {
		ratingMap.put(playerAction, ratingValue);
	}

	/**
	 * Returns the rating value for a given {@link PlayerAction}.
	 * 
	 * @param playerAction {@link PlayerAction} to get the rating value for
	 * @return rating value
	 */
	public float getRating(final PlayerAction playerAction) {
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
		float maxValue = Float.MIN_VALUE;
		for (final PlayerAction action : PlayerAction.values()) {
			final float newValue = ratingMap.get(action);
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
	public float maxRating() {
		return ratingMap.get(maxAction());
	}

	/**
	 * Normalizes the {@link ActionsRating} by dividing all rating values by the
	 * maximum value of all rating values.
	 */
	public void normalize() {
		final float maxValue = maxRating();
		for (final PlayerAction action : PlayerAction.values()) {
			final float oldValue = getRating(action);
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
	public ActionsRating combine(final ActionsRating other, final float otherWeight) {
		final ActionsRating returnValue = new ActionsRating();
		for (final PlayerAction action : PlayerAction.values()) {
			final float otherWeightedValue = other.getRating(action) * otherWeight;
			final float combinedValue = this.getRating(action) + otherWeightedValue;
			returnValue.setRating(action, combinedValue);
		}
		return returnValue;
	}

	@Override
	public String toString() {
		return ratingMap.toString();
	}

}
