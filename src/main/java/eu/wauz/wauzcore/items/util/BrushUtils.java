package eu.wauz.wauzcore.items.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * An util class for reading and writing brush properties.
 * 
 * @author Wauzmons
 *
 * @see ItemUtils
 */
public class BrushUtils {
	
	/**
	 * Gets the brush id of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The brush id.
	 */
	public static String getBrushId(ItemStack itemStack) {
		return ItemUtils.getStringFromLore(itemStack, "Brush UUID: ", 2);
	}
	
	/**
	 * Gets the shape of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The shape.
	 */
	public static String getShape(ItemStack itemStack) {
		String shape = ItemUtils.getStringFromLore(itemStack, "Shape:" + ChatColor.LIGHT_PURPLE, 1);
		return shape != null ? shape.toLowerCase() : null;
	}
	
	/**
	 * Gets the material of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The material.
	 */
	public static String getMaterial(ItemStack itemStack) {
		return ItemUtils.getStringFromLore(itemStack, "Material:" + ChatColor.GREEN, 1);
	}
	
	/**
	 * Gets the biome of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The biome.
	 */
	public static String getBiome(ItemStack itemStack) {
		return ItemUtils.getStringFromLore(itemStack, "Biome:" + ChatColor.GREEN, 1);
	}
	
	/**
	 * Gets the radius of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The radius.
	 */
	public static int getRadius(ItemStack itemStack) {
		return ItemUtils.getIntegerFromLore(itemStack, "Radius:" + ChatColor.YELLOW, 1);
	}
	
	/**
	 * Gets the height of an item stack, based on lore.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The height.
	 */
	public static int getHeight(ItemStack itemStack) {
		if(ItemUtils.doesLoreContain(itemStack, "Height:" + ChatColor.YELLOW + " Max")) {
			return -1;
		}
		return ItemUtils.getIntegerFromLore(itemStack, "Height:" + ChatColor.YELLOW, 1);
	}

}
