package player.analysis.enemyprobability;

import utility.game.player.PlayerAction;

/**
 * Description for a certain path an enemy might take. The size is limited with
 * 32 bit available per descriptor.
 */
public class PathDescriptor {

	private static final int BITS_PER_FRAGMENT = 3;
	private static final int FRAGMENT_BIT_MASK = 0b111;

	private final int integerDescriptor;
	private final int depth;
	private final int mask;

	/**
	 * Generates a new {@link PathDescriptor} out of the given playerId.
	 * 
	 * @param playerId the unique id of the player (1-7)
	 */
	public PathDescriptor(final int playerId) {
		this.integerDescriptor = playerId;
		this.depth = 0;
		this.mask = FRAGMENT_BIT_MASK;
	}

	/**
	 * Internal constructor for a {@link PathDescriptor} which is depending on an
	 * already existing {@link PathDescriptor}.
	 * 
	 * @param integerDescriptor
	 * @param depth
	 * @param mask
	 */
	private PathDescriptor(final int integerDescriptor, final int depth, final int mask) {
		this.integerDescriptor = integerDescriptor;
		this.depth = depth;
		this.mask = mask;
	}

	/**
	 * Creates a new {@link PathDescriptor} which depends on the current
	 * {@link PathDescriptor} and adds the additional given action by its id.
	 * 
	 * @param actionId unique id for a action (1-7)
	 * @return the created {@link PathDescriptor}
	 */
	public PathDescriptor append(final int actionId) {
		final int newDepth = depth + 1;
		final int newDescriptor = (actionId << (BITS_PER_FRAGMENT * newDepth)) & integerDescriptor;
		final int newMask = (mask << BITS_PER_FRAGMENT) + FRAGMENT_BIT_MASK;

		return new PathDescriptor(newDescriptor, newMask, newDepth);
	}

	/**
	 * Creates a new {@link PathDescriptor} which depends on the current
	 * {@link PathDescriptor} and adds the additional given {@link PlayerAction}.
	 * 
	 * @param playerAction {@link PlayerAction} to append
	 * @return the created {@link PathDescriptor}
	 */
	public PathDescriptor append(final PlayerAction playerAction) {
		return this.append(playerAction.ordinal() + 1);
	}

	/**
	 * Returns the integer value of the internal descriptor.
	 * 
	 * @return value of the internal descriptor
	 */
	public int getIntegerValue() {
		return integerDescriptor;
	}

	/**
	 * Returns the mask of the relevant part of the integer descriptor.
	 * 
	 * @return bit mask
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * Returns the depth of the descriptor.
	 * 
	 * @return depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Returns true if the given {@link PathDescriptor} depends on this
	 * {@link PathDescriptor} or if both {@link PathDescriptor} objects point are
	 * equal.
	 * 
	 * @param other {@link PathDescriptor} to compare with
	 * @return result of the calculation
	 */
	public boolean dependsOn(final PathDescriptor other) {
		return (this.getIntegerValue() & other.getMask()) == other.getIntegerValue();
	}

	/**
	 * Returns true if the given {@link PathDescriptor} upgrades the current
	 * {@link PathDescriptor}.
	 * 
	 * @param other {@link PathDescriptor} to compare with
	 * @return result of the calculation
	 */
	public boolean upgrades(final PathDescriptor other) {
		return this.getIntegerValue() == (other.getIntegerValue() & this.getMask()) && this.getMask() > other.getMask();
	}

}
