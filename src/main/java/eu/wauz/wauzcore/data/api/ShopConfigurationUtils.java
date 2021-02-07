package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * Collection of methods for reading and writing data in shop config files.
 * 
 * @author Wauzmons
 */
public class ShopConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all shop names by iterating through all shop configs.
	 * 
	 * @return A list of all shop names.
	 */
	protected static List<String> getShopNameList() {
		File shopDataFolder = new File(core.getDataFolder(), "ShopData/");
		return WauzFileUtils.findRelativePathsRecursive(shopDataFolder, "");
	}

	/**
	 * Gets a string from a shop config.
	 * 
	 * @param shop The name of the shop config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String shopConfigGetString(String shop, String path) {
		File shopDataFile = new File(core.getDataFolder(), "ShopData/" + shop + ".yml");
		FileConfiguration shopDataConfig = YamlConfiguration.loadConfiguration(shopDataFile);	
		return shopDataConfig.getString(path);
	}

	/**
	 * Gets a string list from a shop config.
	 * 
	 * @param shop The name of the shop config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> shopConfigGetStringList(String shop, String path) {
		File shopDataFile = new File(core.getDataFolder(), "ShopData/" + shop + ".yml");
		FileConfiguration shopDataConfig = YamlConfiguration.loadConfiguration(shopDataFile);	
		return shopDataConfig.getStringList(path);
	}

	/**
	 * Gets an int from a shop config.
	 * 
	 * @param shop The name of the shop config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int shopConfigGetInt(String shop, String path) {
		File shopDataFile = new File(core.getDataFolder(), "ShopData/" + shop + ".yml");
		FileConfiguration shopDataConfig = YamlConfiguration.loadConfiguration(shopDataFile);	
		return shopDataConfig.getInt(path);
	}

}
