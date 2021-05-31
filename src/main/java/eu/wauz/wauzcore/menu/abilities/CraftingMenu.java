package eu.wauz.wauzcore.menu.abilities;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingItem;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRecipes;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the job menu, that is used to craft items.
 * 
 * @author Wauzmons
 * 
 * @see WauzCraftingRecipes
 */
public class CraftingMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "crafting";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the list of crafting recipes including the required materials.
	 * Navigation buttons can be used to switch between pages.
	 * 
	 * @param player The player that should view the inventory.
	 * @param skill The crafting skill of the player.
	 * @param recipes The recipes of the crafting skill.
	 * @param page The index of the recipe page, starting at 0.
	 */
	public static void open(Player player, AbstractPassiveSkill skill, List<WauzCraftingItem> recipes, int page) {
		
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and tries to craft the clicked item.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		ItemStack itemOnCursor = player.getItemOnCursor();
		int slot = event.getRawSlot();
		
		if(slot < 27) {
			event.setCancelled(true);
		}		
	}

}
