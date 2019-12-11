package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.util.ItemUtils;

public class RuneKnowledge implements WauzRune {

	public static String RUNE_NAME = "Knowledge";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}

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
