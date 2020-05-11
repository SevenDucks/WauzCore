package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * A generic menu, to show a confirm dialog for the cached player event.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerEvent
 */
public class WauzDialog implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	public String getInventoryId() {
		return "dialog";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public void openInstance(Player player) {
		throw new RuntimeException("Inventory cannot be opened directly!");
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows options to confirm or decline the event from the player data.
	 * Also shows the event title from the player date in the menu name.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzDialog#open(Player, ItemStack)
	 */
	public static void open(Player player) {
		open(player, null);
	}

	/**
	 * Opens the menu for the given player.
	 * Shows options to confirm or decline the event from the player data.
	 * Also shows the event title from the player date in the menu name.
	 * 
	 * @param player The player that should view the inventory.
	 * @param infoItemStack An optional item stack, that can old additional information about the event.
	 * 
	 * @see WauzPlayerData#getWauzPlayerEventName()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, ItemStack infoItemStack) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzDialog());
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Confirm: "
				+ playerData.getWauzPlayerEventName());
		
		ItemStack confirmItemStack = HeadUtils.getConfirmItem();
		ItemMeta confirmItemMeta = confirmItemStack.getItemMeta();
		confirmItemMeta.setDisplayName(ChatColor.GREEN + "CONFIRM");
		confirmItemStack.setItemMeta(confirmItemMeta);
		menu.setItem(0, confirmItemStack);
		
		ItemStack declineItemStack = HeadUtils.getDeclineItem();
		ItemMeta declineItemMeta = declineItemStack.getItemMeta();
		declineItemMeta.setDisplayName(ChatColor.RED + "DECLINE");
		declineItemStack.setItemMeta(declineItemMeta);
		menu.setItem(8, declineItemStack);
		
		if(infoItemStack != null) {
			menu.setItem(4, infoItemStack);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If confirm is selected, the event from the player data will be executed.
	 * Else the inventory will be closed.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerData#getWauzPlayerEvent()
	 * @see WauzPlayerEvent#execute(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData == null || clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "CONFIRM")) {
			playerData.getWauzPlayerEvent().execute(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "DECLINE")) {
			player.closeInventory();
		}
	}
	
}
