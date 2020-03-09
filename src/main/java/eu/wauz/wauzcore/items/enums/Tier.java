package eu.wauz.wauzcore.items.enums;

import org.bukkit.ChatColor;

/**
 * The tier of an equipment or rune item, containing name, level and multiplier.
 * 
 * @author Wauzmons
 */
public enum Tier {
	
	/**
	 * First tier for equipment items "Lesser", with a multiplier of 2.
	 */
	EQUIP_T1("Lesser" + ChatColor.GRAY + " T1" + ChatColor.WHITE, 1, 2),
	
	/**
	 * Second tier for equipment items "Greater", with a multiplier of 3.
	 */
	EQUIP_T2("Greater" + ChatColor.GRAY + " T2" + ChatColor.WHITE, 2, 3),
	
	/**
	 * Third tier for equipment items "Angelic", with a multiplier of 5.
	 */
	EQUIP_T3("Angelic" + ChatColor.GRAY + " T3" + ChatColor.WHITE, 3, 5),
	
	/**
	 * Fourth tier for equipment items "Mythic", with a multiplier of 8.
	 */
	EQUIP_T4("Mythic" + ChatColor.GRAY + " T4" + ChatColor.WHITE, 4, 8),
	
	/**
	 * Fifth tier for equipment items "Divine", with a multiplier of 13.
	 */
	EQUIP_T5("Divine" + ChatColor.GRAY + " T5" + ChatColor.WHITE, 5, 13),
	
	/**
	 * Highest tier for equipment items "Eternal", with a multiplier of 21.
	 */
	EQUIP_T6("Eternal" + ChatColor.GRAY + " T6" + ChatColor.WHITE, 6, 21),
	
	/**
	 * First tier for rune items "Lesser", with a multiplier of 6.
	 */
	RUNE_T1("Lesser" + ChatColor.GRAY + " T1" + ChatColor.WHITE, 1, 6),
	
	/**
	 * Second tier for rune items "Greater", with a multiplier of 9.
	 */
	RUNE_T2("Greater" + ChatColor.GRAY + " T2" + ChatColor.WHITE, 2, 9),
	
	/**
	 * Third tier for rune items "Angelic", with a multiplier of 12.
	 */
	RUNE_T3("Angelic" + ChatColor.GRAY + " T3" + ChatColor.WHITE, 3, 12);
	
	/**
	 * Determines a tier, based on an item name, with a multiplier of 2 to 21 on a scale of T1 to T6.
	 * Contains the tier name, level and multiplier.
	 * 
	 * @param itemName The name of the equipment item.
	 * 
	 * @return The tier of the item.
	 */
	public static Tier getEquipmentTier(String itemName) {
		if(itemName.contains("T6")) {
			return EQUIP_T6;
		}
		else if(itemName.contains("T5")) {
			return EQUIP_T5;
		}
		else if(itemName.contains("T4")) {
			return EQUIP_T4;
		}
		else if(itemName.contains("T3")) {
			return EQUIP_T3;
		}
		else if(itemName.contains("T2")) {
			return EQUIP_T2;
		}
		else {
			return EQUIP_T1;
		}
	}
	
	/**
	 * Determines a tier, based on an item name, with a multiplier of 6, 9 or 12 on a scale of T1 to T3.
	 * Contains the tier name, level and multiplier.
	 * 
	 * @param itemName The name of the rune item.
	 * 
	 * @return The tier of the item.
	 */
	public static Tier getRuneTier(String itemName) {
		if(itemName.contains("T3")) {
			return RUNE_T3;
		}
		else if(itemName.contains("T2")) {
			return RUNE_T2;
		}
		else {
			return RUNE_T1;
		}
	}
	
	/**
	 * The name of the tier.
	 */
	private final String tierName;
	
	/**
	 * The level of the tier.
	 */
	private final int tierLevel;
	
	/**
	 * The multiplier of the tier.
	 */
	private final double tierMultiplier;
	
	/**
	 * Creates a new tier with given values.
	 * 
	 * @param tierName The name of the tier.
	 * @param tierLevel The level of the tier.
	 * @param tierMultiplier The multiplier of the tier.
	 */
	Tier(String tierName, int tierLevel, double tierMultiplier) {
		this.tierName = tierName;
		this.tierLevel = tierLevel;
		this.tierMultiplier = tierMultiplier;
	}

	/**
	 * @return The name of the tier.
	 */
	public String getName() {
		return tierName;
	}

	/**
	 * @return The level of the tier.
	 */
	public int getLevel() {
		return tierLevel;
	}

	/**
	 * @return The multiplier of the tier.
	 */
	public double getMultiplier() {
		return tierMultiplier;
	}

}
