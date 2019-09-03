package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;

public class RuneKnowledge implements WauzRune {

	public static String RUNE_NAME = "Knowledge";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}

	@Override
	public boolean insertInto(ItemStack equipmentItemStack, double runeMight) {
		double bonusExp = runeMight * 50;
		if(bonusExp < 1) {
			bonusExp = 1;
		}

		String bounusLore = ChatColor.AQUA + "+" + (int) bonusExp + "% Exp";
		String socketLore = ChatColor.YELLOW + "Knowledge Rune ("+ bounusLore + ChatColor.YELLOW + ")";
		return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
	}

}
