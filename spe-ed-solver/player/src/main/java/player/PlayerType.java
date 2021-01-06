package player;

import player.analysis.reachablepoints.ReachablePointsType;

/**
 * {@link PlayerType} enum representing multiple types of spe_ed solver players.
 * Each type can generate a {@link ISpeedSolverPlayer} instance.
 */
public enum PlayerType {

	REACHABLE_POINTS {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(5, 0.4f, 0.05f, ReachablePointsType.MULTI_THREADED);
		}
	},
	REACHABLE_POINTS_SINGLE_THREADED {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(5, 0.4f, 0.05f, ReachablePointsType.SINGLE_THREADED);
		}
	},
	GRAPH {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new GraphPlayer(5, 0.4f, 0.6f);
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
