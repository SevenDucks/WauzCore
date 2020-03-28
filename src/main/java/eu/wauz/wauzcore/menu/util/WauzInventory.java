package eu.wauz.wauzcore.menu.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * 
 * @author Wauzmons
 */
public interface WauzInventory {
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * 
	 * @param event The inventory click event.
	 */
	public void selectMenuPoint(InventoryClickEvent event);
	
	/**
	 * Cleans up everything, so the inventory can be closed.
	 * 
	 * @param event The inventory close event.
	 */
	public default void destroyInventory(InventoryCloseEvent event) {
		
	}
	
}
