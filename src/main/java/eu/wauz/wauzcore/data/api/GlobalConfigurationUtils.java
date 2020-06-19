package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
	
// Interact with Main-Config
	
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
	
// Interact with Citizen-Config

	/**
	 * Finds all citizen names by iterating through all citizen configs.
	 * 
	 * @return A list of all citizen names.
	 */
	protected static List<String> getCitizenNameList() {
		List<String> citizenNameList = new ArrayList<>();
		File citizenDataFolder = new File(core.getDataFolder(), "CitizenData/");
		if(citizenDataFolder.exists()) {
			for(File file : citizenDataFolder.listFiles()) {
				citizenNameList.add(file.getName().replace(".yml", ""));
			}
		}
		return citizenNameList;
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
	
// Interact with Shop-Config
	
	/**
	 * Finds all shop names by iterating through all shop configs.
	 * 
	 * @return A list of all shop names.
	 */
	protected static List<String> getShopNameList() {
		List<String> shopNameList = new ArrayList<>();
		File shopDataFolder = new File(core.getDataFolder(), "ShopData/");
		if(shopDataFolder.exists()) {
			for(File file : shopDataFolder.listFiles()) {
				shopNameList.add(file.getName().replace(".yml", ""));
			}
		}
		return shopNameList;
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
	
// Interact with Quest-Config
	
	/**
	 * Finds all quest names by iterating through all quest configs.
	 * 
	 * @return A list of all quest names.
	 */
	protected static List<String> getQuestNameList() {
		List<String> questNameList = new ArrayList<>();
		File questDataFolder = new File(core.getDataFolder(), "QuestData/");
		if(questDataFolder.exists()) {
			for(File file : questDataFolder.listFiles()) {
				questNameList.add(file.getName().replace(".yml", ""));
			}
		}
		return questNameList;
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
	
// Interact with Instance-Config
	
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
	
	/**
	 * Sets the given value in an instance world config.
	 * 
	 * @param world The world of the instance.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void instanceWorldConfigSet(World world, String path, Object value) {
		try {
			File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
			FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);				
			instanceDataConfig.set(path, value);
			instanceDataConfig.save(instanceDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a string from an instance world config.
	 * 
	 * @param world The world of the instance.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String instanceWorldConfigGetString(World world, String path) {
		File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getString(path);
	}
	
	/**
	 * Gets an int from an instance world config.
	 * 
	 * @param world The world of the instance.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int instanceWorldConfigGetInt(World world, String path) {
		File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getInt(path);
	}
	
	/**
	 * Gets a key set from an instance world config.
	 * 
	 * @param world The world of the instance.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested key set.
	 */
	protected static Set<String> instanceWorldConfigGetKeys(World world, String path) {
		File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		if(path == null) {
			return instanceDataConfig.getKeys(false);
		}
		ConfigurationSection section = instanceDataConfig.getConfigurationSection(path);
		return section != null ? section.getKeys(false) : Collections.emptySet();
	}

}
