package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import eu.wauz.wauzcore.data.api.ResourceConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Resource.yml files.
 * 
 * @author Wauzmons
 */
public class ResourceConfigurator extends ResourceConfigurationUtils {
	
// Resource Files

	/**
	 * @return The list of all resource names.
	 */
	public static List<String> getResourceNameList() {
		return ResourceConfigurationUtils.getResourceNameList();
	}
	
// General Parameters
	
	/**
	 * @param resourceName The name of the resource.
	 * 
	 * @return The type of the resource.
	 */
	public static String getResourceType(String resourceName) {
		return resourceConfigGetString(resourceName, "type");
	}
	
	/**
	 * @param resourceName The name of the resource.
	 * 
	 * @return The name of the drop table of the resource.
	 */
	public static String getResourceDropTable(String resourceName) {
		return resourceConfigGetString(resourceName, "droptable");
	}
	
	/**
	 * @param resourceName The name of the resource.
	 * 
	 * @return The respawn minutes of the resource.
	 */
	public static int getResourceRespawnMinutes(String resourceName) {
		return resourceConfigGetInt(resourceName, "respawnmins");
	}
	
	/**
	 * @param resourceName The name of the resource.
	 * 
	 * @return The list of spawn locations of the resource.
	 */
	public static List<Location> getResourceLocations(String resourceName) {
		List<Location> locations = new ArrayList<>();
		for(String locationString : resourceConfigGetStringList(resourceName, "locations")) {
			String[] locationParams = locationString.split(" ");
			World world = Bukkit.getWorld(locationParams[0]);
			float x = Float.parseFloat(locationParams[1]);
			float y = Float.parseFloat(locationParams[2]);
			float z = Float.parseFloat(locationParams[3]);
			locations.add(new Location(world, x, y, z));
		}
		return locations;
	}

}
