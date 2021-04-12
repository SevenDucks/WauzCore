package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * An event that lets a player sleep in an inn of a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenInn implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * The location of the inn to sleep in.
	 */
	private Location location;
	
	/**
	 * Creates an event to sleep in the inn of the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 * @param location The location of the inn to sleep in.
	 */
	public WauzPlayerEventCitizenInn(String citizenName, Location location) {
		this.citizenName = citizenName;
		this.location = location;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.getSelections().setWauzPlayerEventName("Change Home");
			playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventHomeChange(location, false));
			WauzDialog.open(player);
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
