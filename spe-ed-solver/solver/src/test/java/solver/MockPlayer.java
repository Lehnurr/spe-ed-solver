package solver;

import utility.game.player.IPlayer;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

public class MockPlayer implements IPlayer {

	private final int playerId;
	private final PlayerDirection direction;
	private final int speed;
	private final Point2i position;
	private final int round;
	private final boolean active;

	public MockPlayer(int playerId, PlayerDirection direction, int speed, Point2i position, int round, boolean active) {
		super();
		this.playerId = playerId;
		this.direction = direction;
		this.speed = speed;
		this.position = position;
		this.round = round;
		this.active = active;
	}

	@Override
	public int getPlayerId() {
		return playerId;
	}

	@Override
	public PlayerDirection getDirection() {
		return direction;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public Point2i getPosition() {
		return position;
	}

	@Override
	public int getRound() {
		return round;
	}

	@Override
	public boolean isActive() {
		return active;
	}

}
