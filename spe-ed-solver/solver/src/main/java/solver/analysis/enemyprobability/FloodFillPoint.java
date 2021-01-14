package solver.analysis.enemyprobability;

import utility.game.player.IPlayer;
import utility.geometry.Point2i;

public class FloodFillPoint {

	private final int round;
	private final Point2i position;
	private final int speed;
	private final int speedCounter;

	public FloodFillPoint(final int round, final Point2i position, final int speed) {
		this.round = round;
		this.position = position;
		this.speed = speed;
		this.speedCounter = 1;
	}

	public FloodFillPoint(final int round, final Point2i position, final int speed, final int speedCounter) {
		this.round = round;
		this.position = position;
		this.speed = speed;
		this.speedCounter = speedCounter;
	}

	public FloodFillPoint next(final Point2i position) {
		if (speedCounter == speed)
			return new FloodFillPoint(round + 1, position, Math.max(speed + 1, IPlayer.MAX_SPEED));
		else
			return new FloodFillPoint(round, position, speed, speedCounter + 1);

	}

	/**
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @return the position
	 */
	public Point2i getPosition() {
		return position;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @return the speedCounter
	 */
	public int getSpeedCounter() {
		return speedCounter;
	}

}
