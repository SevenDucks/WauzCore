package eu.wauz.wauzcore.menu.abilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the skill menu, that is used for assigning active skills to action slots.
 * 
 * @author Wauzmons
 *
 * @see PlayerPassiveSkillConfigurator
 */
public class SkillAssignMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "skill-assign";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		SkillAssignMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Lets the player select a slot to assign a skill to.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new SkillAssignMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Assign Active Skills");
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and lets the player select a skill for the selected slot.
	 * Also handles the event, to open the skill menu, to learn new skills.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
//		ItemStack clicked = event.getCurrentItem();
//		final Player player = (Player) event.getWhoClicked();
	}

}
