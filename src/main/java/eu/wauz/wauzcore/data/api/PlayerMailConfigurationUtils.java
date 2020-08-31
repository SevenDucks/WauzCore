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

/**
 * Collection of methods for reading and writing data in player mail config files.
 * 
 * @author Wauzmons
 */
public class PlayerMailConfigurationUtils {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
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
	 * Deletes the player mail config file with given name.
	 * 
	 * @param guild The name of the player mail config file.
	 */
	protected static void deletePlayerMail(Player player, String mail) {
		File mailDataFile = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/mail/" + mail + ".yml");
		mailDataFile.delete();
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

}
