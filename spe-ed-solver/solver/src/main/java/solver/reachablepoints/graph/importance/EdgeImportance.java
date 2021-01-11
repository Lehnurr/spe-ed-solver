package solver.reachablepoints.graph.importance;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import solver.analysis.ActionsRating;
import solver.reachablepoints.graph.RatedPredictiveGraphPlayer;
import solver.reachablepoints.graph.board.ConcreteEdge;
import solver.reachablepoints.graph.board.Node;
import utility.game.player.PlayerAction;
import utility.geometry.FloatMatrix;

/**
 * Calculation Class to calculated and combine the importance for
 * {@link ConcreteEdge ConcreteEdges}
 */
public class EdgeImportance {

	private final Map<ConcreteEdge, Integer> initialEdgeImportance;
	private final Map<PlayerAction, ConcreteEdge> initialEdges;
	private FloatMatrix invertedMatrixResult;
	private ActionsRating invertedRatingResult;
	private final int width;
	private final int height;

	private EdgeImportance(int height, int width) {
		initialEdgeImportance = new HashMap<>();
		initialEdges = new EnumMap<>(PlayerAction.class);
		invertedRatingResult = null;
		invertedMatrixResult = null;
		this.width = width;
		this.height = height;
	}

	/**
	 * Initializes a new calculator
	 * 
	 * @param width          the width of the board and the resulting
	 *                       {@link FloatMatrix}
	 * @param height         the height of the board and the resulting
	 *                       {@link FloatMatrix}
	 * @param initialPlayers the possible players for doing one action
	 */
	public EdgeImportance(final int width, final int height, final List<RatedPredictiveGraphPlayer> initialPlayers) {
		this(height, width);
		// determine the initial edges based on the startplayers
		for (final RatedPredictiveGraphPlayer startPlayer : initialPlayers) {
			final ConcreteEdge initialEdge = startPlayer.getEdgeTail().get(0);
			this.initialEdges.put(startPlayer.getInitialAction(), initialEdge);
			this.initialEdgeImportance.put(initialEdge, 0);
		}
	}

	/**
	 * Initializes a new calculator
	 * 
	 * @param width        the width of the board and the resulting
	 *                     {@link FloatMatrix}
	 * @param height       the height of the board and the resulting
	 *                     {@link FloatMatrix}
	 * @param initialEdges the edges that can be passed in one round
	 */
	public EdgeImportance(int width, int height, final Map<PlayerAction, ConcreteEdge> initialEdges) {
		this(height, width);
		for (final Entry<PlayerAction, ConcreteEdge> entry : initialEdges.entrySet()) {
			this.initialEdges.put(entry.getKey(), entry.getValue());
			this.initialEdgeImportance.put(entry.getValue(), 0);
		}
	}

	/**
	 * Returns an array of the edges that can be passed in one round
	 * 
	 * @return {@link ConcreteEdge}-Array
	 */
	public ConcreteEdge[] getInitialEdgeArray() {
		return initialEdges.values().toArray(new ConcreteEdge[initialEdges.size()]);
	}

	/**
	 * increments the importance of the initial edges
	 * 
	 * @param initialEdgeIncrements a Map to link an increment to an edge
	 */
	public void add(final Map<ConcreteEdge, Integer> initialEdgeIncrements) {
		for (final Entry<ConcreteEdge, Integer> entry : initialEdgeIncrements.entrySet()) {
			this.initialEdgeImportance.computeIfPresent(entry.getKey(), (k, v) -> v + entry.getValue());
		}

		invertedRatingResult = null;
		invertedMatrixResult = null;
	}

	/**
	 * increments the importance of the initial edges
	 * 
	 * @param other a {@link EdgeImportance} whose edge-increments should be added
	 *              to the edge increments of this instance
	 */
	public void add(final EdgeImportance other) {
		for (final ConcreteEdge edge : other.initialEdgeImportance.keySet()) {
			this.initialEdgeImportance.computeIfPresent(edge, (k, v) -> v + other.initialEdgeImportance.get(k));
		}

		invertedRatingResult = null;
		invertedMatrixResult = null;
	}

	/**
	 * Returns the inverted, normalized Actionsrating. It will be calculated if it
	 * has not already been.
	 * 
	 * @return Normalized inverted Importance Actionsrating
	 */
	public ActionsRating getInvertedRatingResult() {
		if (invertedRatingResult == null) {
			invertedRatingResult = new ActionsRating();

			if (initialEdgeImportance.isEmpty())
				return invertedRatingResult;

			final int maxImportance = Collections.max(initialEdgeImportance.values());

			if (maxImportance == 0)
				return invertedRatingResult;

			for (final Entry<PlayerAction, ConcreteEdge> entry : initialEdges.entrySet()) {
				final float normalizedImportance = initialEdgeImportance.getOrDefault(entry.getValue(), 0)
						/ (float) maxImportance;

				final float invertedNormalizedImportance = 1 - normalizedImportance;
				invertedRatingResult.setRating(entry.getKey(), invertedNormalizedImportance);
			}
		}

		return invertedRatingResult;
	}

	/**
	 * Returns the inverted, normalized Actionsrating. It will be calculated if it
	 * has not already been.
	 * 
	 * @return Normalized inverted Importance Actionsrating
	 */
	public FloatMatrix getInvertedMatrixResult() {
		if (invertedMatrixResult == null) {
			invertedMatrixResult = new FloatMatrix(width, height);

			if (initialEdgeImportance.isEmpty())
				return invertedMatrixResult;

			final int maxImportance = Collections.max(initialEdgeImportance.values());

			if (maxImportance == 0)
				return invertedMatrixResult;

			for (final ConcreteEdge initialEdge : initialEdges.values()) {
				final float normalizedImportance = initialEdgeImportance.getOrDefault(initialEdge, 0)
						/ (float) maxImportance;

				final float invertedNormalizedImportance = 1 - normalizedImportance;
				for (final Node cell : initialEdge.getPath()) {
					invertedMatrixResult.setValue(cell.getPosition(), invertedNormalizedImportance);
				}
			}
		}

		return invertedMatrixResult;
	}

	/**
	 * Returns a map which links the initial {@link PlayerAction} to the resulting
	 * initial {@link ConcreteEdge}
	 * 
	 * @return {@link Map} mapping a {@link PlayerAction} to its {@link ConcreteEdge
	 *         ConcreteEdges}
	 */
	public Map<PlayerAction, ConcreteEdge> getInitialEdges() {
		return initialEdges;
	}

}
