package eu.wauz.wauzcore.items.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.mobs.pets.WauzPetAbility;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;
import eu.wauz.wauzcore.mobs.pets.WauzPetStat;

/**
 * An util class for reading and writing pet egg properties.
 * 
 * @author Wauzmons
 * 
 * @see ItemUtils
 */
public class PetEggUtils {
	
	/**
	 * Checks if an item stack is a pet egg, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a pet egg.
	 */
	public static boolean isEggItem(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, "Pet Egg");
	}
	
	/**
	 * Checks if an item stack is a pet food item, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a pet food item.
	 */
	public static boolean isFoodItem(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && !isEggItem(itemStack) && ItemUtils.doesLoreContain(itemStack, "Pet Food");
	}
	
	/**
	 * Gets the type of an egg item stack as string, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The category of the item.
	 */
	public static String getPetType(ItemStack itemStack) {
		return ItemUtils.getStringFromLore(itemStack, "Type:" + ChatColor.GREEN, 1);
	}
	
	/**
	 * Gets the category of an egg item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The category of the item.
	 */
	public static String getPetCategory(ItemStack itemStack) {
		return ItemUtils.getStringFromLore(itemStack, "Category:" + ChatColor.GREEN, 1);
	}
	
	/**
	 * Gets the ability of an egg item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The ability of the item.
	 */
	public static WauzPetAbility getPetAbility(ItemStack itemStack) {
		return WauzPetAbilities.getAbility(ItemUtils.getStringFromLore(itemStack, "Ability:" + ChatColor.GREEN, 1));
	}
	
	/**
	 * Gets the hatch time of an egg item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The hatch time of the item.
	 */
	public static long getPetHatchTime(ItemStack itemStack) {
		return ItemUtils.getLongFromLore(itemStack, "Hatch Time:", 2);
	}
	
	/**
	 * Gets a stat of an pet food item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param stat The stat to get.
	 * 
	 * @return The stat value of the item.
	 */
	public static int getPetFoodStat(ItemStack itemStack, WauzPetStat stat) {
		return ItemUtils.getIntegerFromLore(itemStack, "Pet " + stat.getName() + ":", 2);
	}
	
	/**
	 * Gets a stat of an egg item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param stat The stat to get.
	 * 
	 * @return The stat value of the item.
	 * 
	 * @see PetEggUtils#getMaxPetStat(ItemStack, WauzPetStat)
	 * @see PetEggUtils#setPetStat(ItemStack, WauzPetStat, int)
	 */
	public static int getPetStat(ItemStack itemStack, WauzPetStat stat) {
		return ItemUtils.getIntegerFromLore(itemStack, stat.getName() + ":" + ChatColor.GREEN, 1);
	}
	
	/**
	 * Gets a maxiumum stat of an egg item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param stat The stat to get.
	 * 
	 * @return The maxiumum stat value of the item.
	 * 
	 * @see PetEggUtils#getPetStat(ItemStack, WauzPetStat)
	 */
	public static int getMaxPetStat(ItemStack itemStack, WauzPetStat stat) {
		return ItemUtils.getIntegerFromLore(itemStack, stat.getName() + ":" + ChatColor.GREEN, 3);
	}
	
	/**
	 * Gets a stat of an egg item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param stat The stat to set.
	 * @param value The new stat value.
	 * @param max The new maximum stat value.
	 * 
	 * @return If the action was successful.
	 * 
	 * @see PetEggUtils#getPetStat(ItemStack, WauzPetStat)
	 */
	public static boolean setPetStat(ItemStack itemStack, WauzPetStat stat, int value, int max) {
		String description = " " + ChatColor.GRAY + stat.getDescription();
		String oldStatLore = ChatColor.WHITE + stat.getName() + ":" + ChatColor.GREEN + " ";
		String newStatLore = ChatColor.WHITE + stat.getName() + ":" + ChatColor.GREEN + " " + value + " / " + max + description;
		return ItemUtils.replaceStringFromLore(itemStack, oldStatLore, newStatLore);
	}

}
