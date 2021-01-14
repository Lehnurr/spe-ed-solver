package utility.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;

public class EnumExtensionsTest {

    @Test
    public void testGetRandomValue() {
        final PlayerDirection randomEnumValue = EnumExtensions.getRandomValue(PlayerDirection.class);
        final PlayerDirection originalValue = PlayerDirection.values()[randomEnumValue.ordinal()];
        assertNotNull(randomEnumValue);
        assertEquals(randomEnumValue, originalValue);
    }

    @Test
    public void testGetNextValue() {

        final PlayerAction firstAction = PlayerAction.values()[0];
        final PlayerAction secondAction = PlayerAction.values()[1];
        final PlayerAction lastAction = PlayerAction.values()[4];

        final PlayerAction firstNextEnumValue = EnumExtensions.getNextValue(firstAction);
        assertEquals(secondAction, firstNextEnumValue);

        final PlayerAction lastNextEnumValue = EnumExtensions.getNextValue(lastAction);
        assertEquals(firstAction, lastNextEnumValue);
    }
}
