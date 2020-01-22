package eu.wauz.wauzcore.items.enums;

import net.md_5.bungee.api.ChatColor;

/**
 * The tier of an equipment or rune item, containing name, level and multiplier.
 * 
 * @author Wauzmons
 */
public enum Tier {
	
	/**
	 * First tier for equipment items "Lesser", with a multiplier of 2 (2^1).
	 */
	EQUIP_T1("Lesser" + ChatColor.GRAY + " T1" + ChatColor.WHITE, 1, (double) Math.pow(2, 1)),
	
	/**
	 * Second tier for equipment items "Greater", with a multiplier of 4 (2^2).
	 */
	EQUIP_T2("Greater" + ChatColor.GRAY + " T2" + ChatColor.WHITE, 2, (double) Math.pow(2, 2)),
	
	/**
	 * Third tier for equipment items "Angelic", with a multiplier of 8 (2^3).
	 */
	EQUIP_T3("Angelic" + ChatColor.GRAY + " T3" + ChatColor.WHITE, 3, (double) Math.pow(2, 3)),
	
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
	 * Determines a tier, based on an item name, with a multiplier of 2^1 to 2^3 on a scale of T1 to T3.
	 * Contains the tier name, level and multiplier.
	 * 
	 * @param itemName The name of the equipment item.
	 * 
	 * @return The tier of the item.
	 */
	public static Tier getEquipmentTier(String itemName) {
		if(itemName.contains("T3")) {
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
