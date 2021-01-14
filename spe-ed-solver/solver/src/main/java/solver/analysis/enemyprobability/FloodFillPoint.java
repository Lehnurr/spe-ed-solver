package solver.analysis.enemyprobability;

import utility.game.player.IPlayer;
import utility.geometry.Point2i;

/**
 * Helper class for flood filling the enemy minimum steps by storing a single
 * point which has to be calculated into the minimum steps {@link FloatMatrix}.
 */
public class FloodFillPoint {

	private final int round;
	private final Point2i position;
	private final int speed;
	private final int speedCounter;

	/**
	 * Generates a initials {@link FloodFillPoint} with the given parameters.
	 * 
	 * @param round    the relative round from the enemy probability calculation
	 * @param position the {@link Point2i} of the {@link IPlayer} position
	 * @param speed    the speed of the {@link IPlayer}
	 */
	public FloodFillPoint(final int round, final Point2i position, final int speed) {
		this.round = round;
		this.position = position;
		this.speed = speed;
		this.speedCounter = 1;
	}

	/**
	 * Internal constructor used to generate the next {@link FloodFillPoint} based
	 * on the {@link FloodFillPoint#next(Point2i)} function.
	 * 
	 * @param round        the relative round from the enemy probability calculation
	 * @param position     the {@link Point2i} of the {@link IPlayer} position
	 * @param speed        the speed of the {@link IPlayer}
	 * @param speedCounter the internal counter used to determine when to increase
	 *                     speed and round
	 */
	public FloodFillPoint(final int round, final Point2i position, final int speed, final int speedCounter) {
		this.round = round;
		this.position = position;
		this.speed = speed;
		this.speedCounter = speedCounter;
	}

	/**
	 * Creates a new {@link FloodFillPoint} for the next prediction step based on
	 * this {@link FloodFillPoint}.
	 * 
	 * @param position {@link Point2i} next neighbor position
	 * @return resulting {@link FloodFillPoint}
	 */
	public FloodFillPoint next(final Point2i position) {
		if (speedCounter == speed)
			return new FloodFillPoint(round + 1, position, Math.max(speed + 1, IPlayer.MAX_SPEED));
		else
			return new FloodFillPoint(round, position, speed, speedCounter + 1);

	}

	/**
	 * Returns the relative round of the state.
	 * 
	 * @return the relative round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * The {@link Point2i} position of the state.
	 * 
	 * @return the {@link Point2i} position
	 */
	public Point2i getPosition() {
		return position;
	}

	/**
	 * Returns the speed of the state.
	 * 
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Returns the internal speed counter, used to determine when to change round
	 * and speed.
	 * 
	 * @return the speed counter
	 */
	public int getSpeedCounter() {
		return speedCounter;
	}

}
