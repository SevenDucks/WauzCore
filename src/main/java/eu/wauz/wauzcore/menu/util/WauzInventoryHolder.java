package eu.wauz.wauzcore.menu.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * An inventory holder for storing custom event inventories like menus.
 * 
 * @author Wauzmons
 */
public class WauzInventoryHolder implements InventoryHolder {
	
	/**
	 * An inventory that can be used as menu or for other custom interaction mechanics.
	 */
	private WauzInventory inventory;
	
	/**
	 * Creates a new inventory holder for the given custom inventory.
	 * 
	 * @param inventory The custom inventory.
	 */
	public WauzInventoryHolder(WauzInventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * Get the object's inventory.
	 * 
	 * @return The inventory.
	 */
	@Override
	@Deprecated
	public Inventory getInventory() {
		return null;
	}
	
	/**
	 * Gets the name of the custom inventory inside this holder.
	 * 
	 * @return The name of the inventory class.
	 */
	public String getInventoryName() {
		return inventory.getClass().getName();
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * 
	 * @param event The inventory click event.
	 */
	public void selectMenuPoint(InventoryClickEvent event) {
		inventory.selectMenuPoint(event);
	}
	
}
