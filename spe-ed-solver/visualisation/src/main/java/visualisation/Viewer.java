package visualisation;

import java.util.ArrayList;
import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerAction;
import utility.geometry.ContextualFloatMatrix;

/**
 * Reference implementation for the {@link IViewer} interface.
 */
public class Viewer implements IViewer {

	private static final ColorGradient DEFAULT_COLOR_GRADIENT = ColorGradient.FIRE;

	private final ViewerWindow window;

	private final List<ViewerSlice> slices = new ArrayList<ViewerSlice>();

	private int maxRoundIdx = -1;
	private int displayedRoundIdx = -1;

	/**
	 * Generates a new {@link Viewer} with the default {@link ViewerWindow}.
	 *
	 * @param playerType a String representation of the Player-Type
	 */
	public Viewer(final String playerType) {
		this.window = new ViewerWindow(this::showRound, playerType);
	}

	/**
	 * Generates a new viewer with a {@link ViewerWindow}.
	 * 
	 * @param viewerWindow the window to display information in
	 */
	public Viewer(final ViewerWindow viewerWindow) {
		this.window = viewerWindow;
	}

	@Override
	public void commitRound(final int playerId, final double availableTime, final PlayerAction performedAction,
			final double requiredTime, final Board<Cell> board, final List<ContextualFloatMatrix> boardRatings) {

		maxRoundIdx++;

		ViewerSlice slice = new ViewerSlice(playerId, maxRoundIdx, availableTime, performedAction, requiredTime);
		slice.addImage(ImageGeneration.generateImageFromBoard(board));
		boardRatings.stream().map(rating -> ImageGeneration.generateImageFromMatrix(rating, DEFAULT_COLOR_GRADIENT))
				.forEachOrdered(slice::addImage);
		slices.add(slice);

		window.setMaxTimelineValue(maxRoundIdx);

		if (displayedRoundIdx == maxRoundIdx - 1) {
			window.triggerTimlineChange(maxRoundIdx);
		}
	}

	/**
	 * Shows a specific {@link ViewerSlice} to the user in the responsible
	 * {@link ViewerWindow}.
	 * 
	 * @param roundIdx the index of the round to show
	 */
	public void showRound(final int roundIdx) {

		if (roundIdx < 0) {
			throw new IllegalArgumentException("referenced round index is below zero");
		}
		if (roundIdx > maxRoundIdx) {
			throw new IllegalArgumentException("referenced round index is higher than the stored rounds");
		}

		displayedRoundIdx = roundIdx;

		ViewerSlice slice = slices.get(displayedRoundIdx);
		showSlice(slice);
	}

	/**
	 * Responsible for showing a {@link ViewerSlice} on the {@link ViewerWindow}.
	 * 
	 * @param viewerSlice slice to be shown
	 */
	private void showSlice(final ViewerSlice viewerSlice) {

		window.setRoundCounter(viewerSlice.getRound());
		window.setAvailableTime(viewerSlice.getAvailableTime());
		window.setPerformedAction(viewerSlice.getPerformedAction());
		window.setRequiredTime(viewerSlice.getRequiredTime());
		window.setPlayerColor(viewerSlice.getPlayerRgbColor());

		window.updateBoardRatings(viewerSlice.getImages());
	}

}
