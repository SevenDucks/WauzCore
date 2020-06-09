package eu.wauz.wauzcore.menu.social;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.WauzTitle;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for selecting chat titles.
 * 
 * @author Wauzmons
 *
 * @see WauzTitle
 */
public class TitleMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "titles";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		TitleMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * A list of all titles to choose from will be shown.
	 * The menu lets the player choose from all unlocked titles.
	 * Locked titles can be bought here aswell, if requirements are met.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new FriendsMenu());
		Inventory menu = Bukkit.createInventory(holder, 18, ChatColor.BLACK + "" + ChatColor.BOLD + "Title Collection");
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If an already owned title is clicked, it will be selected.
	 * If another title is clicked and requirements are met, it will be bought.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
	}

}
