package eu.wauz.wauzcore.items.runes.insertion;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.EquipmentType;

public interface WauzRune {
	
	public String getRuneId();
	
	public boolean insertInto(ItemStack equipmentItemStack, EquipmentType equipmentType, double runeMightDecimal);

}
