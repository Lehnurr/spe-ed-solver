package utility.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import utility.game.player.PlayerDirection;

public class EnumExtensionsTest {

    @Test
    public void testGetRandomValue() {
        final PlayerDirection randomEnumValue = EnumExtensions.getRandomValue(PlayerDirection.class);
        final PlayerDirection originalValue = PlayerDirection.values()[randomEnumValue.ordinal()];
        assertNotNull(randomEnumValue);
        assertEquals(randomEnumValue, originalValue);
    }
}
