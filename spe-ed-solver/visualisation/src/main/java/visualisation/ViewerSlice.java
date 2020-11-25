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

	// round the slice describes in seconds
	private final int round;

	// time the player had to act in the given round
	private final double availableTime;

	// action performed in the given round
	private final PlayerAction performedAction;

	// time the player actually needed to calculate the performed action in seconds
	private final double timeRequired;

	// named images containing information about round specific board ratings
	private final List<NamedImage> images = new ArrayList<NamedImage>();

	/**
	 * Generates a new viewer slice with round specific information.
	 * 
	 * @param round
	 * @param availableTime
	 * @param performedAction
	 * @param timeRequired
	 */
	public ViewerSlice(int round, double availableTime, PlayerAction performedAction, double timeRequired) {
		super();
		this.round = round;
		this.availableTime = availableTime;
		this.performedAction = performedAction;
		this.timeRequired = timeRequired;
	}

	/**
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @return the availableTime
	 */
	public double getAvailableTime() {
		return availableTime;
	}

	/**
	 * @return the action performed by the player in the given round
	 */
	public PlayerAction getPerformedAction() {
		return performedAction;
	}

	/**
	 * @return the actual time the player needed to calculate his action
	 */
	public double getTimeRequired() {
		return timeRequired;
	}

	/**
	 * Adds an image to the current slice.
	 * 
	 * @param image
	 */
	public void addImage(NamedImage image) {
		images.add(image);
	}

	/**
	 * @return the named images aggregated for the given round
	 */
	public List<NamedImage> getImages() {
		return Collections.unmodifiableList(images);
	}

}
