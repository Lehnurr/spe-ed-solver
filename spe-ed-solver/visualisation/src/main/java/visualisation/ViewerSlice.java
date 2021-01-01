package visualisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utility.game.player.PlayerAction;

/**
 * Slice for a specific game round for aggregating and storing round specific
 * information.
 */
public class ViewerSlice {

	private final int round;

	private final double availableTime;

	private final PlayerAction performedAction;

	private final double requiredTime;

	private final List<NamedImage> images = new ArrayList<NamedImage>();

	/**
	 * Generates a new viewer slice with round specific information.
	 * 
	 * @param round           the round index the viewer slice is valid for
	 * @param availableTime   the available time in seconds
	 * @param performedAction the {@link PlayerAction} chosen by the player for the
	 *                        given round
	 * @param timeRequired    the required time in seconds
	 */
	public ViewerSlice(final int round, final double availableTime, final PlayerAction performedAction,
			final double requiredTime) {
		this.round = round;
		this.availableTime = availableTime;
		this.performedAction = performedAction;
		this.requiredTime = requiredTime;
	}

	/**
	 * @return the round the viewer slice is valid for
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @return the available time in seconds
	 */
	public double getAvailableTime() {
		return availableTime;
	}

	/**
	 * @return the {@link PlayerAction} performed by the player
	 */
	public PlayerAction getPerformedAction() {
		return performedAction;
	}

	/**
	 * @return the actual time the player needed to calculate his action
	 */
	public double getRequiredTime() {
		return requiredTime;
	}

	/**
	 * Adds an image to the current {@link ViewerSlice}.
	 * 
	 * @param image {@link NamedImage} to add to the {@link ViewerSlice}
	 */
	public void addImage(final NamedImage image) {
		images.add(image);
	}

	/**
	 * @return the {@link NamedImage images} aggregated for the given round
	 */
	public List<NamedImage> getImages() {
		return Collections.unmodifiableList(images);
	}

}
