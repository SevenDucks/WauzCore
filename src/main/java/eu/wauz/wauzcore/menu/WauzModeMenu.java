package eu.wauz.wauzcore.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.util.BungeeUtils;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * The main menu of the hub, that will let the player select a gamemode to play.
 * 
 * @author Wauzmons
 *
 * @see WauzMode
 */
public class WauzModeMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "modes";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows hardcoded modes to choose from.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Select a Gamemode!";
		Inventory menu = Components.inventory(new WauzModeMenu(), menuTitle, 27);
		
		ItemStack modeDropGuysItemStack = new ItemStack(Material.HOPPER);
		MenuUtils.setItemDisplayName(modeDropGuysItemStack, ChatColor.DARK_RED + "ALPHA " + ChatColor.RED + "DropGuys");
		menu.setItem(10, modeDropGuysItemStack);
		
		ItemStack modeMmoRpgItemStack = new ItemStack(Material.DRAGON_HEAD);
		MenuUtils.setItemDisplayName(modeMmoRpgItemStack, ChatColor.DARK_RED + "ALPHA " + ChatColor.DARK_PURPLE + "MMORPG");
		menu.setItem(12, modeMmoRpgItemStack);
		
		ItemStack modeOneBlockItemStack = new ItemStack(Material.GRASS_BLOCK);
		MenuUtils.setItemDisplayName(modeOneBlockItemStack, ChatColor.DARK_RED + "BETA " + ChatColor.GOLD + "OneBlock");
		menu.setItem(14, modeOneBlockItemStack);
		
		ItemStack modeEyiorielItemStack = new ItemStack(Material.END_PORTAL_FRAME);
		MenuUtils.setItemDisplayName(modeEyiorielItemStack, ChatColor.GOLD + "Connection Test");
		menu.setItem(16, modeEyiorielItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If the clicked item has a display name, it will be used as selected mode name.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzModeMenu#selectMode(Player, String)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		
		if(clicked == null || !ItemUtils.hasDisplayName(clicked)) {
			return;
		}
		
		String modeName = ChatColor.stripColor(Components.displayName(clicked.getItemMeta()));
		selectMode((Player) event.getWhoClicked(), modeName);
	}
	
	/**
	 * Starts the given mode for the player.
	 * For the character selection for the chosen mode will be shown.
	 * 
	 * @param player The player who selected the mode.
	 * @param modeName The name of the mode that has been selected.
	 * 
	 * @see CharacterSlotMenu#open(Player, WauzMode)
	 */
	public static void selectMode(Player player, String modeName) {
		if(modeName == null) {
			return;
		}
		modeName = modeName.replace("ALPHA ", "");
		modeName = modeName.replace("BETA ", "");
		
		if(modeName.equals("MMORPG")) {
			CharacterSlotMenu.open(player, WauzMode.MMORPG);
		}
		else if(modeName.equals("OneBlock")) {
			CharacterSlotMenu.open(player, WauzMode.SURVIVAL);
		}
		else if(modeName.equals("DropGuys")) {
			CharacterSlotMenu.open(player, WauzMode.ARCADE);
		}
		else if(modeName.equals("Connection Test")) {
			if(WauzRank.getRank(player).isStaff()) {
				BungeeUtils.connect(player, "test");
			}
			else {
				player.sendMessage(ChatColor.RED + "This feature is for staff only!");
			}
		}
	}

}
