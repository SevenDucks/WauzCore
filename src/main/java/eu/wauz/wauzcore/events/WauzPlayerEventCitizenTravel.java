package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.WauzWaypoint;

/**
 * An event that lets a player travel to a waypoint of a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenTravel implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * The waypoint to teleport to.
	 */
	private WauzWaypoint waypoint;
	
	/**
	 * Creates an event to travel to a waypoint of the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 * @param waypoint The waypoint to teleport to.
	 */
	public WauzPlayerEventCitizenTravel(String citizenName, WauzWaypoint waypoint) {
		this.citizenName = citizenName;
		this.waypoint = waypoint;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzTeleporter#waypointTeleport(Player, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzTeleporter.waypointTeleport(player, waypoint.getWaypointKey());
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
