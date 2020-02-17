package eu.wauz.wauzcore.events;

import java.util.List;

import org.bukkit.entity.Player;

/**
 * An event that lets a player talk to a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenTalk implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * The lines the citizen should speak.
	 */
	private List<String> messages;
	
	/**
	 * Creates an event to talk to the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 * @param messages The lines the citizen should speak.
	 */
	public WauzPlayerEventCitizenTalk(String citizenName, List<String> messages) {
		this.citizenName = citizenName;
		this.messages = messages;
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
