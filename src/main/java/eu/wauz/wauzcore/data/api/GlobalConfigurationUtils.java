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

public class GlobalConfigurationUtils {
	
	private static WauzCore core = WauzCore.getInstance();
	
// Interact with Main-Config
	
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
	
	protected static String mainConfigGetString(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getString(path);
	}

	protected static List<String> mainConfigGetStringList(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getStringList(path);
	}

	protected static int mainConfigGetInt(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getInt(path);
	}
	
	protected static boolean mainConfigGetBoolean(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);	
		return mainDataConfig.getBoolean(path);
	}
	
	protected static Set<String> mainConfigGetKeys(String name, String path) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);
		if(path == null)
			return mainDataConfig.getKeys(false);
		ConfigurationSection section = mainDataConfig.getConfigurationSection(path);
		return section != null ? section.getKeys(false) : Collections.emptySet();
	}
	
	protected static Location mainConfigGetLocation(String name, String path, String worldName) {
		File mainDataFile = new File(core.getDataFolder(), name + ".yml");
		FileConfiguration mainDataConfig = YamlConfiguration.loadConfiguration(mainDataFile);
		
		List<Double> coords = new ArrayList<Double>();
		for(String coord : mainDataConfig.getString(path).split(" "))
			coords.add(Double.parseDouble(coord));
		
		return new Location(Bukkit.getWorld(worldName), coords.get(0), coords.get(1), coords.get(2));
	}
	
// Interact with Shop-Config

	protected static String shopConfigGetString(String shop, String path) {
		File shopDataFile = new File(core.getDataFolder(), "ShopData/" + shop + ".yml");
		FileConfiguration shopDataConfig = YamlConfiguration.loadConfiguration(shopDataFile);	
		return shopDataConfig.getString(path);
	}

	protected static List<String> shopConfigGetStringList(String shop, String path) {
		File shopDataFile = new File(core.getDataFolder(), "ShopData/" + shop + ".yml");
		FileConfiguration shopDataConfig = YamlConfiguration.loadConfiguration(shopDataFile);	
		return shopDataConfig.getStringList(path);
	}

	protected static int shopConfigGetInt(String shop, String path) {
		File shopDataFile = new File(core.getDataFolder(), "ShopData/" + shop + ".yml");
		FileConfiguration shopDataConfig = YamlConfiguration.loadConfiguration(shopDataFile);	
		return shopDataConfig.getInt(path);
	}
	
// Interact with Quest-Config
	
	protected static List<String> getQuestNameList() {
		List<String> questNameList = new ArrayList<>();
		for(File file : new File(core.getDataFolder(), "QuestData/").listFiles())
			questNameList.add(file.getName().replaceAll(".yml", ""));
		return questNameList;
	}

	protected static String questConfigGetString(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getString(path);
	}

	protected static List<String> questConfigGetStringList(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getStringList(path);
	}

	protected static int questConfigGetInt(String quest, String path) {
		File questDataFile = new File(core.getDataFolder(), "QuestData/" + quest + ".yml");
		FileConfiguration questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);	
		return questDataConfig.getInt(path);
	}
	
// Interact with Instance-Config
	
	protected static String instanceConfigGetString(String instance, String path) {
		File instanceDataFile = new File(core.getDataFolder(), "InstanceData/" + instance + ".yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getString(path);
	}
	
	protected static List<String> instanceConfigGetStringList(String instance, String path) {
		File instanceDataFile = new File(core.getDataFolder(), "InstanceData/" + instance + ".yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getStringList(path);
	}
	
	protected static int instanceConfigGetInt(String instance, String path) {
		File instanceDataFile = new File(core.getDataFolder(), "InstanceData/" + instance + ".yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getInt(path);
	}
	
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
	
	protected static String instanceWorldConfigGetString(World world, String path) {
		File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getString(path);
	}
	
	protected static int instanceWorldConfigGetInt(World world, String path) {
		File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		return instanceDataConfig.getInt(path);
	}
	
	protected static Set<String> instanceWorldConfigGetKeys(World world, String path) {
		File instanceDataFile = new File(world.getWorldFolder(), "InstanceWorldData.yml");
		FileConfiguration instanceDataConfig = YamlConfiguration.loadConfiguration(instanceDataFile);	
		if(path == null)
			return instanceDataConfig.getKeys(false);
		ConfigurationSection section = instanceDataConfig.getConfigurationSection(path);
		return section != null ? section.getKeys(false) : Collections.emptySet();
	}

}
