package eu.wauz.wauzcore.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
	 * @param command The location of the inn to sleep in.
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
		return false;
	}

}
