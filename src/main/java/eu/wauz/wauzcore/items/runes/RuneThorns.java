package eu.wauz.wauzcore.items.runes;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.runes.insertion.WauzRune;

public class RuneThorns implements WauzRune {

	public static String RUNE_NAME = "Thorns";

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
