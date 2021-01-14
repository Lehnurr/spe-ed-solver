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

	private static final Duration MIN_EXPECTED_CALCULATION_TIME = Duration.ofSeconds(1);

	private static final int TIME_API_REQUESTS = 10;

	private Duration serverTimeOffset = Duration.ZERO;

	/**
	 * Creates a new {@link TimeSynchronizationManager} which initially synchronizes
	 * the local client time with the server time. The created
	 * {@link TimeSynchronizationManager} can then be used to create
	 * {@link IDeadline Deadlines} which can be used on the client side.
	 * 
	 * @param timeApiClient {@link TimeAPIClient} to get the server time from
	 */
	public TimeSynchronizationManager(final TimeAPIClient timeApiClient) {

		try {
			initializeOffsetAndBuffer(timeApiClient);
		} catch (TimeRequestException e) {
			ApplicationLogger.logException(e, LoggingLevel.WARNING);
			ApplicationLogger
					.logWarning("The time API couldn't be reached. Running without synchronization from now on!");
		}

		ApplicationLogger.logInformation(String.format("Server time offset: %d ms", serverTimeOffset.toMillis()));
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
	 * Initializes the time offset and buffer by sending requests multiple with the
	 * given {@link TimeAPIClient}. The response with the shortest response time is
	 * chosen to calculate the time offset and buffer.
	 * 
	 * @param timeApiClient the {@link TimeAPIClient} to send requests with
	 * @throws TimeRequestException thrown when no requests could be sent
	 */
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

		this.serverTimeOffset = minTimeOffset.plus(minRequestDuration);

		ApplicationLogger
				.logInformation(String.format("Minimum server response time: %d ms", minRequestDuration.toMillis()));
		ApplicationLogger.logInformation(String.format("Minimum server time offset: %d ms", minTimeOffset.toMillis()));
	}

	/**
	 * Synchronizes the client with the server with a given deadline. New
	 * Synchronizations are forced, when an impossible deadline has been received.
	 * 
	 * @param deadlineTime the {@link ZonedDateTime} of the deadline
	 */
	private void resync(final ZonedDateTime deadlineTime) {

		final ZonedDateTime clientDeadlineTime = deadlineTime.minus(serverTimeOffset);
		final ZonedDateTime minClientDeadlineTime = ZonedDateTime.now().plus(MIN_EXPECTED_CALCULATION_TIME);

		if (clientDeadlineTime.isBefore(minClientDeadlineTime)) {
			final Duration falseOffset = Duration.between(clientDeadlineTime, minClientDeadlineTime);
			this.serverTimeOffset = this.serverTimeOffset.minus(falseOffset);
			ApplicationLogger.logWarning(
					String.format("The server time offset had to be adjusted by %d ms!", falseOffset.toMillis()));
		}
	}

	/**
	 * Creates a new {@link IDeadline} which is synchronized with the server.
	 * 
	 * @param deadlineTime {@link ZonedDateTime} of the deadline on the server
	 * @return {@link IDeadline} which can be used on the client
	 */
	public IDeadline createDeadline(final ZonedDateTime deadlineTime) {

		resync(deadlineTime);

		return new IDeadline() {

			final ZonedDateTime targetTime = deadlineTime.minus(serverTimeOffset);

			@Override
			public long getRemainingMilliseconds() {
				final ZonedDateTime now = ZonedDateTime.now();
				final Duration durationLeft = Duration.between(now, targetTime);
				return durationLeft.toMillis();
			}
		};
	}

}