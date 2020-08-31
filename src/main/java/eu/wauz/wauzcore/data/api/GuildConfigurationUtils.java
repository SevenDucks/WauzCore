package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;

/**
 * Collection of methods for reading and writing data in guild config files.
 * 
 * @author Wauzmons
 */
public class GuildConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Finds all guild uuids by iterating through all guild configs.
	 * 
	 * @return A list of all guild uuids.
	 */
	protected static List<String> getGuildUuidList() {
		List<String> guildUuidList = new ArrayList<>();
		for(File file : new File(core.getDataFolder(), "PlayerGuildData/").listFiles()) {
			guildUuidList.add(file.getName().replaceAll(".yml", ""));
		}
		return guildUuidList;
	}
	
	/**
	 * Deletes the guild config file with given uuid.
	 * 
	 * @param guild The uuid of the guild config file.
	 */
	protected static void deleteGuild(String guild) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		guildDataFile.delete();
	}
	
	/**
	 * Sets the given value in the guild config.
	 * 
	 * @param guild The uuid of the guild config file.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void guildConfigSet(String guild, String path, Object value) {
		try {
			File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
			FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
			
			guildDataConfig.set(PlayerConfigurationUtils.trimPlayerDataPath(path), value);
			guildDataConfig.save(guildDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a string from a guild config.
	 * 
	 * @param guild The uuid of the guild config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String guildConfigGetString(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getString(path);
	}
	
	/**
	 * Gets a string list from a guild config.
	 * 
	 * @param guild The uuid of the guild config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string list.
	 */
	protected static List<String> guildConfigGetStringList(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getStringList(path);
	}
	
	/**
	 * Gets an int from a guild config.
	 * 
	 * @param guild The uuid of the guild config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static int guildConfigGetInt(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getInt(path);
	}
	
	/**
	 * Gets an item stack from a guild config.
	 * 
	 * @param guild The uuid of the guild config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested item stack.
	 */
	protected static ItemStack guildConfigGetItemStack(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getItemStack(path);
	}

}
