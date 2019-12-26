package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * Collection of methods for reading and writing data in player config files.
 * 
 * @author Wauzmons
 */
public class PlayerConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
// Interact with Player-Config

	/**
	 * Gets either the global or a character specific config file for a player.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of a value. (May start with character slot, if not character specific)
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The player config file.
	 * 
	 * @see WauzPlayerData#getSelectedCharacterSlot()
	 */
	private static File getPlayerDataFile(Player player, String path, Boolean characterSpecific) {
		String characterSlot;
		if(!characterSpecific && path.startsWith("char")) {
			characterSlot = StringUtils.substringBefore(path, ".");
		}
		else {
			characterSlot = characterSpecific ? WauzPlayerDataPool.getPlayer(player).getSelectedCharacterSlot() : "global";
		}
			
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		return new File(playerDirectory, characterSlot + ".yml");
	}
	
	/**
	 * Gets either the global or a character specific config file for an offline player.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of a value. (May start with character slot, if not character specific)
	 * @param characterSpecific If the character specific file should be used. (Will not work here)
	 * 
	 * @return The player config file.
	 */
	private static File getPlayerDataFile(OfflinePlayer player, String path, Boolean characterSpecific) {
		String characterSlot;
		if(!characterSpecific && path.startsWith("char")) {
			characterSlot = StringUtils.substringBefore(path, ".");
		}
		else {
			characterSlot = "global";
		}
		
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		return new File(playerDirectory, characterSlot + ".yml");
	}
	
	/**
	 * Trims the given path to not contain character slots.
	 * 
	 * @param path The path with character slot.
	 * 
	 * @return The path without character slot.
	 */
	private static String trimPlayerDataPath(String path) {
		return path.startsWith("char") ? StringUtils.substringAfter(path, ".") : path;
	}

	/**
	 * Sets the given value in the player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 * @param characterSpecific If the character specific file should be used.
	 */
	protected static void playerConfigSet(Player player, String path, Object value, Boolean characterSpecific) {
		try {
			File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
			FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
			
			playerDataConfig.set(trimPlayerDataPath(path), value);
			playerDataConfig.save(playerDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the given value in the player config.
	 * 
	 * @param player The offline player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 * @param characterSpecific If the character specific file should be used.
	 */
	protected static void playerConfigSet(OfflinePlayer player, String path, Object value, Boolean characterSpecific) {
		try {
			File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
			FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
			
			playerDataConfig.set(trimPlayerDataPath(path), value);
			playerDataConfig.save(playerDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a string from a player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested string.
	 */
	protected static String playerConfigGetString(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getString(trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a string from a player config.
	 * 
	 * @param player The offline player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested string.
	 */
	protected static String playerConfigGetString(OfflinePlayer player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getString(trimPlayerDataPath(path));
	}

	/**
	 * Gets an int from a player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested int.
	 */
	protected static Integer playerConfigGetInt(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getInt(trimPlayerDataPath(path));
	}

	/**
	 * Gets a long from a player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested long.
	 */
	protected static Long playerConfigGetLong(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getLong(trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a long from a player config.
	 * 
	 * @param player offline The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested long.
	 */
	protected static Long playerConfigGetLong(OfflinePlayer player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getLong(trimPlayerDataPath(path));
	}

	/**
	 * Gets a double from a player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested double.
	 */
	protected static Double playerConfigGetDouble(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getDouble(trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a boolean from a player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested boolean.
	 */
	protected static Boolean playerConfigGetBoolean(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getBoolean(trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a boolean from a player config.
	 * 
	 * @param player The offline player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested boolean.
	 */
	protected static Boolean playerConfigGetBoolean(OfflinePlayer player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getBoolean(trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a location from a player config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param characterSpecific If the character specific file should be used.
	 * 
	 * @return The requested location.
	 */
	protected static Location playerConfigGetLocation(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		List<Double> coords = new ArrayList<Double>();
		for(String coord : playerDataConfig.getString(trimPlayerDataPath(path)).split(" ")) {
			coords.add(Double.parseDouble(coord));
		}
		return new Location(Bukkit.getWorld(playerConfigGetString(player, "pos.world", true))
				, coords.get(0), coords.get(1), coords.get(2));
	}
	
// Interact with Player-Quest-Config
	
	/**
	 * Gets a config file for a player quest.
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * 
	 * @return The player quest config file.
	 */
	private static File getPlayerQuestDataFile(Player player, String quest) {
		String characterSlot = WauzPlayerDataPool.getPlayer(player).getSelectedCharacterSlot();
		File playerQuestDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/" + characterSlot + "-quests/");
		playerQuestDirectory.mkdirs();
		return new File(playerQuestDirectory, quest + ".yml");
	}
	
	/**
	 * Sets the given value in the player quest config.
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void playerQuestConfigSet(Player player, String quest, String path, Object value) {
		try {
			File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
			FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
			
			playerQuestDataConfig.set(trimPlayerDataPath(path), value);
			playerQuestDataConfig.save(playerQuestDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets an int from a player quest config.
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param path The key path of the value to set.
	 * 
	 * @return The requested int.
	 */
	protected static Integer playerQuestConfigGetInt(Player player, String quest, String path) {
		File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
		FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
		return playerQuestDataConfig.getInt(trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a long from a player quest config.
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param path The key path of the value to set.
	 * 
	 * @return The requested long.
	 */
	protected static Long playerQuestConfigGetLong(Player player, String quest, String path) {
		File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
		FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
		return playerQuestDataConfig.getLong(trimPlayerDataPath(path));
	}
	
// Interact with Guild-Config
	
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
			
			guildDataConfig.set(trimPlayerDataPath(path), value);
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
	 * @param path The key path of the value to set.
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
	 * @param path The key path of the value to set.
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
	 * @param path The key path of the value to set.
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
	 * @param path The key path of the value to set.
	 * 
	 * @return The requested item stack.
	 */
	protected static ItemStack guildConfigGetItemStack(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getItemStack(path);
	}

}
