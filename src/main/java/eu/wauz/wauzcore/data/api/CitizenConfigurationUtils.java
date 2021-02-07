package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * Collection of methods for reading and writing data in citizen config files.
 * 
 * @author Wauzmons
 */
public class CitizenConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all citizen names by iterating through all citizen configs.
	 * 
	 * @return A list of all citizen names.
	 */
	protected static List<String> getCitizenNameList() {
		File citizenDataFolder = new File(core.getDataFolder(), "CitizenData/");
		return WauzFileUtils.findRelativePathsRecursive(citizenDataFolder, "");
	}

	/**
	 * Gets a string from a citizen config.
	 * 
	 * @param citizen The name of the citizen config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String citizenConfigGetString(String citizen, String path) {
		File citizenDataFile = new File(core.getDataFolder(), "CitizenData/" + citizen + ".yml");
		FileConfiguration citizenDataConfig = YamlConfiguration.loadConfiguration(citizenDataFile);	
		return citizenDataConfig.getString(path);
	}

	/**
	 * Gets a string list from a citizen config.
	 * 
	 * @param citizen The name of the citizen config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> citizenConfigGetStringList(String citizen, String path) {
		File citizenDataFile = new File(core.getDataFolder(), "CitizenData/" + citizen + ".yml");
		FileConfiguration citizenDataConfig = YamlConfiguration.loadConfiguration(citizenDataFile);	
		return citizenDataConfig.getStringList(path);
	}

	/**
	 * Gets an int from a citizen config.
	 * 
	 * @param citizen The name of the citizen config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int citizenConfigGetInt(String citizen, String path) {
		File citizenDataFile = new File(core.getDataFolder(), "CitizenData/" + citizen + ".yml");
		FileConfiguration citizenDataConfig = YamlConfiguration.loadConfiguration(citizenDataFile);	
		return citizenDataConfig.getInt(path);
	}
	
	/**
	 * Gets a boolean from a citizen config.
	 * 
	 * @param citizen The name of the citizen config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested boolean.
	 */
	protected static boolean citizenConfigGetBoolean(String citizen, String path) {
		File citizenDataFile = new File(core.getDataFolder(), "CitizenData/" + citizen + ".yml");
		FileConfiguration citizenDataConfig = YamlConfiguration.loadConfiguration(citizenDataFile);	
		return citizenDataConfig.getBoolean(path);
	}
	
	/**
	 * Gets a key set from a citizen config.
	 * 
	 * @param citizen The name of the citizen config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested key set.
	 */
	protected static Set<String> citizenConfigGetKeys(String citizen, String path) {
		File citizenDataFile = new File(core.getDataFolder(), "CitizenData/" + citizen + ".yml");
		FileConfiguration citizenDataConfig = YamlConfiguration.loadConfiguration(citizenDataFile);	
		if(path == null) {
			return citizenDataConfig.getKeys(false);
		}
		ConfigurationSection section = citizenDataConfig.getConfigurationSection(path);
		return section != null ? section.getKeys(false) : Collections.emptySet();
	}

}
