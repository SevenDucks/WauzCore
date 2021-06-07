package eu.wauz.wauzcore.professions.crafting;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.CraftingConfigurator;

/**
 * Collections of recipes for craftable items.
 * 
 * @author Wauzmons
 * 
 * @see WauzCraftingItem
 */
public class WauzCraftingRecipes {
	
	/**
	 * The key of the smithing crafting category.
	 */
	private static final String CATEGORY_SMITHING = "smithing";
	
	/**
	 * The key of the cooking crafting category.
	 */
	private static final String CATEGORY_COOKING = "cooking";
	
	/**
	 * The key of the inscription crafting category.
	 */
	private static final String CATEGORY_INSCRIPTION = "inscription";
	
	/**
	 * A list of all smithing recipes.
	 */
	private static List<WauzCraftingItem> smithingRecipes;
	
	/**
	 * A list of all cooking recipes.
	 */
	private static List<WauzCraftingItem> cookingRecipes;
	
	/**
	 * A list of all inscription recipes.
	 */
	private static List<WauzCraftingItem> inscriptionRecipes;
	
	/**
	 * The number of all different registred crafting recipes.
	 */
	private static int recipeCount;
	
	/**
	 * Initializes all crafting recipe collections.
	 */
	public static void init() {
		smithingRecipes = getCraftingItems(CATEGORY_SMITHING);
		cookingRecipes = getCraftingItems(CATEGORY_COOKING);
		inscriptionRecipes = getCraftingItems(CATEGORY_INSCRIPTION);
		
		WauzCore.getInstance().getLogger().info("Loaded " + recipeCount + " Crafting Recipes!");
	}
	
	/**
	 * Gets all craftable items of the given category.
	 * 
	 * @param craftingCategory The category of crafting items.
	 * 
	 * @return The list of crafting items.
	 */
	private static List<WauzCraftingItem> getCraftingItems(String craftingCategory) {
		List<WauzCraftingItem> items = new ArrayList<>();
		for(String itemName : CraftingConfigurator.getItemNames(craftingCategory)) {
			items.add(new WauzCraftingItem(craftingCategory, itemName));
			recipeCount++;
		}
		return items;
	}

	/**
	 * @return A list of all smithing recipes.
	 */
	public static final List<WauzCraftingItem> getSmithingRecipes() {
		return smithingRecipes;
	}

	/**
	 * @return A list of all cooking recipes.
	 */
	public static final List<WauzCraftingItem> getCookingRecipes() {
		return cookingRecipes;
	}

	/**
	 * @return A list of all inscription recipes.
	 */
	public static final List<WauzCraftingItem> getInscriptionRecipes() {
		return inscriptionRecipes;
	}

}
