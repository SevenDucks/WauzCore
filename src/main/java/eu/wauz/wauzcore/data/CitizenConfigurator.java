package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Citizen.yml files.
 * 
 * @author Wauzmons
 */
public class CitizenConfigurator extends GlobalConfigurationUtils {
	
// Citizen Files

	/**
	 * @return The list of all citizen names.
	 */
	public static List<String> getCitizenNameList() {
		return getCitizenNameList();
	}

// General Parameters
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The lines of text above the citizen's head.
	 */
	public static List<String> getNameLines(String citizen) {
		return citizenConfigGetStringList(citizen, "name");
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The location of the citizen.
	 */
	public static Location getLocation(String citizen) {
		String worldName = citizenConfigGetString(citizen, "world");
				
		List<Double> coords = new ArrayList<>();
		for(String coord : citizenConfigGetString(citizen, "location").split(" ")) {
			coords.add(Double.parseDouble(coord));
		}
		return new Location(Bukkit.getWorld(worldName), coords.get(0), coords.get(1), coords.get(2));
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The id of the citizen's skin.
	 */
	public static int getSkinId(String citizen) {
		return citizenConfigGetInt(citizen, "skinid");
	}
	
}
