package eu.wauz.wauzcore.items.enums;

import java.util.Random;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * The rarity of an equipment or rune item, containing name, color, multiplier and star string.
 * 
 * @author Wauzmons
 */
public enum Rarity {
	
	/**
	 * The first rarity for equipment items "Normal", in green, with a multiplier of 1.00.
	 */
	NORMAL("Normal", 1, 5, ChatColor.GREEN, 1.00),
	
	/**
	 * The second rarity for equipment items "Magic", in blue, with a multiplier of 1.50.
	 */
	MAGIC("Magic", 2, 5, ChatColor.BLUE, 1.50),
	
	/**
	 * The third rarity for equipment items "Rare", in gold, with a multiplier of 2.00.
	 */
	RARE("Rare", 3, 5, ChatColor.GOLD, 2.00),
	
	/**
	 * The fourth rarity for equipment items "Epic", in dark purple, with a multiplier of 2.50.
	 */
	EPIC("Epic", 4, 5, ChatColor.DARK_PURPLE, 2.50),
	
	/**
	 * The fifth rarity for equipment items "Unique", in dark red, with a multiplier of 3.00.
	 */
	UNIQUE("Unique", 5, 5, ChatColor.DARK_RED, 3.00),
	
	/**
	 * The first rarity for rune items "Whispering", in green, with a multiplier of 1.00.
	 */
	WHISPERING("Whispering", 1, 3, ChatColor.GREEN, 1.00),
	
	/**
	 * The second rarity for rune items "Screaming", in blue, with a multiplier of 1.50.
	 */
	SCREAMING("Screaming", 2, 3, ChatColor.BLUE, 1.50),
	
	/**
	 * The third rarity for rune items "Deafening", in gold, with a multiplier of 2.00.
	 */
	DEAFENING("Deafening", 3, 3, ChatColor.GOLD, 2.00);
	
	/**
	 * A random instance, for rolling random rarities.
	 */
	private static Random random = new Random();
	
	/**
	 * Determines a random equipment rarity with a multiplier of 1-3 on a scale of 1-5 stars.
	 * Contains rarity name, color, multiplier and the star string.</br>
	 * 70% Chance for Normal</br>
	 * 25% Chance for Magic</br>
	 * 4% Chance for Rare</br>
	 * 0.95% Chance for Epic</br>
	 * 0.05% Chance for Unique
	 * 
	 * @return The random rarity.
	 */
	public static Rarity getRandomEquipmentRarity() {
		int rarity = random.nextInt(10000) + 1;
		
		if(rarity <= 7000) {
			return NORMAL;
		}
		else if(rarity <= 9500) {
			return MAGIC;
		}
		else if(rarity <= 9900) {
			return RARE;
		}
		else if(rarity <= 9995) {
			return EPIC;
		}
		else if(rarity <= 10000) {
			return UNIQUE;
		}
		return NORMAL;
	}
	
	/**
	 * Determines a random rune rarity with a multiplier of 1-2 on a scale of 1-3 stars.
	 * Contains rarity name, color, multiplier and the star string.</br>
	 * 80% Chance for Whispering</br>
	 * 17.5% Chance for Screaming</br>
	 * 2.5% Chance for Deafening
	 * 
	 * @return The random rarity.
	 */
	public static Rarity getRandomRuneRarity() {
		int rarity = random.nextInt(1000) + 1;
				
		if(rarity <= 800) {
			return WHISPERING;
		}
		else if(rarity <= 975) {
			return SCREAMING;
		}
		else if(rarity <= 1000) {
			return DEAFENING;
		}
		return WHISPERING;
	}
	
	/**
	 * The name of the rarity.
	 */
	private final String rarityName;
	
	/**
	 * The stars of the rarity.
	 */
	private final String rarityStars;
	
	/**
	 * The color of the rarity.
	 */
	private final ChatColor rarityColor;
	
	/**
	 * The stat multiplier of the rarity.
	 */
	private final double rarityMultiplier;
	
	/**
	 * Creates a new rarity with given values.
	 * 
	 * @param rarityName The name of the rarity.
	 * @param stars The stars of the rarity.
	 * @param starsMax The maximum stars of the rarity.
	 * @param rarityColor The color of the rarity.
	 * @param rarityMultiplier The stat multiplier of the rarity.
	 */
	Rarity(String rarityName, int stars, int starsMax, ChatColor rarityColor, double rarityMultiplier) {
		this.rarityName = rarityName;
		this.rarityColor = rarityColor;
		this.rarityMultiplier = rarityMultiplier;
		
		String rarityStars = "";
		for(int index = 0; index < starsMax; index++) {
			rarityStars += UnicodeUtils.ICON_DIAMOND;
			if(index == stars - 1) {
				rarityStars += ChatColor.GRAY;
			}
		}
		this.rarityStars = rarityStars;
	}

	/**
	 * @return The name of the rarity.
	 */
	public String getName() {
		return rarityName;
	}

	/**
	 * @return The stars of the rarity.
	 */
	public String getStars() {
		return rarityStars;
	}

	/**
	 * @return The color of the rarity.
	 */
	public ChatColor getColor() {
		return rarityColor;
	}

	/**
	 * @return The stat multiplier of the rarity.
	 */
	public double getMultiplier() {
		return rarityMultiplier;
	}

}
