package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * Collection of methods for reading and writing data in instance config files.
 * 
 * @author Wauzmons
 */
public class InstanceConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all instance names by iterating through all instance configs.
	 * 
	 * @return A list of all instance names.
	 */
	protected static List<String> getInstanceNameList() {
		File instanceDataFolder = new File(core.getDataFolder(), "InstanceData/");
		return WauzFileUtils.findRelativePathsRecursive(instanceDataFolder, "");
	}
	
	/**
	 * Gets a string from an instance config.
	 * 
	 * @param instance The name of the instance config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String instanceConfigGetString(String instance, String path) {
		File instanceDataFile = new File(core.getDataFolder(), "InstanceData/" + instance + ".yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getString(path);
	}
	
	/**
	 * Gets a string list from an instance config.
	 * 
	 * @param instance The name of the instance config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> instanceConfigGetStringList(String instance, String path) {
		File instanceDataFile = new File(core.getDataFolder(), "InstanceData/" + instance + ".yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getStringList(path);
	}
	
	/**
	 * Gets an int from an instance config.
	 * 
	 * @param instance The name of the instance config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int instanceConfigGetInt(String instance, String path) {
		File instanceDataFile = new File(core.getDataFolder(), "InstanceData/" + instance + ".yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getInt(path);
	}

}
