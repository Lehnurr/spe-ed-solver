package webcommunication.time;

import java.time.Duration;
import java.time.ZonedDateTime;

import utility.game.step.IDeadline;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;

/**
 * Responsible for the synchronization of server time and client time.
 */
public class TimeSynchronizationManager {

	private static final long BUFFER_NANOSECONDS = 500_000_000;

	private final Duration serverTimeOffset;
	private final Duration bufferDuration;

	/**
	 * Creates a new {@link TimeSynchronizationManager} which initially synchronizes
	 * the local client time with the server time. The created
	 * {@link TimeSynchronizationManager} can then be used to create
	 * {@link IDeadline Deadlines} which can be used on the client side.
	 * 
	 * @param timeApiClient {@link TimeAPIClient} to get the server time from
	 */
	public TimeSynchronizationManager(final TimeAPIClient timeApiClient) {

		final ZonedDateTime clientTime = ZonedDateTime.now();

		Duration serverTimeOffset;
		try {
			final ZonedDateTime serverTime = timeApiClient.getServerTime();
			serverTimeOffset = Duration.between(clientTime, serverTime);
		} catch (TimeRequestException e) {
			ApplicationLogger.logException(e, LoggingLevel.WARNING);
			ApplicationLogger
					.logWarning("The time API couldn't be reached. Running without synchronization from now on!");
			serverTimeOffset = Duration.ZERO;
		}

		final Duration requestDuration = Duration.between(clientTime, ZonedDateTime.now());
		this.bufferDuration = requestDuration.plusNanos(BUFFER_NANOSECONDS);
		this.serverTimeOffset = serverTimeOffset;

		ApplicationLogger.logInformation(String.format("Server response time: %d ms", requestDuration.toMillis()));
		ApplicationLogger.logInformation(String.format("Server time offset: %d ms", serverTimeOffset.toMillis()));
		ApplicationLogger.logInformation(String.format("Server time buffer: %d ms", bufferDuration.toMillis()));
	}

	/**
	 * Creates a new {@link TimeSynchronizationManager} without a connection to a
	 * {@link TimeAPIClient}. This results in an unsynchronized behavior and is not
	 * recommended.
	 */
	public TimeSynchronizationManager() {
		this.serverTimeOffset = Duration.ZERO;
		this.bufferDuration = Duration.ofNanos(BUFFER_NANOSECONDS);

		ApplicationLogger.logWarning("Running the client without synchronizing to the server time API!");
	}

	/**
	 * Creates a new {@link IDeadline} which is synchronized with the server.
	 * 
	 * @param deadlineTime {@link ZonedDateTime} of the deadline on the server
	 * @return {@link IDeadline} which can be used on the client
	 */
	public IDeadline createDeadline(final ZonedDateTime deadlineTime) {

		return new IDeadline() {

			final ZonedDateTime targetTime = deadlineTime.minus(serverTimeOffset).minus(bufferDuration);

			@Override
			public long getRemainingMilliseconds() {
				final ZonedDateTime now = ZonedDateTime.now();
				final Duration durationLeft = Duration.between(now, targetTime);
				return durationLeft.toMillis();
			}
		};
	}

}