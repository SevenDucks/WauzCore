package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Collection of methods for reading and writing data in seasonal config files.
 * 
 * @author Wauzmons
 */
public class SeasonConfigurationUtils {

	/**
	 * Sets the given value in a season config.
	 * 
	 * @param world The seasonal world.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void seasonConfigSet(World world, String path, Object value) {
		try {	
			File seasonDataFile = new File(world.getWorldFolder(), "Season.yml");
			FileConfiguration seasonDataConfig = YamlConfiguration.loadConfiguration(seasonDataFile);
			
			seasonDataConfig.set(path, value);
			seasonDataConfig.save(seasonDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a string from a season config.
	 * 
	 * @param world The seasonal world.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String seasonConfigGetString(World world, String path) {
		File seasonDataFile = new File(world.getWorldFolder(), "Season.yml");
		FileConfiguration seasonDataConfig = YamlConfiguration.loadConfiguration(seasonDataFile);	
		return seasonDataConfig.getString(path);
	}
	
	/**
	 * Gets an int from a season config.
	 * 
	 * @param world The seasonal world.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int seasonConfigGetInt(World world, String path) {
		File seasonDataFile = new File(world.getWorldFolder(), "Season.yml");
		FileConfiguration seasonDataConfig = YamlConfiguration.loadConfiguration(seasonDataFile);	
		return seasonDataConfig.getInt(path);
	}
	
}
