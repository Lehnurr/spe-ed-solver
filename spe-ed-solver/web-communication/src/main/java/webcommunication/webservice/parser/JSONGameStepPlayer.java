package webcommunication.webservice.parser;

/**
 * Class representing a part of a {@link JSONGameStepInformation} object,
 * containing player specific information about each enemy and yourself.
 */
public class JSONGameStepPlayer {
	
	public int x;
	public int y;
	
	public String direction;
	public int speed;
	
	public boolean active;

}
