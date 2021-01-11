package solver.reachablepoints;

import solver.reachablepoints.graph.GraphCalculator;
import solver.reachablepoints.multithreaded.ReachablePointsMultithreaded;
import solver.reachablepoints.singlethreaded.ReachablePointsSingleThreaded;

/**
 * Enum describing multiple types of the {@link IReachablePoints} calculation.
 * Each type is able to provide a {@link IReachablePoints} instance.
 */
public enum ReachablePointsType {

	MULTI_THREADED {
		@Override
		public IReachablePoints newInstance() {
			return new ReachablePointsMultithreaded();
		}
	},
	SINGLE_THREADED {
		@Override
		public IReachablePoints newInstance() {
			return new ReachablePointsSingleThreaded();
		}
	},
	GRAPH {
		@Override
		public IReachablePoints newInstance() {
			return new GraphCalculator();
		}
	};

	/**
	 * Returns a new instance of {@link IReachablePoints} according to the
	 * {@link ReachablePointsType}.
	 * 
	 * @return {@link IReachablePoints} instance
	 */
	public abstract IReachablePoints newInstance();

}
