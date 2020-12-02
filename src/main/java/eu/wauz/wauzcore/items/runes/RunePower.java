package eu.wauz.wauzcore.items.runes;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.annotations.Rune;

/**
 * A rune, that can be inserted into a piece of equipment, with a fitting slot.
 * Effect: Increases the main stat by (Main Stat) * (Might / 100).
 * 
 * @author Wauzmons
 *
 * @see WauzRune
 */
@Rune
public class RunePower implements WauzRune {
	
	/**
	 * The static name of the rune.
	 */
	public static String RUNE_NAME = "Power";

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
		String bonusLore = null;
		
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			int baseAttack = EquipmentUtils.getBaseAtk(equipmentItemStack);
			double bonusAttack = baseAttack * runeMightDecimal + 1;
			bonusLore = ChatColor.RED + "+" + (int) bonusAttack + " Atk";
			EquipmentUtils.setBaseAtk(equipmentItemStack, (int) (baseAttack + bonusAttack));
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			int baseDefense = EquipmentUtils.getBaseDef(equipmentItemStack);
			double bonusDefense = baseDefense * runeMightDecimal + 1;
			bonusLore = ChatColor.BLUE + "+" + (int) bonusDefense + " Def";
			EquipmentUtils.setBaseDef(equipmentItemStack, (int) (baseDefense + bonusDefense));
		}
		
		if(StringUtils.isNoneBlank(bonusLore)) {
			String socketLore = ChatColor.YELLOW + "Power Rune (" + bonusLore + ChatColor.YELLOW + ")";
			return ItemUtils.replaceStringFromLore(equipmentItemStack, ChatColor.GREEN + "Empty", socketLore);
		}
		return false;
	}

}
