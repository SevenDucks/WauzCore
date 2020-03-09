package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * An event that lets a player execute a command of a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenCommand implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * The name of the command to execute.
	 */
	private String command;
	
	/**
	 * Creates an event to execute the command of the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 * @param command The name of the command to execute.
	 */
	public WauzPlayerEventCitizenCommand(String citizenName, String command) {
		this.citizenName = citizenName;
		this.command = command;
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
			return player.performCommand(command);
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while interacting with " + citizenName + "!");
			player.closeInventory();
			return false;
		}
	}

}
