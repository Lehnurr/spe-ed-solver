package visualisation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;

import utility.game.player.PlayerAction;

/**
 * Window class usable to display spe_ed game informations to the user by
 * visualizing them.
 */
public class ViewerWindow {

	private static final int MIN_WINDOW_WIDTH = 700;
	private static final int MIN_WINDOW_HEIGHT = 500;

	private static final int INFO_GAP_HORIZONTAL = 10;
	private static final int INFO_GAP_VERTICAL = 5;

	private static final int BOARD_RATING_PADDING_HORIZONTAL = 32;
	private static final int BOARD_RATING_PADDING_VERTICAL = 8;

	private final JFrame jFrame = new JFrame();

	private final JLabel roundLabel = new JLabel("-1");
	private final JLabel availableTimeLabel = new JLabel("-1");
	private final JLabel performedActionLabel = new JLabel("-1");
	private final JLabel requiredTimeLabel = new JLabel("-1");

	private final JLabel playerTypeLabel = new JLabel("-1", SwingConstants.CENTER);

	private final JPanel boardPanel = new JPanel();

	private final JScrollBar timelineScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);

	private List<NamedImage> boardRatings = new ArrayList<>();

	/**
	 * Generates a new {@link ViewerWindow} and displays it.
	 * 
	 * @param timelineChangeHandler a handler called when the time line changes and
	 *                              information of an other round should be
	 *                              displayed.
	 * @param playerType            a String representation of the Player-Type
	 *                              represented by this {@link ViewerWindow}
	 */
	public ViewerWindow(final IntConsumer timelineChangeHandler, final String playerType) {

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
		timelineScrollBar.addAdjustmentListener(event -> timelineChangeHandler.accept(event.getValue()));

		// panel for info
		JPanel infoPanel = new JPanel();
		mainPanel.add(infoPanel, BorderLayout.EAST);
		infoPanel.setLayout(new BorderLayout());

		// panel for round specific information
		JPanel roundInfoPanel = new JPanel();
		infoPanel.add(roundInfoPanel, BorderLayout.NORTH);
		roundInfoPanel.setLayout(new GridLayout(0, 2, INFO_GAP_HORIZONTAL, INFO_GAP_VERTICAL));

		roundInfoPanel.add(new JLabel("Game round:"));
		roundInfoPanel.add(roundLabel);
		roundInfoPanel.add(new JLabel("Available time:"));
		roundInfoPanel.add(availableTimeLabel);
		roundInfoPanel.add(new JLabel("Performed action:"));
		roundInfoPanel.add(performedActionLabel);
		roundInfoPanel.add(new JLabel("Required time:"));
		roundInfoPanel.add(requiredTimeLabel);

		playerTypeLabel.setText(playerType);
		playerTypeLabel.setOpaque(true);
		infoPanel.add(playerTypeLabel, BorderLayout.SOUTH);

		// set minimum size of window
		jFrame.setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

		// exit program when closing window
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// redraw board ratings when window resize happens
		mainPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redrawBoardPanel();
			}
		});

		// show JFrame
		jFrame.setVisible(true);
	}

	/**
	 * Updates the displayed round in the window.
	 * 
	 * @param roundCounter the displayed round
	 */
	public void setRoundCounter(final int roundCounter) {
		roundLabel.setText(Integer.toString(roundCounter));
	}

	/**
	 * Updates the displayed available time in the window.
	 * 
	 * @param availableTime the available time in seconds
	 */
	public void setAvailableTime(final double availableTime) {
		availableTimeLabel.setText(String.format("%.4f", availableTime));
	}

	/**
	 * Updates the displayed performed action in the window.
	 * 
	 * @param performedAction the performed {@link PlayerAction}
	 */
	public void setPerformedAction(final PlayerAction performedAction) {
		performedActionLabel.setText(performedAction.getName());
	}

	/**
	 * Updates the displayed required time in the window.
	 * 
	 * @param requiredTime the required time in seconds
	 */
	public void setRequiredTime(final double requiredTime) {
		requiredTimeLabel.setText(String.format("%.4f", requiredTime));
	}

	/**
	 * Updates the displayed Player-Color in the window.
	 * 
	 * @param rgbColor the rgb Color Value for the Player
	 */
	public void setPlayerColor(final int rgbColor) {
		final Color color = new Color(rgbColor);
		playerTypeLabel.setForeground(color);
		playerTypeLabel.setBackground(Color.BLACK);
	}

	/**
	 * Adds multiple {@link NamedImages} to the {@link ViewerWindow}.
	 * 
	 * @param namedImages the {@link NamedImage images} to display as board ratings
	 */
	public void updateBoardRatings(List<NamedImage> namedImages) {
		boardRatings = namedImages;
		redrawBoardPanel();
	}

	/**
	 * Internal function to handle a redraw of the board panel.
	 */
	private void redrawBoardPanel() {

		final int displayedBoards = boardRatings.size();

		if (displayedBoards > 0) {
			// recalculate grid layout
			final int xGridElements = (int) Math.ceil(Math.sqrt(displayedBoards));
			final int yGridElements = (int) Math.ceil(displayedBoards * 1. / xGridElements);
			boardPanel.setLayout(new GridLayout(yGridElements, xGridElements));

			// calculates max size in each dimension for board rating
			final float maxElementWidth = boardPanel.getWidth() / xGridElements - BOARD_RATING_PADDING_HORIZONTAL;
			final float maxElementHeight = boardPanel.getHeight() / yGridElements - BOARD_RATING_PADDING_VERTICAL;

			// update graphics
			boardPanel.removeAll();
			for (NamedImage namedImage : boardRatings) {

				// create new panel for Board rating
				JPanel singleBoardPanel = new JPanel();
				boardPanel.add(singleBoardPanel);

				// update layout
				singleBoardPanel.setLayout(new BorderLayout());
				JLabel imageTitle = new JLabel(namedImage.getName(), SwingConstants.CENTER);
				singleBoardPanel.add(imageTitle, BorderLayout.NORTH);
				BufferedImage image = namedImage.getImage();

				// scale and display image
				float scalingFactor = maxElementWidth / image.getWidth();
				if (image.getHeight() * scalingFactor > maxElementHeight)
					scalingFactor = maxElementHeight / image.getHeight();
				final int newWidth = (int) (image.getWidth() * scalingFactor);
				final int newHeight = (int) (image.getHeight() * scalingFactor);
				Image rescaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
				ImageIcon imageIcon = new ImageIcon(rescaledImage);
				JLabel imageLabel = new JLabel(imageIcon);
				singleBoardPanel.add(imageLabel, BorderLayout.CENTER);
			}
		}

		boardPanel.repaint();
		boardPanel.revalidate();
	}

	/**
	 * Sets the max value for the time line, marking the highest round index the
	 * user may select.
	 * 
	 * @param maxValue the max value of the timeline
	 */
	public void setMaxTimelineValue(final int maxValue) {
		this.timelineScrollBar.setMaximum(maxValue);
	}

	/**
	 * Externally shift the time line to a new value.
	 * 
	 * @param newValue the new value the timeline should be set to
	 */
	public void triggerTimlineChange(final int newValue) {
		this.timelineScrollBar.setValue(newValue);
	}

}
