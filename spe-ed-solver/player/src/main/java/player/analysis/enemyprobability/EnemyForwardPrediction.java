package player.analysis.enemyprobability;

import java.util.ArrayList;
import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.geometry.Point2i;

public class EnemyForwardPrediction {

	private final Board<Cell> board;

	private final PathBoundProbability[][] probabilities;

	public EnemyForwardPrediction(final Board<Cell> board, final IPlayer player, final int playerId) {

		this.board = board;

		this.probabilities = new PathBoundProbability[board.getHeight()][board.getWidth()];
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				final PathDescriptor pathDescriptor = new PathDescriptor(playerId);
				this.probabilities[y][x] = new PathBoundProbability(pathDescriptor, 0f);
			}
		}
	}

	private void doRecursiveStep(final PredictivePlayer player, final float probability, final int depth) {

		final List<PredictivePlayer> validPlayers = new ArrayList<>(PlayerAction.values().length);
		for (final PlayerAction action : PlayerAction.values()) {
			final PredictivePlayer adaptedPlayer = new PredictivePlayer(player, action, board);
			if (adaptedPlayer.isActive())
				validPlayers.add(adaptedPlayer);
		}

		final float probabilityFactor = 1f / validPlayers.size();
		final float adaptedProbability = probability * probabilityFactor;
		
		for (final PredictivePlayer adaptedPlayer : validPlayers) {
			for(final Point2i point : adaptedPlayer.getShortTail()) {
				
			}
		}
	}

}
