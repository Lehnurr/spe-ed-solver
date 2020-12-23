package player.analysis.enemyprobability;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.game.player.PlayerAction;

public class PathDescriptorTest {

	@Test
	public void createEmtpyTest() {
		PathDescriptor testDescriptor = new PathDescriptor(2);
		assertEquals(2, testDescriptor.getIntegerValue());
		assertEquals(0, testDescriptor.getDepth());
		assertEquals(0b111, testDescriptor.getMask());
	}

	@Test
	public void appendTest() {
		PathDescriptor baseDescriptor = new PathDescriptor(2);

		PathDescriptor alteredDescriptor = baseDescriptor.append(PlayerAction.CHANGE_NOTHING);

		assertEquals(0b10 + ((PlayerAction.CHANGE_NOTHING.ordinal() + 1) << 3), alteredDescriptor.getIntegerValue());
		assertEquals(1, alteredDescriptor.getDepth());
		assertEquals(0b111111, alteredDescriptor.getMask());
	}

	@Test
	public void dependsOnTest() {
		PathDescriptor baseDescriptor = new PathDescriptor(2);
		PathDescriptor descriptorStep1 = baseDescriptor.append(PlayerAction.CHANGE_NOTHING);
		PathDescriptor descriptorStep2 = descriptorStep1.append(PlayerAction.TURN_LEFT);
		PathDescriptor descriptorStep3 = descriptorStep2.append(PlayerAction.SPEED_UP);
		PathDescriptor descriptorStep4 = descriptorStep3.append(PlayerAction.TURN_RIGHT);

		assertEquals(true, descriptorStep4.dependsOn(descriptorStep3));
		assertEquals(true, descriptorStep3.dependsOn(descriptorStep3));
		assertEquals(false, descriptorStep2.dependsOn(descriptorStep3));
	}

	@Test
	public void upgradesTest() {
		PathDescriptor baseDescriptor = new PathDescriptor(2);
		PathDescriptor descriptorStep1 = baseDescriptor.append(PlayerAction.CHANGE_NOTHING);
		PathDescriptor descriptorStep2 = descriptorStep1.append(PlayerAction.TURN_LEFT);
		PathDescriptor descriptorStep3 = descriptorStep2.append(PlayerAction.SPEED_UP);
		PathDescriptor descriptorStep4 = descriptorStep3.append(PlayerAction.TURN_RIGHT);

		assertEquals(false, descriptorStep4.upgrades(descriptorStep3));
		assertEquals(false, descriptorStep3.upgrades(descriptorStep3));
		assertEquals(true, descriptorStep2.upgrades(descriptorStep3));
	}

}
