package eu.wauz.wauzcore.items.scrolls;

import org.bukkit.event.inventory.InventoryClickEvent;

import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.system.annotations.Scroll;

/**
 * A scroll or similar that can be used on items in a player's inventory.
 * Represensts a rune of unknown type. Can be dragged into equipment to insert it.
 * 
 * @author Wauzmons
 * 
 * @see WauzScrolls
 * @see WauzEquipment#insertRune(InventoryClickEvent)
 */
@Scroll
public class ScrollGenericRune implements InventoryScroll {

	/**
	 * @return The name of the scroll.
	 */
	@Override
	public String getScrollName() {
		return "Generic Rune";
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
		return !itemName.contains("Unidentified") && WauzEquipment.insertRune(event);
	}

}
