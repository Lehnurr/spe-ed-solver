package visualisation;

import java.util.ArrayList;
import java.util.List;

import utility.game.player.PlayerAction;
import utility.geometry.ContextualFloatMatrix;

public class Viewer {

	// window to display the information to
	private final ViewerWindow window;

	// slices for every received round to store displayable information
	private final List<ViewerSlice> slices = new ArrayList<ViewerSlice>();

	// round information
	private int maxRoundIdx = 0;
	private int displayedRoundIdx = 0;

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

	/**
	 * Stores round specific information.
	 */
	public void commitRound(double availableTime, PlayerAction performedAction, double requiredTime,
			List<ContextualFloatMatrix> boardRatings) {

		ViewerSlice slice = new ViewerSlice(maxRoundIdx, availableTime, performedAction, requiredTime);
		// TODO images
		slices.add(slice);

		window.setMaxTimelineValue(maxRoundIdx);
		
		// automatically display the new slice when user is on former newest slice
		if (displayedRoundIdx == maxRoundIdx) {
			showRound(maxRoundIdx);
		}
		
		maxRoundIdx++;
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
	}

}
