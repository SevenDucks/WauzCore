package eu.wauz.wauzcore.menu.social;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.events.WauzPlayerEventUnfriend;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerFriends;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for viewing and managing friends.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerFriends
 */
public class FriendsMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "friends";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		FriendsMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * A list of all the player's friends will be shown.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new FriendsMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Friends List");
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a friend is right clicked, a dialog to unfriend them is shown.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerEventUnfriend
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !event.getClick().toString().contains("RIGHT")) {
			return;
		}
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta sm = (SkullMeta) clicked.getItemMeta();
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			if(sm.getOwningPlayer() == null || playerData == null) {
				return;
			}
			playerData.setWauzPlayerEventName("Unfriend Player");
			playerData.setWauzPlayerEvent(new WauzPlayerEventUnfriend(sm.getOwningPlayer()));
			WauzDialog.open(player, clicked);
		}
	}

}
