package webcommunication.time;

import java.time.Duration;
import java.time.ZonedDateTime;

import utility.game.step.Deadline;

/**
 * Responsible for the synchronization of server time and client time.
 */
public class TimeSynchronizationManager {

	private final Duration serverTimeOffset;

	/**
	 * Creates a new {@link TimeSynchronizationManager} which initially synchronizes
	 * the local client time with the server time. The created
	 * {@link TimeSynchronizationManager} can then be used to create {@link Deadline
	 * Deadlines} which can be used on the client side.
	 * 
	 * @param timeApiClient {@link TimeAPIClient} to get the server time from
	 * @throws TimeRequestException
	 */
	public TimeSynchronizationManager(final TimeAPIClient timeApiClient) {

		final ZonedDateTime clientTime = ZonedDateTime.now();

		Duration serverTimeOffset;
		try {
			ZonedDateTime serverTime = timeApiClient.getServerTime();
			serverTimeOffset = Duration.between(clientTime, serverTime);
		} catch (TimeRequestException e) {
			// TODO LOGGING WARNING
			serverTimeOffset = Duration.ZERO;
		}
		this.serverTimeOffset = serverTimeOffset;
	}

	/**
	 * Creates a new {@link TimeSynchronizationManager} without a connection to a
	 * {@link TimeAPIClient}. This results in an unsynchronized behavior and is not
	 * recommended.
	 */
	public TimeSynchronizationManager() {
		this.serverTimeOffset = Duration.ZERO;
	}

	/**
	 * Creates a new {@link Deadline} which is synchronized with the server.
	 * 
	 * @param deadlineTime {@link ZonedDateTime} of the deadline on the server
	 * @return {@link Deadline} which can be used on the client
	 */
	public Deadline createDeadline(final ZonedDateTime deadlineTime) {

		return new Deadline() {

			final ZonedDateTime targetTime = deadlineTime.plus(serverTimeOffset);

			@Override
			public long getRemainingMilliseconds() {
				final ZonedDateTime now = ZonedDateTime.now();
				final Duration durationLeft = Duration.between(now, targetTime);
				return durationLeft.toMillis();
			}
		};
	}

}