package eu.wauz.wauzcore.data;

import java.util.List;

import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Crafting.yml.
 * 
 * @author Wauzmons
 */
public class CraftingConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The material name of the craftable item.
	 */
	public static String getItemMaterial(int itemIndex) {
		return mainConfigGetString("Crafting", itemIndex + ".type");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The amount of the craftable item.
	 */
	public static int getItemAmount(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".amount");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The name of the craftable item.
	 */
	public static String getItemName(int itemIndex) {
		return mainConfigGetString("Crafting", itemIndex + ".name");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The lores of the craftable item.
	 */
	public static List<String> getItemLores(int itemIndex) {
		return mainConfigGetStringList("Crafting", itemIndex + ".lores");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The level of the craftable item.
	 */
	public static int getItemLevel(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".level");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The amount of requirements to craft the item.
	 */
	public static int getItemCraftingCostsAmount(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".cost.amount");
	}
	
// Potions
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The effect type of the craftable potion.
	 */
	public static PotionEffectType getPotionType(int itemIndex) {
		return PotionEffectType.getByName(mainConfigGetString("Crafting", itemIndex + ".effect.type"));
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The effect duration of the craftable potion.
	 */
	public static int getPotionDuration(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".effect.duration");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * 
	 * @return The effect level of the craftable potion.
	 */
	public static int getPotionLevel(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".effect.level");
	}
	
// Crafting Costs
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * @param requirementIndex The requirement number of the craftable item.
	 * 
	 * @return The name of the required material.
	 */
	public static String getCraftingCostItemString(int itemIndex, int requirementIndex) {
		return mainConfigGetString("Crafting", itemIndex + ".cost." + requirementIndex + "." + "item");
	}
	
	/**
	 * @param itemIndex The number of the craftable item.
	 * @param requirementIndex The reuirement number of the craftable item.
	 * 
	 * @return The amount of the required material.
	 */
	public static int getCraftingCostItemAmount(int itemIndex, int requirementIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".cost." + requirementIndex + "." + "amount");
	}

}
