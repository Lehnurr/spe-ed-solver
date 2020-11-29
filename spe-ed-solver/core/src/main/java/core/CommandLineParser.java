package core;

import picocli.CommandLine.Command;

/**
 * The basic parser for command line argument parsing. Done by implementing a
 * {@link Command}. Implemented subcommands may exists for running the
 * application in another mode. <br>
 * By default the parser will redirect to the {@link PlayLiveCommand} which will
 * get executed.
 */
@Command(subcommands = { PlayLiveCommand.class })
public class CommandLineParser implements Runnable {

	@Override
	public void run() {
		// redirect to play live command as default for handling empty requests
		PlayLiveCommand playLiveCommand = new PlayLiveCommand();
		playLiveCommand.run();
	}

}
