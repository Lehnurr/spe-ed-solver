package player.analysis.reachablepoints;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Class for storing multiple elements in a buffer. The stored elements can be
 * accessed in a queue like structure. If the buffer is filled another element
 * gets replaced by the new one.
 * 
 * @param <Element> type of the elements stored in the buffer
 */
public class LimitedQueue<Element> {

	private static final int RANDOM_REPLACEMENT_SECTION_SIZE = 10;

	private final Random random;

	private Element[] buffer;

	private int readIndex = 0;
	private int writeIndex = 0;
	private int size = 0;

	/**
	 * Creates a new {@link LimitedQueue} of the given Class of a specific given
	 * size.
	 * 
	 * @param elementClass Class of the represented elements
	 * @param bufferSize   size of the underlying buffer
	 */
	@SuppressWarnings("unchecked")
	public LimitedQueue(final Class<?> elementClass, final int bufferSize) {
		this.buffer = (Element[]) Array.newInstance(elementClass, bufferSize);
		this.random = new Random();
	}

	/**
	 * Adds a value to the buffer. If the buffer is filled up another random element
	 * at the end of the queue gets replaced.
	 * 
	 * @param value value to add to the list
	 */
	public void add(final Element value) {
		if (!isFull()) {
			buffer[writeIndex] = value;
			writeIndex = (writeIndex + 1) % buffer.length;
			size++;
		} else {
			final int randomValue = random.nextInt(RANDOM_REPLACEMENT_SECTION_SIZE) + 1;
			final int randomWriteIndex = (writeIndex + (buffer.length - randomValue)) % buffer.length;
			buffer[randomWriteIndex] = value;
		}
	}

	/**
	 * Returns the next element without checking if the access is allowed.
	 * 
	 * @return
	 */
	private Element pollUnprotected() {
		final Element returnValue = buffer[readIndex];
		readIndex = (readIndex + 1) % buffer.length;
		size--;
		return returnValue;
	}

	/**
	 * Returns the next value of the {@link LimitedQueue}. If no next element is
	 * available a {@link NoSuchElementException} is thrown.
	 * 
	 * @return next value
	 */
	public Element poll() {
		if (!hasNext())
			throw new NoSuchElementException("Tried to poll from an empty list!");
		return pollUnprotected();
	}

	/**
	 * Determines if the {@link LimitedQueue} has a next value.
	 * 
	 * @return true if the {@link LimitedQueue} has a next value
	 */
	public boolean hasNext() {
		return size > 0;
	}

	/**
	 * Determines if the {@link LimitedQueue} is full.
	 * 
	 * @return true if the {@link LimitedQueue} is full
	 */
	public boolean isFull() {
		return size == buffer.length;
	}

}
