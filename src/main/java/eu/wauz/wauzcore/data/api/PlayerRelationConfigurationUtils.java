package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * Collection of methods for reading and writing data in player relation config files.
 * 
 * @author Wauzmons
 */
public class PlayerRelationConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
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

}
