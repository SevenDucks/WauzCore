package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * The rarity of a pet, containing name, color and max stat multiplier.
 * 
 * @author Wauzmons
 */
public enum WauzPetRarity {
	
	/**
	 * The first rarity for pets "Normal", in green, with a max stat multiplier of 1.
	 */
	NORMAL("Normal", 1, ChatColor.GREEN, Material.SLIME_SPAWN_EGG),
	
	/**
	 * The second rarity for pets "Magic", in blue, with a max stat multiplier of 2.
	 */
	MAGIC("Magic", 2, ChatColor.BLUE, Material.SQUID_SPAWN_EGG),
	
	/**
	 * The third rarity for pets "Rare", in gold, with a max stat multiplier of 3.
	 */
	RARE("Rare", 3, ChatColor.GOLD, Material.BLAZE_SPAWN_EGG),
	
	/**
	 * The fourth rarity for pets "Epic", in dark purple, with max stat a multiplier of 4.
	 */
	EPIC("Epic", 4, ChatColor.DARK_PURPLE, Material.SHULKER_SPAWN_EGG),
	
	/**
	 * The fifth rarity for pets "Unique", in dark red, with a max stat multiplier of 5.
	 */
	UNIQUE("Unique", 5, ChatColor.DARK_RED, Material.MOOSHROOM_SPAWN_EGG);
	
	/**
	 * The name of the pet rarity.
	 */
	private final String name;
	
	/**
	 * The max stat multiplier of the pet rarity. Also acts as star count.
	 */
	private final int multiplier;
	
	/**
	 * The stars of the pet rarity.
	 */
	private final String rarityStars;
	
	/**
	 * The color of the pet rarity.
	 */
	private final ChatColor color;
	
	/**
	 * The spawn egg material of the pet rarity.
	 */
	private final Material material;
	
	/**
	 * Creates a new pet rarity with given values.
	 * 
	 * @param name The name of the pet rarity.
	 * @param multiplier The max stat multiplier of the pet rarity. Also acts as star count.
	 * @param color The color of the pet rarity.
	 * @param material The spawn egg material of the pet rarity.
	 */
	WauzPetRarity(String name, int multiplier, ChatColor color, Material material) {
		this.name = name;
		this.multiplier = multiplier;
		String rarityStars = "";
		for(int index = 0; index < 5; index++) {
			rarityStars += UnicodeUtils.ICON_DIAMOND;
			if(index == multiplier - 1) {
				rarityStars += ChatColor.GRAY;
			}
		}
		this.rarityStars = rarityStars;
		this.color = color;
		this.material = material;
	}

	/**
	 * @return The name of the pet rarity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The max stat multiplier of the pet rarity. Also acts as star count.
	 */
	public int getMultiplier() {
		return multiplier;
	}
	
	/**
	 * @return The stars of the pet rarity.
	 */
	public String getStars() {
		return rarityStars;
	}


	/**
	 * @return The color of the pet rarity.
	 */
	public ChatColor getColor() {
		return color;
	}

	/**
	 * @return The spawn egg material of the pet rarity.
	 */
	public Material getMaterial() {
		return material;
	}

}
