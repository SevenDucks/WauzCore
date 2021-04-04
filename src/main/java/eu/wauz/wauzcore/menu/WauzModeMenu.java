package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.kyori.adventure.text.Component;

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
	 * Shows three hardcoded modes to choose: "DropGuys", "MMORPG", "OneBlock".
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzModeMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, Component.text(ChatColor.BLACK + "" + ChatColor.BOLD + "Select a Gamemode!"));
		
		ItemStack modeDropGuysItemStack = new ItemStack(Material.HOPPER);
		ItemMeta modeDropGuysItemMeta = modeDropGuysItemStack.getItemMeta();
		modeDropGuysItemMeta.displayName(Component.text(ChatColor.DARK_RED + "ALPHA " + ChatColor.RED + "DropGuys"));
		modeDropGuysItemStack.setItemMeta(modeDropGuysItemMeta);
		menu.setItem(2, modeDropGuysItemStack);
		
		ItemStack modeMmoRpgItemStack = new ItemStack(Material.DRAGON_HEAD);
		ItemMeta modeMmoRpgItemMeta = modeMmoRpgItemStack.getItemMeta();
		modeMmoRpgItemMeta.displayName(Component.text(ChatColor.DARK_RED + "ALPHA " + ChatColor.DARK_PURPLE + "MMORPG"));
		modeMmoRpgItemStack.setItemMeta(modeMmoRpgItemMeta);
		menu.setItem(4, modeMmoRpgItemStack);
		
		ItemStack modeOneBlockItemStack = new ItemStack(Material.GRASS_BLOCK);
		ItemMeta modeOneBlockItemMeta = modeOneBlockItemStack.getItemMeta();
		modeOneBlockItemMeta.displayName(Component.text(ChatColor.GOLD + "OneBlock and Survival"));
		modeOneBlockItemStack.setItemMeta(modeOneBlockItemMeta);
		menu.setItem(6, modeOneBlockItemStack);
		
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
		
		String modeName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
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
		if(modeName.equals("MMORPG")) {
			CharacterSlotMenu.open(player, WauzMode.MMORPG);
		}
		else if(modeName.equals("OneBlock and Survival")) {
			CharacterSlotMenu.open(player, WauzMode.SURVIVAL);
		}
		else if(modeName.equals("DropGuys")) {
			CharacterSlotMenu.open(player, WauzMode.ARCADE);
		}
	}

}
