package core;

import picocli.CommandLine;

public class Main {

	public static void main(String[] args) {
		new CommandLine(new CommandLineParser()).execute(args);
	}

}
