package core.parser;

import core.modes.SimulationMode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

/**
 * {@link Command} which runs a {@link SimulationMode} which simulates the game
 * spe_ed with the given command line arguments.
 */
@Command(name = "simulated",
		description = "Starts a game of spe_ed locally with the given players and simulates the game.")
public class PlaySimulationCommand implements Runnable {

	@Spec
	private CommandSpec spec;

	private int boardWidth;
	private int boardHeight;
	private int playerCount;

	@Option(names = { "-w", "--width" }, description = "The width of the game board.")
	public void setBoardWidth(final int boardWidth) {
		if (boardWidth <= 0) {
			throw new ParameterException(spec.commandLine(), "The board width must be > 0!");
		}
		this.boardWidth = boardWidth;
	}

	@Option(names = { "-h", "--height" }, description = "The height of the game board.")
	public void setBoardHeight(final int boardHeight) {
		if (boardHeight <= 0) {
			throw new ParameterException(spec.commandLine(), "The board height must be > 0!");
		}
		this.boardHeight = boardHeight;
	}

	@Option(names = { "-p", "--playerCount" }, description = "The amount of players to play the simulation with.")
	public void setPlayerCount(final int playerCount) {
		if (playerCount < 2) {
			throw new ParameterException(spec.commandLine(), "You need at least 2 players to simulate a game!");
		}
		if (playerCount > 6) {
			throw new ParameterException(spec.commandLine(), "You can't play spe_ed with more than 6 players!");
		}
		this.playerCount = playerCount;
	}
	
	@Override
	public void run() {
		new SimulationMode(boardHeight, boardWidth, playerCount).run();
	}

}
