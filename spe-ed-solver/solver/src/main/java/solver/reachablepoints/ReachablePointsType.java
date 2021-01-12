package solver.reachablepoints;

import solver.reachablepoints.graph.GraphCalculator;
import solver.reachablepoints.multithreaded.ReachablePointsMultithreaded;
import solver.reachablepoints.singlethreaded.ReachablePointsSingleThreaded;

/**
 * Enum describing multiple types of the {@link IReachablePoints} calculation.
 * Each type is able to provide a {@link IReachablePoints} instance.
 */
public enum ReachablePointsType {
	CLASSIC {
		@Override
		public IReachablePoints newInstance(final int maxThreadCount) {
			if (6 <= maxThreadCount)
				return new ReachablePointsMultithreaded();
			else
				return new ReachablePointsSingleThreaded();
		}
	},
	GRAPH {
		@Override
		public IReachablePoints newInstance(final int maxThreadCount) {
			return new GraphCalculator(maxThreadCount);
		}
	};

	/**
	 * Returns a new instance of {@link IReachablePoints} according to the
	 * {@link ReachablePointsType}.
	 * 
	 * @param maxThreadCount specifies the maximum number of concurrent threads to
	 *                       use
	 * @return {@link IReachablePoints} instance
	 */
	public abstract IReachablePoints newInstance(final int maxThreadCount);

}
