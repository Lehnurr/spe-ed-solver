package core.modes;

/**
 * {@link Runnable} for the live play mode to play spe_ed on an online
 * webservice.
 */
public class LiveMode implements Runnable {

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed on an
	 * online webservice. The constructor is used to set starting parameters.
	 */
	public LiveMode() {
	}

	@Override
	public void run() {
		System.out.println("RUNNING LIVE");
	}

}
