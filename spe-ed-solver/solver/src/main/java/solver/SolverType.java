package solver;

import solver.random.RandomSolver;
import solver.reachablepoints.ReachablePointsSolver;
import solver.reachablepoints.ReachablePointsType;

/**
 * {@link SolverType} enum representing multiple types of spe_ed solvers. Each
 * type can generate a {@link ISpeedSolver} instance.
 */
public enum SolverType {

	REACHABLE_POINTS {
		@Override
		public ISpeedSolver newInstance() {
			return new ReachablePointsSolver(7, 0.4f, 0.05f, ReachablePointsType.MULTI_THREADED);
		}
	},
	REACHABLE_POINTS_SINGLE_THREADED {
		@Override
		public ISpeedSolver newInstance() {
			return new ReachablePointsSolver(7, 0.4f, 0.05f, ReachablePointsType.SINGLE_THREADED);
		}
	},
	GRAPH_AGGRESSIVE {
		@Override
		public ISpeedSolver newInstance() {
			return new ReachablePointsSolver(7, 0.4f, 0.01f, ReachablePointsType.GRAPH);
		}
	},
	GRAPH_DEFENSIVE {
		@Override
		public ISpeedSolver newInstance() {
			return new ReachablePointsSolver(7, 0.1f, 0.35f, ReachablePointsType.GRAPH);
		}
	},
	GRAPH_DYNAMIC {
		@Override
		public ISpeedSolver newInstance() {
			return new ReachablePointsSolver(7, -1f, -1f, ReachablePointsType.GRAPH);
		}
	},
	RANDOM {
		@Override
		public ISpeedSolver newInstance() {
			return new RandomSolver();
		}
	};

	/**
	 * Returns a new instance of the {@link ISpeedSolver} representing the
	 * {@link SolverType}.
	 * 
	 * @return {@link ISpeedSolver} representing the {@link SolverType}.
	 */
	public abstract ISpeedSolver newInstance();

	/**
	 * Returns the default {@link SolverType}, representing the {@link SolverType}
	 * for which most success can be suspected.
	 * 
	 * @return default {@link SolverType}
	 */
	public static SolverType getDefault() {
		return REACHABLE_POINTS;
	}

}
