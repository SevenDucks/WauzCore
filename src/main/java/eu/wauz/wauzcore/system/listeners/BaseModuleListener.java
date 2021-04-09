package eu.wauz.wauzcore.system.listeners;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A listener to catch events, when the core module is inactive.
 * 
 * @author Wauzmons
 */
public class BaseModuleListener implements Listener {
	
	/**
	 * Called when a player interacts with an inventory menu.
	 * If the inventory has a fitting inventory holder, it tries to select a menu point.
	 * 
	 * @param event The click event.
	 * 
	 * @see WauzInventoryHolder#selectMenuPoint(InventoryClickEvent)
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String inventoryName = ChatColor.stripColor(Components.title(player.getOpenInventory()));
		String inventoryType = event.getInventory().getType().toString();
		WauzDebugger.log(player, "You clicked in Inventory: " + inventoryName + " " + inventoryType);
		
		if(event.getInventory().getHolder() instanceof WauzInventoryHolder) {
			WauzInventoryHolder holder = (WauzInventoryHolder) event.getInventory().getHolder();
			WauzDebugger.log(player, "Selected Option in "
					+ StringUtils.substringAfterLast(holder.getInventoryName(), "."));
			holder.selectMenuPoint(event);
		}
	}
	
	/**
	 * Called when a player closes an inventory menu.
	 * If the inventory has a fitting inventory holder, it tries to properly destroy the menu contents.
	 * 
	 * @param event The close event.
	 * 
	 * @see WauzInventoryHolder#destroyInventory(InventoryCloseEvent)
	 */
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		String inventoryName = ChatColor.stripColor(Components.title(player.getOpenInventory()));
		String inventoryType = event.getInventory().getType().toString();
		WauzDebugger.log(player, "You closed the Inventory: " + inventoryName + " " + inventoryType);
		
		if(event.getInventory().getHolder() instanceof WauzInventoryHolder) {
			WauzInventoryHolder holder = (WauzInventoryHolder) event.getInventory().getHolder();
			holder.destroyInventory(event);
		}
	}

}
