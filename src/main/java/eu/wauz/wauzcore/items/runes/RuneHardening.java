package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;

/**
 * A rune, that can be inserted into a piece of equipment, with a fitting slot.
 * Effect: Increases maximum durability by (Might * 24).
 * 
 * @author Wauzmons
 *
 * @see WauzRune
 */
public class RuneHardening implements WauzRune {

	/**
	 * The static name of the rune.
	 */
	public static String RUNE_NAME = "Hardening";

	/**
	 * @return The id of the rune.
	 */
	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}

	/**
	 * Tries to insert the rune into the given equipment item stack.
	 * 
	 * @param equipmentItemStack The equipment item stack that the rune is inserted into.
	 * @param equipmentType The type of the equipment.
	 * @param runeMightDecimal The might of the rune, determinig its power.
	 * 
	 * @return If the rune was inserted.
	 */
	@Override
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal) {
		int baseDurability = EquipmentUtils.getMaximumDurability(equipmentItemStack);
		double bonusDurability = runeMightDecimal * 100 * 24;
		if(bonusDurability < 1) {
			bonusDurability = 1;
		}
		EquipmentUtils.setMaximumDurability(equipmentItemStack, (int) (baseDurability + bonusDurability));
		
		String bonusLore = ChatColor.DARK_GREEN + "+" + (int) bonusDurability + " Dur";
		String socketLore = ChatColor.YELLOW + "Hardening Rune ("+ bonusLore + ChatColor.YELLOW + ")";
		return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
	}

}
