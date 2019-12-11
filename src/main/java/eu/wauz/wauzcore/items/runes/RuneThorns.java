package eu.wauz.wauzcore.items.runes;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;

public class RuneThorns implements WauzRune {

	public static String RUNE_NAME = "Thorns";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}
	
	private String bonusLore;

	@Override
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal) {
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			int baseAttack = EquipmentUtils.getBaseAtk(equipmentItemStack);
			double bonusReflection = baseAttack * runeMightDecimal + 1;
			bonusLore = ChatColor.GREEN + "+" + (int) bonusReflection + " Rfl";
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			int baseDefense = EquipmentUtils.getBaseDef(equipmentItemStack);
			double bonusReflection = baseDefense * runeMightDecimal * 4 + 1;
			bonusLore = ChatColor.GREEN + "+" + (int) bonusReflection + " Rfl";
		}
		
		if(StringUtils.isNotBlank(bonusLore)) {
			String socketLore = ChatColor.YELLOW + "Power Rune (" + bonusLore + ChatColor.YELLOW + ")";
			return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
		}
		return false;
	}

}
