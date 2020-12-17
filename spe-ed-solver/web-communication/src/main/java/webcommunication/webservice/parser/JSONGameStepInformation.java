package webcommunication.webservice.parser;

import java.util.Date;
import java.util.Map;

/**
 * Class representing the JSON Object sent to each spe_ed client in a single
 * game step.
 */
public class JSONGameStepInformation {

	public int width;
	public int height;

	public int[][] cells;
	
	public Map<String, JSONGameStepPlayer> players;

	public int you;

	public boolean running;

	public Date deadline;

}
