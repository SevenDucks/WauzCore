package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;

public class RuneHardening implements WauzRune {

	public static String RUNE_NAME = "Hardening";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}

	@Override
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal) {
		int baseDurability = EquipmentUtils.getMaximumDurability(equipmentItemStack);
		double bonusDurability = runeMightDecimal * 100 * 12;
		if(bonusDurability < 1) {
			bonusDurability = 1;
		}
		EquipmentUtils.setMaximumDurability(equipmentItemStack, (int) (baseDurability + bonusDurability));
		
		String bonusLore = ChatColor.DARK_GREEN + "+" + (int) bonusDurability + " Dur";
		String socketLore = ChatColor.YELLOW + "Hardening Rune ("+ bonusLore + ChatColor.YELLOW + ")";
		return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
	}

}
