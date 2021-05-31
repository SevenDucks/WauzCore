package eu.wauz.wauzcore.professions.crafting;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.CraftingConfigurator;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.ItemManager;

/**
 * A craftable item.
 * 
 * @author Wauzmons
 * 
 * @see WauzCraftingRecipes
 */
public class WauzCraftingItem {
	
	/**
	 * Access to the MythicMobs API item manager.
	 */
	private static ItemManager mythicMobs = MythicMobs.inst().getItemManager();
	
	/**
	 * A stack of the crafting item.
	 */
	private ItemStack craftingItemStack;
	
	/**
	 * The amount of the crafting item.
	 */
	private int craftingItemAmount;
	
	/**
	 * The level of the crafting item.
	 */
	private int craftingItemLevel;
	
	/**
	 * If the item should be automatically identified when crafted.
	 */
	private boolean shouldIdentify;
	
	/**
	 * The material requirements to craft the item.
	 */
	private List<WauzCraftingRequirement> requirements;
	
	/**
	 * Constructs a crafting item, based on the Crafting.yml file.
	 * 
	 * @param craftingCategory The category of the crafting item.
	 * @param itemIndex The number of the crafting item.
	 */
	public WauzCraftingItem(String craftingCategory, int itemIndex) {
		String craftingItemType = CraftingConfigurator.getItemType(craftingCategory, itemIndex);
		this.craftingItemStack = WauzNmsClient.nmsSerialize(mythicMobs.getItemStack(craftingItemType));
		this.craftingItemAmount = CraftingConfigurator.getItemAmount(craftingCategory, itemIndex);
		this.craftingItemLevel = CraftingConfigurator.getItemLevel(craftingCategory, itemIndex);
		this.shouldIdentify = CraftingConfigurator.shouldIdentifyItem(craftingCategory, itemIndex);
		this.requirements = CraftingConfigurator.getItemRequirements(craftingCategory, itemIndex);
	}

	/**
	 * @return A stack of the crafting item.
	 */
	public ItemStack getCraftingItemStack() {
		return craftingItemStack;
	}

	/**
	 * @return The amount of the crafting item.
	 */
	public int getCraftingItemAmount() {
		return craftingItemAmount;
	}

	/**
	 * @return The level of the crafting item.
	 */
	public int getCraftingItemLevel() {
		return craftingItemLevel;
	}

	/**
	 * @return If the item should be automatically identified when crafted.
	 */
	public boolean isShouldIdentify() {
		return shouldIdentify;
	}

	/**
	 * @return The material requirements to craft the item.
	 */
	public List<WauzCraftingRequirement> getRequirements() {
		return requirements;
	}

}
