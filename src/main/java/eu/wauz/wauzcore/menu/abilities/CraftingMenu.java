package eu.wauz.wauzcore.menu.abilities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.InventoryItemRemover;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingItem;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRecipes;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRequirement;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Components;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the job menu, that is used to craft items.
 * 
 * @author Wauzmons
 * 
 * @see WauzCraftingRecipes
 */
public class CraftingMenu implements WauzInventory {
	
	/**
	 * Item slots for the craftable items in the crafting menu.
	 */
	private static List<Integer> recipeSlots = Arrays.asList(2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "crafting";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the list of crafting recipes including the required materials.
	 * Navigation buttons can be used to switch between pages.
	 * 
	 * @param player The player that should view the inventory.
	 * @param skill The crafting skill of the player.
	 * @param recipes The recipes of the crafting skill.
	 * @param page The index of the recipe page, starting at 0.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, AbstractPassiveSkill skill, List<WauzCraftingItem> recipes, int page) {
		CraftingMenu craftingMenu = new CraftingMenu(skill, recipes, page);
		String levelText = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Level " + skill.getLevel();
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + skill.getPassiveName() + " " + levelText;
		Inventory menu = Components.inventory(craftingMenu, menuTitle, 27);
		
		ItemStack lockedItemStack = GenericIconHeads.getUnknownItem();
		MenuUtils.setItemDisplayName(lockedItemStack, ChatColor.GRAY + "Locked Recipe");
		
		ItemStack emptyItemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		MenuUtils.setItemDisplayName(emptyItemStack, " ");
		
		for(int index = 0; index < recipeSlots.size(); index++) {
			int indexSlot = recipeSlots.get(index);
			int indexWithOffset = index + (page * recipeSlots.size());
			if(indexWithOffset < recipes.size()) {
				WauzCraftingItem craftingItem = recipes.get(indexWithOffset);
				boolean unlocked = skill.getLevel() >= craftingItem.getCraftingItemLevel();
				menu.setItem(indexSlot, unlocked ? craftingItem.getInstance(player, false) : lockedItemStack);
			}
			else {
				menu.setItem(indexSlot, emptyItemStack);
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The crafting skill of the player.
	 */
	private AbstractPassiveSkill skill;
	
	/**
	 * The recipes of the crafting skill.
	 */
	private List<WauzCraftingItem> recipes;
	
	/**
	 * The index of the recipe page, starting at 0.
	 */
	private int page;
	
	/**
	 * Creates a new crafting menu instance.
	 * 
	 * @param skill The crafting skill of the player.
	 * @param recipes The recipes of the crafting skill.
	 * @param page The index of the recipe page, starting at 0.
	 */
	public CraftingMenu(AbstractPassiveSkill skill, List<WauzCraftingItem> recipes, int page) {
		this.skill = skill;
		this.recipes = recipes;
		this.page = page;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and tries to craft the clicked item.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		
		if(slot < 27) {
			event.setCancelled(true);
		}
		
		if(recipeSlots.contains(slot)) {
			int indexWithOffset = recipeSlots.indexOf(slot) + (page * recipeSlots.size());
			if(indexWithOffset < recipes.size() && tryToCraft(player, recipes.get(indexWithOffset))) {
				open(player, skill, recipes, page);
			}
		}
	}
	
	/**
	 * Tries to let the player craft the given item.
	 * 
	 * @param player The player crafting the item.
	 * @param itemToCraft The item to craft.
	 * 
	 * @return If the crafting was successful.
	 */
	public boolean tryToCraft(Player player, WauzCraftingItem itemToCraft) {
		Inventory inventory = MaterialPouch.getInventory(player, "questitems");
		InventoryItemRemover itemRemover = new InventoryItemRemover(inventory);
		
		if(player.hasPermission(WauzPermission.DEBUG_CRAFTING.toString())) {
			// TODO
		}
		
		for(WauzCraftingRequirement requirement : itemToCraft.getRequirements()) {
			int materialAmount = 0;
			String materialName = requirement.getMaterial();
			itemRemover.addItemNameToRemove(materialName, itemToCraft.getCraftingItemAmount());
			for(ItemStack materialItemStack : inventory.getContents()) {
				if(materialItemStack != null && ItemUtils.isSpecificItem(materialItemStack, materialName)) {
					materialAmount += materialItemStack.getAmount();
				}
			}
			if(materialAmount < itemToCraft.getCraftingItemAmount()) {
				player.sendMessage(ChatColor.RED + "You don't have enough materials!");
				player.closeInventory();
				return false;
			}
		}
		
		ItemStack craftedItem = itemToCraft.getInstance(player, true);
		if(!ItemUtils.fitsInInventory(player.getInventory(), craftedItem)) {
			player.sendMessage(ChatColor.RED + "Your inventory is full!");
			player.closeInventory();
			return false;
		}
		
		itemRemover.execute();
		player.getInventory().addItem(WauzNmsClient.nmsSerialize(craftedItem));
		player.sendMessage(ChatColor.GREEN + "Your purchase was successful!");
		// TODO exp
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
		return true;
	}

}
