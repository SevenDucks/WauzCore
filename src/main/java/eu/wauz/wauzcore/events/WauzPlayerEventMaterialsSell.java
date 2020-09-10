package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.system.economy.WauzShopActions;

/**
 * An event to let a player sell the materials in an inventory.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventMaterialsSell implements WauzPlayerEvent {
	
	/**
	 * The name of the inventory to sell the contents of.
	 */
	private String inventoryName;

	/**
	 * Creates an event to buy a title.
	 * 
	 * @param inventoryName The name of the inventory to sell the contents of.
	 */
	public WauzPlayerEventMaterialsSell(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	
	/**
	 * Executes the event for the given player.
	 * Sells all contents of the bag section, with the fitting inventory name.
	 * Re-opens the bag section, after everything was sold.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzShopActions#sell(Player, ItemStack, boolean)
	 * @see MaterialPouch#getInventory(Player, String)
	 * @see MaterialPouch#open(Player, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			Inventory inventory = MaterialPouch.getInventory(player, inventoryName);
			for(ItemStack itemToSell : inventory.getContents()) {
				if(itemToSell != null) {
					WauzShopActions.sell(player, itemToSell, false);
				}
			}
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
			player.sendMessage(ChatColor.GREEN + "All bag contents were sold!");
			MaterialPouch.open(player, inventoryName);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while selling bag contents!");
			player.closeInventory();
			return false;
		}
	}

}
