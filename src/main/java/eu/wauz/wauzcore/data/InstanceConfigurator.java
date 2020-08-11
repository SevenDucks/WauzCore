package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.system.instances.WauzInstanceType;

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
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The display title of the instance.
	 */
	public static String getDisplayTitle(String instanceName) {
		return instanceConfigGetString(instanceName, "title");
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The display subtitle of the instance.
	 */
	public static String getDisplaySubtitle(String instanceName) {
		return instanceConfigGetString(instanceName, "subtitle");
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The soundtrack file name of the instance.
	 */
	public static String getSoundtrack(String instanceName) {
		return instanceConfigGetString(instanceName, "music");
	}
	
// Type Specific
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The type of the instance.
	 */
	public static WauzInstanceType getInstanceType(String instanceName) {
		String typeName = instanceConfigGetString(instanceName, "type");
		return WauzInstanceType.valueOf(typeName);
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
