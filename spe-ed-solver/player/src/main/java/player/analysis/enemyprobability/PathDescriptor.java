package player.analysis.enemyprobability;

import utility.game.player.PlayerAction;

/**
 * Description for a certain path an enemy might take. The size is limited with
 * 32 bit available per descriptor.
 */
public class PathDescriptor {

	private static final int BITS_PER_FRAGMENT = 3;

	private final int integerDescriptor;
	private final int bitOffset;

	/**
	 * Generates a new {@link PathDescriptor} out of the given playerId.
	 * 
	 * @param playerId the unique id of the player (1-7)
	 */
	public PathDescriptor(final int playerId) {
		this.integerDescriptor = playerId;
		this.bitOffset = BITS_PER_FRAGMENT;
	}

	/**
	 * Internal constructor for a {@link PathDescriptor} which is depending on an
	 * already existing {@link PathDescriptor}.
	 * 
	 * @param integerDescriptor
	 * @param bitOffset
	 */
	private PathDescriptor(final int integerDescriptor, final int bitOffset) {
		this.integerDescriptor = integerDescriptor;
		this.bitOffset = bitOffset;
	}

	/**
	 * Creates a new {@link PathDescriptor} which depends on the current
	 * {@link PathDescriptor} and adds the additional given action by its id.
	 * 
	 * @param actionId unique id for a action (1-7)
	 * @return the created {@link PathDescriptor}
	 */
	public PathDescriptor append(final int actionId) {
		final int newDescriptor = (actionId << bitOffset) & integerDescriptor;
		final int newOffset = bitOffset + BITS_PER_FRAGMENT;
		return new PathDescriptor(newDescriptor, newOffset);
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
	 * Returns the offset in number of bits.
	 * 
	 * @return offset in the internal descriptor
	 */
	public int getBitOffset() {
		return bitOffset;
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
		return (this.getIntegerValue() & other.getBitOffset()) == other.getIntegerValue();
	}

	/**
	 * Returns true if the given {@link PathDescriptor} upgrades the current
	 * {@link PathDescriptor}.
	 * 
	 * @param other {@link PathDescriptor} to compare with
	 * @return result of the calculation
	 */
	public boolean upgrades(final PathDescriptor other) {
		return this.getIntegerValue() == (other.getIntegerValue() & this.getBitOffset())
				&& this.getBitOffset() > other.getBitOffset();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bitOffset;
		result = prime * result + integerDescriptor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathDescriptor other = (PathDescriptor) obj;
		if (bitOffset != other.bitOffset)
			return false;
		if (integerDescriptor != other.integerDescriptor)
			return false;
		return true;
	}

}
