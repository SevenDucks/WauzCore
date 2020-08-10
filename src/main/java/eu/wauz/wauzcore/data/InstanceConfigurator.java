package eu.wauz.wauzcore.data;

import java.util.List;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Instance.yml files.
 * 
 * @author Wauzmons
 */
public class InstanceConfigurator extends GlobalConfigurationUtils {
	
// Instance Files

	/**
	 * @return The list of instance shop names.
	 */
	public static List<String> getInstanceNameList() {
		return GlobalConfigurationUtils.getInstanceNameList();
	}

// General Parameters
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The name of the world template of the instance.
	 */
	public static String getWorldTemplateName(String instanceName) {
		return instanceConfigGetString(instanceName, "world");
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The maximum players of the instance.
	 */
	public static int getMaximumPlayers(String instanceName) {
		int maxPlayers = instanceConfigGetInt(instanceName, "maxplayers");
		return maxPlayers > 0 ? maxPlayers : 5;
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The maximum deaths per player of the instance.
	 */
	public static int getMaximumDeaths(String instanceName) {
		int maxDeaths = instanceConfigGetInt(instanceName, "maxdeaths");
		return maxDeaths > 0 ? maxDeaths : 3;
	}
	
// Type Specific
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The type of the instance.
	 */
	public static String getInstanceType(String instanceName) {
		String instanceType = instanceConfigGetString(instanceName, "type");
		return StringUtils.isNotBlank(instanceType) ? instanceType : "Unknown";
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The list of key names of the instance.
	 */
	public static List<String> getKeyNameList(String instanceName) {
		return instanceConfigGetStringList(instanceName, "keys");
	}

}
