package solver;

import solver.random.RandomSolver;
import solver.reachablepoints.ReachablePointsSolver;
import solver.reachablepoints.ReachablePointsType;

/**
 * {@link SolverType} enum representing multiple types of spe_ed solvers. Each
 * type can generate a {@link ISpeedSolver} instance.
 */
public enum SolverType {

	CLASSIC_AGGRESSIVE {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new ReachablePointsSolver(6, 0.5f, 0.01f, ReachablePointsType.CLASSIC, maxThreadCount);
		}
	},
	CLASSIC_BALANCED {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new ReachablePointsSolver(6, 0.4f, 0.05f, ReachablePointsType.CLASSIC, maxThreadCount);
		}
	},
	CLASSIC_DEFENSIVE {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new ReachablePointsSolver(6, 0.1f, 0.35f, ReachablePointsType.CLASSIC, maxThreadCount);
		}
	},
	GRAPH_AGGRESSIVE {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new ReachablePointsSolver(6, 0.5f, 0.01f, ReachablePointsType.GRAPH, maxThreadCount);
		}
	},
	GRAPH_BALANCED {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new ReachablePointsSolver(6, 0.35f, 0.06f, ReachablePointsType.GRAPH, maxThreadCount);
		}
	},
	GRAPH_DEFENSIVE {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new ReachablePointsSolver(6, 0.1f, 0.35f, ReachablePointsType.GRAPH, maxThreadCount);
		}
	},
	RANDOM {
		@Override
		public ISpeedSolver newInstance(final int maxThreadCount) {
			return new RandomSolver();
		}
	};

	/**
	 * Returns a new instance of the {@link ISpeedSolver} representing the
	 * {@link SolverType}.
	 * 
	 * @param maxThreadCount specifies the maximum number of concurrent threads to
	 *                       use
	 * 
	 * @return {@link ISpeedSolver} representing the {@link SolverType}.
	 */
	public abstract ISpeedSolver newInstance(final int maxThreadCount);

	/**
	 * Returns the default {@link SolverType}, representing the {@link SolverType}
	 * for which most success can be suspected.
	 * 
	 * @return default {@link SolverType}
	 */
	public static SolverType getDefault() {
		return GRAPH_BALANCED;
	}

}
