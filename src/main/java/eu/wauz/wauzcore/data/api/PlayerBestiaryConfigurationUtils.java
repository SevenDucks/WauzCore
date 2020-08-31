package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * Collection of methods for reading and writing data in player bestiary config files.
 * 
 * @author Wauzmons
 */
public class PlayerBestiaryConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Gets a config file for a player bestiary.
	 * 
	 * @param player The player that owns the config file.
	 * 
	 * @return The player bestiary config file.
	 */
	private static File getPlayerBestiaryDataFile(Player player) {
		String characterSlot = WauzPlayerDataPool.getPlayer(player).getSelectedCharacterSlot();
		return new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/" + characterSlot + "-bestiary.yml");
	}
	
	/**
	 * Sets the given value in the player bestiary config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to set.
	 * @param value The value to set.
	 */
	protected static void playerBestiaryConfigSet(Player player, String path, Object value) {
		try {
			File playerBestiaryDataFile = getPlayerBestiaryDataFile(player);
			FileConfiguration playerBestiaryDataConfig = YamlConfiguration.loadConfiguration(playerBestiaryDataFile);
			
			playerBestiaryDataConfig.set(PlayerConfigurationUtils.trimPlayerDataPath(path), value);
			playerBestiaryDataConfig.save(playerBestiaryDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets an int from a player bestiary config.
	 * 
	 * @param player The player that owns the config file.
	 * @param path The key path of the value to get.
	 * 
	 * @return The requested int.
	 */
	protected static Integer playerBestiaryConfigGetInt(Player player, String path) {
		File playerBestiaryDataFile = getPlayerBestiaryDataFile(player);
		FileConfiguration playerBestiaryDataConfig = YamlConfiguration.loadConfiguration(playerBestiaryDataFile);
		return playerBestiaryDataConfig.getInt(PlayerConfigurationUtils.trimPlayerDataPath(path));
	}

}
