package eu.wauz.wauzcore.items.util;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * An util class for reading and writing food properties.
 * 
 * @author Wauzmons
 *
 * @see ItemUtils
 */
public class FoodUtils {
	
	/**
	 * Checks if an item stack is a food item, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a food item.
	 */
	public static boolean isFoodItem(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Food Item");
	}
	
	/**
	 * Gets the cooldown of an item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The cooldown seconds.
	 */
	public static int getCooldown(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Cooldown:" + ChatColor.YELLOW, 1) : 1;
	}
	
	/**
	 * Checks if an item stack contains a saturation modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains a saturation modifier.
	 * 
	 * @see FoodUtils#getSaturation(ItemStack)
	 */
	public static boolean containsSaturationModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Saturation:" + ChatColor.DARK_GREEN);
	}
	
	/**
	 * Gets the saturation value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The saturation value.
	 * 
	 * @see FoodUtils#containsSaturationModifier(ItemStack)
	 */
	public static int getSaturation(ItemStack itemStack) {
		return ItemUtils.getIntegerFromLore(itemStack, "Saturation:" + ChatColor.DARK_GREEN, 1);
	}
	
	/**
	 * Checks if an item stack contains a healing modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains a healing modifier.
	 * 
	 * @see FoodUtils#getHealing(ItemStack)
	 */
	public static boolean containsHealingModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Healing:" + ChatColor.RED);
	}
	
	/**
	 * Gets the healing value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The heling value.
	 * 
	 * @see FoodUtils#containsHealingModifier(ItemStack)
	 */
	public static int getHealing(ItemStack itemStack) {
		return ItemUtils.getIntegerFromLore(itemStack, "Healing:" + ChatColor.RED, 1);
	}
	
	/**
	 * Checks if an item stack contains a pvp protection modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains a pvp protection modifier.
	 * 
	 * @see FoodUtils#getPvPProtection(ItemStack)
	 */
	public static boolean containsPvPProtectionModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "PvP Protection:" + ChatColor.YELLOW);
	}
	
	/**
	 * Gets the the pvp protectino value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The pvp protection value.
	 * 
	 * @see FoodUtils#containsPvPProtectionModifier(ItemStack)
	 */
	public static int getPvPProtection(ItemStack itemStack) {
		return ItemUtils.getIntegerFromLore(itemStack, "PvP Protection:" + ChatColor.YELLOW, 2);
	}
	
	/**
	 * Checks if an item stack contains a heat resistance modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains a cold resistance modifier.
	 * 
	 * @see FoodUtils#getHeatResistance(ItemStack)
	 */
	public static boolean containsHeatResistanceModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Heat Resistance:" + ChatColor.DARK_RED);
	}
	
	/**
	 * Gets the heat resistance value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The heat resistance value.
	 * 
	 * @see FoodUtils#containsHeatResistanceModifier(ItemStack)
	 */
	public static int getHeatResistance(ItemStack itemStack) {
		return ItemUtils.getIntegerFromLore(itemStack, "Heat Resistance:" + ChatColor.DARK_RED, 2);
	}
	
	/**
	 * Checks if an item stack contains a cold resistance modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains a cold resistance modifier.
	 * 
	 * @see FoodUtils#getColdResistance(ItemStack)
	 */
	public static boolean containsColdResistanceModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Cold Resistance:" + ChatColor.DARK_AQUA);
	}
	
	/**
	 * Gets the cold resistance value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The cold resistance value.
	 * 
	 * @see FoodUtils#containsColdResistanceModifier(ItemStack)
	 */
	public static int getColdResistance(ItemStack itemStack) {
		return ItemUtils.getIntegerFromLore(itemStack, "Cold Resistance:" + ChatColor.DARK_AQUA, 2);
	}
	
	/**
	 * Checks if an item stack contains an attack boost modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains an attack boost modifier.
	 */
	public static boolean containsAttackBoostModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Attack Boost:" + ChatColor.GOLD);
	}
	
	/**
	 * Gets the attack boost value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The attack boost value and strength.
	 */
	public static Pair<Integer, Integer> getAttackBoost(ItemStack itemStack) {
		int duration = ItemUtils.getIntegerFromLore(itemStack, "Attack Boost:" + ChatColor.GOLD, 2);
		int strength = ItemUtils.getIntegerFromLore(itemStack, "Attack Boost:" + ChatColor.GOLD, 4);
		return Pair.of(duration, strength);
	}
	
	/**
	 * Checks if an item stack contains a defense boost modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains a defense boost modifier.
	 */
	public static boolean containsDefenseBoostModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Defense Boost:" + ChatColor.BLUE);
	}
	
	/**
	 * Gets the defense boost value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The defense boost value and strength.
	 */
	public static Pair<Integer, Integer> getDefenseBoost(ItemStack itemStack) {
		int duration = ItemUtils.getIntegerFromLore(itemStack, "Defense Boost:" + ChatColor.BLUE, 2);
		int strength = ItemUtils.getIntegerFromLore(itemStack, "Defense Boost:" + ChatColor.BLUE, 4);
		return Pair.of(duration, strength);
	}
	
	/**
	 * Checks if an item stack contains an exp boost modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains an exp boost modifier.
	 */
	public static boolean containsExpBoostModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Exp Boost:" + ChatColor.LIGHT_PURPLE);
	}
	
	/**
	 * Gets the exp boost value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The exp boost value and strength.
	 */
	public static Pair<Integer, Integer> getExpBoost(ItemStack itemStack) {
		int duration = ItemUtils.getIntegerFromLore(itemStack, "Exp Boost:" + ChatColor.LIGHT_PURPLE, 2);
		int strength = ItemUtils.getIntegerFromLore(itemStack, "Exp Boost:" + ChatColor.LIGHT_PURPLE, 4);
		return Pair.of(duration, strength);
	}
	
	/**
	 * Checks if an item stack contains an evasion chance modifier, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item contains an evasion chance modifier.
	 */
	public static boolean containsEvasionChanceModifier(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Evasion Chance:" + ChatColor.AQUA);
	}
	
	/**
	 * Gets the evasion chance value of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The evasion chance value and strength.
	 */
	public static Pair<Integer, Integer> getEvasionChance(ItemStack itemStack) {
		int duration = ItemUtils.getIntegerFromLore(itemStack, "Evasion Chance:" + ChatColor.AQUA, 2);
		int strength = ItemUtils.getIntegerFromLore(itemStack, "Evasion Chance:" + ChatColor.AQUA, 4);
		return Pair.of(duration, strength);
	}
	
}
