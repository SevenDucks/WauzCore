package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.ShopMenu;

/**
 * An event that lets a player view a shop of a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenShop implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * The name of the shop to display.
	 */
	private String shopName;
	
	/**
	 * Creates an event to view the shop of the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 * @param shopName The name of the shop to display.
	 */
	public WauzPlayerEventCitizenShop(String citizenName, String shopName) {
		this.citizenName = citizenName;
		this.shopName = shopName;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see ShopMenu#open(Player, String, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			ShopMenu.open(player, shopName, citizenName);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while interacting with " + citizenName + "!");
			player.closeInventory();
			return false;
		}
	}

}
