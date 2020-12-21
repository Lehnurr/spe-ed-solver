package visualisation;

import java.util.ArrayList;
import java.util.List;

import utility.game.player.PlayerAction;
import utility.geometry.ContextualFloatMatrix;

/**
 * Reference implementation for the {@link IViewer} interface.
 */
public class Viewer implements IViewer {

	// color gradient to apply to every matrix
	private static final ColorGradient DEFAULT_COLOR_GRADIENT = ColorGradient.FIRE;

	// window to display the information to
	private final ViewerWindow window;

	// slices for every received round to store displayable information
	private final List<ViewerSlice> slices = new ArrayList<ViewerSlice>();

	// round information
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
	public void commitRound(double availableTime, PlayerAction performedAction, double requiredTime,
			List<ContextualFloatMatrix> boardRatings) {

		maxRoundIdx++;

		ViewerSlice slice = new ViewerSlice(maxRoundIdx, availableTime, performedAction, requiredTime);
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
	 * @param roundIdx
	 */
	public void showRound(int roundIdx) {

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

		// update game info
		window.setRoundCounter(viewerSlice.getRound());
		window.setAvailableTime(viewerSlice.getAvailableTime());
		window.setPerformedAction(viewerSlice.getPerformedAction());
		window.setRequiredTime(viewerSlice.getRequiredTime());

		// update board ratings
		window.updateBoardRatings(viewerSlice.getImages());
	}

}
