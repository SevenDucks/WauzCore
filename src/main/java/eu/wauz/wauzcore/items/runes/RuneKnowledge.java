package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.annotations.Rune;

/**
 * A rune, that can be inserted into a piece of equipment, with a fitting slot.
 * Effect: Increases the EXP gain by (Might / 2) %.
 * 
 * @author Wauzmons
 *
 * @see WauzRune
 */
@Rune
public class RuneKnowledge implements WauzRune {

	/**
	 * The static name of the rune.
	 */
	public static String RUNE_NAME = "Knowledge";

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
		double bonusExp = runeMightDecimal * 50;
		if(bonusExp < 1) {
			bonusExp = 1;
		}

		String bonusLore = ChatColor.AQUA + "+" + (int) bonusExp + "% Exp";
		String socketLore = ChatColor.YELLOW + "Knowledge Rune ("+ bonusLore + ChatColor.YELLOW + ")";
		return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
	}

}
