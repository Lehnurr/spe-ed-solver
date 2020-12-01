package core.modes;

/**
 * {@link Runnable} for the live play mode to play spe_ed in an offline
 * simulation of the game.
 */
public class SimulationMode implements Runnable {

	/**
	 * Creates a new {@link Runnable} for the live play mode to play spe_ed offline
	 * in an simulation. The constructor is used to set starting parameters.
	 */
	public SimulationMode() {
	}

	@Override
	public void run() {
		System.out.println("RUNNING SIMULATED");
	}

}
