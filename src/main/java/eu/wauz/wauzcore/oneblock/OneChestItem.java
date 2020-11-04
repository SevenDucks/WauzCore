package eu.wauz.wauzcore.oneblock;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.system.util.Chance;

/**
 * An item that can be contained inside a reward chest of the one-block gamemode.
 * 
 * @author Wauzmons
 *
 * @see OneChest
 */
public class OneChestItem {
	
	/**
	 * The material of the item.
	 */
	private Material material;
	
	/**
	 * How common it is to find the item, relative to others.
	 */
	private int probability;
	
	/**
	 * The minimum stack size of the item.
	 */
	private int minAmount;
	
	/**
	 * The maximum stack size of the item.
	 */
	private int maxAmount;
	
	/**
	 * Creates an item, based on the given item string.
	 * Automatically makes it a possible reward inside the given chest.
	 * 
	 * @param chest The chest, the item can be found in.
	 * @param itemString The string, to parse the item stack from.
	 */
	public static void create(OneChest chest, String itemString) {
		OneChestItem item = new OneChestItem(itemString);
		for(int count = 0; count < item.getProbability(); count++) {
			chest.getContentItemStacks().add(item);
		}
	}
	
	/**
	 * Constructs an item of to put in a chest, based on an item string.
	 * 
	 * @param itemString The string, to parse the item stack from.
	 */
	private OneChestItem(String itemString) {
		String[] itemStringParts = itemString.split(" ");
		material = Material.valueOf(itemStringParts[0]);
		probability = Integer.parseInt(itemStringParts[1]);
		
		String[] itemAmountRange = itemStringParts[2].split("-");
		minAmount = Integer.parseInt(itemAmountRange[0]);
		maxAmount = Integer.parseInt(itemAmountRange[1]);
	}
	
	/**
	 * Creates a normal item stack from the chest item.
	 * 
	 * @return The item stack.
	 */
	public ItemStack generateItemStack() {
		int amount = Chance.minMax(minAmount, maxAmount);
		return new ItemStack(material, amount);
	}

	/**
	 * @return The material of the item.
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @return How common it is to find the item, relative to others.
	 */
	public int getProbability() {
		return probability;
	}

	/**
	 * @return The minimum stack size of the item.
	 */
	public int getMinAmount() {
		return minAmount;
	}

	/**
	 * @return The maximum stack size of the item.
	 */
	public int getMaxAmount() {
		return maxAmount;
	}

}
