package utility.game.step;

/**
 * An interface to specify how a requestable deadline from the spe-ed WebServer
 * have to be
 */
public interface IDeadline {

	/**
	 * The amount of milliseconds until the {@link IDeadline} is reached.
	 * 
	 * @return the amount of milliseconds (< 0 if the deadline exceeded)
	 */
	long getRemainingMilliseconds();

}
