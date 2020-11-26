package utility.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import utility.game.player.PlayerDirection;

public class EnumExtensionsTest {

    @Test
    public void testGetRandomValue() {
        var randomEnumValue = EnumExtensions.getRandomValue(PlayerDirection.class);
        var originalValue = PlayerDirection.values()[randomEnumValue.ordinal()];
        assertNotNull(randomEnumValue);
        assertEquals(randomEnumValue, originalValue);
    }
}
