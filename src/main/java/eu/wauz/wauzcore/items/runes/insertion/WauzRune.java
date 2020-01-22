package eu.wauz.wauzcore.items.runes.insertion;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.EquipmentType;

/**
 * A rune, that can be inserted into a piece of equipment, with a fitting slot.
 * 
 * @author Wauzmons
 *
 * @see WauzRuneInserter
 */
public interface WauzRune {
	
	/**
	 * @return The id of the rune.
	 */
	public String getRuneId();
	
	/**
	 * Tries to insert the rune into the given equipment item stack.
	 * 
	 * @param equipmentItemStack The equipment item stack that the rune is inserted into.
	 * @param equipmentType The type of the equipment.
	 * @param runeMightDecimal The might of the rune, determinig its power.
	 * 
	 * @return If the rune was inserted.
	 */
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal);

}
