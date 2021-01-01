package visualisation;

import java.util.ArrayList;
import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerAction;
import utility.geometry.ContextualFloatMatrix;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;

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
	 */
	public Viewer() {
		this.window = new ViewerWindow(this::showRound);
	}

	/**
	 * Generates a new viewer with a {@link ViewerWindow}.
	 * 
	 * @param viewerWindow the window to display information in
	 */
	public Viewer(ViewerWindow viewerWindow) {
		this.window = viewerWindow;
	}

	@Override
	public void commitRound(double availableTime, PlayerAction performedAction, double requiredTime, Board<Cell> board,
			List<ContextualFloatMatrix> boardRatings) {

		maxRoundIdx++;

		ViewerSlice slice = new ViewerSlice(maxRoundIdx, availableTime, performedAction, requiredTime);
		slice.addImage(ImageGeneration.generateImageFromBoard(board));
		boardRatings.stream().map((rating) -> ImageGeneration.generateImageFromMatrix(rating, DEFAULT_COLOR_GRADIENT))
				.forEachOrdered((image) -> slice.addImage(image));
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
	public void showRound(int roundIdx) {

		if (roundIdx < 0) {
			ApplicationLogger.logAndThrowException(new IllegalArgumentException("referenced round index is below zero"),
					LoggingLevel.ERROR);
		}
		if (roundIdx > maxRoundIdx) {
			ApplicationLogger.logAndThrowException(
					new IllegalArgumentException("referenced round index is higher than the stored rounds"),
					LoggingLevel.ERROR);
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

		window.updateBoardRatings(viewerSlice.getImages());
	}

}
