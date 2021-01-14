package utility.extensions;

import java.util.Random;

/**
 * Generic Extension functions for Enums.
 */
public final class EnumExtensions {

	private EnumExtensions() {
	}

	/**
	 * Returns a random Enum-Value.
	 * 
	 * @param <EnumType> the type of the result
	 * @param enumClass  the class of the enum, from which a random value should be
	 *                   chosen
	 * @return a random Enum-Value of the given Enum-Class
	 */
	public static <EnumType extends Enum<EnumType>> EnumType getRandomValue(Class<EnumType> enumClass) {
		final EnumType[] enumValues = enumClass.getEnumConstants();

		if (enumValues == null)
			return null;

		final int randomIndex = new Random().nextInt(enumValues.length);
		return enumValues[randomIndex];
	}

	/**
	 * Returns the {@link Enum#ordinal() ordinal} next {@link Enum enum value}.
	 * Returns the {@link Enum#ordinal() ordinal} first {@link Enum enum value} for
	 * the {@link Enum#ordinal() ordinal} last {@link Enum enum value}
	 * 
	 * @param <EnumType> the type of the {@link Enum enum}
	 * @param enumValue  an {@link Enum enum} value for which a follower is searched
	 *                   for
	 * @return the {@link Enum#ordinal() ordinal} following {@link Enum enum value}
	 */
	public static <EnumType extends Enum<EnumType>> EnumType getNextValue(EnumType enumValue) {
		final EnumType[] enumValues = enumValue.getDeclaringClass().getEnumConstants();

		if (enumValues == null)
			return enumValue;

		final int nextOrdinalValue = (enumValue.ordinal() + 1) % enumValues.length;
		return enumValues[nextOrdinalValue];
	}
}