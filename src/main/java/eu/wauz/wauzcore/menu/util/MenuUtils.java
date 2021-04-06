package eu.wauz.wauzcore.menu.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.items.scrolls.WauzScrolls;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An util class for creating and checking menu items.
 * 
 * @author Wauzmons
 */
public class MenuUtils {
	
	/**
	 * A list of items to block interactions for, in MMORPG mode.
	 */
	private static List<Material> staticItems = Arrays.asList(
			Material.FILLED_MAP, Material.COMPASS, Material.NETHER_STAR, Material.BARRIER, Material.PLAYER_HEAD);
	
	/*
	 * Sets a main menu opener to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param index The slot to use in the inventory.
	 */
	public static void setMainMenuOpener(Inventory menu, int index) {
		ItemStack mainMenuItemStack = new ItemStack(Material.NETHER_STAR);
		ItemMeta mainMenuItemMeta = mainMenuItemStack.getItemMeta();
		Components.displayName(mainMenuItemMeta, ChatColor.GOLD + "Open Main Menu");
		mainMenuItemStack.setItemMeta(mainMenuItemMeta);
		menu.setItem(index, mainMenuItemStack);
	}
	
	/**
	 * Sets a currency and reputation display to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param player The player whose currencies should be shown.
	 * @param index The slot to use in the inventory.
	 */
	public static void setCurrencyDisplay(Inventory menu, Player player, int index) {
		ItemStack currencyItemStack = MenuIconHeads.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		Components.displayName(currencyItemMeta, ChatColor.GREEN + "Common Currency");
		List<String> lores = new ArrayList<String>();
		lores.add(Formatters.formatCoins(PlayerCollectionConfigurator.getCharacterCoins(player))
			+ ChatColor.YELLOW + " Coins");
		lores.add(ChatColor.RED + Formatters.INT.format(PlayerCollectionConfigurator.getCharacterSoulstones(player))
			+ ChatColor.YELLOW + " Soulstones");
		lores.add(ChatColor.RED + Formatters.INT.format(PlayerCollectionConfigurator.getCharacterMedals(player))
			+ ChatColor.YELLOW + " Medals");
		lores.add(ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player))
			+ ChatColor.YELLOW + " Tokens");
		Components.lore(currencyItemMeta, lores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(index, currencyItemStack);
	}
	
	/**
	 * Sets a global token display to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param player The player whose currencies should be shown.
	 * @param index The slot to use in the inventory.
	 */
	public static void setGlobalCurrencyDisplay(Inventory menu, Player player, int index) {
		ItemStack currencyItemStack = MenuIconHeads.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		Components.displayName(currencyItemMeta, ChatColor.GREEN + "Global Currency");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player))
			+ ChatColor.YELLOW + " Tokens");
		Components.lore(currencyItemMeta, lores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(index, currencyItemStack);
	}
	
	/**
	 * Sets a trashcan to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param indexes The slot to use in the inventory.
	 */
	public static void setTrashcan(Inventory menu, int index) {
		ItemStack trashcanItemStack = MenuIconHeads.getTrashItem();
		ItemMeta trashcanItemMeta = trashcanItemStack.getItemMeta();
		Components.displayName(trashcanItemMeta, ChatColor.RED + "Trashcan");
		List<String> trashcanLores = new ArrayList<String>();
		trashcanLores.add(ChatColor.GRAY + "Drag Items here to destroy them...");
		Components.lore(trashcanItemMeta, trashcanLores);
		trashcanItemStack.setItemMeta(trashcanItemMeta);
		menu.setItem(index, trashcanItemStack);
	}
	
	/**
	 * Sets a material bag to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param indexes The slot to use in the inventory.
	 */
	public static void setMaterials(Inventory menu, int index) {
		ItemStack materialsItemStack = MenuIconHeads.getBagItem();
		ItemMeta materialsItemMeta = materialsItemStack.getItemMeta();
		Components.displayName(materialsItemMeta, ChatColor.YELLOW + "Materials");
		List<String> materialsLores = new ArrayList<String>();
		materialsLores.add(ChatColor.GRAY + "Click to view Materials and Quest Items...");
		Components.lore(materialsItemMeta, materialsLores);
		materialsItemStack.setItemMeta(materialsItemMeta);
		menu.setItem(index, materialsItemStack);
	}
	
	/**
	 * Sets a backpack to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param indexes The slot to use in the inventory.
	 */
	public static void setBackpack(Inventory menu, int index) {
		ItemStack backpackItemStack = MenuIconHeads.getBackpackItem();
		ItemMeta backpackItemMeta = backpackItemStack.getItemMeta();
		Components.displayName(backpackItemMeta, ChatColor.DARK_GREEN + "Backpack");
		List<String> backpackLores = new ArrayList<String>();
		backpackLores.add(ChatColor.GRAY + "Click to manage Backpack Contents...");
		Components.lore(backpackItemMeta, backpackLores);
		backpackItemStack.setItemMeta(backpackItemMeta);
		menu.setItem(index, backpackItemStack);
	}
	
	/**
	 * Sets a coming soon sign with an optional label to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param lore The optional label in the lore of the sign.
	 * @param index The slot to use in the inventory.
	 */
	public static void setComingSoon(Inventory menu, String lore, int index) {
		ItemStack soonItemStack = new ItemStack(Material.OAK_SIGN);
		ItemMeta soonItemMeta = soonItemStack.getItemMeta();
		Components.displayName(soonItemMeta, ChatColor.RED + "Coming Soon");
		if(StringUtils.isNotBlank(lore)) {
			List<String> lores = new ArrayList<String>();
			lores.add(lore);
			Components.lore(soonItemMeta, lores);
		}
		soonItemStack.setItemMeta(soonItemMeta);
		menu.setItem(index, soonItemStack);
	}
	
	/**
	 * Sets borders (unnamed iron bars) to all free slots in an inventory.
	 * 
	 * @param menu The menu inventory.
	 */
	public static void setBorders(Inventory menu) {
		ItemStack borderItemStack = new ItemStack(Material.IRON_BARS);
		ItemMeta borderItemMeta = borderItemStack.getItemMeta();
		Components.displayName(borderItemMeta, " ");
		borderItemStack.setItemMeta(borderItemMeta);
		
		for(int slot = 0; slot < menu.getSize(); slot++) {
			if(menu.getItem(slot) == null)
				menu.setItem(slot, borderItemStack);
		}
	}
	
	/**
	 * Creates and sets a new menu item in an inventory.
	 * 
	 * @param menu The menu inventory. 
	 * @param slot The slot to place the menu item stack in.
	 * @param itemStack The base item stack.
	 * @param displayName The display name of the menu item stack.
	 * @param lores The lores of the menu item stack.
	 */
	public static void setMenuItem(Inventory menu, int slot, ItemStack itemStack, String displayName, List<String> lores) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		Components.displayName(itemMeta, displayName);
		Components.lore(itemMeta, lores);
		itemStack.setItemMeta(itemMeta);
		menu.setItem(slot, itemStack);
	}
	
	/**
	 * Sets the display name of an item stack.
	 * 
	 * @param itemStack The item to set the display name for.
	 * @param displayName The new display name.
	 */
	public static void setItemDisplayName(ItemStack itemStack, String displayName) {
		if(itemStack == null) {
			return;
		}
		ItemMeta itemMeta = itemStack.getItemMeta();
		Components.displayName(itemMeta, displayName);
		itemStack.setItemMeta(itemMeta);
	}
	
	/**
	 * Adds a lore line to an item stack.
	 * 
	 * @param itemStack The item stack which receives the lore.
	 * @param loreLine The lore line to add to the item stack.
	 * @param replaceAll If existing lore should be replaced.
	 */
	public static void addItemLore(ItemStack itemStack, String loreLine, boolean replaceAll) {
		if(itemStack == null || !itemStack.hasItemMeta()) {
			return;
		}
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lores = (replaceAll || !itemMeta.hasLore()) ? new ArrayList<>() : itemMeta.getLore();
		lores.add(loreLine);
		Components.lore(itemMeta, lores);
		itemStack.setItemMeta(itemMeta);
	}
	
	/**
	 * Rounds the given inventory size, to a valid chest size.
	 * 
	 * @param inventorySize The raw inventory size.
	 * 
	 * @return The rounded inventory size.
	 */
	public static int roundInventorySize(int inventorySize) {
		if(inventorySize <= 9) {
			inventorySize = 9;
		}
		else if(inventorySize <= 18) {
			inventorySize = 18;
		}
		else if(inventorySize <= 27) {
			inventorySize = 27;
		}
		else if(inventorySize <= 36) {
			inventorySize = 36;
		}
		else if(inventorySize <= 45) {
			inventorySize = 45;
		}
		else {
			inventorySize = 54;
		}
		return inventorySize;
	}
	
	/**
	 * Prevents interactions with static and cosmetic items on click.
	 * Also triggers scroll effects and the main menu opening.
	 * 
	 * @param event The inventory click event.
	 */
	public static void onSpecialItemInventoryClick(InventoryClickEvent event) {
		boolean numberKeyPressed = event.getClick().equals(ClickType.NUMBER_KEY);
		if(numberKeyPressed && !isHotbarItemInteractionValid(event)) {
			return;
		}
		ItemStack itemStack = event.getCurrentItem();
		if(itemStack == null) {
			return;
		}
		if(staticItems.contains(itemStack.getType())) {
			if(itemStack.getType().equals(Material.NETHER_STAR)) {
				WauzMenu.open((Player) event.getWhoClicked());
			}
			event.setCancelled(true);
			return;
		}
		String itemName = ItemUtils.hasDisplayName(itemStack) ? itemStack.getItemMeta().getDisplayName() : "";
		if(itemName.contains("Cosmetic Item")) {
			event.setCancelled(true);
			return;
		}
		if(!WauzPetEgg.tryToFeed(event)) {
			WauzScrolls.onScrollItemInteract(event, itemName);
		}
	}
	
	/**
	 * Prevents interactions with static and cosmetic items on hotbar number press.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @return If the event was not cancelled.
	 */
	private static boolean isHotbarItemInteractionValid(InventoryClickEvent event) {
		ItemStack itemStack = event.getClickedInventory().getItem(event.getHotbarButton());
		if(itemStack != null) {
			if(staticItems.contains(itemStack.getType())) {
				event.setCancelled(true);
				return false;
			}
			String itemName = ItemUtils.hasDisplayName(itemStack) ? itemStack.getItemMeta().getDisplayName() : "";
			if(itemName.contains("Cosmetic Item")) {
				event.setCancelled(true);
				return false;
			}
		}
		return true;
	}

	/**
	 * Prevents the dropping of static and cosmetic items.
	 * 
	 * @param event The drop event.
	 */
	public static void checkForStaticItemDrop(PlayerDropItemEvent event) {
		if(staticItems.contains(event.getItemDrop().getItemStack().getType())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Checks if the given item stack is a static item.
	 * No null check included.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If it is a static item.
	 */
	public static boolean checkForStaticItem(ItemStack itemStack) {
		return staticItems.contains(itemStack.getType());
	}

}
