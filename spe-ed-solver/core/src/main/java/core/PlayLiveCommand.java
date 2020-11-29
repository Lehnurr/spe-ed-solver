package core;

import picocli.CommandLine.Command;

/**
 * {@link Command} which runs the application in the live mode. Thereby
 * connecting to the webservice with the given environment variables. The game
 * spe_ed is played with a single player instance.<br>
 * This {@link Command} is set as default by the {@link CommandLineParser} and
 * will get executed automatically if no arguments were given to the
 * application. Therefore fulfilling the requirements to be able to run in the
 * desired docker container.
 */
@Command(name = "live", description = "Starts a game of spe_ed on the given webservice with one player instance.")
public class PlayLiveCommand implements Runnable {

	@Override
	public void run() {
		// TODO everything
	}

}
