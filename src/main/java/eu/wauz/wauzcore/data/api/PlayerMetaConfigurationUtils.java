package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * Collection of methods for reading and writing data in player config files.
 * 
 * @author Wauzmons
 */
public class PlayerMetaConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
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
			
			playerQuestDataConfig.set(PlayerConfigurationUtils.trimPlayerDataPath(path), value);
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
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static Integer playerQuestConfigGetInt(Player player, String quest, String path) {
		File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
		FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
		return playerQuestDataConfig.getInt(PlayerConfigurationUtils.trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a long from a player quest config.
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested long.
	 */
	protected static Long playerQuestConfigGetLong(Player player, String quest, String path) {
		File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
		FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
		return playerQuestDataConfig.getLong(PlayerConfigurationUtils.trimPlayerDataPath(path));
	}
	
// Interact with Player-Relation-Config
	
	/**
	 * Gets a config file for a player relation.
	 * 
	 * @param player The player that owns the config file.
	 * @param citizen The citizen that the config belongs to.
	 * 
	 * @return The player relation config file.
	 */
	private static File getPlayerRelationDataFile(Player player, String citizen) {
		String characterSlot = WauzPlayerDataPool.getPlayer(player).getSelectedCharacterSlot();
		File playerQuestDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/" + characterSlot + "-relations/");
		playerQuestDirectory.mkdirs();
		return new File(playerQuestDirectory, citizen + ".yml");
	}
	
	/**
	 * Sets the given value in the player relation config.
	 * 
	 * @param player The player that owns the config file.
	 * @param citizen The citizen that the config belongs to.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void playerRelationConfigSet(Player player, String citizen, String path, Object value) {
		try {
			File playerRelationDataFile = getPlayerRelationDataFile(player, citizen);
			FileConfiguration playerRelationDataConfig = YamlConfiguration.loadConfiguration(playerRelationDataFile);
			
			playerRelationDataConfig.set(PlayerConfigurationUtils.trimPlayerDataPath(path), value);
			playerRelationDataConfig.save(playerRelationDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets an int from a player relation config.
	 * 
	 * @param player The player that owns the config file.
	 * @param citizen The citizen that the config belongs to.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static Integer playerRelationConfigGetInt(Player player, String citizen, String path) {
		File playerRelationDataFile = getPlayerRelationDataFile(player, citizen);
		FileConfiguration playerRelationDataConfig = YamlConfiguration.loadConfiguration(playerRelationDataFile);
		return playerRelationDataConfig.getInt(PlayerConfigurationUtils.trimPlayerDataPath(path));
	}
	
// Interact with Player-Mail-Config
	
	/**
	 * Finds all player mail names by iterating through all player mail configs.
	 * 
	 * @param player The player that owns the config files.
	 * 
	 * @return A list of all player mail names.
	 */
	protected static List<String> getPlayerMailNameList(Player player) {
		List<String> playerMailNameList = new ArrayList<>();
		File playerMailDataFolder = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/mail/");
		if(playerMailDataFolder.exists()) {
			for(File file : playerMailDataFolder.listFiles()) {
				playerMailNameList.add(file.getName().replace(".yml", ""));
			}
		}
		return playerMailNameList;
	}

	/**
	 * Gets a config file for a player mail.
	 * 
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * 
	 * @return The player mail config file.
	 */
	private static File getPlayerMailDataFile(OfflinePlayer player, String mail) {
		File playerMailDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/mail/");
		playerMailDirectory.mkdirs();
		return new File(playerMailDirectory, mail + ".yml");
	}
	
	/**
	 * Sets the given value in the player mail config.
	 * 
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void playerMailConfigSet(OfflinePlayer player, String mail, String path, Object value) {
		try {
			File playerMailDataFile = getPlayerMailDataFile(player, mail);
			FileConfiguration playerMailDataConfig = YamlConfiguration.loadConfiguration(playerMailDataFile);
			
			playerMailDataConfig.set(PlayerConfigurationUtils.trimPlayerDataPath(path), value);
			playerMailDataConfig.save(playerMailDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a string from a player mail config.
	 * 
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested string.
	 */
	protected static String playerMailConfigGetString(OfflinePlayer player, String mail, String path) {
		File playerMailDataFile = getPlayerMailDataFile(player, mail);
		FileConfiguration playerMailDataConfig = YamlConfiguration.loadConfiguration(playerMailDataFile);
		return playerMailDataConfig.getString(PlayerConfigurationUtils.trimPlayerDataPath(path));
	}
	
	/**
	 * Gets a long from a player mail config.
	 * 
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested long.
	 */
	protected static Long playerMailConfigGetLong(OfflinePlayer player, String mail, String path) {
		File playerMailDataFile = getPlayerMailDataFile(player, mail);
		FileConfiguration playerMailDataConfig = YamlConfiguration.loadConfiguration(playerMailDataFile);
		return playerMailDataConfig.getLong(PlayerConfigurationUtils.trimPlayerDataPath(path));
	}
	
	/**
	 * Gets an item stack from a player mail config.
	 * 
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested item stack.
	 */
	protected static ItemStack playerMailConfigGetItemStack(OfflinePlayer player, String mail, String path) {
		File playerMailDataFile = getPlayerMailDataFile(player, mail);
		FileConfiguration playerMailDataConfig = YamlConfiguration.loadConfiguration(playerMailDataFile);
		return playerMailDataConfig.getItemStack(PlayerConfigurationUtils.trimPlayerDataPath(path));
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
