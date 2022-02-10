package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Waypoints.yml.
 * 
 * @author Wauzmons
 */
public class WaypointConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @return The keys of all waypoints.
	 */
	public static List<String> getAllWaypointKeys() {
		return new ArrayList<>(mainConfigGetKeys("Waypoints", null));
	}
	
	/**
	 * @param waypoint The key of the waypoint.
	 * 
	 * @return The chat display name of the waypoint.
	 */
	public static String getWaypointName(String waypoint) {
		return mainConfigGetString("Waypoints", waypoint + ".name");
	}
	
	/**
	 * @param waypoint The key of the waypoint.
	 * 
	 * @return The world name of the waypoint.
	 */
	public static String getWaypointWorldName(String waypoint) {
		return mainConfigGetString("Waypoints", waypoint + ".world");
	}
	
	/**
	 * @param waypoint The key of the waypoint.
	 * 
	 * @return The location of the waypoint.
	 */
	public static Location getWaypointLocation(String waypoint) {
		String worldName = getWaypointWorldName(waypoint);
		return mainConfigGetLocation("Waypoints", waypoint + ".coords", worldName);
	}
	
	/**
	 * Adds a new waypoint.
	 * 
	 * @param waypoint The key of the waypoint.
	 * @param location The location of the waypoint.
	 */
	public static void addWaypoint(String waypoint, Location location) {
		mainConfigSet("Waypoints", waypoint + ".name", waypoint);
		mainConfigSet("Waypoints", waypoint + ".world", location.getWorld().getName());
		String locationString = location.getX() + " " + location.getY() + " " + location.getZ();
		mainConfigSet("Waypoints", waypoint + ".coords", locationString);
	}

}
