package player;

/**
 * {@link PlayerType} enum representing multiple types of spe_ed solver players.
 * Each type can generate a {@link ISpeedSolverPlayer} instance.
 */
public enum PlayerType {

	REACHABLE_POINTS {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(5, 0.4f);
		}
	},
	REACHABLE_POINTS_BASE {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new ReachablePointsPlayer(5, 0f);
		}
	},
	GRAPH {
		@Override
		public ISpeedSolverPlayer newInstance() {
			return new GraphPlayer(5, 0.4f);
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
