package eu.wauz.wauzcore.items.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import eu.wauz.wauzcore.system.annotations.Scroll;
import eu.wauz.wauzcore.system.economy.WauzShopActions;

/**
 * A scroll or similar that can be used on items in a player's inventory.
 * Can be dragged on a damaged equip item to repair it.
 * 
 * @author Wauzmons
 * 
 * @see WauzScrolls
 * @see WauzShopActions#repair(Player, org.bukkit.inventory.ItemStack, boolean)
 */
@Scroll
public class ScrollToughness implements InventoryScroll {

	/**
	 * @return The name of the scroll.
	 */
	@Override
	public String getScrollName() {
		return "Scroll of Toughness";
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
		return !itemName.contains("Scroll") && WauzShopActions.repair((Player) event.getWhoClicked(), event.getCurrentItem(), false);
	}

}
