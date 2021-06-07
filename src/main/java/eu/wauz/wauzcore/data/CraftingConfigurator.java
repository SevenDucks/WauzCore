package eu.wauz.wauzcore.data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRequirement;

/**
 * Configurator to fetch or modify data from the Crafting.yml.
 * 
 * @author Wauzmons
 */
public class CraftingConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @param craftingCategory The category of crafting items.
	 * 
	 * @return The names of the items in the category.
	 */
	public static Set<String> getItemNames(String craftingCategory) {
		return mainConfigGetKeys("Crafting", craftingCategory);
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemName The name of the crafting item.
	 * 
	 * @return The type of the crafting item.
	 */
	public static String getItemType(String craftingCategory, String itemName) {
		return mainConfigGetString("Crafting", craftingCategory + "." + itemName + ".type");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemName The name of the crafting item.
	 * 
	 * @return The amount of the crafting item.
	 */
	public static int getItemAmount(String craftingCategory, String itemName) {
		return mainConfigGetInt("Crafting", craftingCategory + "." + itemName + ".amount");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemName The name of the crafting item.
	 * 
	 * @return The level of the crafting item.
	 */
	public static int getItemLevel(String craftingCategory, String itemName) {
		return mainConfigGetInt("Crafting", craftingCategory + "." + itemName + ".level");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemName The name of the crafting item.
	 * 
	 * @return If the item should be automatically identified when crafted.
	 */
	public static boolean shouldIdentifyItem(String craftingCategory, String itemName) {
		return mainConfigGetBoolean("Crafting", craftingCategory + "." + itemName + ".identify");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemName The name of the crafting item.
	 * 
	 * @return The material requirements to craft the item.
	 */
	public static List<WauzCraftingRequirement> getItemRequirements(String craftingCategory, String itemName) {
		return mainConfigGetStringList("Crafting", craftingCategory + "." + itemName + ".cost").stream()
				.map(materialString -> new WauzCraftingRequirement(materialString))
				.collect(Collectors.toList());
	}

}
