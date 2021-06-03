package eu.wauz.wauzcore.menu.abilities;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingItem;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRecipes;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.system.util.Components;

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
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, AbstractPassiveSkill skill, List<WauzCraftingItem> recipes, int page) {
		CraftingMenu craftingMenu = new CraftingMenu(skill, recipes, page);
		String levelText = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Level " + skill.getLevel();
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + skill.getPassiveName() + " " + levelText;
		Inventory menu = Components.inventory(craftingMenu, menuTitle, 9);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The crafting skill of the player.
	 */
	private AbstractPassiveSkill skill;
	
	/**
	 * The recipes of the crafting skill.
	 */
	private List<WauzCraftingItem> recipes;
	
	/**
	 * The index of the recipe page, starting at 0.
	 */
	private int page;
	
	/**
	 * Creates a new crafting menu instance.
	 * 
	 * @param skill The crafting skill of the player.
	 * @param recipes The recipes of the crafting skill.
	 * @param page The index of the recipe page, starting at 0.
	 */
	public CraftingMenu(AbstractPassiveSkill skill, List<WauzCraftingItem> recipes, int page) {
		this.skill = skill;
		this.recipes = recipes;
		this.page = page;
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
