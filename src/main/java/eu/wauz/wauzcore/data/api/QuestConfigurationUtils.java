package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * Collection of methods for reading and writing data in quest config files.
 * 
 * @author Wauzmons
 */
public class QuestConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all quest names by iterating through all quest configs.
	 * 
	 * @return A list of all quest names.
	 */
	protected static List<String> getQuestNameList() {
		File questDataFolder = new File(core.getDataFolder(), "QuestData/");
		return WauzFileUtils.findRelativePathsRecursive(questDataFolder, "");
	}

	/**
	 * Gets a string from a quest config.
	 * 
	 * @param quest The name of the quest config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String questConfigGetString(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getString(path);
	}

	/**
	 * Gets a string list from a quest config.
	 * 
	 * @param quest The name of the quest config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> questConfigGetStringList(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getStringList(path);
	}

	/**
	 * Gets an int from a quest config.
	 * 
	 * @param quest The name of the quest config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int questConfigGetInt(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getInt(path);
	}
	
	/**
	 * Gets a double from a quest config.
	 * 
	 * @param quest The name of the quest config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested double.
	 */
	protected static double questConfigGetDouble(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getDouble(path);
	}

}
