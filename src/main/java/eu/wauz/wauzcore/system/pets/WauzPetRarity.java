package eu.wauz.wauzcore.system.pets;

import org.bukkit.ChatColor;

/**
 * The rarity of a pet, containing name, color and max stat multiplier.
 * 
 * @author Wauzmons
 */
public enum WauzPetRarity {
	
	/**
	 * The first rarity for pets "Normal", in green, with a max stat multiplier of 1.
	 */
	NORMAL("Normal", 1, ChatColor.GREEN),
	
	/**
	 * The second rarity for pets "Magic", in blue, with a max stat multiplier of 2.
	 */
	MAGIC("Magic", 2, ChatColor.BLUE),
	
	/**
	 * The third rarity for pets "Rare", in gold, with a max stat multiplier of 3.
	 */
	RARE("Rare", 3, ChatColor.GOLD),
	
	/**
	 * The fourth rarity for pets "Epic", in dark purple, with max stat a multiplier of 4.
	 */
	EPIC("Epic", 4, ChatColor.DARK_PURPLE),
	
	/**
	 * The fifth rarity for pets "Unique", in dark red, with a max stat multiplier of 5.
	 */
	UNIQUE("Unique", 5, ChatColor.DARK_RED);
	
	/**
	 * The name of the pet rarity.
	 */
	private final String name;
	
	/**
	 * The max stat multiplier of the pet rarity.
	 */
	private final int multiplier;
	
	/**
	 * The color of the pet rarity.
	 */
	private final ChatColor color;
	
	/**
	 * Creates a new pet rarity with given values.
	 * 
	 * @param name The name of the pet rarity.
	 * @param multiplier The max stat multiplier of the pet rarity.
	 * @param color The color of the pet rarity.
	 */
	WauzPetRarity(String name, int multiplier, ChatColor color) {
		this.name = name;
		this.multiplier = multiplier;
		this.color = color;
	}

	/**
	 * @return The name of the pet rarity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The max stat multiplier of the pet rarity.
	 */
	public int getMultiplier() {
		return multiplier;
	}

	/**
	 * @return The color of the pet rarity.
	 */
	public ChatColor getColor() {
		return color;
	}

}
