package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;

/**
 * Collection of methods for reading and writing data in bestiary config files.
 * 
 * @author Wauzmons
 */
public class BestiaryConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all species names by iterating through all species configs of a given category.
	 * 
	 * @param category The name of the bestiary category.
	 * 
	 * @return A list of all species names.
	 */
	protected static List<String> getSpeciesNameList(String category) {
		List<String> speciesNameList = new ArrayList<>();
		File speciesDataFolder = new File(core.getDataFolder(), "BestiaryData/" + category + "/");
		if(speciesDataFolder.exists()) {
			for(File file : speciesDataFolder.listFiles()) {
				speciesNameList.add(file.getName().replace(".yml", ""));
			}
		}
		return speciesNameList;
	}
	
	/**
	 * Gets a key set from a species config.
	 * 
	 * @param category The name of the bestiary category.
	 * @param species The name of the species config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested key set.
	 */
	protected static Set<String> speciesConfigGetKeys(String category, String species, String path) {
		File speciesDataFile = new File(core.getDataFolder(), "BestiaryData/" + category + "/" + species + ".yml");
		FileConfiguration speciesDataConfig = YamlConfiguration.loadConfiguration(speciesDataFile);	
		if(path == null) {
			return speciesDataConfig.getKeys(false);
		}
		ConfigurationSection section = speciesDataConfig.getConfigurationSection(path);
		return section != null ? section.getKeys(false) : Collections.emptySet();
	}
	
	/**
	 * Gets a string from a species config.
	 * 
	 * @param category The name of the bestiary category.
	 * @param species The name of the species config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String speciesConfigGetString(String category, String species, String path) {
		File speciesDataFile = new File(core.getDataFolder(), "BestiaryData/" + category + "/" + species + ".yml");
		FileConfiguration speciesDataConfig = YamlConfiguration.loadConfiguration(speciesDataFile);	
		return speciesDataConfig.getString(path);
	}

}
