package webcommunication.webservice.parser;

import utility.game.player.IPlayer;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;
import webcommunication.webservice.parser.jsonobject.JSONGameStepPlayer;

/**
 * Simple Class which implements {@link IPlayer} and contains informations of
 * players for a single {@link GameStep}.
 */
public class GameStepPlayer implements IPlayer {

	private final int playerId;
	private final boolean active;
	private final PlayerDirection direction;
	private final int speed;
	private final Point2i position;

	/**
	 * Creates a {@link GameStepPlayer} with the given values.
	 * 
	 * @param playerId  unique id of the player
	 * @param active    shows if the player is alive
	 * @param direction direction the player is currently moving in
	 * @param speed     current speed the player is moving with
	 * @param position  position as {@link Position2i} of the player on the
	 *                  {@link Board}
	 */
	public GameStepPlayer(final int playerId, final boolean active, final PlayerDirection direction, final int speed,
			final Point2i position) {
		this.playerId = playerId;
		this.active = active;
		this.direction = direction;
		this.speed = speed;
		this.position = position;
	}

	/**
	 * Creates a new {@link GameStepPlayer} from a given {@link JSONGameStepPlayer}
	 * and the unique id of the player.
	 * 
	 * @param playerId           unique id of the player
	 * @param jsonGameStepPlayer {@link JSONGameStepPlayer} received from the server
	 */
	public GameStepPlayer(final int playerId, final JSONGameStepPlayer jsonGameStepPlayer) {
		this.playerId = playerId;
		this.active = jsonGameStepPlayer.active;
		this.direction = PlayerDirection.valueOf(jsonGameStepPlayer.direction.toUpperCase());
		this.speed = jsonGameStepPlayer.speed;
		this.position = new Point2i(jsonGameStepPlayer.x, jsonGameStepPlayer.y);
	}

	@Override
	public int getPlayerId() {
		return playerId;
	}

	@Override
	@Override
	public boolean isActive() {
		return active;
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

}