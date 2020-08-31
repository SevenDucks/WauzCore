package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;

/**
 * Collection of methods for reading and writing data in global config files.
 * 
 * @author Wauzmons
 */
public class GlobalConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Sets the given value in a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void mainConfigSet(String name, String path, Object value) {
		try {	
			File mainDataFile = new File(core.getDataFolder(), name + ".yml");
			FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);
			
			mainDataConfig.set(path, value);
			mainDataConfig.save(mainDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a string from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String mainConfigGetString(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getString(path);
	}

	/**
	 * Gets a string list from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> mainConfigGetStringList(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getStringList(path);
	}

	/**
	 * Gets an int from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int mainConfigGetInt(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getInt(path);
	}
	
	/**
	 * Gets a long from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested long.
	 */
	protected static long mainConfigGetLong(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getLong(path);
	}
	
	/**
	 * Gets a double from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested double.
	 */
	protected static double mainConfigGetDouble(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getDouble(path);
	}
	
	/**
	 * Gets a boolean from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested boolean.
	 */
	protected static boolean mainConfigGetBoolean(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getBoolean(path);
	}
	
	/**
	 * Gets a key set from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested key set.
	 */
	protected static Set<String> mainConfigGetKeys(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);
		if(path == null) {
			return mainDataConfig.getKeys(false);
		}
		ConfigurationSection section = mainDataConfig.getConfigurationSection(path);
		return section != null ? section.getKeys(false) : Collections.emptySet();
	}
	
	/**
	 * Gets a location from a specific config.
	 * 
	 * @param name The name of the config file.
	 * @param path The key path of the value to get.
	 * @param worldName The name of the location's world.
	 * 
	 * @return The requested location.
	 */
	protected static Location mainConfigGetLocation(String name, String path, String worldName) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);
		
		List<Double> coords = new ArrayList<>();
		for(String coord : mainDataConfig.getString(path).split(" ")) {
			coords.add(Double.parseDouble(coord));
		}
		return new Location(Bukkit.getWorld(worldName), coords.get(0), coords.get(1), coords.get(2));
	}
	
}
