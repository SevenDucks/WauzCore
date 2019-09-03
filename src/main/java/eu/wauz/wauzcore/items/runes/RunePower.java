package eu.wauz.wauzcore.items.runes;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.runes.insertion.WauzRune;

public class RunePower implements WauzRune {
	
	public static String RUNE_NAME = "Power";

	@Override
	public String getRuneId() {
		return RUNE_NAME;
	}

	@Override
	public boolean insertInto(ItemStack equipmentItemStack, double runeMight) {
		// TODO Auto-generated method stub
		return false;
	}

}
