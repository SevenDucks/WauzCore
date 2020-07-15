package eu.wauz.wauzcore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.WaypointConfigurator;

/**
 * A world waypoint, generated from a waypoint config file.
 * 
 * @author Wauzmons
 */
public class WauzWaypoint {
	
	/**
	 * A map of waypoints, indexed by name.
	 */
	private static Map<String, WauzWaypoint> waypointMap = new HashMap<>();
	
	/**
	 * Initializes all waypoints from the config and fills the internal waypoint map.
	 * 
	 * @see WaypointConfigurator#getAllWaypointKeys()
	 */
	public static void init() {
		for(String waypointKey : WaypointConfigurator.getAllWaypointKeys()) {
			waypointMap.put(waypointKey, new WauzWaypoint(waypointKey));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + waypointMap.size() + " Waypoints!");
	}
	
	/**
	 * @param waypointKey A waypoint key.
	 * 
	 * @return The waypoint with that key or null.
	 */
	public static WauzWaypoint getWaypoint(String waypointKey) {
		return waypointMap.get(waypointKey);
	}
	
	/**
	 * @return A list of all waypoint keys.
	 */
	public static List<String> getAllWaypointKeys() {
		return new ArrayList<>(waypointMap.keySet());
	}
	
	/**
	 * The key of the waypoint.
	 */
	private String waypointKey;
	
	/**
	 * The chat display name of the waypoint.
	 */
	private String waypointDisplayName;
	
	/**
	 * The location of the waypoint.
	 */
	private Location waypointLocation;
	
	/**
	 * Constructs a waypoint, based on the waypoint file in the /WauzCore folder.
	 * 
	 * @param waypointKey The key of the waypoint.
	 */
	public WauzWaypoint(String waypointKey) {
		this.waypointKey = waypointKey;
		waypointDisplayName = WaypointConfigurator.getWaypointName(waypointKey);
		waypointLocation = WaypointConfigurator.getWaypointLocation(waypointKey);
	}

	/**
	 * @return The key of the waypoint.
	 */
	public String getWaypointKey() {
		return waypointKey;
	}

	/**
	 * @return The chat display name of the waypoint.
	 */
	public String getWaypointDisplayName() {
		return waypointDisplayName;
	}

	/**
	 * @return The location of the waypoint.
	 */
	public Location getWaypointLocation() {
		return waypointLocation;
	}
	
}
