package eu.wauz.wauzcore.menu.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * 
 * @author Wauzmons
 */
public interface WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	public String getInventoryId();
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	public default List<WauzMode> getGamemodes() {
		return new ArrayList<>();
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public default void openInstance(Player player) {
		throw new RuntimeException("Inventory cannot be opened directly!");
	}
	
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
