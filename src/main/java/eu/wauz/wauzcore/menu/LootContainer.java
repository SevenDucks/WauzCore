package eu.wauz.wauzcore.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import eu.wauz.wauzcore.menu.util.WauzInventory;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Represents a virtual chest, to hand out loot to a player.
 * 
 * @author Wauzmons
 */
public class LootContainer implements WauzInventory {

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The event will be normally executed, just like inside a chest.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		
	}
	
	/**
	 * Cleans up everything, so the inventory can be closed.
	 * All the contents of the container will be dropped.
	 * 
	 * @param event The inventory close event.
	 */
	@Override
	public void destroyInventory(InventoryCloseEvent event) {
		
	}

}
