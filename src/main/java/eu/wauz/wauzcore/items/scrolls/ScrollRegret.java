package eu.wauz.wauzcore.items.scrolls;

import org.bukkit.event.inventory.InventoryClickEvent;

import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.system.annotations.Scroll;

/**
 * A scroll or similar that can be used on items in a player's inventory.
 * Can be dragged on an equip item to destroy socketed runes and skillgems.
 * 
 * @author Wauzmons
 * 
 * @see WauzScrolls
 * @see WauzEquipment#clearAllSockets(InventoryClickEvent)
 */
@Scroll
public class ScrollRegret implements InventoryScroll {

	/**
	 * @return The name of the scroll.
	 */
	@Override
	public String getScrollName() {
		return "Scroll of Regret";
	}

	/**
	 * Uses the scroll on the clicked item.
	 * 
	 * @param event The inventory event.
	 * @param itemName The name of the item, the scroll is used on.
	 * 
	 * @return If the usage was successful and the scroll should be consumed.
	 */
	@Override
	public boolean use(InventoryClickEvent event, String itemName) {
		return !itemName.contains("Scroll") && WauzEquipment.clearAllSockets(event);
	}

}
