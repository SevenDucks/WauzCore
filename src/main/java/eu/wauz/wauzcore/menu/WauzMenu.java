package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.abilities.AbilityMenu;
import eu.wauz.wauzcore.menu.collection.CollectionMenu;
import eu.wauz.wauzcore.menu.social.SocialMenu;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * The main menu of the game, that will by default show all sub menus of the MMORPG mode.
 * 
 * @author Wauzmons
 * 
 * @see WauzModeMenu
 */
public class WauzMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	public String getInventoryId() {
		return "mainmenu";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public void openInstance(Player player) {
		WauzMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * If it is opened in the hub, it will be redirected to the mode selection.
	 * Else all sub menus of the MMORPG mode plus a short information is shown.
	 * This includes the ability, collection and social menus.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzModeMenu#open(Player)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		if(WauzMode.inHub(player)) {
			WauzModeMenu.open(player);
			return;
		}
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Main Menu");
		
		ItemStack abilitesItemStack = HeadUtils.getAbilityItem();
		ItemMeta abilitiesItemMeta = abilitesItemStack.getItemMeta();
		abilitiesItemMeta.setDisplayName(ChatColor.GOLD + "Abilities");
		List<String> abilitiesLores = new ArrayList<String>();
		abilitiesLores.add(ChatColor.YELLOW + "Sub Menu Contents:");
		abilitiesLores.add(ChatColor.GRAY + "Travelling, Crafting, Disguises");
		abilitiesLores.add(ChatColor.GRAY + "Subclasses, Passive Skills, Perk Tree");
		abilitiesItemMeta.setLore(abilitiesLores);
		abilitesItemStack.setItemMeta(abilitiesItemMeta);
		menu.setItem(2, abilitesItemStack);
		
		ItemStack collectionItemStack = HeadUtils.getCollectionItem();
		ItemMeta collectionItemMeta = collectionItemStack.getItemMeta();
		collectionItemMeta.setDisplayName(ChatColor.GOLD + "Collection");
		List<String> collectionLores = new ArrayList<String>();
		collectionLores.add(ChatColor.YELLOW + "Sub Menu Contents:");
		collectionLores.add(ChatColor.GRAY + "Currency, Tabards, Questlog");
		collectionLores.add(ChatColor.GRAY + "Achievements, Bestiary, Pets");
		collectionItemMeta.setLore(collectionLores);
		collectionItemStack.setItemMeta(collectionItemMeta);
		menu.setItem(4, collectionItemStack);
		
		ItemStack socialItemStack = HeadUtils.getSocialItem();
		ItemMeta socialItemMeta = socialItemStack.getItemMeta();
		socialItemMeta.setDisplayName(ChatColor.GOLD + "Social");
		List<String> socialLores = new ArrayList<String>();
		socialLores.add(ChatColor.YELLOW + "Sub Menu Contents:");
		socialLores.add(ChatColor.GRAY + "Mailbox, Titles, Player vs Player");
		socialLores.add(ChatColor.GRAY + "Group, Guild, Friends");
		socialItemMeta.setLore(socialLores);
		socialItemStack.setItemMeta(socialItemMeta);
		menu.setItem(6, socialItemStack);

		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a sub menu was clicked, it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see AbilityMenu#open(Player)
	 * @see CollectionMenu#open(Player)
	 * @see SocialMenu#open(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Abilities")) {
			AbilityMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Collection")) {
			CollectionMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Social")) {
			SocialMenu.open(player);
		}
	}

}
