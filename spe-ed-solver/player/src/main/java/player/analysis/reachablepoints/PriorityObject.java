package player.analysis.reachablepoints;

public class PriorityObject<ValueClass> implements Comparable<PriorityObject<ValueClass>> {

	final float priority;
	final ValueClass value;

	public PriorityObject(float priority, ValueClass value) {
		super();
		this.priority = priority;
		this.value = value;
	}

	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}

	/**
	 * @return the value
	 */
	public ValueClass getValue() {
		return value;
	}

	@Override
	public int compareTo(final PriorityObject<ValueClass> other) {
		return Float.valueOf(priority).compareTo(getPriority());
	}

}
