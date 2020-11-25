package visualisation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ViewerWindow {

	// minimum dimensions of the window
	private static final int MIN_WINDOW_WIDTH = 700;
	private static final int MIN_WINDOW_HEIGHT = 500;

	// gap values for the info panel grid layout
	private static final int INFO_GAP_HORIZONTAL = 10;
	private static final int INFO_GAP_VERTICAL = 5;

	// gap values for the board panel grid layout
	private static final int BOARD_GAP_HORIZONTAL = 100;
	private static final int BOARD_GAP_VERTICAL = 100;

	// parent JFrame
	private final JFrame jFrame = new JFrame();

	// information labels
	private final JLabel roundLabel = new JLabel("-1");
	private final JLabel availableTimeLabel = new JLabel("-1");
	private final JLabel performedActionLabel = new JLabel("-1");
	private final JLabel requiredTimeLabel = new JLabel("-1");

	// panel to show different boards with specific ratings
	private final JPanel boardPanel = new JPanel();

	// scrollbar to scroll through the different rounds
	private final JScrollBar timelineScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);

	/**
	 * Generates a new window and shows it.
	 */
	public ViewerWindow(Consumer<Integer> timelineChangeHandler) {

		// main panel of the whole window
		JPanel mainPanel = new JPanel();
		jFrame.add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		// panel to show boards with specific ratings
		mainPanel.add(boardPanel, BorderLayout.CENTER);

		// scroll bar to navigate the time line
		mainPanel.add(timelineScrollBar, BorderLayout.SOUTH);
		timelineScrollBar.setMinimum(0);
		timelineScrollBar.setMaximum(0);
		timelineScrollBar.setBlockIncrement(1);
		timelineScrollBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent event) {
				timelineChangeHandler.accept(event.getValue());
			}
		});

		// panel for info
		JPanel infoPanel = new JPanel();
		mainPanel.add(infoPanel, BorderLayout.EAST);
		infoPanel.setLayout(new BorderLayout());

		// panel for round specific information
		JPanel roundInfoPanel = new JPanel();
		infoPanel.add(roundInfoPanel, BorderLayout.NORTH);
		roundInfoPanel.setLayout(new GridLayout(0, 2, INFO_GAP_HORIZONTAL, INFO_GAP_VERTICAL));
		roundInfoPanel.add(new JLabel("gameround"));
		roundInfoPanel.add(roundLabel);
		roundInfoPanel.add(new JLabel("available time"));
		roundInfoPanel.add(availableTimeLabel);
		roundInfoPanel.add(new JLabel("performed action"));
		roundInfoPanel.add(performedActionLabel);
		roundInfoPanel.add(new JLabel("required time"));
		roundInfoPanel.add(requiredTimeLabel);

		// set minimum size of window
		jFrame.setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

		// exit program when closing window
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// show JFrame
		jFrame.setVisible(true);
	}

	/**
	 * Updates the text of the displayed round in the window.
	 * 
	 * @param roundCounter
	 */
	public void setRoundCounterText(String roundCounter) {
		roundLabel.setText(roundCounter);
	}

	/**
	 * Updates the text of the displayed available time in the window.
	 * 
	 * @param availableTime
	 */
	public void setAvailableTimeText(String availableTime) {
		availableTimeLabel.setText(availableTime);
	}

	/**
	 * Updates the text for the displayed performed action in the window.
	 * 
	 * @param performedAction
	 */
	public void setPerformedActionText(String performedAction) {
		performedActionLabel.setText(performedAction);
	}

	/**
	 * Updates the text for the displayed required time in the window.
	 * 
	 * @param requiredTime
	 */
	public void setRequiredTimeText(String requiredTime) {
		requiredTimeLabel.setText(requiredTime);
	}

	/**
	 * Adds multiple {@link NamedImages} to the {@link ViewerWindow}.
	 * 
	 * @param namedImages
	 */
	public void updateBoardRatings(List<NamedImage> namedImages) {
		
		// recalculate grid layout
		int displayedBoards = namedImages.size();
		int xGridElements = (int) Math.ceil(Math.sqrt(displayedBoards));
		boardPanel.setLayout(new GridLayout(0, xGridElements, BOARD_GAP_VERTICAL, BOARD_GAP_HORIZONTAL));

		// update graphics
		boardPanel.removeAll();
		for (NamedImage namedImage : namedImages) {
			JPanel singleBoardPanel = new JPanel();
			singleBoardPanel.setLayout(new BorderLayout());
			singleBoardPanel.add(new JLabel(namedImage.getName()), BorderLayout.NORTH);
			singleBoardPanel.add(new JLabel(new ImageIcon(namedImage.getImage())), BorderLayout.CENTER);
			boardPanel.add(singleBoardPanel);
		}
		boardPanel.repaint();
		boardPanel.revalidate();
	}

	/**
	 * Sets the max value for the time line, marking the highest round index the
	 * user may select.
	 * 
	 * @param maxValue
	 */
	public void setMaxTimelineValue(int maxValue) {
		this.timelineScrollBar.setMaximum(maxValue);
	}

	/**
	 * Externally shift the time line to a new value.
	 * 
	 * @param newValue the new value the timeline should be set to
	 */
	public void triggerTimlineChange(int newValue) {
		this.timelineScrollBar.setValue(newValue);
	}

}
