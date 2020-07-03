package eu.wauz.wauzcore.items.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import eu.wauz.wauzcore.items.identifiers.WauzIdentifier;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * A scroll or similar that can be used on items in a player's inventory.
 * Can be dragged on an unidentified item to identify it.
 * 
 * @author Wauzmons
 * 
 * @see WauzScrolls
 * @see WauzIdentifier#identify(InventoryClickEvent, String)
 */
public class ScrollWisdom implements InventoryScroll {

	/**
	 * @return The name of the scroll.
	 */
	@Override
	public String getScrollName() {
		return "Scroll of Wisdom";
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
		if(itemName.contains("Unidentified")) {
			WauzIdentifier.identify(event, itemName);
			AchievementTracker.addProgress((Player) event.getWhoClicked(), WauzAchievementType.IDENTIFY_ITEMS, 1);
			return true;
		}
		return false;
	}

}
