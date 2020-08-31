package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * Collection of methods for reading and writing data in player quest config files.
 * 
 * @author Wauzmons
 */
public class PlayerQuestConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
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

}
