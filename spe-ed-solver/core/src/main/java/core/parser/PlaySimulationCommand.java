package core.parser;

import core.modes.SimulationMode;
import picocli.CommandLine.Command;

/**
 * {@link Command} which runs a {@link SimulationMode} which simulates the game
 * spe_ed with the given command line arguments.
 */
@Command(name = "simulated",
		description = "Starts a game of spe_ed locally with the given players and simulates the game.")
public class PlaySimulationCommand implements Runnable {

	@Override
	public void run() {
		new SimulationMode().run();
	}

}
