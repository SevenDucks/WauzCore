package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the RandomMessages.yml.
 * 
 * @author Wauzmons
 */
public class RandomMessageConfiguration extends GlobalConfigurationUtils {
	
	/**
	 * @return All possible randomized message parts for the message of the day.
	 */
	public static List<String> getMotds() {
		return mainConfigGetStringList("RandomMessages", "motds");
	}
	
	/**
	 * @return All possible tip messages.
	 */
	public static List<String> getTips() {
		return mainConfigGetStringList("RandomMessages", "tips");
	}
	
	/**
	 * @return All possible equipment prefixes.
	 */
	public static List<String> getEquipPrefixes() {
		return mainConfigGetStringList("RandomMessages", "equipprefixes");
	}

}
