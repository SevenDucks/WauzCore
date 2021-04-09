package eu.wauz.wauzcore.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Ignore;
import org.junit.Test;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentBuilder;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.tests.abstracts.AbstractCoreTest;

/**
 * Tests the equipment generation and functionality from WauzCore.
 * 
 * @author Wauzmons
 */
@Ignore
public class EquipmentTest extends AbstractCoreTest {
	
	/**
	 * Tests that only higher rarity items can have rune sockets.
	 */
	@Test
	public void testRuneSockets() {
		WauzEquipmentBuilder builder = new WauzEquipmentBuilder(Material.WOODEN_SWORD);
		ItemStack normalItemStack = builder.generate(Tier.EQUIP_T1, Rarity.NORMAL, EquipmentType.WEAPON, "Test");
		assertFalse(EquipmentUtils.hasRuneSocket(normalItemStack));
		ItemStack magicItemStack = builder.generate(Tier.EQUIP_T1, Rarity.MAGIC, EquipmentType.WEAPON, "Test");
		assertTrue(EquipmentUtils.hasRuneSocket(magicItemStack));
		ItemStack uniqueItemStack = builder.generate(Tier.EQUIP_T3, Rarity.UNIQUE, EquipmentType.ARMOR, "Test");
		assertTrue(EquipmentUtils.hasRuneSocket(uniqueItemStack));
	}
	
}
