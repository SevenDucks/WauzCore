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
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.scrolls.WauzScrolls;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzMenu;
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
	private static List<Material> staticItems = new ArrayList<>(Arrays.asList(
			Material.FILLED_MAP, Material.COMPASS, Material.NETHER_STAR, Material.BARRIER, Material.PLAYER_HEAD));
	
	/*
	 * Sets a main menu opener to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param index The slot to use in the inventory.
	 */
	public static void setMainMenuOpener(Inventory menu, int index) {
		ItemStack mainMenuItemStack = new ItemStack(Material.NETHER_STAR);
		ItemMeta mainMenuItemMeta = mainMenuItemStack.getItemMeta();
		mainMenuItemMeta.setDisplayName(ChatColor.GOLD + "Open Main Menu");
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
		ItemStack currencyItemStack = HeadUtils.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		currencyItemMeta.setDisplayName(ChatColor.GREEN + "Common Currency");
		List<String> lores = new ArrayList<String>();
		lores.add(Formatters.formatCoins(PlayerConfigurator.getCharacterCoins(player))
			+ ChatColor.YELLOW + " Coins");
		lores.add(ChatColor.RED + Formatters.INT.format(PlayerConfigurator.getCharacterSoulstones(player))
			+ ChatColor.YELLOW + " Soulstones");
		currencyItemMeta.setLore(lores);
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
		ItemStack currencyItemStack = HeadUtils.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		currencyItemMeta.setDisplayName(ChatColor.GREEN + "Global Currency");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GOLD + Formatters.INT.format(PlayerConfigurator.getTokens(player))
			+ ChatColor.YELLOW + " Tokens");
		currencyItemMeta.setLore(lores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(index, currencyItemStack);
	}
	
	/**
	 * Sets a trashcan to one or more inventory slots.
	 * 
	 * @param menu The menu inventory.
	 * @param indexes The slots to use in the inventory.
	 */
	public static void setTrashcan(Inventory menu, int... indexes) {
		ItemStack trashcanItemStack = new ItemStack(Material.BARRIER);
		ItemMeta trashcanItemMeta = trashcanItemStack.getItemMeta();
		trashcanItemMeta.setDisplayName(ChatColor.RED + "Trashcan");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "Drag Items here to destroy them.");
		trashcanItemMeta.setLore(lores);
		trashcanItemStack.setItemMeta(trashcanItemMeta);
		for(int index : indexes) {
			menu.setItem(index, trashcanItemStack);
		}
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
		soonItemMeta.setDisplayName(ChatColor.RED + "Coming Soon");
		if(StringUtils.isNotBlank(lore)) {
			List<String> lores = new ArrayList<String>();
			lores.add(lore);
			soonItemMeta.setLore(lores);
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
		borderItemMeta.setDisplayName(" ");
		borderItemStack.setItemMeta(borderItemMeta);
		
		for(int slot = 0; slot < menu.getSize(); slot++) {
			if(menu.getItem(slot) == null)
				menu.setItem(slot, borderItemStack);
		}
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
		itemMeta.setDisplayName(displayName);
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
		itemMeta.setLore(lores);
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
		WauzScrolls.onScrollItemInteract(event, itemName);
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
	 * Prevents the swapping of static and cosmetic items.
	 * 
	 * @param event The swap event.
	 */
	public static void checkForStaticItemSwap(PlayerSwapHandItemsEvent event) {
		Material mainHandType = event.getMainHandItem().getType();
		Material offHandType = event.getOffHandItem().getType();
		if(staticItems.contains(mainHandType) || staticItems.contains(offHandType)) {
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
