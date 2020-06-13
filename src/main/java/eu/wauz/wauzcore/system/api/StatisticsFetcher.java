package eu.wauz.wauzcore.system.api;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import com.google.common.io.Files;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * Used to fetch player statistics from Minecraft config files.
 * Create an instance for a single player's stats or get global stats from static access.
 * 
 * @author Wauzmons
 */
public class StatisticsFetcher {

	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * The amount of MythicMobs entity files, if already calculated.
	 */
	private static String totalCustomEntitiesString;
	
	/**
	 * The amount of all players, that ever played, if already calculated.
	 */
	private static String totalPlayersString;
	
	/**
	 * The amount of days all players together played, if already calculated.
	 */
	private static String totalPlaytimeDaysString;
	
	/**
	 * Calculates all global statistics.
	 */
	public static void calculate() {
		totalCustomEntitiesString = getTotalCustomEntities();
		totalPlayersString = getTotalPlayers();
		totalPlaytimeDaysString = getTotalPlaytimeDays();
	}
	
	/**
	 * @return The amount of MythicMobs entity files, if already calculated.
	 */
	public static String getTotalCustomEntitiesString() {
		return totalCustomEntitiesString;
	}
	
	/**
	 * @return The amount of all players, that ever played, if already calculated.
	 */
	public static String getTotalPlayersString() {
		return totalPlayersString;
	}

	/**
	 * @return The amount of days all players together played, if already calculated.
	 */
	public static String getTotalPlaytimeDaysString() {
		return totalPlaytimeDaysString;
	}
	
	/**
	 * Counts the amount of MythicMobs entity files, by iterating through the mobs folder.
	 * 
	 * @return The amount of MythicMobs entity files.
	 */
	private static String getTotalCustomEntities() {
		int customEntities = 0;
		String statisticsPath = core.getDataFolder().getAbsolutePath().replace("WauzCore", "MythicMobs/Mobs/Wauzland");
		
		List<File> foldersToScan = new ArrayList<>();
		foldersToScan.addAll(Arrays.asList(new File(statisticsPath).listFiles()));
		
		while(!foldersToScan.isEmpty()) {
			for(File file : foldersToScan.get(0).listFiles()) {
				if(file.isDirectory()) {
					foldersToScan.add(file);
				}
				else {
					customEntities++;
				}
			}
			foldersToScan.remove(foldersToScan.get(0));
		}
		return Integer.toString(customEntities);
	}

	/**
	 * Counts the amount of all players, that ever played, by counting statistics files.
	 * 
	 * @return The amount of all players, that ever played.
	 */
	private static String getTotalPlayers() {
		File statisticsFolder = new File(core.getDataFolder().getAbsolutePath().replace("plugins/WauzCore", "HubNexus/stats/"));
		return Integer.toString(statisticsFolder.list().length);
	}
	
	/**
	 * Counts the amount of days all players together played, by looking in statistics files.
	 * 
	 * @return The amount of days all players together played.
	 */
	private static String getTotalPlaytimeDays() {
		long playedHours = 0;
		String statisticsPath = core.getDataFolder().getAbsolutePath().replace("plugins/WauzCore", "HubNexus/stats/%uuid%.json");
		for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			File statisticsFile = new File(statisticsPath.replace("%uuid%", player.getUniqueId().toString()));
			playedHours += getPlayedHoursFromStatistics(statisticsFile);
		}
		return Long.toString(playedHours / 24);
	}
	
	/**
	 * The player whose statistics are collected.
	 */
	private OfflinePlayer offlinePlayer;
	
	/**
	 * The Minecraft file, that contains statistics of a specific player.
	 */
	private File statisticsFile;
	
	/**
	 * Information about the player's 1st character slot.
	 */
	private String characterString1 = "None";
	
	/**
	 * Information about the player's 2nd character slot.
	 */
	private String characterString2 = "None";
	
	/**
	 * Information about the player's 3rd character slot.
	 */
	private String characterString3 = "None";
	
	/**
	 * Creates an instance to fetch player specific statistics, including character slot information.
	 * 
	 * @param offlinePlayer The player whose statistics are collected.
	 */
	public StatisticsFetcher(OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
		String statisticsPath = core.getDataFolder().getAbsolutePath().replace("plugins/WauzCore", "HubNexus/stats/%uuid%.json");
		statisticsFile = new File(statisticsPath.replace("%uuid%", offlinePlayer.getUniqueId().toString()));
		createCharacterStrings(offlinePlayer.getUniqueId().toString());
	}
	
	/**
	 * Creates strings with character information for the given player UUID.
	 * 
	 * @param uuidString UUID of the player.
	 */
	private void createCharacterStrings(String uuidString) {
		characterString1 = createCharacterString(uuidString, 1);
		characterString2 = createCharacterString(uuidString, 2);
		characterString3 = createCharacterString(uuidString, 3);
	}
	
	/**
	 * Creates a string with character information for the given player UUID and slot.
	 * 
	 * @param uuidString UUID of the player.
	 * @param slot Character slot of the player.
	 * 
	 * @return Information about the player's character slot.
	 */
	private String createCharacterString(String uuidString, int slot) {
		String characterString = "Empty";
		
		File playerDataFile = new File(core.getDataFolder(), "PlayerData/" + uuidString + "/charMMORPG-" + slot + ".yml");
		if(!playerDataFile.exists()) {
			return characterString;
		}
		
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		if(!playerDataConfig.getBoolean("exists")) {
			return characterString;
		}
		
		String characterClass = playerDataConfig.getString("class");
		String world = playerDataConfig.getString("pos.world");
		String level = playerDataConfig.getString("level");
		characterString = "Level " + level + " " + characterClass + " on " + world;
		return characterString;
	}
	
	/**
	 * Delivers the on construction created string, with character information for the player's given  slot.
	 * 
	 * @param charSlotNumber Character slot of the player.
	 * 
	 * @return Information about the player's character slot.
	 */
	public String getCharacterString(int charSlotNumber) {
		switch(charSlotNumber) {
		case 1:
			return characterString1;
		case 2:
			return characterString2;
		case 3:
			return characterString3;
		default:
			return "None";
		}
	}
	
	/**
	 * Adds infos about the player and their characters to a list of lores.
	 * 
	 * @param lores The lore list that should be extended.
	 * 
	 * @see PlayerConfigurator#getLastPlayed(OfflinePlayer)
	 * @see PlayerConfigurator#getSurvivalScore(OfflinePlayer)
	 * @see StatisticsFetcher#getCharacterString(int)
	 */
	public void addCharacterLores(List<String> lores) {
		lores.add(ChatColor.GRAY + "Last Online: " + (offlinePlayer.isOnline()
				? ChatColor.GREEN + "Now"
				: ChatColor.BLUE + PlayerConfigurator.getLastPlayed(offlinePlayer) + " ago"));
		lores.add("");
		lores.add(ChatColor.DARK_PURPLE + "MMORPG Characters: ");
		for(int character = 1; character <= 3; character++) {
			lores.add(ChatColor.WHITE + getCharacterString(character));
		}
		lores.add("");
		lores.add(ChatColor.DARK_PURPLE + "Survival Score: ");
		lores.add(ChatColor.WHITE + Formatters.INT.format(PlayerConfigurator.getSurvivalScore(offlinePlayer)));
	}
	
	/**
	 * @return A formatted string, showing the player's playtime in hours, from their statistics file.
	 */
	public String getPlayedHoursString() {
		return Formatters.INT.format(getPlayedHoursFromStatistics(statisticsFile));
	}
	
	/**
	 * @return A string, showing the player's playtime in hours, from their statistics file.
	 */
	private static long getPlayedHoursFromStatistics(File file) {
		long playedHours = 0;
		try {
			String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
			long playedTicks = line.contains("\"stat.playOneMinute\":")
				? Long.parseLong(StringUtils.substringBetween(line, "\"stat.playOneMinute\":", ","))
				: Long.parseLong(StringUtils.substringBetween(line, "\"minecraft:play_one_minute\":", ","));
			playedHours = playedTicks / 72000;
		}
		catch (Exception e) {
			WauzDebugger.catchException(StatisticsFetcher.class, e);
		}
		return playedHours;
	}
	
	/**
	 * @return A formatted string, showing the player's killed mobs, from their statistics file.
	 */
	public String getKilledMobsString() {
		return Formatters.INT.format(getKilledMobsFromStatistics(statisticsFile));
	}
	
	/**
	 * @return A string, showing the player's killed mobs, from their statistics file.
	 */
	private static long getKilledMobsFromStatistics(File file) {
		long killedMobs = 0;
		try {
			String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
			killedMobs = line.contains("\"stat.mobKills\":")
				? Long.parseLong(StringUtils.substringBetween(line, "\"stat.mobKills\":", ","))
				: Long.parseLong(StringUtils.substringBetween(line, "\"minecraft:mob_kills\":", ","));
		}
		catch (Exception e) {
			WauzDebugger.catchException(StatisticsFetcher.class, e);
		}
		return killedMobs;
	}
	
	/**
	 * @return A formatted string, showing the player's walked metres, from their statistics file.
	 */
	public String getWalkedMetresString() {
		return Formatters.INT.format(getWalkedMetresFromStatistics(statisticsFile));
	}
	
	/**
	 * @return A string, showing the player's walked metres, from their statistics file.
	 */
	private static long getWalkedMetresFromStatistics(File file) {
		long walkedMetres = 0;
		try {
			String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
			long walkedCentimetres = line.contains("\"stat.walkOneCm\":")
				? Long.parseLong(StringUtils.substringBetween(line, "\"stat.walkOneCm\":", ","))
				: Long.parseLong(StringUtils.substringBetween(line, "\"minecraft:walk_one_cm\":", ","));
			walkedMetres = walkedCentimetres / 100;
		}
		catch (Exception e) {
			WauzDebugger.catchException(StatisticsFetcher.class, e);
		}
		return walkedMetres;
	}
	
}
