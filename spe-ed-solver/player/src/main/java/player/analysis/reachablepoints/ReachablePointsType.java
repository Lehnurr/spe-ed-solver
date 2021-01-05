package player.analysis.reachablepoints;

import player.analysis.reachablepoints.multithreaded.ReachablePointsMultithreaded;
import player.analysis.reachablepoints.singlethreaded.ReachablePointsSingleThreaded;

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
	};

	/**
	 * Returns a new instance of {@link IReachablePoints} according to the
	 * {@link ReachablePointsType}.
	 * 
	 * @return {@link IReachablePoints} instance
	 */
	public abstract IReachablePoints newInstance();

}
