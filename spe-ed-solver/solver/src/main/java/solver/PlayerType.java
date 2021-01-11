package solver;

import solver.random.RandomPlayer;
import solver.reachablepoints.ReachablePointsPlayer;
import solver.reachablepoints.ReachablePointsType;

/**
 * {@link PlayerType} enum representing multiple types of spe_ed solver players.
 * Each type can generate a {@link ISpeedSolverPlayer} instance.
 */
public enum PlayerType {

	REACHABLE_POINTS {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(7, 0.4f, 0.05f, ReachablePointsType.MULTI_THREADED);
		}
	},
	REACHABLE_POINTS_SINGLE_THREADED {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(7, 0.4f, 0.05f, ReachablePointsType.SINGLE_THREADED);
		}
	},
	GRAPH_AGGRESSIVE {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(7, 0.4f, 0.01f, ReachablePointsType.GRAPH);
		}
	},
	GRAPH_DEFENSIVE {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(7, 0.1f, 0.35f, ReachablePointsType.GRAPH);
		}
	},
	GRAPH_DYNAMIC {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(7, -1f, -1f, ReachablePointsType.GRAPH);
		}
	},
	RANDOM {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new RandomPlayer();
		}
	};

	/**
	 * Returns a new instance of the {@link ISpeedSolverPlayer} representing the
	 * {@link PlayerType}.
	 * 
	 * @return {@link ISpeedSolverPlayer} representing the {@link PlayerType}.
	 */
	public abstract ISpeedSolverPlayer newInstance();

	/**
	 * Returns the default {@link PlayerType}, representing the {@link PlayerType}
	 * for which most success can be suspected.
	 * 
	 * @return default {@link PlayerType}
	 */
	public static PlayerType getDefault() {
		return REACHABLE_POINTS;
	}

}
