package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Represents a virtual chest, to hand out loot to a player.
 * 
 * @author Wauzmons
 */
public class LootContainer implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Shows all the given loot item stacks, for the player to claim.
	 * 
	 * @param player The player that should view the inventory.
	 * @param lootItemStacks The items to randomly scatter around the loot container.
	 */
	public static void open(Player player, List<ItemStack> lootItemStacks) {
		LootContainer lootContainer = new LootContainer();
		WauzInventoryHolder holder = new WauzInventoryHolder(lootContainer);
		Inventory menu = Bukkit.createInventory(holder, 27, ChatColor.BLACK + "" + ChatColor.BOLD + "Loot Chest");
		lootContainer.setLootChest(menu);
		
		Random random = new Random();
		List<Integer> freeSlots = new ArrayList<>();
		for(int index = 0; index < menu.getSize(); index++) {
			freeSlots.add(Integer.valueOf(index));
		}
		
		for(ItemStack lootItemStack : lootItemStacks) {
			Integer slot = freeSlots.get(random.nextInt(freeSlots.size()));
			menu.setItem(slot, lootItemStack);
			freeSlots.remove(slot);
		}
		
		player.openInventory(menu);
	}
	
	/**
	 * The inventory instance to hold the loot.
	 */
	private Inventory lootChest;

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The event will be normally executed, just like inside a chest.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		
	}
	
	/**
	 * Cleans up everything, so the inventory can be closed.
	 * All the contents of the container will be dropped.
	 * 
	 * @param event The inventory close event.
	 */
	@Override
	public void destroyInventory(InventoryCloseEvent event) {
		Location dropLocation = event.getPlayer().getLocation();
		for(ItemStack lootItemStack : lootChest.getContents()) {
			if(lootItemStack != null) {
				dropLocation.getWorld().dropItemNaturally(dropLocation, lootItemStack);
			}
		}
	}

	/**
	 * @param lootChest The new inventory instance to hold the loot.
	 */
	public void setLootChest(Inventory lootChest) {
		this.lootChest = lootChest;
	}

}
