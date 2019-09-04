package eu.wauz.wauzcore.items.runes;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;

public class RunePower implements WauzRune {
	
	public static String RUNE_NAME = "Power";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}
	
	private String bonusLore;

	@Override
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal) {
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			int baseAttack = ItemUtils.getBaseAtk(equipmentItemStack);
			double bonusAttack = baseAttack * runeMightDecimal + 1;
			bonusLore = ChatColor.RED + "+" + (int) bonusAttack + " Atk";
			
			String oldAttackLore = "Attack:" + ChatColor.RED + " " + baseAttack;
			String newAttackLore = "Attack:" + ChatColor.RED + " " + ((int) (baseAttack + bonusAttack));
			ItemUtils.replaceStringFromLore(equipmentItemStack, oldAttackLore, newAttackLore);
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			int baseDefense = ItemUtils.getBaseDef(equipmentItemStack);
			double bonusDefense = baseDefense * runeMightDecimal + 1;
			bonusLore = ChatColor.BLUE + "+" + (int) bonusDefense + " Def";
			
			String oldDefenseLore = "Defense:" + ChatColor.BLUE + " " + baseDefense;
			String newDefenseLore = "Defense:" + ChatColor.BLUE + " " + ((int) (baseDefense + bonusDefense));
			ItemUtils.replaceStringFromLore(equipmentItemStack, oldDefenseLore, newDefenseLore);
		}
		
		if(StringUtils.isNoneBlank(bonusLore)) {
			String socketLore = ChatColor.YELLOW + "Power Rune (" + bonusLore + ChatColor.YELLOW + ")";
			return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
		}
		return false;
	}

}
