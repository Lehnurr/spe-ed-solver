package webcommunication.time;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Iterator;

import utility.game.step.IDeadline;
import utility.logging.ApplicationLogger;
import utility.logging.LoggingLevel;

/**
 * Responsible for the synchronization of server time and client time.
 */
public class TimeSynchronizationManager {

	private static final long BUFFER_NANOSECONDS = 500_000_000;

	private static final int TIME_API_REQUESTS = 10;

	private Duration serverTimeOffset = Duration.ZERO;
	private Duration bufferDuration = Duration.ofNanos(BUFFER_NANOSECONDS);

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

		try {
			initializeOffsetAndBuffer(timeApiClient);
		} catch (TimeRequestException e) {
			this.serverTimeOffset = Duration.ZERO;
			this.bufferDuration = Duration.ofNanos(BUFFER_NANOSECONDS);
			
			ApplicationLogger.logException(e, LoggingLevel.WARNING);
			ApplicationLogger
					.logWarning("The time API couldn't be reached. Running without synchronization from now on!");
		}

		ApplicationLogger.logInformation(String.format("Server time offset: %d ms", serverTimeOffset.toMillis()));
		ApplicationLogger.logInformation(String.format("Server time buffer: %d ms", bufferDuration.toMillis()));
	}

	private void initializeOffsetAndBuffer(final TimeAPIClient timeApiClient) throws TimeRequestException {

		Duration minRequestDuration = Duration.ofDays(1);
		Duration minTimeOffset = Duration.ZERO;

		for (int i = 0; i < TIME_API_REQUESTS; i++) {
			final ZonedDateTime clientTime = ZonedDateTime.now();
			final ZonedDateTime serverTime = timeApiClient.getServerTime();
			final Duration requestDuration = Duration.between(clientTime, ZonedDateTime.now());
			if (requestDuration.compareTo(minRequestDuration) < 0) {
				minRequestDuration = requestDuration;
				minTimeOffset = Duration.between(clientTime, serverTime);
			}
		}

		this.serverTimeOffset = minTimeOffset;
		this.bufferDuration = minRequestDuration.plusNanos(BUFFER_NANOSECONDS);

		ApplicationLogger
				.logInformation(String.format("Minimum server response time: %d ms", minRequestDuration.toMillis()));
	}

	/**
	 * Creates a new {@link TimeSynchronizationManager} without a connection to a
	 * {@link TimeAPIClient}. This results in an unsynchronized behavior and is not
	 * recommended.
	 */
	public TimeSynchronizationManager() {
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