package eu.wauz.wauzcore.professions.crafting;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.CraftingConfigurator;
import eu.wauz.wauzcore.items.InventorySerializer;
import eu.wauz.wauzcore.items.identifiers.WauzIdentifier;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.MythicUtils;

/**
 * A craftable item.
 * 
 * @author Wauzmons
 * 
 * @see WauzCraftingRecipes
 */
public class WauzCraftingItem {
	
	/**
	 * If the crafting item is just an empty slot.
	 */
	private boolean isEmpty = false;
	
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
	 * @param itemName The name of the crafting item.
	 */
	public WauzCraftingItem(String craftingCategory, String itemName) {
		String craftingItemType = CraftingConfigurator.getItemType(craftingCategory, itemName);
		if(StringUtils.equals(craftingItemType, "_")) {
			isEmpty = true;
			return;
		}
		String[] nameParts = craftingItemType.split(";");
		String canonicalName = nameParts[0];
		String displayNameSuffix = nameParts.length > 1 ? nameParts[1] : null;
		this.craftingItemStack = MythicUtils.getItemStack(canonicalName, "Crafting " + craftingCategory);
		if(this.craftingItemStack != null) {
			this.craftingItemStack = InventorySerializer.serialize(this.craftingItemStack);
			if(StringUtils.isNotBlank(displayNameSuffix) && ItemUtils.hasDisplayName(craftingItemStack)) {
				ItemMeta craftingItemMeta = craftingItemStack.getItemMeta();
				Components.displayName(craftingItemMeta, Components.displayName(craftingItemMeta) + displayNameSuffix);
				craftingItemStack.setItemMeta(craftingItemMeta);
			}
		}
		this.craftingItemAmount = CraftingConfigurator.getItemAmount(craftingCategory, itemName);
		this.craftingItemLevel = CraftingConfigurator.getItemLevel(craftingCategory, itemName);
		this.shouldIdentify = CraftingConfigurator.shouldIdentifyItem(craftingCategory, itemName);
		this.requirements = CraftingConfigurator.getItemRequirements(craftingCategory, itemName);
	}

	/**
	 * @return If the crafting item is just an empty slot.
	 */
	public boolean isEmpty() {
		return isEmpty;
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
	
	/**
	 * Generates a player specific instance of the crafting item stack.
	 * 
	 * @param player The player who can craft the item.
	 * @param crafted If the item was crafted.
	 * 
	 * @return A stack of the crafting item.
	 */
	public ItemStack getInstance(Player player, boolean crafted) {
		ItemStack itemStack = craftingItemStack.clone();
		if(crafted && shouldIdentify) {
			WauzIdentifier.identify(player, itemStack, ItemUtils.getDisplayName(itemStack), false);
		}
		MenuUtils.addItemLore(itemStack, "", false);
		
		if(crafted) {
			MenuUtils.addItemLore(itemStack, ChatColor.GRAY + "Crafted by " + player.getName(), false);
		}
		else {
			Inventory inventory = MaterialPouch.getInventory(player, "materials");
			for(WauzCraftingRequirement requirement : requirements) {
				int materialCost = requirement.getAmount();
				int materialAmount = 0;
				String materialName = requirement.getMaterial();
				for(ItemStack materialItemStack : inventory.getContents()) {
					if(materialItemStack != null && ItemUtils.isSpecificItem(materialItemStack, materialName)) {
						materialAmount += materialItemStack.getAmount();
					}
				}
				ChatColor materialAmountColor = materialAmount >= materialCost ? ChatColor.GREEN : ChatColor.RED;
				
				String materialCostString = ChatColor.GOLD + Formatters.INT.format(materialCost);
				String materialAmountString = materialAmountColor + Formatters.INT.format(materialAmount);
				String priceString = materialCostString + " (" + materialAmountString + ChatColor.GOLD + ") ";
				MenuUtils.addItemLore(itemStack, priceString + materialName, false);
			}
		}
		itemStack.setAmount(craftingItemAmount);
		return itemStack;
	}

}
