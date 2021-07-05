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
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingItem;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRecipes;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRequirement;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
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
		CraftingMenu craftingMenu = new CraftingMenu(player, skill, recipes, page);
		String levelText = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Level " + skill.getLevel();
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + skill.getPassiveName() + " " + levelText;
		Inventory menu = Components.inventory(craftingMenu, menuTitle, 27);
		craftingMenu.setMenu(menu);
		
		ItemStack backItemStack = MenuIconHeads.getCraftItem();
		MenuUtils.setItemDisplayName(backItemStack, ChatColor.YELLOW + "Back to Jobs");
		menu.setItem(0, backItemStack);
		
		craftingMenu.refreshPage();
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The player that should view the inventory.
	 */
	private Player player;
	
	/**
	 * The crafting inventory menu.
	 */
	private Inventory menu;
	
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
	 * The count of recipe pages.
	 */
	private int pageCount;
	
	/**
	 * Creates a new crafting menu instance.
	 * 
	 * @param player The player that should view the inventory.
	 * @param skill The crafting skill of the player.
	 * @param recipes The recipes of the crafting skill.
	 * @param page The index of the recipe page, starting at 0.
	 */
	public CraftingMenu(Player player, AbstractPassiveSkill skill, List<WauzCraftingItem> recipes, int page) {
		this.player = player;
		this.skill = skill;
		this.recipes = recipes;
		this.page = page;
		this.pageCount = (int) Math.ceil((float) recipes.size() / recipeSlots.size());
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and tries to craft the clicked item.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see CraftingMenu#tryToCraft(Player, WauzCraftingItem)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot >= 27) {
			return;
		}
		
		event.setCancelled(true);
		final Player player = (Player) event.getWhoClicked();
		if(slot == 0) {
			JobMenu.open(player);
		}
		if(slot == 8) {
			page = page + 1 < pageCount ? page + 1 : 0;
			refreshPage();
		}
		if(recipeSlots.contains(slot)) {
			int indexWithOffset = recipeSlots.indexOf(slot) + (page * recipeSlots.size());
			if(indexWithOffset < recipes.size() && tryToCraft(player, recipes.get(indexWithOffset))) {
				open(player, skill, recipes, page);
			}
		}
	}
	
	/**
	 * Refreshes the content of the current page.
	 */
	private void refreshPage() {
		ItemStack nextItemStack = GenericIconHeads.getNextItem();
		nextItemStack.setAmount(page + 1);
		String pageCountString = nextItemStack.getAmount() + " / " + pageCount;
		MenuUtils.setItemDisplayName(nextItemStack, ChatColor.YELLOW + "Switch Page (" + pageCountString + ")");
		menu.setItem(8, nextItemStack);
		
		ItemStack lockedItemStack = GenericIconHeads.getUnknownItem();
		MenuUtils.setItemDisplayName(lockedItemStack, ChatColor.GRAY + "Locked Recipe");
		
		ItemStack emptyItemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		MenuUtils.setItemDisplayName(emptyItemStack, " ");
		
		boolean debug = player.hasPermission(WauzPermission.DEBUG_CRAFTING.toString());
		for(int index = 0; index < recipeSlots.size(); index++) {
			int indexSlot = recipeSlots.get(index);
			int indexWithOffset = index + (page * recipeSlots.size());
			if(indexWithOffset < recipes.size()) {
				WauzCraftingItem craftingItem = recipes.get(indexWithOffset);
				if(!craftingItem.isEmpty()) {
					boolean unlocked = debug || skill.getLevel() >= craftingItem.getCraftingItemLevel();
					menu.setItem(indexSlot, unlocked ? craftingItem.getInstance(player, false) : lockedItemStack);
					continue;
				}
			}
			menu.setItem(indexSlot, emptyItemStack);
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
	private boolean tryToCraft(Player player, WauzCraftingItem itemToCraft) {
		if(itemToCraft.isEmpty()) {
			return false;
		}
		boolean debug = player.hasPermission(WauzPermission.DEBUG_CRAFTING.toString());
		if(!debug && skill.getLevel() < itemToCraft.getCraftingItemLevel()) {
			return false;
		}
		Inventory inventory = MaterialPouch.getInventory(player, "materials");
		InventoryItemRemover itemRemover = new InventoryItemRemover(inventory);
		
		if(!debug) {
			for(WauzCraftingRequirement requirement : itemToCraft.getRequirements()) {
				int materialAmount = 0;
				int neededMaterialAmount = requirement.getAmount();
				String materialName = requirement.getMaterial();
				itemRemover.addItemNameToRemove(materialName, neededMaterialAmount);
				for(ItemStack materialItemStack : inventory.getContents()) {
					if(materialItemStack != null && ItemUtils.isSpecificItem(materialItemStack, materialName)) {
						materialAmount += materialItemStack.getAmount();
					}
				}
				if(materialAmount < neededMaterialAmount) {
					player.sendMessage(ChatColor.RED + "You don't have enough materials!");
					player.closeInventory();
					return false;
				}
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
		AchievementTracker.addProgress(player, WauzAchievementType.CRAFT_ITEMS, 1);
		if(skill.getLevel() < itemToCraft.getCraftingItemLevel() + 5) {
			skill.grantExperience(player, 1);
			player.sendMessage(ChatColor.DARK_AQUA + "You earned 1 " + skill.getPassiveName() + " exp!");
		}
		else {
			player.sendMessage(ChatColor.YELLOW + "You can't earn exp from this low tier recipe anymore!");
		}
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
		return true;
	}
	
	/**
	 * @param menu The new crafting inventory menu.
	 */
	public void setMenu(Inventory menu) {
		this.menu = menu;
	}

}
