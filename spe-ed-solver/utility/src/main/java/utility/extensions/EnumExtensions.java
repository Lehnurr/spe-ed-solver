package utility.extensions;

import java.util.Random;

/**
 * EnumExtensions
 */
public final class EnumExtensions {

    private EnumExtensions() {
    }

    /**
     * Returns a random Enum-Value
     * 
     * @param enumClass The class of the enum, from which a random value should be
     *                  chosen
     * @return a random Enum-Value of the given Enum-Class
     */
    public static <EnumType extends Enum<EnumType>> EnumType getRandomValue(Class<EnumType> enumClass) {
        final EnumType[] enumValues = enumClass.getEnumConstants();

        if (enumValues == null)
            return null;

        final int randomIndex = new Random().nextInt(enumValues.length);
        return enumValues[randomIndex];
    }
}