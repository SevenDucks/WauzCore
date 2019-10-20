package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerData;

/**
 * An Event bound to a player data.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData
 */
public interface WauzPlayerEvent {
	
	/**
	 * A direct reference to the main class.
	 */
	static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 */
	public boolean execute(Player player);

}
