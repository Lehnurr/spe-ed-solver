package player.analysis.reachablepoints;

import player.analysis.reachablepoints.multithreaded.ReachablePointsMultithreaded;
import player.analysis.reachablepoints.singlethreaded.ReachablePointsSingleThreaded;

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

	public abstract IReachablePoints newInstance();

}
