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
 * Effect: Reflects damage (Main Stat) * (Might / 100) [x4 if Armor] to attackers.
 * 
 * @author Wauzmons
 *
 * @see WauzRune
 */
@Rune
public class RuneThorns implements WauzRune {

	/**
	 * The static name of the rune.
	 */
	public static String RUNE_NAME = "Thorns";

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
