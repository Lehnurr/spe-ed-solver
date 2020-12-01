package core.parser;

import picocli.CommandLine.Command;

/**
 * The basic parser for command line argument parsing. Done by implementing a
 * {@link Command}. Implemented subcommands may exists for running the
 * application in another mode. <br>
 * By default the parser will redirect to the {@link PlayLiveCommand} which will
 * get executed.
 */
@Command(name = "start", subcommands = { PlayLiveCommand.class, PlaySimulationCommand.class })
public class CommandLineParser implements Runnable {

	@Override
	public void run() {
		PlayLiveCommand playLiveCommand = new PlayLiveCommand();
		playLiveCommand.run();
	}

}
