package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;

public class RuneHardening implements WauzRune {

	public static String RUNE_NAME = "Hardening";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}

	@Override
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal) {
		int baseDurability = ItemUtils.getMaximumDurability(equipmentItemStack);
		double bonusDurability = runeMightDecimal * 100 * 12;
		if(bonusDurability < 1) {
			bonusDurability = 1;
		}
		
		int maxDurability = (int) (baseDurability + bonusDurability);
		String oldDurabilityLore = "Durability:" + ChatColor.DARK_GREEN + " ";
		String newDurabilityLore = "Durability:" + ChatColor.DARK_GREEN + " " + maxDurability;
		newDurabilityLore += " " + ChatColor.DARK_GRAY + "/ " + maxDurability;
		ItemUtils.replaceStringFromLore(equipmentItemStack, oldDurabilityLore, newDurabilityLore);
		
		Damageable damageable = (Damageable) equipmentItemStack.getItemMeta();
		damageable.setDamage(0);
		equipmentItemStack.setItemMeta((ItemMeta) damageable);

		String bonusLore = ChatColor.DARK_GREEN + "+" + (int) bonusDurability + " Dur";
		String socketLore = ChatColor.YELLOW + "Hardening Rune ("+ bonusLore + ChatColor.YELLOW + ")";
		return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
	}

}
