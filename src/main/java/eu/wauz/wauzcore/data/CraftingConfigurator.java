package eu.wauz.wauzcore.data;

import java.util.List;
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
	 * @param craftingCategory The category of the crafting item.
	 * @param itemIndex The number of the crafting item.
	 * 
	 * @return The type of the crafting item.
	 */
	public static String getItemType(String craftingCategory, int itemIndex) {
		return mainConfigGetString("Crafting", craftingCategory + "." + itemIndex + ".type");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemIndex The number of the crafting item.
	 * 
	 * @return The amount of the crafting item.
	 */
	public static int getItemAmount(String craftingCategory, int itemIndex) {
		return mainConfigGetInt("Crafting", craftingCategory + "." + itemIndex + ".amount");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemIndex The number of the crafting item.
	 * 
	 * @return The level of the crafting item.
	 */
	public static int getItemLevel(String craftingCategory, int itemIndex) {
		return mainConfigGetInt("Crafting", craftingCategory + "." + itemIndex + ".level");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemIndex The number of the crafting item.
	 * 
	 * @return If the item should be automatically identified when crafted.
	 */
	public static boolean shouldIdentifyItem(String craftingCategory, int itemIndex) {
		return mainConfigGetBoolean("Crafting", craftingCategory + "." + itemIndex + ".identify");
	}
	
	/**
	 * @param craftingCategory The category of the crafting item.
	 * @param itemIndex The number of the crafting item.
	 * 
	 * @return The material requirements to craft the item.
	 */
	public static List<WauzCraftingRequirement> getItemRequirements(String craftingCategory, int itemIndex) {
		return mainConfigGetStringList("Crafting", craftingCategory + "." + itemIndex + ".cost").stream()
				.map(materialString -> new WauzCraftingRequirement(materialString))
				.collect(Collectors.toList());
	}

}
