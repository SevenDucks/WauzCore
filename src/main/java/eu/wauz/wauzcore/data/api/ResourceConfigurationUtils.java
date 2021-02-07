package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * Collection of methods for reading and writing data in resource config files.
 * 
 * @author Wauzmons
 */
public class ResourceConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all resource names by iterating through all resource configs.
	 * 
	 * @return A list of all resource names.
	 */
	protected static List<String> getResourceNameList() {
		File resourceDataFolder = new File(core.getDataFolder(), "ResourceData/");
		return WauzFileUtils.findRelativePathsRecursive(resourceDataFolder, "");
	}
	
	/**
	 * Gets a string from a resource config.
	 * 
	 * @param resource The name of the resource config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String resourceConfigGetString(String resource, String path) {
		File resourceDataFile = new File(core.getDataFolder(), "ResourceData/" + resource + ".yml");
		FileConfiguration resourceDataConfig = YamlConfiguration.loadConfiguration(resourceDataFile);	
		return resourceDataConfig.getString(path);
	}

	/**
	 * Gets a string list from a resource config.
	 * 
	 * @param resource The name of the resource config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> resourceConfigGetStringList(String resource, String path) {
		File resourceDataFile = new File(core.getDataFolder(), "ResourceData/" + resource + ".yml");
		FileConfiguration resourceDataConfig = YamlConfiguration.loadConfiguration(resourceDataFile);	
		return resourceDataConfig.getStringList(path);
	}

	/**
	 * Gets an int from a resource config.
	 * 
	 * @param resource The name of the resource config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int resourceConfigGetInt(String resource, String path) {
		File resourceDataFile = new File(core.getDataFolder(), "ResourceData/" + resource + ".yml");
		FileConfiguration resourceDataConfig = YamlConfiguration.loadConfiguration(resourceDataFile);	
		return resourceDataConfig.getInt(path);
	}

}
