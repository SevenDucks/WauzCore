package eu.wauz.wauzcore.items.scrolls;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * A scroll or similar that can be used on items in a player's inventory.
 * 
 * @author Wauzmons
 * 
 * @see WauzScrolls
 */
public interface InventoryScroll {
	
	/**
	 * @return The name of the scroll.
	 */
	public String getScrollName();
	
	/**
	 * Uses the scroll on the clicked item.
	 * 
	 * @param event The inventory event.
	 * @param itemName The name of the item, the scroll is used on.
	 * 
	 * @return If the usage was successful and the scroll should be consumed.
	 */
	public boolean use(InventoryClickEvent event, String itemName);

}
