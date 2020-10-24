package eu.wauz.wauzcore.menu.collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that is used to breed pets.
 * 
 * @author Wauzmons
 *
 * @see WauzPetEgg
 */
public class BreedingMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "breeding";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		throw new RuntimeException("Inventory cannot be opened directly!");
		
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows an interface to breed pets.
	 * 
	 * @param player The player that should view the inventory.
	 * @param exp The taming experience of the player.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, int exp) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new BestiaryMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Bestiary - Categories");
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		// TODO Auto-generated method stub
		
	}

}
