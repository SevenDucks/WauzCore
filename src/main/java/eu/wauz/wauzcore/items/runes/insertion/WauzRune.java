package eu.wauz.wauzcore.items.runes.insertion;

import org.bukkit.inventory.ItemStack;

public interface WauzRune {
	
	public String getRuneId();
	
	public boolean insertInto(ItemStack equipmentItemStack, double runeMight);

}
